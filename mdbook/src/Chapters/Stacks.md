<img align="right" width="150" src="../Images/Plates.png">

# Stacks

The entire concept of a RPN calculator is based on a stack, which can be thoughts of as numbers stacked on top of each other.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). 

With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  If you didn't specify a named stack,  the `default` stack is used.  When you start the program you can specify which stack to load with the `-l` command.  If none is entered, the `default` stack is loaded.  You may also load / create a new stack inside of the program with the `load` command.

When you perform calculations or commands, they generally work from the top of the stack down.  In RPNCalc, the top of the stack is `line1` and it's actually at the bottom of the display.  When you perform calculations, you are working on your command line and the lines above you.  This might be a bit hard to visualize, but makes a lot of sense when you actually use the calculator.  Trust me.  

For example, if you want to take the square root of 25, you enter `25 [ENTER]` and it will be added to the top of the stack at `line1`.  Then execute the command `SQRT [ENTER]`.  This will remove 25 from the stack on `line1`, perform the square root, then place the result `5` back onto the stack.  Some operations require more than one stack item.  The math `add` function, for example, will take the last two numbers off the stack, add them together, and then place the result on the stack.  

The order of the numbers on the stack is important for some of the operands.  This is detailed out in the `Operand` chapter.


## Stack Management

Saving and loading stacks is fundamental to RPNCalc.  You can have as many named stacks as you like.  They are stored in the Java Preferences location which varies by OS:

|OS|Location|
|--|--------|
|Windows| Stored in the current user hive of the registry at`(HKCU\Software\JavaSoft\Prefs\org\fross\rpn)`|
|Linux| Linux uses the .java directory in your home directory|
|Mac| The preferences files are named `com.apple.java.util.prefs` are are stored in their home directory at `~/Library/Preferences`.  I don't have a Mac and this was sources from the internet.|

It is safe to delete these if you wish to stay tidy, but of course you'll lose any saved stacks, memory slots, and user defined functions which are stored there.  They will be recreated again when RPNCalc is started.

Each stack you load (default or a named stack) actually has 2 internal stacks defined; a primary and secondary.  You can quickly swap stacks using the swap stack `ss` command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The primary and secondadry stack data is saved and restored upon loading the stack.

When you start up RPNCalc, you can load a named stack with the `-l name` command.  If the stack `name` exists, it will be loaded.  If it does not exist, the stack will be created and when you leave the program it will be saved.  You can always view what stack you are using in the lower right of the dashed bar. The `:1` or `:2` after the stack name will tell you if you are on the primary or secondary stack.

As a side note, both stacks and memory slots are saved during shutdown.  While the data in a stack is specific to that stack, memory slots are global.  The default stack items are restored at startup (or whatever stack you choose to load with `-l name`.)  Memory slots are also restored at startup.  `list mem` will show the values in current memory, and `list stacks` will show the saved stacks available to load.

<hr>

***`Please note that the undo functionality is shared (and shouldn't be) so you could undo on one stack and restore the other.  I'm looking into correcting this or perhaps removing the secondary stack as I'm not sure how much it's being used`***

<hr>
