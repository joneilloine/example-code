#!/bin/bash

function printHelp {
  echo "Usage: start.sh"
  echo "Start 2 Jetty instances on port 8080 and 8081 with Terracotta enabled"
}

if [ "$1" = "help" ]; then
  printHelp
  exit
fi

workdir=`dirname $0`/..
workdir=`cd "$workdir" && pwd`
cd "$workdir"
bin/startNode.sh 8080 -tc
if [ $? -ne 0 ]; then
  echo "Failed to start web server at port 8080"
  exit 1
fi  
sleep 5
bin/startNode.sh 8081 -tc
if [ $? -ne 0 ]; then
  echo "Failed to start web server at port 8081"
  exit 1
else  
  echo Please wait for both web servers to start
fi
