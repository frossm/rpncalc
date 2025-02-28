<img align="right" width="125" src="../Images/HighLevelUsage.png">

# High Level Usage

RPNCalc is a command line application that must be run from a console / command prompt.  Executing it with a `-h` (or `-?`) switch, or starting the program and entering 
the `h` (or `help` or `?`) command will display the in-program help page.  This page lists all the commands and operands that can be used, but it is fairly terse  This can be also viewed at the bottom of the `Introduction Chapter` of this guide. This document is meant as a more comprehensive guide.

There are various command line switches that can be used when starting the program as are detailed in the `Command Line Options Chapter`.  They generally exist so that aliases can be used to control several key parameters, most likely the `-l StackName` switch.

Once inside the program, you'll be presented a prompt where numbers, operands, and commands may be entered.  Numbers will be added to the stack which you can think of as an upside down stack of plates.  The top stack item (represented by `line1` in the program) is on the bottom.  You can think of this stack of plates as a Last In First Out (LIFO) approach.

For example, you could enter `2 [ENTER]` it would be in the `line1` position and would be on the top of the stack.  If you then enter `3 [ENTER]` the `2` would move up go `line2` and the `3` would then be on `line1` and be on the top of the stack.  You can then enter an operand, such as `+` to perform the action on the items opn the top of the stack. To continue our example, pressing `+ [ENTER]` would take the top two items off of the stack, add them, and put the result back on top of the stack (`line1`).  

I've gone into this in more detail in the `What is an RPN Calculator Chapter` and elsewhere and it's fairly easy and intuitive.  Once you get the hang of it, you will regret having to use a standard calculator in the future. ;-)

#### Why is the stack "upside down?"

One question I get with RPNCalc is why is the top of the stack on the bottom?  The reason is that it's simply more intuitive.  The command line is on the bottom.  You are usually dealing with the top of the stack so having `line1` directly above makes sense.  Also, for some operations, the order is important (think subtraction or division).  Having `line1` "underneath" `line2` is easy to understand as that's how we learned to do subtraction.  `line1` is subtracted from `line2`.

### Decimals & Fractions

In RPNCalc, the stacks always store numbers as decimals.  You can, however, enter in fractions, and they will be instantly converted to a decimal equivalent and added to 
the stack.

**Example:**

`1 5/16 [ENTER]` 

will add `1.3125` to the top of the stack (`line1`)

`14/8 [ENTER]` 

will add `1.75` to the stack

While you can never directly convert a decimal number on the stack back to a fraction, you can display an approximation of what the top of the stack (`line1`) value would be as a fraction.  You do have to decide on the smallest denominator that will be used (called the base.)  The default base is `64` which is the equivalent of `1/64` and will be used if no base is specified.  Use the `frac [base]` command to display the approximate fractional equivalent.  RPNCalc will simplify the fraction as much as it can so if `line1` is `0.5`, the command `frac 4` won't convert and display `2/4`.  The result would be `1/2`.  Also note that `frac [based]` is a display command and will not actually change the stack in any way.

**Example:**
`12.3456 [Enter]`
`frac [Enter]`

No base was entered, so use `64`. It will display `12 11/32`  RPNCalc converted it to `12 22/64` and then reduced it.

`1 3/64 [Enter]`
`frac 16 [Enter]`

will display `1 1/16` as that's as close as it could get with a granularity of 1/16.

`1 3/64 [ENTER]`
`frac 100000`

will display `1 293/6250`.  This is a closer approximation than using base 16. This also shows why this is an approximate.

### Scientific Notation

As of version 5, scientific notation is supported.  You can enter in values with the format `1.2345E18` and it will be saved as a value in the stack. There are a few areas where it's not 100% supported (i.e. NumOps at the time of this writing) but just about everything will work with it.

### Percent Entry

As of version 5.4.0, a number, followed by a `%` can be entered.  This will be divided by 100 prior to being added onto the stack.  This will work with both negative 
numbers and scientific notation.

## Operands, Numbers, and Commands

Numbers, whether decimal or fractions, can be entered on the command line, and they get added to the stack.  That's fairly self-explanatory.

Operands perform basic match functions on those numbers.

Commands do the more exciting things.  You can add the speed of light constant to the stack (`sol`) or PI (`pi`), take the sine of the number, add it to a memory slot, and then save that sequence of commands as a user defined function.  Most of the rest of this guide will be talking about the various RPNCalc commands.

Lastly, as of `v4.6.0`, the arrow keys can be used within RPNCalc.  Up/Down will move you through your historical entries, and Left/Right will move you within the current command line.  This is probably what you would have expected it to do as it behaves similar to common consoles.

## Precision
The intent of RPNCalc is to have unlimited precision in the numbers and calculations.  RPNCalc leverages a Java technology called `BigDecimal` which limits precision only by the amount of memory in your machine.  This is most likely more than you will ever need.  However, the program does make use of several Java math methods which must be done via a Java `Double`.

A `Double` has the following characteristics:
- The upper range of a double in Java is `1.7976931348623157 x 10^308`
- The lower range of a double in Java is `4.9 x 10^-324`

I will attempt to point out in this guide when a command is using `Double` and therefore has a limit on precision. Double is, however, huge and shouldn't pose an issue for most use cases.