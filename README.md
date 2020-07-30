<p align="center"> <img width="120" height = "120" src ="https://github.com/frossm/rpncalc/blob/master/graphics/PostIt-200x200.jpg"> </p> 

<p align="center"> <b><i>RPNCalc: The Command Line Reverse Polish Notation Calculator**<i></b></p>

## INTRODUCTION
<img align="right" width="50%" src="https://github.com/frossm/rpncalc/blob/master/graphics/ScreenShot.jpg">RPNCalc is a Reverse Polish Notation (RPN) calculator.  Sounds complicated, but it is just an easy to do complex calculations, especially if there are parentheses involved.  For a quick easy example, consider the following:

`x = SQRT((((5+3) * 8)/2) ^ 6)`

With an RPN Calculator, to solve for x you would start on the inner calculation and work outwards by entering the following:

`5 enter 3 + 8 * 2 / 6 ^ SQRT`

It's based on a Last In First Out (LIFO) stack and really makes intuitive sense when you use one.  On a RPN calculator, there is no equal sign, but the there is an enter key.

Over the years I've used various RPN calculators on my computer, but I failed to find a simple command line version that I liked.  Therefore, I decided to write one.  It was easy to write, easily extensible, and since it's in Java, should run wherever I need it to run.

While this calculator doesn't have **every** function of a complex calculator, I've been continually adding to it and it has gotten fairly complete.

If you have not heard of an RPN calculator, or just enjoying reading about various calculator notations, here is a link to the [Wikipedia Article](https://en.wikipedia.org/wiki/Reverse_Polish_notation) on RPN.

I have embedded all of the dependencies into the Java jar file.  In order to execute a file, you need to have a Java Run-time Environment, (JRE) installed and and in your path. 

To Execute:
`java.exe -jar rpncalc.jar`

## High Level Usage
RPNCalc is a console application that must be run from a console.  Executing it with a -h switch, or starting the program and entering the h command will display the in-program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  

On this command line you'll enter numbers and command, then press enter.  The numbers will then be added to the stack.  RPNCalc operates on a stack where the first in is the last out.  You can then enter in an operand, such as + or /, to perform the action on the items at the end of the stack.  So to add two numbers you can simply enter `2 [ENTER]` which add the number 2 to the stack.  Then  `3 [ENTER]` which will put it on top of the stack (which is the bottom in the program).  Then `+ [ENTER]` to add them.  The 2 and 3 come off the stack and 5 is added.  As a shortcut, for the basic operands, you can skip a  step by entering `3+ [ENTER]` and it will perform the shortcut.  I'm not going to into a lot of detail on how a RPN calculator works, that's Wikipedia's job, but it's fairly easy.  Once I got the hand of it, I rarely use another style.

One note is that the stack always contains decimal numbers.  You can enter in a simple fraction and it will convert it.  
*For example:*

`1 5/16 [ENTER] will add 1.3125 to the stack`

`14/8 [ENTER] will add 1.75 to the stack`

While you can never convert it back to a fraction directly as the stacks only support decimals, you can use the `frac <base>` command.  This will convert the fraction to the base provided and then simplify as much as possible.  The default base is 64 which means that it will calculate to a precision of 1/64.  For example:

`12.3456 enter`
`frac enter`

Will display `12 11/32`  It reduced from 1/64.  

## Stacks
The entire concept of a RPN calculator is based on stacks which you can think of as numbers stacked on top of each other.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  When you start the program you can specify which stack to load.  If none is entered, the default stack is loaded.

When you perform calculations or commands, they generally work bottom up.  For example, if you want to take the square root of 25, you enter 25.  Press return to add it to the stack.  Then execute **SQRT**.  This will remove 25 from the stack, perform the square root, then place the result (5) back onto the stack.  Some operations require more than one stack item.  The math `add` function, for example, will take the last two numbers off the stack, add then, and then place the result on the stack.  

Saving and loading stacks is fundamental to RPNCalc.  You can have as many saved stacks as you like.  They are stored in the Java Preferences location which varies by OS.  Windows stores them in the registry (HKCU\Software\JavaSoft\Prefs\org\fross\rpn).  Linux uses the .java directory in your home directory.  It is safe to delete these if you wish to stay tidy, but of course you'll use the data in the stacks.

Each stack you load (default or a named stack) actually has 2 stacks defined; a primary and secondary.  You can quickly swap stacks using the **ss** command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The order is saved and restored upon loading.

While stacks are saved on shutdown, the memory slots used by the **mem** command are not saved between sessions. The slots are just temporary memory location to use when you wish to save a value to use later in the current session. You can also copy a value with the copy command which placed a copy of the top stack item onto the stack.

## Command Line Options
Currently there are a small number of command line options, and all are optional.

|Option|Description|
|-------|-----------|
|-D | Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I usually use this as I debug the program, but if you wish to get a bit more insight into what's going on, go for it.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode on/off by entering in the command 'debug' while within the program - you don't have to leave the program|
|-l name |Load a saved stack by it's name.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load the stack with the -l command line option, or from within the program by using the 'load' command.  Please note the name field is whatever you want to call the instance and you can have many of them.  I'm not aware of a limit.  If the name to load does not exist, it will be created.  All of RPNCalc's saved information is stored as a java preference.  This location will vary by OS.|
|-a <l,d,r> |Alignment choice.  Alignment can either be an 'l' for LEFT alignment, an 'r' for RIGHT alignment, or a 'd' to align to the decimal point.  This is a display choice only and doesn't impact the calculations.  For example, when I use it as a checkbook, I align by decimal which makes it a bit easier to read.  Alignments can also be changed within the program itself using the `a` command.|
|-m [slots]| Override the default value of 10 available memory slots.  If you need 12 memory slot, just use `-m 12` when starting the program.  Please note that if you have 12 slots, the slot numbers within the program will be 0 - 11|
|-v|Simply show the version information and exit.  The help command line option (-h or -?) will also show this information|
|-h or -?|Display the standard help information and exit|

## Operands
The following is the list of operands supported by RPNCalc:

|Operand|Description|
|-------|-----------|
|+ |Add the last two items on the stack|
|- |Subtract line 1 from line 2|
|* |Multiply the top two items (line 1 & 2) in the stack|
|/ |Divide line 2 by line 1|
|^ |Take line 2 to the power of line 1|


## Calculator Commands
|Command|Description|
|-------|-----------|
|u |Undo last operation.  Before commands and operations are performed, RPNCalc saves a copy of the current stack into an Undo Stack.  Upon an undo command, the current stack will be replaced by the top of the Undo Stack.  If you are curious about what the undo stack looks like at any given point, run the `listundo` command|
|f |Flip the sign on the top (line 1) stack item. This is the same as multiplying by -1|
|c |Clear the current stack and the screen|
|d [#] |Delete the last item (line 1) on the stack with just a `d` command or, optionally, delete the line number provided with `d <linenumber`|
|s [#] [#]|Swap the position of the top two stack items (line 1 & 2) with `s`.  You can swap any two line items in your stack by providing the two line numbers `s # #`|
|copy| Adds a copy of the top stack item (line 1) back on the stack.  The result is you'll have two of the same items on top of the stack|
|% |Convert the top stack item (line 1) into it's percentage number.  This will simply multiply the last value by 0.01.  For example, if you want to take 50.123% of a number, you could just enter in `50.123 [ENTER] % [ENTER] *`|
|mod|Modulus is the remainder after a division.  This command will perform a division of the top two stack items using the `/` operand and return the remainder only back on the stack|
|sqrt|Perform a square root of the top item in the stack|
|sin, cos, tan|Calculate the trigonometry function|
|asin, acos, atan|Calculate the arc trigonometry function|
|log, log10|Calculates the natural logarithm (base e) or the base10 logarithm|
|mem [slot] add|Add the top stack item (line 1) into the memory slot provided.  There are 10 slots; 0 through 9.  If you do not provide a slot number it will simply default to slot 0.  Example:  `mem add`   will add the last stack item into the Slot0|
|mem [slot] clr|Clear the contents of the memory slot provided.  There are 10 slots, 0 through 9.  Defaults to Slot0 if no slot is provided.  Example: `mem 2 clr`  The command `clear` can also be used instead of `clr`|
|mem [slot] copy|Copies the contents of memory slot provided (0 through 9) back onto the stack.  Defaults to Slot0 if no slot number is provided.  `recall` can also be used instead of `copy`|
|ss|Swap the current stack with the secondary. The primary and secondary stacks are described above in the Stacks secsion.  SS will swap them back again.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit|
|rand [l] [h]|Generate a random integer number between the provided low and high numbers inclusive to both.  If no numbers are provided, then the random number will be between 1 and 100.|
|frac [base]|Display the last stack item as a fraction with the maximum granularity of 1/base.  Default is 1/64th.  Only decimals are stored on the stack but this command will display the results.  For example, if you had **1.1234** on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  It would have used a base of 64 (which means maximum granularity would be 1/64.  However, it auto reduces which is why you get the `1 1/8`. if you entered frac 2 (which means 1/2 is maximum granularity, you get `1.1234 is approximately 1 0/1` or just one.  Need to fix that display oddity.|
|dice XdY|Roll a Y sided die X times and add the results to the stack.  Default is 1d6. While not a normal calculator function, I find it useful.|

## Constants
|Constant|Description|
|--|--|
|pi| Insert the value of PI onto the stack.  Pi is approximately `3.14159265359`|
|phi| Add PHI, also known as the Golden Ratio, to the stack.  Phi is approximately `1.618033989`|

## Operational Commands
|Command|Description  |
|-------|-------------|
|list stacks|List the current saved stacks on the system.  `list stack` will also work|
|list mem|list the contents of all 10 memory slots|
|list undo|List the current undo stack.  This command will show you the saved undo stacks.  Basically what your stack will look like when you perform an undo|
|load NAME|Load the named stack.  You can `load` a stack name even if it doesn't exist, and it will be created.  Exiting the program or loading another stack will save the current stack.
|debug|Toggle debug mode which will display additional information on what's happening internally to the program.  Save as the `-D` command line switch.  Probably not the useful for a normal user.|
|a <l,d,r>| Align the display output to be l(eft), d(ecimal), or r(ight).  This is the same as the `-a <l, d, r>` command line switch|
|ver| Display the current version number and copyright.  These can also be seen in the help screen|
|h or ?|Display the help information|
|x or q or cx |`x` or `q` will exit the program.  The primary and secondary stacks will be saved.  `cx` will clear the stack before exiting|

## NumOps
There is an important shortcut that you can (and should use.)  You can append an operand at the end of an entered number and the program will, behind the scenes, place the number on the stack and then execute the operand.  For example:

`2 [Enter]`
`3+ [Enter]`
 
When the second enter is pressed,  2 will be removed from the stack.  Added together, and the result, `5`, will be added back.

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions for enhancements by emailing rpncalc@fross.org.  

If you are a snap user, I would encourage anyone with a supported Linux platform to use it.  You can download, install, and keep RPNCalc up to date very easily, safely and automatically by installing the snap via :

`sudo snap install rpncalc`

This will install the application into a sandbox where it is separate from other applications.  I do want to look at packaging it via Flatpak as well, but my understanding is that Maven is not well supported.  However, I need to do more investigation.

## License
[The MIT License](https://opensource.org/licenses/MIT)

Copyright (C) 2011-2020 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
