# PowerShell script to test the User Management API endpoints

# Base URL
$baseUrl = "http://localhost:8080"

# Admin credentials
$adminUsername = "admin"
$adminPassword = "admin123"

# Super admin credentials
$superAdminUsername = "superadmin"
$superAdminPassword = "superadmin123"

# Function to get authentication token
function Get-AuthToken {
    param (
        [string]$username,
        [string]$password
    )
    
    $body = @{
        username = $username
        password = $password
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -ContentType "application/json" -Body $body
    return $response.token
}

# Get admin token
Write-Host "Getting admin token..."
$adminToken = Get-AuthToken -username $adminUsername -password $adminPassword
Write-Host "Admin token: $adminToken"

# Get super admin token
Write-Host "Getting super admin token..."
$superAdminToken = Get-AuthToken -username $superAdminUsername -password $superAdminPassword
Write-Host "Super Admin token: $superAdminToken"

# Test: Get all users (as admin)
Write-Host "`nTest: Get all users (as admin)"
$headers = @{
    Authorization = "Bearer $adminToken"
}
$allUsers = Invoke-RestMethod -Uri "$baseUrl/users" -Method Get -Headers $headers
$allUsers | ConvertTo-Json

# Test: Get all users (as super admin)
Write-Host "`nTest: Get all users (as super admin)"
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
$allUsers = Invoke-RestMethod -Uri "$baseUrl/users" -Method Get -Headers $headers
$allUsers | ConvertTo-Json

# Test: Create a new user (as super admin)
Write-Host "`nTest: Create a new user (as super admin)"
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
$newUser = @{
    firstName = "Test"
    lastName = "User"
    email = "testuser@school.com"
    username = "testuser"
    password = "Password123!"
    role = "TEACHER"
    isActive = $true
} | ConvertTo-Json

$createdUser = Invoke-RestMethod -Uri "$baseUrl/users" -Method Post -Headers $headers -ContentType "application/json" -Body $newUser
$createdUser | ConvertTo-Json

# Get the newly created user ID
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
$allUsers = Invoke-RestMethod -Uri "$baseUrl/users" -Method Get -Headers $headers
$newUserId = ($allUsers | Where-Object { $_.email -eq "testuser@school.com" }).id
Write-Host "`nNew user ID: $newUserId"

# Test: Update user (as admin)
Write-Host "`nTest: Update user (as admin)"
$headers = @{
    Authorization = "Bearer $adminToken"
}
$updatedUser = @{
    firstName = "Updated"
    lastName = "User"
    email = "testuser@school.com"
    username = "testuser"
    phoneNumber = "1234567890"
} | ConvertTo-Json

$result = Invoke-RestMethod -Uri "$baseUrl/users/$newUserId" -Method Put -Headers $headers -ContentType "application/json" -Body $updatedUser
$result | ConvertTo-Json

# Test: Deactivate user (as admin)
Write-Host "`nTest: Deactivate user (as admin)"
$headers = @{
    Authorization = "Bearer $adminToken"
}
$result = Invoke-RestMethod -Uri "$baseUrl/users/$newUserId/deactivate" -Method Post -Headers $headers
$result | ConvertTo-Json

# Test: Activate user (as super admin)
Write-Host "`nTest: Activate user (as super admin)"
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
$result = Invoke-RestMethod -Uri "$baseUrl/users/$newUserId/activate" -Method Post -Headers $headers
$result | ConvertTo-Json

# Test: Reset user password (as super admin)
Write-Host "`nTest: Reset user password (as super admin)"
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
$result = Invoke-RestMethod -Uri "$baseUrl/users/$newUserId/reset-password" -Method Post -Headers $headers
$result | ConvertTo-Json

# Test: Delete user (as super admin)
Write-Host "`nTest: Delete user (as super admin)"
$headers = @{
    Authorization = "Bearer $superAdminToken"
}
Invoke-RestMethod -Uri "$baseUrl/users/$newUserId" -Method Delete -Headers $headers

Write-Host "`nTests completed"
