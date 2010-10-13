@echo off

rem
rem  All content copyright (c) 2003-2009 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

setlocal

set workdir=%~d0%~p0..
cd %workdir%
call bin\stopNode.bat 8080
call bin\stopNode.bat 8081

endlocal
