@echo off
cd /d "%~dp0"
call gradlew.bat clean build -x test --console=plain
pause

