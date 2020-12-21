<p align="center"> <img width="120" height = "120" src ="https://github.com/frossm/rpncalc/blob/master/graphics/PostIt-200x200.jpg"> </p> 

<p align="center"> <b><i>RPNCalc: The Command Line Reverse Polish Notation Calculator</i></b></p>

## INTRODUCTION
<img align="right" width="50%" src="https://github.com/frossm/rpncalc/blob/master/graphics/ScreenShot.jpg">RPNCalc is a command-line based Reverse Polish Notation (RPN) calculator.  RPN calculators make it very simple to do complex calculations, especially if there are parentheses involved.  For a quick easy example, consider solving for X with the following:

`x = SQRT((((5+3) * 8)/2) ^ 6)`

With a RPN Calculator, to solve for x you would start on the inner calculation and work outwards by entering the following:
- `5`
- `3`
- `+`
- `8`
- `*`
- `2`
- `/`
- `6`
- `^`
- `SQRT`

RPN is based on a Last In First Out (LIFO) stack and really makes intuitive sense when you use one.  On a RPN calculator, there is no equal sign, but the there is an enter key to put a value onto the stack.

Over the years I've used various RPN calculators on my computer, but I failed to find a simple command line version that I liked.  Therefore, I decided to write one.  It was easy to write, easily extensible, and since it's in Java, should run wherever I need it to run. And, while this calculator doesn't have **every** function a complex calculator would have, I've been using it exclusively and have been successful.  I am continually adding capabilities to it and I think it is fairly full featured now.  I will, however, continue to add new capabilities based on my ideas and those that I receive.

If you have not heard of an RPN calculator, or just have a passion for various calculator notations (and who doesn't?), you can read more about it here: [Wikipedia Article](https://en.wikipedia.org/wiki/Reverse_Polish_notation) on RPN.

As for the .jar bundled with a release, I have embedded all of the dependencies into the file and it is directly executable.  You do not need to install anything, just be able to execute a java program.  You will need a java runtime (JRE) installed in your path. 

To Execute:

`java -jar rpncalc.jar`

If you've installed via SNAP:

`rpncalc`

As I've stated above, this is a java program and uses maven.  If you download/clone the source, and have maven installed, it should be simple to build from source if desired - however, this is not needed.

`mvn package` from the root of the project (the one containing the **pom.xml** file)

## High Level Usage
RPNCalc is a command line application that must be run from a console.  Executing it with a `-h` (or `-?`) switch, or starting the program and entering the `h` command will display the in-program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  The screen shot above shows the help screen.

On the RPNCalc command line you'll enter numbers or commands, then press enter.  The numbers will then be added to the stack.  RPNCalc operates on a stack where the last in is the first out.  You can then enter in an operand, such as `+` or `/`, to perform the action on the items at the end of the stack.  So to add two numbers you can simply enter `2 [ENTER]` which adds the number 2 to the stack.  Then  `3 [ENTER]` which will put it on top of the stack (line 1).  Then `+ [ENTER]` to add them.  The 2 and 3 come off the stack and 5 is added.  I'm not going to into a lot of detail on how a RPN calculator works, that's Wikipedia's job, but it's fairly easy.  Once I got the hang of it, I rarely use another style.

### Decimals & Fractions
In RPNCalc, the stacks always store numbers as decimals.  You can, however, enter in fractions and they will be converted to a decimal equivalent and added to the stack.

**Example:**

`1 5/16 [ENTER]` 

will add `1.3125` to the stack

`14/8 [ENTER]` 

will add `1.75` to the stack

While you can never directly convert a decimal number on the stack back to a fraction, you can display what the last stack item would be as a fraction.  You do have to decide on the smallest denominator that will be used (called the base.)  The default base is `64` which is the equivalent of `1/64` and will be used if none is specified.  Use the `frac [base]` command to display the fractional equivilent.  RPNCalc will simplify the fraction as much as it can so you won't see `2/4` as a fraction, you'd see `1/2`.

**Example:**
`12.3456 [Enter]`
`frac [Enter]`

Will display `12 11/32`  RPNCalc converted it to `12 22/64` and then reduced it.

`1 3/64 [Enter]`
`frac 16 [Enter]`

will display `1 1/16` as that's as close as it could get with a granularity of 1/16.


## Stacks
The entire concept of a RPN calculator is based on stacks, which can be thoughts of as numbers stacked on top of each other.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  If you didn't specify a stack,  the `default` stack is used.  When you start the program you can specify which stack to load with the `-l` command.  If none is entered, the `default` stack is loaded.

When you perform calculations or commands, they generally work from the top of the stack down.  In RPNCalc, the top of the stack is `line 1` and it's actually at the bottom.  When you perform calculations, you are working on your command line and the line above you (line 1).  This makes a lot of sense when you actually use the calculator.  Trust me.  

For example, if you want to take the square root of 25, you enter 25.  Press return to add it to the stack.  Then execute `SQRT`.  This will remove 25 from the stack on Line 1, perform the square root, then place the result `(5)` back onto the stack.  Some operations require more than one stack item.  The math `add` function, for example, will take the last two numbers off the stack, add then, and then place the result on the stack.  Subtraction, division, power, and other commands care about what order the numbers in the stack hold.  For example:

`3`  `2`  `-`  will execute 3 - 2 and will yield `1`

`2`  `3`  `-` will execute 2 - 3 and will yield `-1`


### Stack Management
Saving and loading stacks is fundamental to RPNCalc.  You can have as many named stacks as you like.  They are stored in the Java Preferences location which varies by OS.  Windows stores them in the registry (HKCU\Software\JavaSoft\Prefs\org\fross\rpn).  Linux uses the .java directory in your home directory.  It is safe to delete these if you wish to stay tidy, but of course you'll lose the data in the stacks.

Each stack you load (default or a named stack) actually has 2 stacks defined; a primary and secondary.  You can quickly swap stacks using the `ss` command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The stack data is saved and restored upon loading the stack.

When you start up RPNCalc, you can load a stack with the `-l name` command.  If the stack `name` exists, it will be loaded.  If it does not exist, the stack will be created and when you leave the program it will be saved.  You can always view what stack you are using in the lower right of the dashed bar. The `:1` or `:2` after the stack name will tell you if you are on the primary or "swapped" stack.

As a side note, while stacks are saved on shutdown, the memory slots used by the `mem` commands are not saved between sessions. The memory slots are just temporary memory locations used when you wish to save a value to use later in the current session.

## Command Line Options
Currently there are several command line options, and all are optional.

|Option|Description|
|-------|-----------|
|-D | Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I usually use this as I debug the program, but if you wish to get a bit more insight into what's going on, go for it.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode on/off by entering in the command `debug` while within the program - you don't have to restart RPNCalc|
|-l name |Load a saved stack called by it's name.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load the stack with the -l command line option, or from within the program by using the 'load' command.  Please note the name field is whatever you want to call the instance and you can have many of them.  I'm not aware of a limit.  If the name to load does not exist, it will be created.  All of RPNCalc's saved information is stored as a java preference.  This location will vary by OS - see above|
|-a <l,d,r> |Alignment choice.  Alignment can either be an 'l' for LEFT alignment, an 'r' for RIGHT alignment, or a 'd' to align to the decimal point.  This is a display choice only and doesn't impact the calculations.  For example, when I load my saved stack `-l checkbook`, I align by decimal which makes it a bit easier to read.  Alignments can also be changed within the program itself using the `a` command|
|-m [slots]| Override the default of ten available memory slots.  If you need 12 memory slots, just use `-m 12` when starting the program.  Please note that if you have 12 slots, the slot numbers within the program will be 0 - 11|
|-w width|Set the width of the program header and status line.  Default is 70 characters. Useful if you are using a very small terminal|
|-z|Disable colorized output|
|-v|Simply display the version information and exit.  The help command line option (-h or -?) will also show this information|
|-h or -?|Display the program help and exit|

## Operands
The following is the list of operands supported by RPNCalc.  These are supported by the NumOps shortcut described below.  

|Operand|Description|
|-------|-----------|
|+ |Add the last two items on the stack|
|- |Subtract line 1 from line 2|
|* |Multiply line 1 & line 2|
|/ |Divide line 2 by line 1|
|^ |Take line 2 to the power of line 1|

## NumOps Shortcut
There is an important shortcut that you can (and should use.)  You can append an operand at the end of an entered number and the program will, behind the scenes, place the number on the stack and then execute the operand.  For example:

`2 [Enter]`
`3+ [Enter]`
 
When the second enter is pressed,  2 will be removed from the stack.  Added together, and the result, `5`, will be added back.

## Calculator Commands
|Command|Description|
|-------|-----------|
|u \| undo|Undo last operation.  Before an operations affecting the stack is performed, RPNCalc saves a copy of the current stack into an Undo Stack.  Upon an undo command, the current stack will be replaced by the Undo Stack.  If you are curious about what the undo stack looks like at any given point, run the `list undo` command|
|f \| flip |Flip the sign on the top stack item (line 1). This is simply done by multiplying by -1|
|c \| clear|Clear the screen, and empty the current stack.  Memory data is retained and you can undo the clear with the undo command `u`|
|cl \| clean|Clear the current screen, but keep the stack.  After cleaning, the stack will be displayed at the top of the screen|
|d \| delete [Linenumber]|Delete the top stack item (line 1) with just a `d` command or, optionally, delete the line number provided with `d <linenumber>`|
|s \| swap [Line1] [Line2]|Swap the position of the top two stack items (line 1 & 2) with `s`.  You can swap any two line items in your stack by providing the two line numbers `s # #`|
|%|Assumes line 1 contains a percent.  This converts that into a number by simply multiplying the last value by 0.01.  For example, if you want to take 50.123% of a number, you could just enter in `50.123 [ENTER] % [ENTER] *`|
|sqrt|Perform a [square root](https://en.wikipedia.org/wiki/Square_root) of the top item in the stack|
|round [n]|Round the top stack item to [n] decimal places.  If [n] is not given, round to the nearest integer (zero decimal places).  Example1: `3.14159` `round` would round to `3`.  Example2: `3.14159` `round 4` would round to `3.1416`|
|aa [keep]|Add all stack items together and return the result to the stack.  If the optional `keep` command is sent, the elements added will be retained and the total will be added to the top of the stack.  The entire `keep` command is not necessary, anything that starts with `k` will work|
|mod|Modulus is the remainder after a division.  This command will perform a division of the top two stack items using the `/` operand and return the remainder only back to the stack|
|copy| Adds a copy of the top stack item (line 1) back on the stack.  The result is you'll have two of the same items on top of the stack|
|log, log10|Calculates the [natural logarithm (base e)](https://en.wikipedia.org/wiki/Natural_logarithm) or the [base10 logarithm](https://en.wikipedia.org/wiki/Common_logarithm)|
|rand [l] [h]|Generate a random integer number between the provided [l]ow and [h]igh numbers inclusive to both.  If no numbers are provided, then the random number will be between 1 and 100 inclusive|
|dice XdY|Roll a Y sided die X times and add the results to the stack.  Default is 1d6. While not a normal calculator function, I find it fun|
|frac [base]|Display a fractional estimate of the last stack item with the maximum granularity of 1/base.  Default is 1/64th.  Only decimals are stored on the stack but this command will display the results.  For example, if you had **1.1234** on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  It would have used a base of 64 (which means maximum granularity would be 1/64.  However, it auto reduces which is why you get the `1 1/8`. if you entered frac 2 (which means 1/2 is maximum granularity, you get `1.1234 is approximately 1 0/1` or just one.  Need to fix that display oddity.|

## Trigonometry Functions
|Command|Description|
|-------|-----------|
|rad|Convert line1 into [radians](https://en.wikipedia.org/wiki/Radian). Assume line1 contains a value in degrees.  `radian` would also work|
|deg|Convert line1 into degrees.  Assumes line1 contains radians.  `degree` would also work|
|sin, cos, tan [rad]|Calculate the [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Angles are input as degrees by default unless the **rad** parameter is given in which case the angles will be in [radians](https://en.wikipedia.org/wiki/Radian).  Example: `tan` will calculate the tangent using row 1 as the angle in degrees.  Use `tan rad` if row 1 contains the angle in radians|
|asin, acos, atan [rad]|Calculate the arc [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Result is returned in degrees unless **rad** parameter is provided|
|hypot|Returns the hypotenuse of the top two stack items using the [Pythagorean theorem](https://en.wikipedia.org/wiki/Pythagorean_theorem).  Specifically, it returns SQRT( (line1)^2 + (line2)^2 ).  `hypotenuse` can also be used|

## Memory Commands
|Command|Description|
|-------|-----------|
|mem [slot] add|Add the top stack item (line 1) into the memory slot provided.  By default, there are 10 slots; 0 through 9.  If you do not provide a slot number it will simply default to slot 0.  Example:  `mem add`   will add the last stack item into Mem Slot0|
|mem [slot] copy|Copies the contents of memory slot provided back onto the stack.  Defaults to Slot0 if no slot number is provided.  `recall` can also be used instead of `copy`|
|mem [slot] clr|Clear the contents of the memory slot provided.  Defaults to Slot0 if no slot is provided.  Example: `mem 2 clr`  The command `clear` can also be used instead of `clr`|
|mem clearall|Clear the contents of all memory slots.  Not sure why this would be needed, but seems like something that should be here.  There is no need to include a SlotNumber. Note `mem clrall` will also work|

## Constants
|Constant|Description|
|--|--|
|pi| Insert the value of [PI](https://en.wikipedia.org/wiki/Pi) onto the stack.  Pi is approximately `3.14159265359`|
|phi| Insert [PHI](https://en.wikipedia.org/wiki/Golden_ratio), also known as the Golden Ratio, to the stack.  Phi is approximately `1.618033989`|
|euler| Insert [Euler's number (e)](https://en.wikipedia.org/wiki/E_(mathematical_constant)) to the stick.  e is approximately `2.7182818284590`|

## Operational Commands
|Command|Description  |
|-------|-------------|
|list stacks|List the current saved stacks on the system.  `list stack` will also work|
|list mem|list the contents of all memory slots|
|list undo|List the current undo stack.  This command will show you the saved undo stacks.  Basically what your stack will look like when you perform an undo|
|load NAME|Load the named stack.  You can `load` a stack name even if it doesn't exist, and it will be created.  Exiting the program or loading another stack will save the current stack|
|ss|Swap the current stack with the secondary. The primary and secondary stacks are described above in the Stacks section.  Executing `ss` again will swap them back.  The secondary stack it just a place to do a bit of other work then you can swap back.  They are in no way connected.  The secondary stack is also saved upon exit|
|debug|Toggle debug mode which will display additional information on what's happening internally to the program.  Same as the `-D` command line switch.  Probably not the useful for a normal user|
|a <l,d,r>| Align the display output to be l(eft), d(ecimal), or r(ight).  This is the same as the `-a <l, d, r>` command line switch|
|ver| Display the current version number and copyright.  These can also be seen in the help screen|
|h or ?|Display the help information|
|x or q or cx |`x` or `q` will exit the program.  The primary and secondary stacks will be saved.  `cx` will clear the stack before exiting|

## SNAP
[![rpncalc](https://snapcraft.io//rpncalc/badge.svg)](https://snapcraft.io/rpncalc)

I would encourage anyone with a supported Linux platform to use snap.  See [Snapcraft Homepage](https://snapcraft.io) for more information. You can download, install, and keep RPNCalc up to date automatically by installing the snap.  You don't even have to have java as it's bundled within the snap virtual machine.  Install via:

`sudo snap install rpncalc`  (Assuming snapd is installed)

This will install the application into a sandbox where it is separate from other applications.  I do want to look at packaging it via Flatpak as well, but my understanding is that Maven is not well supported.  However, I need to do more investigation.

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/rpncalc)

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions for enhancements by emailing rpncalc@fross.org.  

## License
[The MIT License](https://opensource.org/licenses/MIT)

Copyright (C) 2011-2020 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
