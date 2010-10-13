#!/bin/bash 

#  All content copyright (c) 2003-2009 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.

function printHelp {
  echo "Usage: startnode.sh [port] [-tc]"
  echo "port: 8080 or 8081"
  echo "-tc: start with Terracotta enabled"
}

if [ "$1" = "help" ]; then
  printHelp
  exit
fi

if [ $# -eq 0 ]; then
  printHelp
  exit
fi

if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME environment variable must be set"
  exit 1
fi

if [ ! -d "${JAVA_HOME}" ]; then
  echo "JAVA_HOME points to non-existed path: ${JAVA_HOME}"
  exit 1
fi

if [ -z "${TC_INSTALL_DIR}" ]; then
  echo "TC_INSTALL_DIR environment variable must be set to Terracotta installation"
  exit 1
fi

if [ ! -d "${TC_INSTALL_DIR}" ]; then
  echo "TC_INSTALL_DIR points to non-existed path: ${TC_INSTALL_DIR}"
  exit 1
fi

port=$1
tc_enabled=$2

case "$port" in
  8080|8081) ;;
  *) echo "port must be specified: 8080 or 8081" && exit 1 ;;
esac

case "$tc_enabled" in
  "-tc") tc_enabled=true ;;
  *) tc_enabled=false ;;
esac

stop_port=`expr $port + 1001`
cygwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
esac

workdir=`dirname $0`/..
workdir=`cd "${workdir}" && pwd`
cd "${workdir}"

echo "Starting web server on port $port"
echo "Terracotta enabled: $tc_enabled"

if $tc_enabled; then
  TC_CONFIG_PATH="${workdir}"/tc-config.xml
  if $cygwin; then
    TC_CONFIG_PATH=`cygpath -d "${TC_CONFIG_PATH}"`
  fi
  export TC_CONFIG_PATH
  . "${TC_INSTALL_DIR}"/bin/dso-env.sh -q
  export JAVA_OPTS="$TC_JAVA_OPTS $JAVA_OPTS"
fi

JETTY_HOME=${workdir}/vendor/jetty-6.1.15

if [ ! -d "$JETTY_HOME" ]; then
  echo "JETTY_HOME is not found"
  exit 1
fi

base=${workdir}/work/$port
cp -r "${workdir}"/webapps/examinator "${base}"/webapps

# when running without TC, the api jar needs to be in the classpath of the webapp
# but not when TC is enabled
if $tc_enabled; then
  rm -rf "${base}"/webapps/examinator/WEB-INF/lib/terracotta-api-*.jar
else
  cp "$TC_INSTALL_DIR"/lib/terracotta-api-*.jar "${base}"/webapps/examinator/WEB-INF/lib
fi

if $cygwin; then
  base=`cygpath -d "$base"`
  export JETTY_HOME=`cygpath -d "$JETTY_HOME"`
fi
console_log="${base}"/logs/console.log
cd "${base}"

"$JAVA_HOME"/bin/java $JAVA_OPTS -DSTOP.PORT=$stop_port -DSTOP.KEY=secret -Dconfig.home=$base -Djetty.home="${JETTY_HOME}" \
-jar "${JETTY_HOME}"/start.jar etc/jetty.xml > "$console_log" 2>&1 &
 
echo Check startup log at work/$port/logs/console.log
echo "After the server has started, hit the url http://localhost:$port/examinator"
echo 
