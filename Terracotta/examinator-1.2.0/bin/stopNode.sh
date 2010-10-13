#!/bin/bash

#  All content copyright (c) 2003-2009 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.

function printHelp {
  echo "Usage: $0 <port>"
  echo "port: 8080 or 8081"
  exit 0
}

if [ "$1" = "help" ]; then
  printHelp
fi

cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

case "$1" in
  8080|8081) port=$1 ;;
  *) printHelp ;;
esac

workdir=`dirname $0`/..
workdir=`cd "${workdir}" && pwd`
cd "${workdir}"

JETTY_HOME="${workdir}/vendor/jetty-6.1.15"

if $cygwin; then
  JETTY_HOME=`cygpath -d "$JETTY_HOME"`
fi

stop_port=`expr $port + 1001`
echo "Stopping jetty instance at port $port"
java -DSTOP.PORT=$stop_port -DSTOP.KEY=secret -Djetty.home="${JETTY_HOME}" -jar "$JETTY_HOME/start.jar" --stop
