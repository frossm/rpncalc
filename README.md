
***RPNCalc: The Commandline Reverse Polish Notation Calculator***
## INTRODUCTION
With a Reverse Polish Notation (RPN) calculator it is easy to do complex calculations, especially if there are percentiles involved.  For example, to perform the following calculation:

`((5+3) * 8)/2 = x`

`RPN:  5 enter 3 + 8 * 2 /`

You start on the inside and work yourself out.  It's based on a stack and really makes intuitive sense when you use one.

Over the years I've used various RPN calculators on my computer, but I failed to find a simple command line version that I liked.  Therefore, I decided to write one.  It was easy to write, easily extensible, and since it's in Java, should run wherever I need it to run.

One point is that I am not trying to replicate all of the functions of a real calculator.  There is currently no trig functions, no binary, radians, etc.  But it does the basic math functions along with some useful (to me) features.

If you have not heard of an RPN calculator, or just enjoying reading about various calculator notations, here is a link to the [Wikipedia Article](https://en.wikipedia.org/wiki/Reverse_Polish_notation).

To execute a Java jar file, you need to have a Java Run-time Environment, (JRE) installed and and in your path. 

To Execute:
`java.exe -jar rpncalc.jar`

## High Level Usage
RPNCalc is a console application that will start at a prompt.  Entering 'h [ENTER]' will display the in-program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  
On this command line you'll enter numbers and press enter.  These will then be added to the stack.  RPNCalc operates on a stack where first in is last out.  You can then enter in an operand, such as + or /, to perform the action on the items at the end of the stack.  So to add two numbers you can simply enter 2 [ENTER] which add the number 2 to the stack.  Then  3 [ENTER] which will put it on top of the stack (which is the bottom in the program).  Then + [ENTER] to add them.  The 2 and 3 come off the stack and 5 is added.  As a shortcut, for the basic operands, you can skip a  step by entering 3+ [ENTER] and the end and it will perform the shortcut.  I'm not going to into a lot of detail on how an RPN calculator works, that's Wikipedia's job, but it's fairly easy.  Once I got the hand of it, I rarely use another style.

One note is that the stack always contains decimal numbers.  You can enter in a simple fraction and it will convert it.  
*For example:*

`1 5/16 [ENTER] will add 1.3125 to the stack`

`14/8 [ENTER] will add 1.75 to the stack`

## Stacks
The entire concept of a RPN calculator is based on stacks.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  When you start you can specify which stack to load.  If none is entered, the default stack is loaded.

When you perform calculations or commands, they generally work bottom up.  For example, if you want to take the square root of 25, you enter 25.  Press return to add it to the stack.  Then execute **SQRT**.  This will remove 25 from the stack, perform the square root, then place the result (5) back onto the stack.  Some operations require more than one stack item.  The math functions, for example, will take the last two numbers off the stack, and add the result back.  

Saving and loading stacks is fundamental to RPNCalc.  You can have as many saved stacks as you like.  They are stored in the Java Preferences location which varies by OS.  Windows stores them in the registry (HKCU\Software\JavaSoft\Prefs\org\fross\rpn).  Linux uses the .java directory in your home directory.  It is safe to delete these if you wish to stay tidy, but of course you'll use the data in the stacks.

Each stack you load (default or a named stack) actually has 2 stacks defined; primary and secondary.  You can quickly swap stacks using the **ss** command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The order is saved and restored upon loading.

## Command Line Options
Currently there are a small number of command line options, and all are optional.

**-D**
Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I most just use this to help be debug as I write the code, but may want to get a bit more insight into what's going on.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode by entering in the command 'debug' while within the program - you don't have to leave and relaunch without the -D.

**-l name**
Load a saved stack by it's name.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load a named session from within the program by using the 'load' command.  Please note the name field is whatever you want to call the instance and you can have many of them.  I'm not aware of a limit.  If the name to load does not exist, it will be created.  This is the same behavior as using the 'load' command while in the calculator.  I'll of RPNCalc's saved information is stored as a java preference.  This location will vary by OS.

**-a alignment**
Alignment choice.  Alignment can either be an 'l' for LEFT alignment, an 'r' for RIGHT alignment, or a 'd' to align to the decimal point.  This is a display choice only and doesn't impact the calculations.  For example, when I use it as a checkbook, I align by decimal which makes it a bit easier to read.  Alignments can also be changed within the program itself.

**-v**
Simply show the version information and exit.  The help command line option (-h or -?) will also show this information

**-h or -?**
Display the standard help information and exit.  

## Operands
The list of operands available will hopefully continue to grow as my needs change (or as I get suggestions from all of you.)  At the time of this writing, the following are supported:

|Operand|Description|
|-------|-----------|
|+ |Add the last two items on the stack|
|- |Subtrack the last stack item from the previous item|
|* |Multiply the last two items in the stack|
|/ |Divide the second to the last item by the last|
|^ |The second to the last item to the power of the last|


## Calculator Commands
|Command|Description  |
|-------|-------------|
|u |Undo last operation|
|f |Flip the sign on the last stack item|
|c |Clear the current stack|
|d [#] |Delete the last item in the stack or, optionally, the stack number provided|
|s [#] [#]|Swap the position of the last two stack items or, optionally, the stack numbers provided|
|copy| Copy the item at the top of the stack.  This adds a copy of the last item back to the stack so you'll have two of the last items.|
|% |Convert the last stack item into it's percentage (multiply by 0.01).  For example, if you want to take 50.123% of a number, you could just enter in `50.123 [ENTER] % [ENTER] *`|
|mod|Modulus is the remainder after a division.  This command will perform a division and put the remainder only back on the stack|
|pi| Insert the value of PI onto the stack|
|phi| Add PHI, also known as the Golden Ratio, to the stack|
|sqrt|Perform a square root of the last item in the stack|
|sin, cos, tan|Calculate the trigonometry functions|
|asin, acos, atan|Calculate the arc trignometry functions|
|log, log10|Calculates the natural logarithm (base e) or the base10 logarithm|
|ss|Swap the current stack with the secondary.  SS will swap them back again.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit.|
|rand [l] [h]|Generate a random integer number between the provided low and high numbers inclusive to both.  If no numbers are provided, then the random number will be between 1 and 100.|
|frac [base]|Display the last stack item as a fraction with the maximum granularity of 1/base.  Default is 1/64th.  Only decimals are stored on the stack but this will display the results.  For example, if you had *1.1234* on the stack, frac would show you `1.1234 is approximately 1 1/8`  It would have used a base of 64 (which means maximum granularity would be 1/64.  It auto reduces which is why you get the eight. if you entered frac 2 (which means 1/2 is maximum granularity, you get `1.1234 is approximately 1 0/1` or just one.  Need to fix that display oddity.|
|dice XdY|Roll a Y sided die X times and display the results.  Default is 1d6. Not the most useful command...|


## Operational Commands
|Command|Description  |
|-------|-------------|
|liststacks|Display the current saved stacks|
|load NAME|Load the named stack.  If name does not exist a new one will be created|
|listundo|Show the current undo stack|
|debug|Toggle debug mode which will display additional information on what's happening|
|a [l d r]| Align the display output to be l(eft), d(ecimal), or r(ight)|
|ver| Display the current version number.  Can also be seen in the help screen|
|h or ?|Display the help information|
|x or q |Exit the program.  The primary and secondary stacks will be saved.  'cx' will clear the stack before exiting.|

One note is that you can perform a shortcut.  You can append an operand at the end of an entered number and the program will place the number on the stack and then execute the operand.  For example:

 - 2
 - ENTER
 - 3+
 - ENTER
 
 2 and 3 will be removed from the stack.  Added together, and the result, 5, will be added to the stack.

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions.
rpncalc@fross.org

## License
[The MIT License](https://opensource.org/licenses/MIT)
https://opensource.org/licenses/MIT

Copyright 2011-2020 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
