@echo off
setlocal enabledelayedexpansion

echo ==============================================================================
echo   Student Project and Skill Tracking System (SPSTS) - Build & Run Script
echo   Department of Information Technology - Velalar College of Engg and Tech
echo   Author: Gokul . S (Reg No: 732925ITR032) | Class: 25IT3A
echo ==============================================================================

rem Check if javac is on system PATH
where javac >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
    goto FOUND_JAVA
)

rem Check local appdata jdk17
for /d %%D in ("%LOCALAPPDATA%\jdk17\jdk*") do (
    if exist "%%D\bin\javac.exe" (
        set "JAVAC_CMD=%%D\bin\javac.exe"
        set "JAVA_CMD=%%D\bin\java.exe"
        goto FOUND_JAVA
    )
)

rem Check Program Files Java
for /d %%D in ("C:\Program Files\Java\jdk*" "C:\Program Files\Eclipse Adoptium\jdk*" "C:\Program Files\Microsoft\jdk*") do (
    if exist "%%D\bin\javac.exe" (
        set "JAVAC_CMD=%%D\bin\javac.exe"
        set "JAVA_CMD=%%D\bin\java.exe"
        goto FOUND_JAVA
    )
)

echo [!] Error: JDK 17+ (javac/java) could not be located on your machine.
echo Please install OpenJDK 17 or ensure javac is added to your PATH.
pause
exit /b 1

:FOUND_JAVA
echo [INFO] Using Java Compiler: "%JAVAC_CMD%"
echo [INFO] Using Java Runtime:  "%JAVA_CMD%"

rem Create bin directory
if not exist "bin" mkdir bin

echo [INFO] Compiling Java source files...
"%JAVAC_CMD%" -cp "lib\*;src" -d bin src\com\vcet\tracking\model\*.java src\com\vcet\tracking\exception\*.java src\com\vcet\tracking\dao\*.java src\com\vcet\tracking\service\*.java src\com\vcet\tracking\util\*.java src\com\vcet\tracking\Main.java

if %ERRORLEVEL% neq 0 (
    echo [!] Compilation Failed! Please check compilation errors above.
    pause
    exit /b %ERRORLEVEL%
)

echo [SUCCESS] Compilation successful!
echo [INFO] Starting Application...
echo.

"%JAVA_CMD%" -cp "bin;lib\*" com.vcet.tracking.Main

pause
