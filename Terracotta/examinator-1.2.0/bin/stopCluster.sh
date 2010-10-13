#!/bin/bash

#  All content copyright (c) 2003-2009 Terracotta, Inc.,
#  except as may otherwise be noted in a separate copyright notice.
#  All rights reserved.

workdir=`dirname $0`/..
workdir=`cd $workdir && pwd`
cd "$workdir"

bin/stopNode.sh 8080
bin/stopNode.sh 8081
