June 30, 2008

If session clustering for "example2" doesn't appear to work, there may be a
problem with the environment setup in setenv.sh.

To determine the problem, first launch the Terracotta admin console:

  UNIX:
  <TC_HOME>/bin/admin.sh

  Windows:
  <TC_HOME>\bin\admin.bat

Check the number of connected clients and look to see if there are any root
objects.  If the admin console reports no connected clients and there are no
root objects, then your Tomcat instances are not properly configured to connect
to the Terracotta server.  This may be the case even if you see output like the
following in from Tomcat:

2008-07-01 06:44:23,053 INFO - Terracotta 2.6.0, as of 20080520-120516 (Revision 8595 by cruise@rh4mo0 from 2.6.0)

This is because the "dso-env" script ("dso-env.bat" on Windows, "dso-env.sh" on
UNIX) prints that statement to the console, a potentially misleading sign that
Terracotta is correctly configured. 

To correct this problem, try executing the "dso-env" script (without the -q
flag) from the command line by itself, making sure to run it from the same
directory as your tc-config.xml file.  You should see output that looks like
this (it will vary slightly, depending on your operating system and the
location of your tc-config.xml file and your Terracotta installation location):

%> /usr/local/terracotta/bin/dso-env.sh
2008-07-01 07:29:08,871 INFO - Configuration loaded from the file at '/private/tmp/apache-tomcat-6.0.16/tc-config.xml'.
-Xbootclasspath/p:/usr/local/terracotta/lib/dso-boot/dso-boot-hotspot_osx_150_13.jar -Dtc.install-root=/usr/local/terracotta

Next, change the "setenv" script (<TOMCAT_HOME>\bin\setenv.bat on
Windows, <TOMCAT_HOME>/bin/setenv.sh on UNIX) so that it sets the JAVA_OPTS
environment variable to the java options declared on the last line of the
"dso-env" script's output.

Now restart the Terracotta server and any running Tomcat instances and check to
see if session clustering is working.
