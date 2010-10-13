@echo off

rem  All content copyright (c) 2003-2009 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.

setlocal
goto :start

:printHelp
  echo "Usage: h2db.sh [ help | start | stop | console | create-exam-db ]"
  echo   help           Print this help and exit
  echo   start          Start the H2 Server
  echo   stop           Stop the H2 Server
  echo   console        Start the H2 Console
  echo   create-exam-db Create exam database
  exit
goto:eof

:startH2Server
	echo Starting H2 Server...
	cd "%dbBaseDir%"
	start "H2 server" "%JAVA_HOME%\bin\java" -cp "%H2_CLASSPATH%" org.h2.tools.Server -tcp -tcpAllowOthers
goto:eof

:stopH2Server
	echo Stopping H2 Server...
	cd "%dbBaseDir%"
	"%JAVA_HOME%\bin\java" -cp "%H2_CLASSPATH%" org.h2.tools.Server -tcpShutdown tcp://localhost:9092
goto:eof

:startH2Console
	echo Starting H2 Console...
	cd "%dbBaseDir%"
	start /b "H2 console" "%JAVA_HOME%\bin\java" -cp "%H2_CLASSPATH%" org.h2.tools.Console -browser
goto:eof

:createDatabase
	echo Creating database exam...
	"%JAVA_HOME%\bin\java" -cp "%H2_CLASSPATH%" org.h2.tools.RunScript -script conf/schema.sql -url jdbc:h2:tcp://localhost/target/data/exam
goto:eof

:start

if "%1" == "help" call :printHelp
if "%1" == "" call :printHelp

if not defined JAVA_HOME (
  echo JAVA_HOME environment variable must be set
  exit /b 1
)

set workdir=%~d0%~p0..
set H2_CLASSPATH=%workdir%\webapps\examinator\WEB-INF\lib\h2-1.1.116.jar
set dbBaseDir=%workdir%\work\h2data

if not exist "%dbBaseDir%" (
  mkdir "%dbBaseDir%" > NUL
)

cd "%workdir%"

if "%1" == "start" (
  call :startH2Server
)

if "%1" == "console" (
  call :startH2Console
)

if "%1" == "stop" (
  call :stopH2Server
)

if "%1" == "create-exam-db" (
  call :createDatabase
)

endlocal
