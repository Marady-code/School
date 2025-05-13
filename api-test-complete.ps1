Write-Host "Testing School Management System API" -ForegroundColor Cyan

function Invoke-ApiRequest {
    param (
        [string]$Method = "GET",
        [string]$Endpoint,
        [object]$Body = $null,
        [hashtable]$Headers = @{},
        [string]$Description = ""
    )
    
    $fullUrl = "http://localhost:8080$Endpoint"
    $bodyJson = if ($Body) { $Body | ConvertTo-Json -Depth 10 } else { $null }
    
    Write-Host "`n$Description - $Method $fullUrl" -ForegroundColor Yellow
    
    try {
        $params = @{
            Uri = $fullUrl
            Method = $Method
            ContentType = "application/json"
            Headers = $Headers
        }
        
        if ($Body -and $Method -ne "GET") {
            $params.Body = $bodyJson
        }
        
        $response = Invoke-RestMethod @params -ErrorVariable apiError
        
        Write-Host "SUCCESS: Status 200" -ForegroundColor Green
        $response | ConvertTo-Json -Depth 5
        return $response
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "ERROR: Status $statusCode" -ForegroundColor Red
        
        if ($_.ErrorDetails) {
            Write-Host "Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
        }
        else {
            Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        }
        return $null
    }
}

# Test health endpoint
$health = Invoke-ApiRequest -Endpoint "/api/public/health" -Description "Health Check"

# Test registration
$registerBody = @{
    firstName = "Admin"
    lastName = "User"
    email = "admin@example.com"
    password = "password123"
    role = "ADMIN"
}

$registration = Invoke-ApiRequest -Method "POST" -Endpoint "/api/auth/register" -Body $registerBody -Description "Register Admin User"

if ($registration) {
    $token = $registration.token
    $authHeaders = @{
        "Authorization" = "Bearer $token"
    }
    
    # Test protected endpoints
    Invoke-ApiRequest -Endpoint "/api/students" -Headers $authHeaders -Description "Get Students (Admin)"
    
    # Create a class
    $classBody = @{
        className = "Mathematics 101"
        description = "Introduction to Mathematics"
        startDate = "2025-05-15"
        endDate = "2025-08-15"
        capacity = 30
    }
    
    $class = Invoke-ApiRequest -Method "POST" -Endpoint "/api/classes" -Body $classBody -Headers $authHeaders -Description "Create Class (Admin)"
    
    # Test teacher registration
    $teacherBody = @{
        firstName = "Teacher"
        lastName = "User"
        email = "teacher@example.com"
        password = "password123"
        role = "TEACHER"
    }
    
    $teacherReg = Invoke-ApiRequest -Method "POST" -Endpoint "/api/auth/register" -Body $teacherBody -Description "Register Teacher User"
    
    if ($teacherReg) {
        $teacherToken = $teacherReg.token
        $teacherHeaders = @{
            "Authorization" = "Bearer $teacherToken"
        }
        
        # Test teacher access
        Invoke-ApiRequest -Endpoint "/api/classes" -Headers $teacherHeaders -Description "Get Classes (Teacher)"
        
        # Test student registration
        $studentBody = @{
            firstName = "Student"
            lastName = "User"
            email = "student@example.com"
            password = "password123"
            role = "STUDENT"
        }
        
        $studentReg = Invoke-ApiRequest -Method "POST" -Endpoint "/api/auth/register" -Body $studentBody -Description "Register Student User"
        
        if ($studentReg) {
            $studentToken = $studentReg.token
            $studentHeaders = @{
                "Authorization" = "Bearer $studentToken"
            }
            
            # Test student access
            Invoke-ApiRequest -Endpoint "/api/classes" -Headers $studentHeaders -Description "Get Classes (Student)"
        }
    }
}

Write-Host "`nAPI Testing Completed" -ForegroundColor Cyan
