
*Document Version 2.0*

## INTRODUCTION ##
When I studied engineering in college I used a Reverse Polish Notation calculator extensively.  An RPN calculator It was easy to do complex calculations, and just made sense to me.  Over the years I've used various rpn calculators on my computer, but most were complex or had a GUI that actually made it more difficult to use.

Therefore, I decided to write a simple command line RPN calculator.  It was easy to write, easily extensible, and since it's in Java, should run wherever I need it to run.

One point is that I am not trying to replicate all of the functions of a real calculator.  There is currently no trig functions, no binary, radians, etc.  But it does basic math functions and that's all I need it for.

If you have not heard of an RPN calculator, or just enjoying reading about various calculator notations, here is a link to the [Wikipedia Article](https://en.wikipedia.org/wiki/Reverse_Polish_notation).

To execute a Java jar file, you need to have a Java Run-time Environment,ent (JRE) installed and java.exe must be in your path. 

```java.exe -jar rpn.jar```

## High Level Usage
rpn is a console application that will start at a prompt.  Entering 'h' at the start of the line will display the in-program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  
On this command line you'll enter numbers and press enter.  These will then be added to the stack.  You can then enter in an operand, such as + or /, to perform the action on the items at the end of the stack.  I'm not going to into how an rpn calculator works, that's Wikipedia's job, but it's fairly easy.  Once I got the hand of it, I rarely use another style.

**Savings Stacks**
One of the important uses for me is I wanted to be able to save my stacks for future instances of the program.  I also wanted to have a secondary stack that I could toggle in case I wanted to do some work and then toggle back to the primary stack.  Lastly, I wanted to be able to name and save a stack that could be loaded at another time.  All of this is build into the program.

## Command Line Options
Currently there are only three command line options, and both are not that necessary in normal use.

**-D**
Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I most just use this to help be debug as I was writing it, but sometimes you want to see what's going on.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode by entering in the command 'debug' while within the program.

**-l name**
Load a saved stack by it's name.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load a named session from within the program by using the 'load' command.  Please note the name field is whatever you want to call the instance and you can have many of them.  I'm not aware of a limit.  If the name to load does not exist, it will be created.  This is the same behavior as using the 'load' command while in the calculator.

**-a alignment**
Alignment choice.  Alignment can either be an 'l' for LEFT alignment, an 'r' for RIGHT alignment, or a 'd' to align to the decimal point.

## Operands
The list of operands available will hopefully continue to grow as my needs change (or as I get suggestions from folks.)  At the time of this writing, the following are supported:

|Operand|Description|
|-------|-----------|
|+ |Add the last two items on the stack|
|- |Subtrack the last stack item from the previous item|
|* |Multiply the last two items in the stack|
|/ |Divide the second to the last item by the last|
|^ |The second to the last item to the power of the last|
|% |Convert the last stack item into it's percentage (multiply by 0.01)|

## Commands
|Command|Description  |
|-------|-------------|
|listundo|Show the current undo stack
|u |Undo last operation
|f |Flip the sign on the last stack item
|c |Clear the current stack
|d [#] |Delete the last item in the stack or, optionally, the line number provided
|s [#] [#]|Swap the position of the last two stack items or, optionally, the element numbers provided
|copy| Copy the item at the top of the stack
|pi| Insert the value of PI onto the stack
|sqrt|Perform a square root of the last item in the stack
|ss|Swap the current stack with the secondary.  SS will swap them back again
|load NAME|Load the named stack.  If name does not exist a new one will be created
|debug|Toggle debug mode which will display additional information on what's happening
|a [l d r]| Align the display output to be l(eft), d(ecimal), or r(ight)
|ver| Display the current version number.  Can also be seen in the help screen
|h or ?|Display the help information
|x or q |Exit the program.  The primary and secondary stacks will be saved.

One note is that you can perform a shortcut.  You can append an operand at the end of an entered number and the program will place the number on the stack and then execute the operand.  For example:

 - 2
 - enter
 - 3+
 - enter
 The result will be 5.

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions.
rpn@fross.org

## License
[The MIT License](https://opensource.org/licenses/MIT)
https://opensource.org/licenses/MIT

Copyright 2011-2019 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
