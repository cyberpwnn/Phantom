@echo off
gource --camera-mode overview --seconds-per-day 1 -c 10 --auto-skip-seconds 1 --max-file-lag 100.8 --bloom-multiplier 1.0 --bloom-intensity 0.5 -e 0.1 --hide filenames,dirnames
pause