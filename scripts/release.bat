@Echo off
echo Apply Script: COPY
echo %1
echo F|xcopy /y /s /f /q "%1" "%2"