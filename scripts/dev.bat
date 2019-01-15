@Echo off
echo Apply Script: COPY
echo %1
echo F|xcopy /y /s /f /q "%1" "C:\Users\cyberpwn\Documents\development\servers\dynamic\plugins\Phantom.jar"
echo F|xcopy /y /s /f /q "%1" "C:\Users\cyberpwn\Documents\development\servers\dynamic\bungeecord\plugins\Phantom.jar"