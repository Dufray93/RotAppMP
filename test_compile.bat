@echo off
echo Compilando proyecto RotAppMP...
cd /d "C:\Users\fray9\AndroidStudioProjects\RotAppMP"
gradlew.bat :composeApp:assembleDebug --stacktrace > compile_output.txt 2>&1
echo Compilación completada. Revisa compile_output.txt para ver el resultado.
type compile_output.txt
pause

