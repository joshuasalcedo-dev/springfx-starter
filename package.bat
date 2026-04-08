@echo off
setlocal

REM ============================================================
REM  Package script for AtlantaFX Starter
REM  Builds the fat JAR then runs jpackage to create an installer.
REM
REM  Usage:
REM    package.bat              Build .msi installer (default)
REM    package.bat exe          Build .exe installer
REM    package.bat jar          Build fat JAR only
REM ============================================================

set APP_NAME=AtlantaFX Starter
set ICON_SRC=src\main\resources\assets\icons\app-icon.png
set ICON_DST=src\main\packaging\app-icon.ico
set INSTALLER_TYPE=%1
if "%INSTALLER_TYPE%"=="" set INSTALLER_TYPE=MSI
REM Normalize to uppercase for jpackage-maven-plugin enum
if /i "%INSTALLER_TYPE%"=="msi" set INSTALLER_TYPE=MSI
if /i "%INSTALLER_TYPE%"=="exe" set INSTALLER_TYPE=EXE

REM --- Step 1: Convert PNG to ICO if needed ---
if not exist "%ICON_DST%" (
    echo [info] Converting app icon to .ico format...
    where magick >nul 2>&1
    if %errorlevel%==0 (
        magick "%ICON_SRC%" -define icon:auto-resize=256,128,64,48,32,16 "%ICON_DST%"
        echo [info] Icon converted successfully.
    ) else (
        echo [warn] ImageMagick not found. Skipping .ico conversion.
        echo [warn] Install ImageMagick or manually place app-icon.ico in src\main\packaging\
        echo [warn] jpackage will use a default icon.
    )
)

REM --- Step 2: Build ---
if "%INSTALLER_TYPE%"=="jar" (
    echo [info] Building fat JAR...
    call mvn clean package -DskipTests
) else (
    echo [info] Building %INSTALLER_TYPE% installer...
    call mvn clean package -Pinstaller -Dinstaller.type=%INSTALLER_TYPE% -DskipTests
)

if %errorlevel% neq 0 (
    echo [error] Build failed.
    exit /b 1
)

echo.
echo [done] Build complete.
if "%INSTALLER_TYPE%"=="jar" (
    echo        Output: target\fx-*.jar
) else (
    echo        Output: target\installer\
)

endlocal
