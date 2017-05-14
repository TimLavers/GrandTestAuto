Welcome to GTA!

This software is built using Apache ANT.

To build it:
- Download and install the JDK.
- Download Apache ANT and put it in the path.
- Set the JAVA_HOME environment variable to point to the JDK installation directory. (This is a standard part of installing ANT.)
- Under the GrandTestAuto directory (i.e. the directory containing this file) create a directory called 'lib'.
- Download the apache commons-io library and copy commons-io.jar into the lib directory.
- Copy the ant.jar file from your ANT installation into the lib directory.
- From the command line type in `ant compile` to compile the code.
- Type in `ant test` to run the tests. This will run the unit, function (integration) and load tests.

It will probably take about 2 or 3 minutes to run the tests.

There will be a lot of printouts, including stack traces for exceptions thrown as part of the tests.

