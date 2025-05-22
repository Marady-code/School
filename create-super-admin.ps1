# PowerShell script to create a Super Admin user
# This script requires the PostgreSQL client (psql) to be installed and in your PATH

$DB_HOST = "localhost"
$DB_PORT = "5432"
$DB_NAME = "School"
$DB_USER = "School"
$DB_PASS = "150304"

$SUPER_ADMIN_EMAIL = "superadmin@school.com"
$SUPER_ADMIN_USERNAME = "superadmin"
# Password hash for "superadmin123" - create your own with BCrypt if needed
$SUPER_ADMIN_PASSWORD = '$2a$10$4NUdPFI0.jTsJipG9dO8WuoybGPwqj7XaHdZYtdh520EgbK3TSlJC'

Write-Host "Checking if SUPER_ADMIN role exists..."
$roleCheck = @"
SELECT COUNT(*) FROM roles WHERE name = 'SUPER_ADMIN';
"@

$roleCount = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "$roleCheck")
$roleCount = $roleCount.Trim()

if ($roleCount -eq "0") {
    Write-Host "Creating SUPER_ADMIN role..."
    $createRole = @"
INSERT INTO roles (name, description, is_active, created_at, updated_at)
VALUES ('SUPER_ADMIN', 'Super Administrator with all privileges', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
"@
    psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -c "$createRole"
}

Write-Host "Checking if SUPER_ADMIN user exists..."
$userCheck = @"
SELECT COUNT(*) FROM users WHERE email = '$SUPER_ADMIN_EMAIL';
"@

$userCount = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "$userCheck")
$userCount = $userCount.Trim()

if ($userCount -eq "0") {
    Write-Host "Creating SUPER_ADMIN user..."
    $createUser = @"
INSERT INTO users (username, first_name, last_name, email, password, role, is_active, is_email_verified, created_at, updated_at)
VALUES ('$SUPER_ADMIN_USERNAME', 'Super', 'Admin', '$SUPER_ADMIN_EMAIL', '$SUPER_ADMIN_PASSWORD', 'SUPER_ADMIN', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
"@
    psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -c "$createUser"
    
    Write-Host "Getting user_id and role_id to create relationship..."
    $userId = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "SELECT user_id FROM users WHERE email = '$SUPER_ADMIN_EMAIL';")
    $userId = $userId.Trim()
    
    $roleId = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "SELECT role_id FROM roles WHERE name = 'SUPER_ADMIN';")
    $roleId = $roleId.Trim()
    
    Write-Host "Creating user_roles relationship..."
    $createUserRole = @"
INSERT INTO user_roles (user_id, role_id) VALUES ($userId, $roleId);
"@
    psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -c "$createUserRole"
} else {
    Write-Host "SUPER_ADMIN user already exists. Updating password and ensuring role assignment..."
    
    $updateUser = @"
UPDATE users SET password = '$SUPER_ADMIN_PASSWORD', is_active = true, role = 'SUPER_ADMIN' WHERE email = '$SUPER_ADMIN_EMAIL';
"@
    psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -c "$updateUser"
    
    Write-Host "Ensuring user has SUPER_ADMIN role in user_roles table..."
    $userId = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "SELECT user_id FROM users WHERE email = '$SUPER_ADMIN_EMAIL';")
    $userId = $userId.Trim()
    
    $roleId = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "SELECT role_id FROM roles WHERE name = 'SUPER_ADMIN';")
    $roleId = $roleId.Trim()
    
    $checkUserRole = @"
SELECT COUNT(*) FROM user_roles WHERE user_id = $userId AND role_id = $roleId;
"@
    $userRoleCount = (psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -t -c "$checkUserRole")
    $userRoleCount = $userRoleCount.Trim()
    
    if ($userRoleCount -eq "0") {
        $createUserRole = @"
INSERT INTO user_roles (user_id, role_id) VALUES ($userId, $roleId);
"@
        psql -h $DB_HOST -p $DB_PORT -d $DB_NAME -U $DB_USER -c "$createUserRole"
    }
}

Write-Host "SUPER_ADMIN user setup complete!"
Write-Host "Username: $SUPER_ADMIN_USERNAME"
Write-Host "Password: superadmin123"
