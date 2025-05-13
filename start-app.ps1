Write-Host "Starting School Management System Application..." -ForegroundColor Cyan

try {
    # Kill existing java process related to this application if any are running
    $javaPids = Get-WmiObject Win32_Process -Filter "name = 'java.exe'" | Where-Object { $_.CommandLine -like "*School-0.0.1-SNAPSHOT.jar*" } | Select-Object -ExpandProperty ProcessId
    foreach ($pid in $javaPids) {
        Write-Host "Stopping existing Java process with PID: $pid" -ForegroundColor Yellow
        Stop-Process -Id $pid -Force
    }
    
    Write-Host "Starting application..." -ForegroundColor Yellow
    
    # Start the application
    $jarPath = "g:\SchoolManagementSystem\SchoolBackend\target\School-0.0.1-SNAPSHOT.jar"
    
    # Create a new process with captured output
    $processInfo = New-Object System.Diagnostics.ProcessStartInfo
    $processInfo.FileName = "java"
    $processInfo.Arguments = "-jar `"$jarPath`""
    $processInfo.RedirectStandardError = $true
    $processInfo.RedirectStandardOutput = $true
    $processInfo.UseShellExecute = $false
    
    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $processInfo
    
    # Set up event handlers for output
    $outputSb = New-Object System.Text.StringBuilder
    $errorSb = New-Object System.Text.StringBuilder
    
    $outputHandler = [System.EventHandler[System.Diagnostics.DataReceivedEventArgs]]{
        if (-not [String]::IsNullOrEmpty($EventArgs.Data)) {
            $outputSb.AppendLine($EventArgs.Data) | Out-Null
            Write-Host $EventArgs.Data -ForegroundColor Gray
        }
    }
    
    $errorHandler = [System.EventHandler[System.Diagnostics.DataReceivedEventArgs]]{
        if (-not [String]::IsNullOrEmpty($EventArgs.Data)) {
            $errorSb.AppendLine($EventArgs.Data) | Out-Null
            Write-Host $EventArgs.Data -ForegroundColor Red
        }
    }
    
    $process.OutputDataReceived += $outputHandler
    $process.ErrorDataReceived += $errorHandler
    
    # Start the process and capture output
    $process.Start() | Out-Null
    $process.BeginOutputReadLine()
    $process.BeginErrorReadLine()
    
    Write-Host "Application starting... (PID: $($process.Id))" -ForegroundColor Green
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow

    # Wait for the process to exit (which it shouldn't unless there's an error)
    $process.WaitForExit()
    
    Write-Host "Process exited with code: $($process.ExitCode)" -ForegroundColor $(if($process.ExitCode -eq 0) { "Green" } else { "Red" })
    
} catch {
    Write-Host "Error starting application: $_" -ForegroundColor Red
}
