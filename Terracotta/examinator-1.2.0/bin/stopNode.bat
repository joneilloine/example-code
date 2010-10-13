@echo off

rem
rem  All content copyright (c) 2003-2009 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

setlocal
set port=%1

if not defined JAVA_HOME (
  echo JAVA_HOME environment variable must be set
  exit /b 1
)

set JAVA_HOME="%JAVA_HOME:"=%"
set workdir=%~d0%~p0..
set workdir="%workdir:"=%"
cd %workdir%

set JETTY_HOME=%workdir%\vendor\jetty-6.1.15

if "%port%" == "8080" (
  goto set_stop_port
) else if "%port%" == "8081" (
  goto set_stop_port
) else (
  echo Usage: %~n0 [port]
  echo port: 8080 or 8081
  exit /b 1
)

:set_stop_port
set /a stop_port=%port%+1001

echo Stopping jetty instance at port %port%
%JAVA_HOME%\bin\java -DSTOP.PORT=%stop_port% -DSTOP.KEY=secret -Djetty.home=%JETTY_HOME% -jar %JETTY_HOME%\start.jar --stop
 
endlocal
