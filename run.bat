@echo off
echo ==============================================================================
echo   SPSTS - Universal Project Runner (PowerShell Auto-Launch)
echo   Department of Information Technology - Velalar College of Engg and Tech
echo   Author: Gokul . S (Reg No: 732925ITR032) - Class: 25IT3A
echo ==============================================================================
echo.

powershell -ExecutionPolicy Bypass -File "%~dp0run.ps1" %*

pause
