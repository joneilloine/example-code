@echo off

rem
rem  All content copyright (c) 2003-2009 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

if "%1" == "help" goto printHelp
if "%1" == "" goto printHelp

if not defined JAVA_HOME (
  echo JAVA_HOME environment variable must be set
  exit /b 1
)

if not defined TC_INSTALL_DIR (
  echo TC_INSTALL_DIR environment variable must be set to Terracotta installation
  exit /b 1
)

setlocal
set TC_INSTALL_DIR="%TC_INSTALL_DIR:"=%"
set JAVA_HOME="%JAVA_HOME:"=%"
set workdir=%~d0%~p0..
set workdir="%workdir:"=%"

rem make sure JAVA_HOME and TC_INSTALL_DIR are pointing to existing dirs
if not exist %JAVA_HOME% (
  echo JAVA_HOME points to non-existed path: %JAVA_HOME%
  exit /b 1
)

if not exist %TC_INSTALL_DIR% (
  echo TC_INSTALL_DIR points to non-existed path: %TC_INSTALL_DIR%
  exit /b 1
)

set port=%1
set tc_enabled=false
if "%2" == "-tc" set tc_enabled=true

if "%port%" == "8080" (
  set stop_port=9081
)  else if "%port%" == "8081" ( 
  set stop_port=9082
) else (
  echo port must be specified: 8080 or 8081
  exit /b 1
)

set base=%workdir%\work\%port%
set base="%base:"=%"

echo Starting web server on port %port%
echo Terracotta enabled: %tc_enabled%


if "%tc_enabled%" == "true" (
 goto enable_tc
) else (
 goto next
)

:enable_tc
set TC_CONFIG_PATH=%workdir%\tc-config.xml
call %TC_INSTALL_DIR%\bin\dso-env.bat -q
set JAVA_OPTS=%TC_JAVA_OPTS% %JAVA_OPTS%

:next
set JETTY_HOME=%workdir%\vendor\jetty-6.1.15

if not exist %JETTY_HOME% (
  echo JETTY_HOME is not found at %JETTY_HOME%
  exit /b 1
)

if not exist %base%\webapps\examinator (
  mkdir %base%\webapps\examinator > NUL
)
xcopy /e /y %workdir%\webapps\examinator %base%\webapps\examinator >NUL

rem when running without TC, the api jar needs to be in the classpath of the webapp
rem but not when TC is enabled
if "%tc_enabled%" == "true" (
  del %base%\webapps\examinator\WEB-INF\lib\terracotta-api-*.jar 2>NUL
) else (
  copy /Y %TC_INSTALL_DIR%\lib\terracotta-api-*.jar %base%\webapps\examinator\WEB-INF\lib >NUL
)

set console_log=%base%\logs\console.log
cd "%base%"

start /b "jetty-%port%" %JAVA_HOME%\bin\java %JAVA_OPTS% -DSTOP.PORT=%stop_port% -DSTOP.KEY=secret -Dconfig.home=%base% -Djetty.home=%JETTY_HOME% -jar %JETTY_HOME%\start.jar etc\jetty.xml > %console_log% 2>&1

echo Check startup log at work\%port%\logs\console.log
echo After the server has started, hit the url http://localhost:%port%/examinator
echo.
endlocal

goto end

:printHelp
  echo Usage: startnode.sh [port] [-tc]
  echo port: 8080 or 8081
  echo -tc: start with Terracotta enabled

:end
