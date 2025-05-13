Write-Host "Testing role-based access and class creation..." -ForegroundColor Cyan

# First authenticate as admin
try {
    Write-Host "Authenticating as admin..." -ForegroundColor Yellow
    $loginBody = @{
        email = "admin@example.com"
        password = "password123"
    } | ConvertTo-Json

    $auth = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "Admin authentication: SUCCESS" -ForegroundColor Green
    
    # Store token for subsequent requests
    $token = $auth.token
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    # Create a new class (requires ADMIN role)
    Write-Host "`nCreating a new class..." -ForegroundColor Yellow
    try {
        $classBody = @{
            className = "Mathematics 101"
            description = "Introduction to Mathematics"
            startDate = "2025-05-15"
            endDate = "2025-08-15"
            capacity = 30
        } | ConvertTo-Json

        $newClass = Invoke-RestMethod -Uri "http://localhost:8080/api/classes" -Method Post -Body $classBody -ContentType "application/json" -Headers $headers
        Write-Host "Class creation response: " -NoNewline
        $newClass | ConvertTo-Json -Depth 3
        Write-Host "Class creation: SUCCESS" -ForegroundColor Green
        
        # Store class ID
        $classId = $newClass.id
        
        # Now authenticate as a teacher
        Write-Host "`nAuthenticating as teacher..." -ForegroundColor Yellow
        $teacherLoginBody = @{
            email = "testuser@example.com"  # Use the previously created teacher account
            password = "password123"
        } | ConvertTo-Json

        $teacherAuth = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $teacherLoginBody -ContentType "application/json"
        Write-Host "Teacher authentication: SUCCESS" -ForegroundColor Green
        
        # Store teacher token
        $teacherToken = $teacherAuth.token
        $teacherHeaders = @{
            "Authorization" = "Bearer $teacherToken"
        }
        
        # Try to view the class details as a teacher (should work)
        Write-Host "`nViewing class details as teacher..." -ForegroundColor Yellow
        try {
            $classDetails = Invoke-RestMethod -Uri "http://localhost:8080/api/classes/$classId" -Method Get -Headers $teacherHeaders
            Write-Host "Class details response: " -NoNewline
            $classDetails | ConvertTo-Json -Depth 3
            Write-Host "Viewing class details as teacher: SUCCESS" -ForegroundColor Green
        } catch {
            Write-Host "Viewing class details as teacher: FAILED" -ForegroundColor Red
            Write-Host $_.Exception.Message
        }
        
    } catch {
        Write-Host "Class creation: FAILED" -ForegroundColor Red
        Write-Host $_.Exception.Message
    }
    
} catch {
    Write-Host "Admin authentication: FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
