<img align="right" width="150" src="../Images/CmdLine.png">

# Command Line Options

Currently there are several command line options, and all are optional.  Normally, one would simply run the program and you would enter in numbers, operands, and commands.

However, there are several options that can be set when the program is executed.  If you always want to run RPNCalc with a specific command line option, you should create an alias in your shell to do so.  Then ever time you run rpncalc, it would run with those options.

You can set the number of memory slots, decimal alignment, debug mode, and others via these options.  The below table lists all of the allowed command line options and their functions.

|Option|Name|Description|
|------|----|-----------|
|-D | `DEBUG MODE`| Runs the program in debug mode.  This will display quite a bit of information in RED as you use the program.  This is mostly used by the developer and clutters everythign up, but you may find it useful if you are trying to debug something.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode on/off by entering in the command `debug` while within the program - you don't have to restart RPNCalc|
|-l `name` |`LOAD STACK`| Load a saved stack.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load the stack with the `-l name` command line option, or from within the program by using the `load name` command.  Please note the name field is whatever you want to call the instance but avoid spaces in the name.  I'm not aware of a limit to the number of saved stacks You can have.  If the name to load does not exist, the stack will be created and saved when you exit.|
|-z| `DISABLE COLOR`| Disable colorized output.  Useful if your current terminal doesn't support ANSI color sequences|
|-v| `VERSION`| This will display the current program version, but will also check GitHub for the leatest release.  It is possible, especially if you are using RPNCalc as a Snap, to have a later version than the latest GitHub release.|
|-h or -?| `HELP`| Display the program help page and exit.  This is the same as the `h` or `help` command within RPNCalc|