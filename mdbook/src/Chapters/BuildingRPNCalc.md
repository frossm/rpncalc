<img align="right" width="150" src="../Images/Build.png">

# Building RPNCalc from the Source

RPNCalc is written in Java and uses [Maven](https://maven.apache.org/what-is-maven.html) as it's build tool (although I am starting to look at [Gradle](https://gradle.org/) which seems more flexible).  To build, you'll need to have the Java Development Kit (JDK) installed on your system.  Make sure you get the JDK and not the Java Runtime Environment (JRE) which most people would have on their machines.  The JRE allows you to run Java applications, but not build them. At the time of this writing, you'll need Java 11 or newer.

After the JDK is installed, you'll need to install the Apache Maven build tool.  Then download/clone of the source code from Github along with my [library package](https://github.com/frossm/library).  This library package contains many methods that I use across my applications.

To install the library, execute the following from the top level library directory (the one with the `pom.xml` file in it):

`mvn install`

That should install the library into the Maven cache on your computer.  You can remove the library directory.

Secondly, you'll need to build the executable RPNCalc jar file.  Download, or clone, the RPNCalc source code from the link provided above.  Unzip it to a directory and from the top level directory of RPNCalc, execute:

`mvn package`

Assuming all goes well, you'll have a new shiny `rpncalc.jar` file in the `target` directory.

Automated testing in RPNCalc (using JUnit5) is fairly complete (although testing can always be more extensive) and should catch most issues with the code.  The hope is I find them before it's released, but pay attention to the Maven output and you'll see if there are issues.

I'll discuss this more in the Snap chapter, but if you are on a Linux system that supports snaps, I would encourage that you leverage it. Not only are Snap applications "sandboxed" so it's more secure, all of the dependencies are bundled in so you don't even need to have java installed on the machine. It is also automatically updated so you'll always have the latest.  Although this can also be forced with `sudo snap refresh`.

My preference is to use the Snap installation which is what I do on my Ubuntu machines.