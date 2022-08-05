<img align="right" width="125" src="../Images/OperationalCmds.png">

# Operational Commands

Operational commands are commands that do not directly impact your stack numbers. These tend to be display commands or those the interact with the the setting of RPNCalc directly.

|Command|Description  |
|-------|-------------|
|list stacks|List the current saved stacks on the system, including the one currently in use|
|list mem|Display the contents of the memory slots|
|list undo|Displays all of the saved stacks and their contents used for undo operations.  Basically what your stack will look like when you perform an undo.  These are not saved between RPNCalc executions|
|list func|Show a list of the defined User Defined Functions (UDF) and the steps within each one|
|load `NAME`|Load the named stack.  You can `load` a stack name even if it doesn't exist, and it will be created. This is how a new stack is created and is similar to using the `-l name` command line switch. Exiting the program or loading another stack will save the current stack before switching|
|import `FILENAME`|With `import` RPNCalc will replace the current stack with one loaded from a file.  The file format is simple, just one number per line.  Do not include any comments or alphanumeric/special characters.  Just one number per line with the last number being `line1` - just like the display in RPNCalc|
|ss|Swap the current stack with the secondary. The primary and secondary stacks are described in the Stacks section.  Executing `ss` again will swap them back.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit|
|debug|Toggle debug mode which will display additional information on what's happening internally in the program.  Same as the `-D` command line switch.  Probably not the useful for a normal user|
|set|Display the current values of the configurable persistent settings|
|set width `NUM`| Sets the width of the program.  The default is 80 characters. If you are using a small display, and the calculator wraps, this can be used to make the width smaller (or larger).  Please note that there is a minimum width that must be used.  At the time of this writing, it's 46 characters.  This setting is persistent across RPNCalc executions|
|set memslots `NUM`| Set the number of memory slots available to RPNCalc to `NUM`.  The default is 10 slots, numbed 0 through 9.  If you need more, or less, it can be changed with this command.  The setting is persistent across RPNCalc executions.  `set mem` or `set memoryslots` may also be used.  See the memory commands chapter for more information|
|set align `l\|d\|r`| Set the alignment of the stack when it's displayed.  The default is `l` or left alignment.  The numbers are aligned to the left.  `r` or right alignment has the numbers aligned to the right.  `d` or decimal aligns all of the decimal points together in a column.  This setting is persistent across RPNCalc executions. `set alignment` may also be used| 
|reset| This command resets the configuration setting that are set with the `set` command back to their default values|
|ver| Display the current version number and copyright as well as the latest GitHub release.  This is equivalent to the `-v` command line option.  Users of Snap installations will automatically be up to date.|
|h <br> ?|`h` or `?` will display the in-program help page|
|cx|`cx` will clear the stack and then exit the program|
|x <br>exit<br>quit<br>ctrl-c|Exit the program.  The primary stack, secondary stack, and memory slots will be saved upon exit|