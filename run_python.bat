@echo off
echo ==============================================================================
echo   SPSTS - Python Data Science & Visualization Runner
echo   Department of Information Technology - Velalar College of Engg and Tech
echo   Author: Gokul . S (Reg No: 732925ITR032) | Class: 25IT3A
echo ==============================================================================

set "PYTHON_EXE=%LOCALAPPDATA%\Programs\Python\Python312\python.exe"

if not exist "%PYTHON_EXE%" (
    where python >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        set "PYTHON_EXE=python"
    ) else (
        echo [!] Python executable not found. Please install Python 3.9+ or run with python on PATH.
        pause
        exit /b 1
    )
)

echo [INFO] Using Python: %PYTHON_EXE%
echo [INFO] Running Data Science Pipeline and Generating Visualizations...
echo.

"%PYTHON_EXE%" python\data_analysis.py

echo.
echo [SUCCESS] Visualization charts saved to 'visualizations/' directory.
echo [SUCCESS] Summary metrics written to 'sample_outputs/python_execution_output.txt'.
pause
