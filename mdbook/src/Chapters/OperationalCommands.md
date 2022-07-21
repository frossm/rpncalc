<img align="right" width="125" src="../Images/OperationalCmds.png">

# Operational Commands

Operational commands are commands that do not directly impact your stack numbers. These tend to be display commands or those the interact with the the setting of RPNCalc directly.

|Command|Description  |
|-------|-------------|
|list stacks|List the current saved stacks on the system, including the one currently in use|
|list mem|Display the contents of the memory slots|
|list undo|Displays all of the saved stacks and their contents used for undo operations.  Basically what your stack will look like when you perform an undo.  These are not saved between RPNCalc executions|
|list func|Show a list of the defined User Defined Functions (UDF) and the steps within each one|
|load NAME|Load the named stack.  You can `load` a stack name even if it doesn't exist, and it will be created. This is how a new stack is created and is similar to using the `-l name` command line switch. Exiting the program or loading another stack will save the current stack before switching|
|import FILENAME|With `import` RPNCalc will replace the current stack with one loaded from a file.  The file format is simple, just one number per line.  Do not include any comments or alphanumeric/special characters.  Just one number per line with the last number being `line1` - just like the display in RPNCalc|
|ss|Swap the current stack with the secondary. The primary and secondary stacks are described in the Stacks section.  Executing `ss` again will swap them back.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit|
|debug|Toggle debug mode which will display additional information on what's happening internally in the program.  Same as the `-D` command line switch.  Probably not the useful for a normal user|
|a l <br> a d <br> a r| Align the display output to be `l(eft)`, `d(ecimal)`, or `r(ight)`.  This is the same as the `-a <l, d, r>` command line switch. `align` or `alignment` may also be used|
|ver| Display the current version number and copyright as well as the latest GitHub release.  This is equivalent to the `-v` command line option.  Users of Snap installations will automatically be up to date.|
|h <br> ?|`h` or `?` will display the in-program help page|
|cx|`cx` will clear the stack and then exit the program|
|x <br> exit|`x` or `q` will exit the program.  The primary stack, secondary stack, and memory slots will be saved upon exit|