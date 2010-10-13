NOTE: Terracotta, as of version 2.6, only works with Spring version 2.0.8.


Running the sample.

Without Spring:

- Compile:

  %> javac -d bin -sourcepath src/main/java src/main/java/org/terracotta/example/inventory/cli/Main.java 

- Run:

  %> java -cp bin org.terracotta.example.inventory.cli.Main

- Run clustered with Terracotta:

  0. Start the Terracotta server:

  %> /usr/local/terracotta/bin/start-tc-server.sh -f tc-config-no-spring.xml

  1. Determine the jvm arguments you need (they depend on your installation of
     terracotta, but we'll assume you have Terracotta installed in
     /usr/local/terracotta) by running the dso-env.sh script with
     localhost:9510 as its argument (this tells Terracotta to get it's
     configuration from the Terracotta server:

    %> /usr/local/terracotta/bin/dso-env.sh localhost:9510

  2. Run java with the appropriate jvm args.  It should look something like
     this (but not exactly like this, so don't just copy and paste it):
  
    %> java -Xbootclasspath/p:/usr/local/terracotta/bin/../lib/dso-boot/dso-boot-hotspot_osx_150_13.jar  -Dtc.install-root=/usr/local/terracotta/bin/..  -Dtc.config=localhost:9510 -cp bin org.terracotta.book.inventory.cli.Main
  
  3. Run step (2) again in a different terminal to see the clustered Store.

  4. Run the admin console and look at the runtime clustered object graph to
     observe changes made to the various products:

     %> /usr/local/terracotta/bin/admin.sh

With Spring

- Compile:

  %> javac -cp lib/spring-2.0.8.jar -d bin -sourcepath src/main/java src/main/java/org/terracotta/book/inventory/cli/SpringMain.java

- Run:

  %> java -cp .:bin:lib/spring-2.0.8.jar:lib/commons-logging.jar org.terracotta.book.inventory.cli.SpringMain

- Run clustered with Terracotta :

  0. Start the Terracotta server:

  %> /usr/local/terracotta/bin/start-tc-server.sh -f tc-config-spring.xml

  1. Determine the jvm arguments you need (they depend on your installation of
     terracotta, but we'll assume you have Terracotta installed in
     /usr/local/terracotta) by running the dso-env.sh script with
     localhost:9510 as its argument (this tells Terracotta to get it's
     configuration from the Terracotta server

    %> /usr/local/terracotta/bin/dso-env.sh localhost:9510

  2. Run java with the appropriate jvm args.  It should look something like
     this (but not exactly like this, so don't just copy and paste it):

  %> java -Xbootclasspath/p:/usr/local/terracotta/bin/../lib/dso-boot/dso-boot-hotspot_osx_150_13.jar  -Dtc.install-root=/usr/local/terracotta/bin/..  -Dtc.config=localhost:9510 -cp .:bin:lib/spring-2.0.8.jar:lib/commons-logging.jar org.terracotta.book.inventory.cli.SpringMain

  3. Run step (2) again in a different terminal to see the clustered Store.

  4. Run the admin console and look at the runtime clustered object graph to
     observe changes made to the various products:

     %> /usr/local/terracotta/bin/admin.sh
