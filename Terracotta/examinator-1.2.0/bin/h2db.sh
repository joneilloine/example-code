#!/bin/bash

#  All content copyright (c) 2003-2009 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.

function printHelp {
  echo "Usage: h2db.sh [ help | start | stop | console | create-exam-db ]"
  echo "  help           Print this help and exit"
  echo "  start          Start the H2 Server"
  echo "  stop           Stop the H2 Server"
  echo "  console        Start the H2 Console"
  echo "  create-exam-db Create exam database"
  echo ""
}


function startH2Server {
	echo "Starting H2 Server..."
	cd "$dbBaseDir"
	"$JAVA_HOME/bin/java" -cp "$H2_CLASSPATH" org.h2.tools.Server -tcp -tcpAllowOthers &
  sleep 3
}

function stopH2Server {
	echo "Stopping H2 Server..."
	cd "$dbBaseDir"
	"$JAVA_HOME/bin/java" -cp "$H2_CLASSPATH" org.h2.tools.Server -tcpShutdown tcp://localhost:9092
}

function startH2Console {
	echo "Starting H2 Console..."
	cd "$dbBaseDir"
	"$JAVA_HOME/bin/java" -cp "$H2_CLASSPATH" org.h2.tools.Console -browser &
}

function createDatabase {
	echo "Creating database exam..."
	"$JAVA_HOME/bin/java" -cp "$H2_CLASSPATH" org.h2.tools.RunScript \
  -script conf/schema.sql -url jdbc:h2:tcp://localhost/target/data/exam
}

if [ "$1" = "-help" ]; then
  printHelp
  exit
fi

if [ $# -eq 0 ]; then
  printHelp
  exit
fi

if [ "$1" != "start" -a "$1" != "console" -a "$1" != "stop" -a "$1" != "create-exam-db" ]; then
  printHelp
  exit
fi

if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME environment variable must be set"
  exit 1
fi

cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

workdir=`dirname $0`/..
workdir=`cd "$workdir" && pwd`
cd "$workdir"

LIB="$workdir/webapps/examinator/WEB-INF/lib"
if $cygwin; then
  LIB=`cygpath -d "$LIB"`
fi

H2_CLASSPATH="$LIB/h2-1.1.116.jar"

dbBaseDir="$workdir/work/h2data"
mkdir -p "$dbBaseDir"

if [ "$1" = "start" ]; then
  startH2Server
fi

if [ "$1" = "console" ]; then
  startH2Console
fi

if [ "$1" = "stop" ]; then
  stopH2Server
fi

if [ "$1" = "create-exam-db" ]; then
  createDatabase
fi


