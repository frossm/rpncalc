# High Level Usage
<img align="right" width="25%" src="../Images/CalcIcon.png">

RPNCalc is a command line application that must be run from a console / command prompt.  Executing it with a `-h` (or `-?`) switch, or starting the program and entering the `h` command will display the in-program help page.  This page lists all of the commands and operands that can be used, but it is fairly terse  This can be viewed at the bottom of the `Introduction` page of this guide. This document is meant as a more comprehensive guide.

There are various command line switches that can be used when starting the program as are detailed in the `Command Line Options` page.  They generally exist so that aliases can be used to control several key parameters.  

Once inside the program, you'll be presented a prompt where numbers, operands, and commands may be entered.  Numbers will be added to the stack which you can think of a stack of plates.  The top stack item (represented by `line1` in the program).  You can think of this stack of plates as a Last In First Out (LIFO) arrangement.

For example, you could enter `2 [ENTER]` it would be in the `line1` position and would be on the top of the stack.  If you then enter `3 [ENTER]` the `2` would move up and the `3` would then be on `line1` and be on the top of the stack.  You can then enter in an operand, such as `+` to perform the action on the items opn the top of the stack.  To continue our example, pressing `+ [ENTER]` would take the top two items off of the stack, add them, and put the result back on top of the stack.  

I'll go into a bit more detail below on using stacks, but it's fairly easy and intutive.  Once you get the hang of it, you'll regret having to use a standard calculator in the future.

### Decimals & Fractions

In RPNCalc, the stacks always store numbers as decimals.  You can, however, enter in fractions and they will be instantly converted to a decimal equivalent and added to the stack.  The original fraction you entered is lost in the conversion.

**Example:**

`1 5/16 [ENTER]` 

will add `1.3125` to the top of the stack (`line1`)

`14/8 [ENTER]` 

will add `1.75` to the stack

While you can never directly convert a decimal number on the stack back to a fraction, you can display an approximation of what the last stack item would be as a fraction.  You do have to decide on the smallest denominator that will be used (called the base.)  The default base is `64` which is the equivalent of `1/64` and will be used if none is specified.  Use the `frac [base]` command to display the approximate fractional equivalent.  RPNCalc will simplify the fraction as much as it can so you won't see `2/4` as a fraction, you'd see `1/2`.

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

## Operands, Numbers, and Commands

Numbers, whether decimal or fractions, can be entered on the command line and they get added to the stack.  That's fairly self explanatory.

Operands perform basic match functions on those numbers.

Commands do the more exciting things.  You can add the speed of light constant to the stack (`sol`) or PI (`pi`), take the sine of the number, add it to a memory slot, and then save that as a user defined function.  Most of the rest of this guide will be talking about the various RPNCalc commands.