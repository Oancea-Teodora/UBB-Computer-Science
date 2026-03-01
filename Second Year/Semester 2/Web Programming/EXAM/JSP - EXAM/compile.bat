@echo off
echo Compiling Tic Tac Toe Web Application...

REM Create output directory
if not exist "target\classes" mkdir "target\classes"

REM Download servlet API if not present
if not exist "lib" mkdir "lib"

echo.
echo Note: You'll need servlet-api.jar to compile this project.
echo You can download it from:
echo https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/3.1.0
echo.
echo Place servlet-api-3.1.0.jar in the 'lib' directory and run this script again.
echo.

REM Check if servlet API exists
if not exist "lib\servlet-api-3.1.0.jar" (
    echo servlet-api-3.1.0.jar not found in lib directory!
    echo Please download and place it in the lib directory.
    pause
    exit /b 1
)

REM Compile Java classes
echo Compiling Java classes...
javac -cp "lib\servlet-api-3.1.0.jar" -d "target\classes" src\main\java\com\tictactoe\model\*.java src\main\java\com\tictactoe\util\*.java src\main\java\com\tictactoe\servlet\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo To create a WAR file manually:
echo 1. Create a directory called 'tictactoe'
echo 2. Copy src\main\webapp contents to 'tictactoe'
echo 3. Create 'tictactoe\WEB-INF\classes' directory
echo 4. Copy 'target\classes\com' directory to 'tictactoe\WEB-INF\classes\'
echo 5. Create a ZIP file of the 'tictactoe' directory and rename it to 'tictactoe.war'
echo.
pause 