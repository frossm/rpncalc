
*Document Version 2.0*

## INTRODUCTION ##
When I studied engineering in college I used a Reverse Polish Notation calculator extensively.  It was easy to do complex calculations, and just made sense to me.  Over the years I've used various rpn calculators on my computer, but most were complex or had a GUI that actually made it more difficult to use.

Therefore, I decided to write a simple command line rpn calculator.  It was easy to write, easily extensible, and since it's in Java, should run wherever I need it to run.

One warning is that I am not trying to replicate all of the functions of a real calculator.  It just does basic math functions and that's all I need it for.  No bitwise, no trig, etc.

If you have not heard of an rpn calculator, or just enjoying reading about various notations, here is a link to the [Wikipedia Article](https://en.wikipedia.org/wiki/Reverse_Polish_notation).

To execute a Java jar file, you need to have a Java Run-time Environment,ent (JRE) installed and java.exe must be in your path. 

```java.exe -jar rpn.jar```

## High Level Usage
rpn is a console application that will start at a prompt.  Entering 'h' at the start of the line will display the in-program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  
On this command line you'll enter numbers and press enter.  These will then be added to the stack.  You can then enter in an operand, such as + or /, to perform the action on the items at the end of the stack.  I'm not going to into how an rpn calculator works, that's Wikipedia's job, but it's fairly easy.  Once I got the hand of it, I rarely use another style.

**Savings Stacks**
One of the important uses for me is I wanted to be able to save my stacks for future instances of the program.  I also wanted to have a secondary stack that I could toggle in case I wanted to do some work and then toggle back to the primary stack.  Lastly, I wanted to be able to name and save a stack that could be loaded at another time.  All of this is build into the program.

## Command Line Options
Currently there are only two command line options, and both are not that necessary in normal use.

**-D**
Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I most just use this to help be debug as I was writing it, but sometimes you want to see what's going on.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode by entering in the command 'debug' while within the program.

**-l name**
Load a saved stack by it's name.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load a named session from within the program by using the 'load' command.  Please note the name field is whatever you want to call the instance and you can have many of them.  I'm not aware of a limit.

## Operands
The list of operands available will hopefully continue to grow as my needs change (or as I get suggestions from folks.)  At the time of this writing, the following are supported:

|Operand|Description|
|--|--|
|+ |Add the last two items on the stack|
|- |Subtrack the last stack item from the previous item|
|* |Multiply the last two items in the stack|
|/ |Divide the second to the last item by the last|
|^ |The second to the last item to the power of the last|
|% |Convert the last stack item into it's percentage (multiply by 0.01)|
|q |Perform a square root of the last item in the stack|

## Commands
|Command|Description  |
|--|--|
|s |Change the sign on the last stack item
|c |Clear the current stack
|d |Delete the last item in the stack
|f |Flip the position of the last two stack items
|ss|Swap the current stack with the secondary.  SS will swap them back again
|load NAME|Load the named stack.  If name does not exist a new one will be started
|debug|Toggle debug mode which will display additional information on what's happening
|h or ?|Display the help information
|x |Exit the program.  The primary and secondary stacks will be saved.

One note is that you can perform a shortcut.  You can append an operand at the end of an entered number and the program will place the number on the stack and then execute the operand.  For example:

 - 2
 - enter
 - 3+
 - enter
 The result will be 5.

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions.
rpn@fross.org