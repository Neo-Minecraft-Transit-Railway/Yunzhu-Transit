@echo off
echo ========================================
echo   Build YTE for All MC Versions
echo ========================================
echo.
echo.
echo ----- Building for Minecraft 1.20.1 -----
call gradlew.bat :fabric:build :forge:build -PminecraftVersion=1.20.1 -x test --no-daemon
if %ERRORLEVEL% NEQ 0 (
    echo FAILED: Minecraft 1.20.1
    exit /b 1
)
echo.
echo ========================================
echo   All versions built successfully!
echo   JARs are in build/release/
echo ========================================
