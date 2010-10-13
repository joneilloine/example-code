#!/bin/sh
#
# All content copyright (c) 2003-2007 Terracotta, Inc.,
# except as may otherwise be noted in a separate copyright notice.
# All rights reserved

TC_CLASSPATH=$CLASSPATH:target/spider-1.0.0-SNAPSHOT-jar-with-dependencies.jar

$TC_HOME/bin/dso-java.sh -classpath $TC_CLASSPATH -server -Xms256m -Xmx256m -Dtc.install-root=$TC_HOME -Dtc.config=tc-config.xml org.terracotta.workmanager.spider.StartWorker $1
