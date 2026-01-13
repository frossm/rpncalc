<img align="right" width="150" src="../Images/Build.png">

# Building RPNCalc from the Source

RPNCalc is written in Java and uses [Gradle](https://gradle.org/) as it's build tool. To build, you'll need to have the Java Development Kit (JDK) 
installed on your system.  Make sure you get the JDK and not the Java Runtime Environment (JRE) which most people would have on their machines.  The JRE allows you to run 
Java applications, but not build them. At the time of this writing, you'll need Java 21 or newer.

To build using Gradle, simply run the Gradle wrapper, and it will install what's needed.

`./gradlew shadowJar`

Assuming all goes well, you'll have a new shiny `rpncalc.jar` file in the `build/libs` directory. You can put this file anywhere on your system that you like, and you'll 
run it by executing `java -jar rpncalc.jar`  Of course you can alias this to make it easier (`alias rpncalc='java -jar /path/to/rpncalc.jar'`) but this will depend on your 
OS or shell.

Automated testing in RPNCalc (using [JUnit](https://junit.org/)) is fairly complete (although testing can always be more extensive) and should catch most issues with the code.  The hope is I 
find them before it's released, but pay attention to the Gradle output, and you'll see if there are issues.

I'll discuss this more in the Snap chapter, but if you are on a Linux system that supports snaps, I would encourage that you leverage it. Not only are Snap applications 
"sandboxed" so it's more secure, but all the dependencies are bundled in so you don't even need to have java installed on the machine. It is also automatically updated so 
you'll always have the latest.  Although this can also be forced with `sudo snap refresh`.

My preference is to use the Snap installation which is what I do on my Ubuntu machines.

<br><br>

<img src="../Images/Gradle.svg" style="display: block; margin: auto; width: 30%;">


