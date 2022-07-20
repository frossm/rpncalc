<img align="right" width="125" src="../Images/Install.png">

# Installation

There are two ways to install RPNCalc.  The first is simply download `rpncalc.jar` file from the latest release on [GitHub](https://github.com/frossm/rpncalc).  I have embedded all of the dependencies into this one file and it is directly executable.  You do not need to install anything.  Of course, you will need a java runtime (JRE) installed in your path. 

The second is to run it as a [Snap](https://en.wikipedia.org/wiki/Snap_(software)).  There are many advantages to using it as a snap and I'll get into that in the SNAP secion of this user guide.  My personal preference is install it via Snap.  No Java to worry about, I'm protected as it runs in a sandbox, no aliases needed, always kept up to date, etc.  You can read more about this in in the Snap chapter.

Please note that I only have the ability to test it in Windows and Linux (Ubuntu). While I don't think there would be issues on other platforms, it's something to keep in mind.

## Standard Usage

To run RPNCalc use the following command:

`java -jar /path/to/rpncalc.jar`

This is much too long to type every time you need to run it, so I simply create an alias.  Here is example from the Bash shell:

`alias rpncalc='java -jar /path/to/rpncalc.jar`

Now, I just need to type RPNCalc to run it.

## Standard Uninstall

If you wish to uninstall RPNCalc, just delete the file and, if you created an alias, remove that as well.  Easy and simple.  

However, RPNCalc does use the Java preferences to store data.  It's located in different places depending on the OS:

- Windows keeps it in the registry `(HKCU\Software\JavaSoft\Prefs)`
- Linux in a hidden directory in your home folder `(~/.java/userPrefs)`

It is very small and removing it is not really necessary, but if you like to keep things tidy, delete the `org.fross.rpn` entry in the preference system.

## Snap Installation

If you are on a Linux system and have Snap installed (it's comes default on most Ubuntu based distributions, but can be installed by most others if not already there), you can install RPNCalc as a snap.  It does not require any special snap permissions.  To install via snap use:

`sudo snap install rpncalc`

To run it after installation, simply execute:

`rpncalc`

## Snap Uninstall

To uninstall, execute the following command:

`sudo snap remove rpncalc`

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/rpncalc)