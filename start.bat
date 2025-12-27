@echo off
REM Library Management System - Windows Startup Script
REM Version 2.0.0

echo ================================
echo Library Management System v2.0.0
echo ================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 11 or higher
    pause
    exit /b 1
)

echo [1/4] Checking Java installation... OK
echo.

REM Check if MySQL is running
echo [2/4] Checking MySQL connection...
REM You can add MySQL check here if needed
echo.

REM Set memory options
set JAVA_OPTS=-Xms512m -Xmx1024m

REM Check if JAR file exists
if not exist "target\library-management-system-2.0.0.jar" (
    echo [3/4] JAR file not found. Building with Maven...
    call mvn clean package -DskipTests
    if %errorlevel% neq 0 (
        echo ERROR: Maven build failed
        pause
        exit /b 1
    )
) else (
    echo [3/4] JAR file found... OK
)
echo.

echo [4/4] Starting application...
echo.
echo ================================
echo Application is starting...
echo Close this window to stop the application
echo ================================
echo.

REM Run the application
java %JAVA_OPTS% -jar target\library-management-system-2.0.0.jar

pause
