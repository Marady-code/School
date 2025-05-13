Write-Host "Registering a new user in the School Management System..." -ForegroundColor Cyan

# Test user registration
try {
    Write-Host "Registering new test user..." -ForegroundColor Yellow
    
    $registerBody = @{
        firstName = "Test"
        lastName = "User"
        email = "testuser@example.com"
        password = "password123"
        role = "TEACHER"  # Valid roles: ADMIN, TEACHER, STUDENT, PARENT
    } | ConvertTo-Json

    $registration = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
    
    Write-Host "Registration response: " -NoNewline
    $registration | ConvertTo-Json -Depth 3
    Write-Host "Registration test: SUCCESS" -ForegroundColor Green
    
    # Store token for subsequent requests
    $token = $registration.token
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    # Test protected endpoint with the new user
    Write-Host "`nTesting protected endpoint access with new user token..." -ForegroundColor Yellow
    try {
        $teachers = Invoke-RestMethod -Uri "http://localhost:8080/api/teachers" -Method Get -Headers $headers
        Write-Host "Protected endpoint response: " -NoNewline
        $teachers | ConvertTo-Json -Depth 3
        Write-Host "Protected endpoint test: SUCCESS" -ForegroundColor Green
    } catch {
        Write-Host "Protected endpoint test: FAILED" -ForegroundColor Red
        Write-Host $_.Exception.Message
    }
    
} catch {
    Write-Host "Registration test: FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
