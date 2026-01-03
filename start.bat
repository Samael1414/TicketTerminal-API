@echo off
setlocal enabledelayedexpansion
cd /d %~dp0

docker compose up -d --build

echo Waiting for API container to become healthy (max 180s)...
set /a SECS=0

:wait
for /f "usebackq delims=" %%S in (`docker inspect -f "{{.State.Health.Status}}" ticketterminal-api-ticket-terminal-api-1 2^>nul`) do set STATUS=%%S

if /i "!STATUS!"=="healthy" goto ready

set /a SECS+=1
if !SECS! GEQ 180 goto timeout
timeout /t 1 >nul
goto wait

:ready
start "" "http://localhost/"
exit /b 0

:timeout
echo API is not healthy after 180 seconds.
echo Showing last 200 logs from API container...
docker compose logs --tail=200 ticket-terminal-api
pause
exit /b 1
