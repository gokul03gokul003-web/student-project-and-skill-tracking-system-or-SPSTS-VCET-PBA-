# Universal PowerShell Web Launcher for SPSTS
$ProjectRoot = $PSScriptRoot
Set-Location $ProjectRoot

Write-Host "==============================================================================" -ForegroundColor Cyan
Write-Host "  SPSTS: Launching Project Web Application Dashboard..." -ForegroundColor Green
Write-Host "  Author: Gokul . S (Reg No: 732925ITR032) | Class: 25IT3A" -ForegroundColor Yellow
Write-Host "==============================================================================" -ForegroundColor Cyan

# 1. Immediately open index.html in the default web browser
Start-Process "$ProjectRoot\index.html"
Write-Host "[SUCCESS] Web Dashboard opened in your browser: file:///$ProjectRoot/index.html" -ForegroundColor Green

# 2. Check if Python is available to also host on http://localhost:8080
$PythonExe = "$env:LOCALAPPDATA\Programs\Python\Python312\python.exe"
if (-not (Test-Path $PythonExe)) {
    $PythonCmd = Get-Command python -ErrorAction SilentlyContinue
    if ($PythonCmd) { $PythonExe = "python" }
}

if (Test-Path $PythonExe) {
    Write-Host "`n[INFO] Starting local HTTP server on http://localhost:8080..." -ForegroundColor Cyan
    Write-Host "[INFO] Press Ctrl+C anytime to stop the server.`n" -ForegroundColor Yellow
    & $PythonExe -m http.server 8080
}
