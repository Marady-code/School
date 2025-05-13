Write-Host "Testing School Management System API..." -ForegroundColor Cyan

# Test health endpoint
try {
    Write-Host "Testing health endpoint..." -ForegroundColor Yellow
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/public/health" -Method Get
    Write-Host "Health endpoint response: " -NoNewline
    $health | ConvertTo-Json -Depth 3
    Write-Host "Health endpoint test: SUCCESS" -ForegroundColor Green
} catch {
    Write-Host "Health endpoint test: FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}

# Test authentication
try {
    Write-Host "`nTesting authentication..." -ForegroundColor Yellow
    $loginBody = @{
        email = "admin@example.com"
        password = "password123"
    } | ConvertTo-Json

    $auth = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "Authentication response: " -NoNewline
    $auth | ConvertTo-Json -Depth 3
    Write-Host "Authentication test: SUCCESS" -ForegroundColor Green
    
    # Store token for subsequent requests
    $token = $auth.token
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    # Test protected endpoint (students)
    Write-Host "`nTesting protected endpoint (students)..." -ForegroundColor Yellow
    try {
        $students = Invoke-RestMethod -Uri "http://localhost:8080/api/students" -Method Get -Headers $headers
        Write-Host "Protected endpoint response: " -NoNewline
        $students | ConvertTo-Json -Depth 3
        Write-Host "Protected endpoint test: SUCCESS" -ForegroundColor Green
    } catch {
        Write-Host "Protected endpoint test: FAILED" -ForegroundColor Red
        Write-Host $_.Exception.Message
    }
    
} catch {
    Write-Host "Authentication test: FAILED" -ForegroundColor Red
    Write-Host $_.Exception.Message
}
