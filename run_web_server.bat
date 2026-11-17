@echo off
setlocal

echo ==============================================================================
echo   SPSTS - Local Web Server Dashboard
echo   Department of Information Technology - Velalar College of Engg and Tech
echo   Author: Gokul . S (Reg No: 732925ITR032) - Class: 25IT3A
echo ==============================================================================

set "PYTHON_EXE=%LOCALAPPDATA%\Programs\Python\Python312\python.exe"

if not exist "%PYTHON_EXE%" (
    where python >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        set "PYTHON_EXE=python"
    ) else (
        echo [INFO] Opening index.html directly in browser...
        start "" "%~dp0index.html"
        pause
        exit /b 0
    )
)

echo [INFO] Starting local web server on port 8080...
echo [INFO] Serving Student Project and Skill Tracking System Dashboard at:
echo        http://localhost:8080
echo.
echo Press Ctrl+C in this terminal to stop the server anytime.
echo.

start http://localhost:8080
"%PYTHON_EXE%" -m http.server 8080
