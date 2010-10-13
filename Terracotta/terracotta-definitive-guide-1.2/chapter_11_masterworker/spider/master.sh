#!/bin/sh
#
# All content copyright (c) 2003-2007 Terracotta, Inc.,
# except as may otherwise be noted in a separate copyright notice.
# All rights reserved

WEB_ROOT=http://www.terracotta.org

TC_CLASSPATH=$CLASSPATH:target/spider-1.0.0-SNAPSHOT-jar-with-dependencies.jar

$TC_HOME/bin/dso-java.sh -classpath $TC_CLASSPATH -server -Xms256m -Xmx256m -Dtc.install-root=$TC_HOME -Dtc.config=tc-config.xml org.terracotta.workmanager.spider.StartMaster --url $WEB_ROOT --depth 4 --external-links false  $1 $2 $3 $4 $5 $6 $7 $8 $9
