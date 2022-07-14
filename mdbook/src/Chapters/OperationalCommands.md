# Operational Commands

Commands that are used that do not impact the stack

|Command|Description  |
|-------|-------------|
|list stack[s]|List the current saved stacks on the system|
|list mem|list the contents of the memory slots|
|list undo|Displays all of the saved stacks used for undo.  Basically what your stack will look like when you perform an undo|
|list func|Show a list of the saved user defined functions|
|load NAME|Load the named stack.  You can `load` a stack name even if it doesn't exist, and it will be created.  Exiting the program or loading another stack will save the current stack|
|import FILENAME|With `import` RPNCalc will replace the current stack with one loaded from a file.  The file format is simple, just one number per line.  Do not include any comments or alphanumeric/special characters.  Just one number per line|
|ss|Swap the current stack with the secondary. The primary and secondary stacks are described above in the Stacks section.  Executing `ss` again will swap them back.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit|
|debug|Toggle debug mode which will display additional information on what's happening internally to the program.  Same as the `-D` command line switch.  Probably not the useful for a normal user|
|a l <br> a d <br> a r| Align the display output to be l(eft), d(ecimal), or r(ight).  This is the same as the `-a <l, d, r>` command line switch|
|ver| Display the current version number and copyright as well as the latest GitHub release.  This is equivalent to the `-v` command line option|
|h <br> ?|Display the in-program help|
|cx|`cx` will clear the stack before exiting|
|x <br> exit|`x` or `q` will exit the program.  The primary stack, secondary stack, and memory slots will be saved|