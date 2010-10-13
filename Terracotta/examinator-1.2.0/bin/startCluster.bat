@echo off

rem
rem  All content copyright (c) 2003-2009 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

setlocal

set workdir=%~d0%~p0..
cd "%workdir%"
call bin\startNode.bat 8080 -tc
if %ERRORLEVEL% NEQ 0 (
  echo Failed to start web server at port 8080
  exit /b 1
)

call bin\startNode.bat 8081 -tc
if %ERRORLEVEL% NEQ 0 (
  echo Failed to start web server at port 8081
  exit /b 1
) else (  
  echo Please wait for both web servers to start
)

endlocal
