<img align="right" width="150" src="../Images/Plates.png">

# Stacks

The entire concept of a RPN calculator is based on a stack, which can be thoughts of as numbers stacked on top of each other.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). 

With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  If you didn't specify a named stack,  the `default` stack is loaded.  When you start the program you can specify which stack to load with the `-l` command.  You may also load / create a new stack inside of the program with the `load` command.

When you perform calculations or commands, they generally work from the top of the stack down.  In RPNCalc, the top of the stack is `line1` and it's actually at the bottom of the display.  When you perform calculations, you are working on your command line and the lines you are using are above the text entry area.  This might be a bit hard to visualize, but makes a lot of sense when you actually use the calculator.  Trust me.  

For example, if you want to take the square root of 25, you enter `25 [ENTER]` and it will be added to the top of the stack at `line1`.  Anything else on the stack is pushed "up" on the display. Then execute the command `SQRT [ENTER]`.  This will remove 25 from the stack on `line1`, perform the square root, then place the result `5` back onto the stack.  Some operations require more than one stack item.  The addition command `+`, for example, will take the last two numbers off the stack, add them together, and then place the result on the stack.  

The order of the numbers on the stack is important for some of the operands.  This is detailed out in the `Operand chapter`.


## Stack Management

Saving and loading stacks is fundamental to RPNCalc.  You can have as many named stacks as you like.  They are stored in the Java Preferences system which is located in various placed depending on the OS:

|OS|Location|
|--|--------|
|Windows| Stored in the current user hive of the registry at`(HKCU\Software\JavaSoft\Prefs\org\fross\rpn)`|
|Linux| Linux uses the .java directory in your home directory|
|Mac| The preferences files are named `com.apple.java.util.prefs` are are stored in their home directory at `~/Library/Preferences`.  I don't have access to a Mac and can not confirm this location|

It is safe to delete these if you wish, but of course you'll lose any saved stacks, memory slots, persistent configurations, and user defined functions which are stored there.  The structure, not the data, will be recreated again when RPNCalc is restarted.

Each stack you load (default or a named stack) actually has 2 internal stacks defined; a primary and secondary.  You can quickly swap stacks using the swap stack `ss` command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The primary and secondary stack data is saved and restored upon loading the stack.  The primary and secondary stacks have their own unique undo stacks as of version 4.5.  This was long standing issue that's now been resolved.

When you start up RPNCalc, you can load a named stack with the `-l name` command.  If the stack `name` exists, it will be loaded.  If it does not exist, the stack will be created and when you leave the program it will be saved.  You can always view the current stack you are using in the lower right of the dashed bar. The `:1` or `:2` after the stack name will tell you if you are on the primary or secondary stack.

As a side note, both stacks and memory slots are saved during shutdown.  While the data in a stack is specific to that stack, memory slots and user created functions are global.  The default stack items are restored at startup (or whatever stack you choose to load with `-l name`.)  Memory slots are also restored at startup.  `list mem` will show the values in current memory, and `list stacks` will show the existing stacks available to load.
