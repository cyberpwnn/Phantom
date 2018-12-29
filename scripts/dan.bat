@Echo off
echo Apply Script: COPY
echo %1
echo F|xcopy /y /s /f /q "%1" "C:\Users\Dan\Documents\development\servers\dynamic\plugins\SkyPrime.jar"