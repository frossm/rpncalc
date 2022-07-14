# Building RPNCalc from the source

RPNCalc is written in Java and uses [Maven](https://maven.apache.org/what-is-maven.html) as it's build tool.  To build, you'll need the Java Development Kit (JDK).  Not the Java Runtime Environment (JRE) which most people would have on their machines.  At the time of this writing, you'll need Java 11 or newer.

Secondly you'll need to install Maven.  Then download/clone of the source code from Github along with my [library package](https://github.com/frossm/library).  This library contains many methods that I use across my applications.

To install the library, execute the following from the top level library directory (the one with the `pom.xml` file in it):

`mvn install`

That should install the library into the Maven cache on your computer.

Secondly, you'll need to build the executable jar file.  From the top level directory of RPNCalc, execute:

`mvn package`

Assuming all goes well, you'll have a new shiny rpncalc.jar file in the **target** directory.

I'll discuss this more in the SNAP chapter, but if you are on a Linux system that supports snaps, I would encourage that you leverage it. Not only are the applications "sandboxed" so it's secure, all of the dependencies are bundled in so you don't need to have java installed on the machine. It is also automatically updated so you'll always have the latest. You can force this with sudo snap refresh Just a suggestion.
