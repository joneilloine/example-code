@echo off

@rem IMPORTANT: you have to set TC_HOME to the root of
@rem the Terracotta distribution

set CLASSPATH=%CLASSPATH%;target/spider-1.0.0-SNAPSHOT-jar-with-dependencies.jar

"%TC_HOME%\bin\dso-java.bat" -Xms256m -Xmx256m -Dtc.config=tc-config.xml org.terracotta.workmanager.spider.StartWorker %1 
