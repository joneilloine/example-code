@echo off

@rem IMPORTANT: you have to set TC_HOME to the root of
@rem the Terracotta distribution

set CLASSPATH=%CLASSPATH%;target/spider-1.0.0-SNAPSHOT-jar-with-dependencies.jar

@rem You can change the web root to any web location or to a folder containing 
@rem a hierarchy of html files
@rem set WEB_ROOT=http://www.terracottatech.com

@rem "%TC_HOME%\bin\dso-java.bat" -server -Xms256m -Xmx256m -Dtc.config=tc-config.xml org.terracotta.workmanager.spider.StartMaster --url %WEB_ROOT% --depth 4 --external-links false  %1 %2 %3 %4 %5 %6 %7 %8 %9
"%TC_HOME%\dso-java.bat" -server -Xms256m -Xmx256m -Dtc.config=tc-config.xml org.terracotta.workmanager.spider.StartMaster --external-links false  %1 %2 %3 %4 %5 %6 %7 %8 %9
