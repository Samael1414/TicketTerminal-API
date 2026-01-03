@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d %~dp0

docker compose up -d --build

echo Waiting for API container to become healthy (max 180s)...
set /a SECS=0

REM Получаем ID контейнера сервиса (не имя!)
for /f "usebackq delims=" %%I in (`docker compose ps -q ticket-terminal-api`) do set CID=%%I

if not defined CID (
  echo ERROR: API container id not found. Showing compose ps...
  docker compose ps
  pause
  exit /b 1
)

:wait
set "STATUS="
for /f "usebackq delims=" %%S in (`docker inspect -f "{{if .State.Health}}{{.State.Health.Status}}{{else}}nohealth{{end}}" !CID! 2^>nul`) do set "STATUS=%%S"

if /i "!STATUS!"=="healthy" goto ready
if /i "!STATUS!"=="unhealthy" goto unhealthy

set /a SECS+=1
if !SECS! GEQ 180 goto timeout
timeout /t 1 >nul
goto wait

:ready
echo OK: API is healthy. Opening UI...
start "" "http://localhost/"
echo.
echo You can close this window.
pause
exit /b 0

:unhealthy
echo ERROR: API became UNHEALTHY. Showing last 200 logs...
docker compose logs --tail=200 ticket-terminal-api
pause
exit /b 1

:timeout
echo ERROR: API did not become healthy after 180s. Showing last 200 logs...
docker compose logs --tail=200 ticket-terminal-api
pause
exit /b 1
