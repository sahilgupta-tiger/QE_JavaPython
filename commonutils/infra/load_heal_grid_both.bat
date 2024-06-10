call pg_ctl -D "D:\My_Programs\PostgreSQL\data" -l logfile start
timeout 5
cd selenium-grid
start cmd /K start_seleniumserver.bat
cd ..
timeout 5
"%LOCALAPPDATA%\Programs\Git\usr\bin\sh.exe" --login -i -c "sh start_healenium.sh"