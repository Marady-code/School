@echo off
echo Testing School Management System API...
echo.

REM Test the health endpoint
curl -v http://localhost:8080/api/public/health

echo.
echo Testing complete!
