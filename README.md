<p align="center"> <img width="1024" src ="https://github.com/frossm/rpncalc/raw/master/graphics/ReadmeHeader.jpg"> </p> 

## INTRODUCTION
<img align="right" width="50%" src="https://github.com/frossm/rpncalc/raw/master/graphics/ScreenShot.jpg">RPNCalc is the command-line based Reverse Polish Notation (RPN) calculator.  RPN calculators make it very simple to do complex calculations, especially if there are parentheses involved.  For a quick easy example, consider solving for X with the following:

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

RPN is based on a Last In First Out (LIFO) stack framework and really makes intuitive sense when you use one.  On a RPN calculator, there is no equal sign, but the there is an enter key to put a value onto the stack.

Over the years I've used various RPN calculators, but I failed to find a simple command line version that I liked.  Therefore, I decided to write one.  It is a fun project to write, easily extensible, and since it's in Java, should run wherever I need it to run. And, while this calculator doesn't have **every** function a complex calculator would have, I've been using it exclusively and have been successful.  I am continually adding capabilities to it and I think it is fairly full featured now.  I will, however, continue to add new capabilities based on my ideas and those that I receive.

If you have not heard of reverse Polish notation, or just have a passion for various calculator notations (and who doesn't?), you can read more about it here: [Wikipedia](https://en.wikipedia.org/wiki/Reverse_Polish_notation).

As for this calculator, there is a .jar file bundled with the release.  I have embedded all of the dependencies into this one file and it is directly executable.  You do not need to install anything, just be able to execute a java program.  You will need a java runtime (JRE) installed in your path. 

To Execute:

`java -jar rpncalc.jar`

If you've installed via SNAP, simply run the command name:

`rpncalc`

As I've stated above, this is a java program and uses maven.  If you download/clone the source, along with my [library package](https://github.com/frossm/library), and have maven installed, it shouldn't be difficult to build from source if desired.  However, this is not needed as the binary single file executable is included with each release and using the SNAP package is much easier and will always be up to date.

`mvn package` from the root of the project (the one containing the **pom.xml** file)

## High Level Usage
RPNCalc is a command line application that must be run from a console.  Executing it with a `-h` (or `-?`) switch, or starting the program and entering the `h` command will display the program help page.  This lists all of the commands and operands that can be used, but it is fairly terse.  The screen shot above shows the help screen.  This document is meant as a more comprehensive guide.

There are various command line switches that can be used when starting the program.  They generally exist so that aliases can be used to control several key parameters.  Once inside the program, you'll be presented a prompt where numbers and commands may be entered.  Numbers will then be added to the stack which you can think of a stack of plates.  The top stack item (represented at the bottom or line1) is the most important item in this Last In First Out (LIFO) approach. For example, you could enter `2 [ENTER]` and then `3 [ENTER]` and they would sit in the stack.  2 would be in the line2 position as it was added first.  3 would be at the top of the stack in line1.  You can then enter in an operand, such as `+ [ENTER]` to perform the action on the items at the end of the stack.  To continue our example, pressing `+ [ENTER]` would take the top two items off of the stack, add them, and put the result back on top of the stack.  I'll go into a bit more detail below on using stacks, but I'm not going to in depth as that's Wikipedia's job, but it's fairly easy.  Once I got the hang of it, you'll regret having to use a standard one.

### Decimals & Fractions
In RPNCalc, the stacks always store numbers as decimals.  You can, however, enter in fractions and they will be instantly converted to a decimal equivalent and added to the stack.

**Example:**

`1 5/16 [ENTER]` 

will add `1.3125` to the stack

`14/8 [ENTER]` 

will add `1.75` to the stack

While you can never directly convert a decimal number on the stack back to a fraction, you can display what the last stack item would be as a fraction.  You do have to decide on the smallest denominator that will be used (called the base.)  The default base is `64` which is the equivalent of `1/64` and will be used if none is specified.  Use the `frac [base]` command to display the approximate fractional equivalent.  RPNCalc will simplify the fraction as much as it can so you won't see `2/4` as a fraction, you'd see `1/2`.

**Example:**
`12.3456 [Enter]`
`frac [Enter]`

Will display `12 11/32`  RPNCalc converted it to `12 22/64` and then reduced it.

`1 3/64 [Enter]`
`frac 16 [Enter]`

will display `1 1/16` as that's as close as it could get with a granularity of 1/16.

`1 3/64 [ENTER]`
`frac 100000`

will display `1 293/6250`.  This is a closer approximation than using base 16.


## Stacks
The entire concept of a RPN calculator is based on stacks, which can be thoughts of as numbers stacked on top of each other.  You add numbers to the stack and they are normally processed Last In First Out (LIFO). With RPNCalc, when you leave the program, the current (and secondary) stacks are saved.  If you didn't specify a named stack,  the `default` stack is used.  When you start the program you can specify which stack to load with the `-l` command.  If none is entered, the `default` stack is loaded.  You may also load / create a new stack inside of the program with the `load` command.

When you perform calculations or commands, they generally work from the top of the stack down.  In RPNCalc, the top of the stack is `line 1` and it's actually at the bottom.  When you perform calculations, you are working on your command line and the line above you (line 1).  This makes a lot of sense when you actually use the calculator.  Trust me.  

For example, if you want to take the square root of 25, you enter `25 [ENTER]` and it will be added to the stack.  Then execute `SQRT [ENTER]`.  This will remove 25 from the stack on Line 1, perform the square root, then place the result `5` back onto the stack.  Some operations require more than one stack item.  The math `add` function, for example, will take the last two numbers off the stack, add them and then place the result on the stack.  Subtraction, division, power, and other commands care about what order the numbers are on the stack hold.  For example:

`3`  `2`  `-`  will execute 3 - 2 and will yield `1`

`2`  `3`  `-` will execute 2 - 3 and will yield `-1`


### Stack Management
Saving and loading stacks is fundamental to RPNCalc.  You can have as many named stacks as you like.  They are stored in the Java Preferences location which varies by OS.  Windows stores them in the registry *(HKCU\Software\JavaSoft\Prefs\org\fross\rpn)*.  Linux uses the .java directory in your home directory.  It is safe to delete these if you wish to stay tidy, but of course you'll lose the data.

<img align="right" width="120" height = "120" src ="https://github.com/frossm/rpncalc/raw/master/graphics/PostIt-200x200.jpg">Each stack you load (default or a named stack) actually has 2 internal stacks defined; a primary and secondary.  You can quickly swap stacks using the swap stack `ss` command.  For example, you are working on something and need to do a few calculations that you wish to keep separate from your main work.  You can swap stacks, do the work, then swap back.  They do not communicate in any way and are distinctly separate.  The primary and secondadry stack data is saved and restored upon loading the stack.

When you start up RPNCalc, you can load a named stack with the `-l name` command.  If the stack `name` exists, it will be loaded.  If it does not exist, the stack will be created and when you leave the program it will be saved.  You can always view what stack you are using in the lower right of the dashed bar. The `:1` or `:2` after the stack name will tell you if you are on the primary or "swapped" stack.

As a side note, both stacks and memory slots are saved during shutdown.  While the data in a stack is specific to that stack, memory slots are global.  The default stack items are restored at startup (or whatever stack you choose to load.)  Memory slots are also restored at startup.  `list mem` will show the values in current memory, and `list stacks` will show the saved stacks available to load.

## Command Line Options
Currently there are several command line options, and all are optional.  The overall goal is to set the operating parameters of the program.  Some settings, like `-D` Debug, `-a` alignment and `-l` load can also be used from within the program.  But they exist here so that aliases can be used to run them by default.

|Option|Description|
|-------|-----------|
|-D | **DEBUG ON:** Run program in debug mode.  This will display quite a bit of information on the program as it's running.  I usually use this as I debug the program, but if you wish to get a bit more insight into what's going on, go for it.  I could certainly add a lot more if needed, but it's useful today.  You can also toggle debug mode on/off by entering in the command `debug` while within the program - you don't have to restart RPNCalc|
|-l name |**LOAD STACK:** Load a saved stack.  This essentially will "name" your session and store the stack upon exit in the Java preferences system.  You can load the stack with the `-l` command line option, or from within the program by using the `load` command.  Please note the name field is whatever you want to call the instance and you can have as many of them as you like.  I'm not aware of a limit.  If the name to load does not exist, it will be created.  All of RPNCalc's saved information is stored as a java preference.  This location will vary by OS - see above|
|-a l <br> -a d <br> -a r | **ALIGNMENT:** The display alignment of the numbers on the stack.  Alignment can either be an `-a l` for LEFT alignment, a `-a r` for RIGHT alignment, or a `-a d` to align to the decimal point.  This is a display choice only and doesn't impact the calculations.  For example, when I load my saved stack `-l checkbook`, I align by decimal which makes it a bit easier to read a stack with only two decimal place numbers.  Alignments can also be changed within the program itself using the `a` command|
|-m [slots]| **MEMORY SLOTS:** Override the default of ten available memory slots.  If you need 25 memory slots, just use `-m 25` when starting the program.  Please note that if you have 25 slots, the slot numbers within the program will be 0 - 24.  Slots are saved and restored between sessions, but if you run the program again and do not specify `-m 25`, it will default to 10 and you'll lose the memory slots greater than that|
|-w width| **WIDTH:** Set the width of the program header and status line.  Default is 80 characters. Useful if you are using a very small terminal.  The minimum width of the console is currently 46 characters and RPNCalc will adjust to that if a lesser number is provided|
|-z| **DISABLE COLOR:** Disable colorized output.  Useful if your current terminal doesn't support ANSI color sequences|
|-v| **VERSION:** Simply display the version information and exit.  `-v` will also query GitHub and display the latest official release|
|-h or -?| **HELP:** Display the program help and exit|

## Operands
The following is the list of operands supported by RPNCalc.  These are also supported by the NumOps shortcut described below.  

|Operand|Description|
|-------|-----------|
|+ |Add the last two items on the stack|
|- |Subtract line1 from line2|
|* |Multiply line1 and line2|
|/ |Divide line2 by line1|
|^ |Take line2 to the power of line1|

## NumOps Shortcut
There is an important shortcut that you can use.  You can append one of the above operands at the end of an entered number and the program will, behind the scenes, place the number on the stack and then execute the operand.  For example:

`2 [ENTER]`
`3+ [ENTER]`
 
When the second enter is pressed,  the two stack items will be removed, added together, and the result, `5`, will be added back.  It's the same as entering this the following  three commands:  `2 [ENTER]` `3 [ENTER]` `+ [ENTER]`

## Calculator Commands
|Command|Description|
|-------|-----------|
|u [#]<br> undo [#]| **UNDO:** By default, undo the last operation.  However, if an undo stack line number is given, as displayed in the `list undo` command, undo will restore the stack back to that step.  Please keep in mind that if you restore back to a previous undo state, later undo states will be discarded.  Typically, however, `u` undo is used to undo the previous step|
|f <br> flip | **FLIP SIGN:** Flip the sign on the top stack item / line1|
|c <br> clear| **CLEAR:** Clear the screen, and empty the current stack.  Memory data is retained and you can undo the clear with the `u` undo command|
|cl <br> clean| **CLEAN SCREEN:** Clear the current screen, but keep the stack.  After cleaning, the stack will be displayed at the top of the screen|
|d [#] <br> del [#] <br> drop [#]| **DELETE:** Delete the top stack item (line 1) or, optionally, delete the line number provided with `d <linenumber>` command|
|s [#] \[#] <br> swap [#] \[#]| **SWAP LINES:** Swap the position of the top two stack items (line 1 & 2) with `s`.  You can swap any two line numbers in your stack by providing the two line numbers `s # #`|
|%| **PERCENT:** Assumes line1 contains a percent.  This converts that into a number by simply dividing that value by 100.  For example, if you want to take 50.123% of a number, you could just enter in `50.123 [ENTER] % [ENTER] *`|
|sqrt| **SQUARE ROOT:** Perform a [square root](https://en.wikipedia.org/wiki/Square_root) of the top stack item / line1|
|round [n]| **ROUND:** Round the top stack item to [n] decimal places.  If [n] is not given, round to the nearest integer (zero decimal places).  Example1: `3.14159` `round` would round to `3`.  Example2: `3.14159` `round 4` would round to `3.1416`|
|aa [keep]| **ADD ALL:** Add all stack items together and return the result to the stack.  If the optional `keep` parameter is sent, the elements added will be retained on the stack and the total will be added to the top of the stack.  The entire `keep` command is not necessary, anything that starts with `k` will work|
|mod| **MODULUS:** [Modulus](en.wikipedia.org/wiki/Modular_arithmetic) is the remainder after a division.  This command will perform a division of the top two stack items and return the remainder only back to the stack|
|avg [keep]|**AVERAGE:** Calculate the average of the numbers on the stack.  The stack will be replaced with the average value.  If `keep` is provided, the stack will be retained and the average will be added on top. `average` or `mean` can also be used|
|sd [keep]|**STANDARD DEVIATION:** Calculate the [standard deviation](https://en.wikipedia.org/wiki/Standard_deviation) of the items in the stack.  The stack items will be replaced by the result. If `keep` is provided, the the standard deviation will simple be added to the top of the stack in line1|
|copy [#] <br> dup [#]|**COPY:** With no number provided, copy will duplicate the top stack item / line1 and place it on the stack.  If the optional line number is given, the value at the line will be duplicated to the top of the stack|
|log|**LOGARITHM BASE e:** Calculates the [natural logarithm (base e)](https://en.wikipedia.org/wiki/Natural_logarithm)|
|log10|**LOGARITHM BASE 10:** Calculates the [base10 logarithm](https://en.wikipedia.org/wiki/Common_logarithm)|
|int| **INTEGER:** Converts the top stack item (line 1) to it's integer value.  This will discard the decimal portion regardless of it's value.  For example: `4.34` will result in `4`.  `4.999` will also result in `4`. If rounding is desired, execute the `round` command prior to `int`|
|abs| **ABSOLUTE VALUE:** Takes the [absolute value](https://en.wikipedia.org/wiki/Absolute_value#:~:text=In%20mathematics%2C%20the%20absolute%20value,and%20%7C0%7C%20%3D%200) of line 1.  The returns the positive value of the number|
|Min|**MINIMUM VALUE:** Add the smallest value in the stack to the top of the stack|
|Max|**MAXIMUM VALUE:** Add the largest value in the stack to the top of the stack|
|rand [low] \[high] | **RANDOM NUMBER GENERATION:** Generate a random integer number between the provided [l]ow and [h]igh numbers inclusive to both.  If no numbers are provided, then the random number will be between 1 and 100 inclusive|
|dice XdY| **DICE ROLL:** Roll a Y sided die X times and add the results to the stack.  Default is 1d6. While not a normal calculator function, I find it fun|

## Conversions
The conversion commands make assumption as to the unit of the numbers and will convert to another unit base.  The example I use this for mostly, is inch to millimeters and the reverse.  I can easily add additional conversions if requested
|Command|Description|
|-------|-----------|
|frac [base]|Display a fractional estimate of the last stack item with the maximum granularity of 1/base.  The default base is 64 which corresponds to 1/64th.  Only decimals are stored on the stack but this command will display the results.  For example, if you had **1.1234** on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  It would have used a base of 64 (which means maximum granularity would be 1/64.  However, it auto reduces which is why you get the `1 1/8`. if you entered `frac 2` (which means 1/2 is maximum granularity, you get `1.1234 is approximately 1 0/1` or just one.  Need to fix that display oddity.  See above for a more detailed description|
|in2mm|Converts the value in line1 from inches to millimeters.  `2mm` command will also work|
|mm2in|Converts the value in line1 from millimeters to inches.  `2in` command will also work||
|deg2rad|Convert line1 from degrees into [radians](https://en.wikipedia.org/wiki/Radian). `2rad` would also work|
|rad2deg|Convert line1 from radians into degrees.  `2deg` would also work|

## Trigonometry Functions
|Command|Description|
|-------|-----------|
|sin, cos, tan [rad]|Calculate the [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Angles are input as degrees by default unless the **rad** parameter is given in which case the angles will be in [radians](https://en.wikipedia.org/wiki/Radian).  Example: `tan` will calculate the tangent using row 1 as the angle in degrees.  Use `tan rad` if row 1 contains the angle in radians|
|asin, acos, atan [rad]|Calculate the arc [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Result is returned in degrees unless **rad** parameter is provided|
|hypot <br> hypotenuse| Assumes the top two stack items are the right triangles legs and this function returns the hypotenuse using the [Pythagorean theorem](https://en.wikipedia.org/wiki/Pythagorean_theorem).  Specifically, it returns SQRT( (line1)^2 + (line2)^2 )|

## Memory Commands
|Command|Description|
|-------|-----------|
|mem [slot] add|Add the top stack item / line1 into the memory slot provided.  By default, there are 10 slots; 0 through 9.  If you do not provide a slot number it will default to slot 0.  Example:  `mem add`   will add the last stack item into memory slot0|
|mem [slot] <br> copy [slot] <br> recall [slot]|Copies the contents of memory slot provided back onto the stack.  Defaults to Slot0 if no slot number is provided|
|mem [slot] clr|Clear the contents of the memory slot provided.  Defaults to Slot0 if no slot is provided.  Example: `mem 2 clr`  The command `clear` can also be used instead of `clr`|
|mem clearall|Clear the contents of all memory slots.  There is no need to include a SlotNumber as they will all be erased.  Note `mem clrall` will also work|
|mem copyall|Copy all items in memory to the stack.  These will be ordered via memory slot number. i.e. Memory slot 0 will be at the top of the stack / line1. `recallall` will also work|
|mem addall|Add all items in the stack to a memory slot.  The order will be the same as the stack with memory slot0 being the top of the stack.  The command will fail if there are more stack items than there are memory slots - which default to 10.  It may simply be easier to save the stack with a name using the load `-l` command.  Also, any values currently in a needed memory slot will be overwritten|

## Constants
Simply add the value of the requested constant to the top of the stack / line1
|Constant|Description|
|--|--|
|pi| Archimedes' constant, or PI, is the name given to the ratio of the circumference of a circle to the diameter. `PI` inserts the value of [PI](https://en.wikipedia.org/wiki/Pi) onto the stack.  Pi is approximately `3.14159265359`|
|phi| Insert [PHI](https://en.wikipedia.org/wiki/Golden_ratio), also known as the Golden Ratio, to the stack.  Phi is approximately `1.618033989`|
|euler| Euler's number is also known as the exponential growth constant. It is the base for natural logarithms and is found in many areas of mathematics. The command `euler` inserts [Euler's number (e)](https://en.wikipedia.org/wiki/E_(mathematical_constant)) onto the stack.  e is approximately `2.7182818284590`|
|sol|Inserts the speed of light onto the stack in meters/second.  **c = 299,792,458 m/s**|

## User Defined Functions
RPNCalc can record your commands and save them as a user defined function.  In essence, you are creating a new command that can be run.

When recording a new function, start by adding data to your stack that would emulate when you would run your function.  The only data that will be part of your recording will be data & commands entered ***after*** recording has been turned on.  When you are ready, enter the `record on` command.  A red `[Recording]` alert will appear in the status line. Anything you add during this period will be recorded with the exception of the commands listed below.  Enter your commands, numbers, etc. until you are done.

When recording is complete, enter `record off` to complete your recording.  You'll be prompted for a function name.  Your new command will be this name so choose a name without spaces and that is easy to type when you wish to execute the function in the future.  You should also not choose a name of an existing command as your function will not be able to be called.  If you do not provide a name, i.e, just hit enter, the recording will be discarded.

Then you can run your function whever you like on the stack currently available.  To run the function, simply type the name of your function as a standard command. To see a list of the saved functions, execute `list func` and it will display the name and the steps you recorded.

User defined functions can be deleted with the `func del NAME` command or you can delete all of the functions with `func delall`

Functions are global and can work across any stack.  They are saved in the preferences system and will be reloaded when RPNCalc starts.  They are saved immediatly after you give a new recording a name and press enter.

When you execute a function, the steps of that function are executed one after the other.  Therefore when you execute `undo` you will undo back through your function step by step. You do not `undo` the entire function in one command.

The following commands can be entered during a recording, but are not recorded.
- frac
- list
- debug
- ver, version
- help, h, ?
- rec, record
- func, function
- cx, x, or exit

|Command|Description|
|-------|-----------|
|record on<br>rec on|Turn on recording.  Most commands and numbers entered after record is enabled will be saved.  There are some that are excluded from being recorded as detailed above|
|record off<br>rec off| Turn off recording.  The user will be prompted to enter in a name for this function and that name will be used to run it in the future.  If you do not enter in a name the recording is canceled and  nothing will be saved|
|func del NAME|Delete a saved function.  The name must match the one given when saved.  A list of functions can be viewed with `list func`.  Undo will not recover a deleted function|
|func delall|Delete all saved user defined functions. Please note that undo will not recover deleted functions|

**Example:**
|||
|-------|-----------|
|`c`|Clear the stack|
|`3 [Enter]`|Add the number 3 to the stack
|`record on`|Start recording
|`2`|Add 2 to the stack
|`^`|Square the 3
|`record off`|Stop recording and give the name "square" to the user defined function
|`square`|Square the top item on the stack

## Operational Commands
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

## SNAP
[![rpncalc](https://snapcraft.io//rpncalc/badge.svg)](https://snapcraft.io/rpncalc)

I would encourage anyone with a supported Linux platform to use the Snap package.  See [Snapcraft Homepage](https://snapcraft.io) for more information. You can download, install, and keep RPNCalc up to date automatically by installing the snap.  You don't even have to have java as it's bundled within the snap virtual machine.  Install via:

`sudo snap install rpncalc`  (Assuming snapd is installed)

This will install the application into a 'sandbox' where it is separate from other applications.

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/rpncalc)

## Wrapup
I'm making this available in the hope that others may find this useful.  Please let me know if you have any issues, thoughts or suggestions for enhancements by sending an email to rpncalc@fross.org.  

## License
[The MIT License](https://opensource.org/licenses/MIT)

Copyright (C) 2011-2022 by Michael Fross

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
