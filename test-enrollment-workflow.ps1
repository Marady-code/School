# Test script for the new secure enrollment workflow
# This script demonstrates the admin-based user creation and first-login password change

$baseUrl = "http://localhost:8080"
$adminToken = ""
$userId = 0
$temporaryPassword = ""

# 1. Admin login
Write-Host "Logging in as admin..." -ForegroundColor Cyan
$adminLoginBody = @{
    "email" = "admin@schooldemo.com"
    "password" = "Admin123!"
} | ConvertTo-Json

$adminLoginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -ContentType "application/json" -Body $adminLoginBody

if ($adminLoginResponse.success) {
    Write-Host "Admin login successful!" -ForegroundColor Green
    $adminToken = $adminLoginResponse.token
    Write-Host "Token: $adminToken"
} else {
    Write-Host "Admin login failed!" -ForegroundColor Red
    exit 1
}

# 2. Create a new student through admin enrollment
Write-Host "`nCreating new student account..." -ForegroundColor Cyan
$studentData = @{
    "firstName" = "John"
    "lastName" = "Smith"
    "username" = "johnsmith"
    "email" = "johnsmith@example.com"
    "phoneNumber" = "555-123-4567"
    "grade" = "10"
    "section" = "A"
    "studentId" = "S10001"
    "sendCredentialsEmail" = $true
} | ConvertTo-Json

$headers = @{
    "Authorization" = "Bearer $adminToken"
    "Content-Type" = "application/json"
}

try {
    $studentResponse = Invoke-RestMethod -Uri "$baseUrl/api/enrollment/students" -Method Post -Headers $headers -Body $studentData
    $userId = $studentResponse.id
    Write-Host "Student created successfully with ID: $userId" -ForegroundColor Green

    # In a real script we would capture the temporary password from the response
    # Here we'll use a dummy value for demonstration
    $temporaryPassword = "Temp123!"
    Write-Host "Temporary password set: $temporaryPassword"
} catch {
    Write-Host "Failed to create student: $_" -ForegroundColor Red
    exit 1
}

# 3. Check if password change is required
Write-Host "`nChecking if password change is required..." -ForegroundColor Cyan
try {
    $passwordChangeRequired = Invoke-RestMethod -Uri "$baseUrl/api/enrollment/password-change-required/$userId" -Method Post -Headers $headers
    if ($passwordChangeRequired) {
        Write-Host "Password change is required for this user." -ForegroundColor Yellow
    } else {
        Write-Host "Password change is NOT required for this user." -ForegroundColor Green
    }
} catch {
    Write-Host "Failed to check password change status: $_" -ForegroundColor Red
}

# 4. Simulate first login and password change
Write-Host "`nSimulating first login and password change..." -ForegroundColor Cyan

# First login using temporary password
$loginBody = @{
    "email" = "johnsmith@example.com"
    "password" = $temporaryPassword
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -ContentType "application/json" -Body $loginBody
    
    if ($loginResponse.passwordChangeRequired) {
        Write-Host "Login successful, password change required as expected." -ForegroundColor Green
        
        # Change password
        $passwordChangeBody = @{
            "temporaryPassword" = $temporaryPassword
            "newPassword" = "NewSecurePass123!"
            "confirmNewPassword" = "NewSecurePass123!"
        } | ConvertTo-Json
        
        $userToken = $loginResponse.token
        $passwordChangeHeaders = @{
            "Authorization" = "Bearer $userToken"
            "Content-Type" = "application/json"
        }
        
        $changePasswordResponse = Invoke-RestMethod -Uri "$baseUrl/api/enrollment/password-change/$userId" -Method Post -Headers $passwordChangeHeaders -Body $passwordChangeBody
        
        Write-Host "Password changed successfully!" -ForegroundColor Green
        
        # Login again with new password
        $newLoginBody = @{
            "email" = "johnsmith@example.com"
            "password" = "NewSecurePass123!"
        } | ConvertTo-Json
        
        $newLoginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -ContentType "application/json" -Body $newLoginBody
        
        if (-not $newLoginResponse.passwordChangeRequired) {
            Write-Host "Logged in with new password successfully!" -ForegroundColor Green
        } else {
            Write-Host "Login successful but password change still required!" -ForegroundColor Yellow
        }
    } else {
        Write-Host "Login successful but password change was not required!" -ForegroundColor Yellow
    }
} catch {
    Write-Host "First login simulation failed: $_" -ForegroundColor Red
}

Write-Host "`nTest completed!" -ForegroundColor Cyan
