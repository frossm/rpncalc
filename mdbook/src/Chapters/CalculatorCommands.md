<img align="right" width="175" src="../Images/CalcCommands.png">

# Calculator Commands

We've discussed numbers, both fractional and decimal as well as operands that perform basic mathematical functions.  This chapter starts discussing commands which are the heart and soul of RPNCalc.

In Calculator Commands, these are the basic commands that operations on the numbers on the stack.  Most are fairly simple and intuitive, but there are a few that are a bit odd.  I'm looking at you `dice`.

These commands, like the others you'll read about later, are executed by typing the command name, and any needed arguments into RPNCalc.  Often there are several different names and abreviations for the same command.  This is just to make it easier to remember.  Brackets, `[]` around an option denotes it is optional.

|Command|Description|
|-------|-----------|
|%| **PERCENT**<br>Assumes `line1` contains a percent.  This converts that into a number by simply dividing that value by 100.  For example, if you want to take 50.123% of a number, you could just enter in `50.123 [ENTER] % [ENTER] *`|
|aa [keep]| **ADD ALL**<br>Add all stack items together and replace the numbers on the stack with the result.  If the optional `keep` parameter is sent, the elements added will be retained on the stack and the total will be added to the top of the stack.  The entire `keep` command is not necessary, anything that starts with `k` will work|
|abs| **ABSOLUTE VALUE**<br>Takes the [absolute value](https://en.wikipedia.org/wiki/Absolute_value#:~:text=In%20mathematics%2C%20the%20absolute%20value,and%20%7C0%7C%20%3D%200) of line 1.  The returns the positive value of the number.  Not the most useful command on it's own as you can always `f` flip the sign.  However it could be useful as part of a User Defined Function|
|avg [keep]|**AVERAGE**<br>Calculate the average of all of the numbers on the stack.  The stack will be replaced with the result.  If `keep` is provided, the stack will be retained and the average will be added on top. `average` or `mean` can also be used|
|c <br> clear| **CLEAR**<br>Clear the current stack and the screen.  Memory data is retained and you can undo the clear with the undo `u` command|
|cl <br> clean| **CLEAN SCREEN**<br>Clear the current screen, but keep the stack.  After cleaning, the stack will be displayed at the top of the screen.  Used to remove the clutter|
|copy [#] <br> dup [#]|**COPY**<br>With no number provided, copy will duplicate the top stack item / `line1` and place it on the stack.  If the optional line number is given, the value at the line will be duplicated to the top of the stack|
|d<br>d [#]<br>d [#-#]| **DELETE**<br>If `d` is given without any parameters, it will delete `line1`.  If a line number is given after the `d`, that line number will be deleted.  A range can be given as well, such as `d 1-3` and RPNCalc will delete those lines and everything in between.  `del` can also be used|
|dice XdY| **DICE ROLL**<br>Roll a Y sided die X times and add the results to the stack.  Default is 1d6. While not a normal calculator function, it's fun!|
|f <br> flip | **FLIP SIGN**<br>Flip the sign on the top stack item (`line1`). This is effectively multiplying `line1` by -1|
|int| **INTEGER**<br>Converts the top stack item (`line1`) to it's integer value.  This will discard the decimal portion regardless of it's value.  For example: `4.34` will result in `4`.  `4.999` will also result in `4`. If rounding is desired, execute the `round` command prior to `int`|
|log|**LOGARITHM BASE e**<br>Calculates the [natural logarithm (base e)](https://en.wikipedia.org/wiki/Natural_logarithm)|
|log10|**LOGARITHM BASE 10**<br>Calculates the [base10 logarithm](https://en.wikipedia.org/wiki/Common_logarithm)|
|Min|**MINIMUM VALUE**<br>Copies the small value in the current stack to `line1`|
|Max|**MAXIMUM VALUE**<br>Copies the largest value in the current stack to `line1`|
|mod| **MODULUS**<br>[Modulus](en.wikipedia.org/wiki/Modular_arithmetic) is the remainder after a division.  This command will perform a division of the top two stack items and return the remainder only back to the stack|
|rand [low] \[high] | **RANDOM NUMBER GENERATION**<br>Generate a random integer number between the provided `[l]ow` and `[h]igh` numbers inclusive to both.  If no numbers are provided, then the random number will be between 1 and 100 inclusive|
|round [n]| **ROUND**<br>Round `line1` to `[n]` decimal places.  If `[n]` is not given, round to the nearest integer (zero decimal places).  **Example 1:** `3.14159` `round` would round to `3`.  **Example 2:** `3.14159` `round 4` would round to `3.1416`|
|s [#] \[#] <br> swap [#] \[#]| **SWAP LINES**<br>Without an argument, swap the top two stack items (`line1 & line2`).  You can swap any two line numbers in your stack by providing the two line numbers|
|sd [keep]|**STANDARD DEVIATION**<br>Calculate the [standard deviation](https://en.wikipedia.org/wiki/Standard_deviation) of the items in the stack.  The stack items will be replaced by the result. If `keep` is provided, the numbers on the stack will not be removed and the standard deviation will simply be added to the top of the stack on `line1`|
|sqrt| **SQUARE ROOT**<br>Perform a [square root](https://en.wikipedia.org/wiki/Square_root) of the top stack item (`line1`)|
|u [#]<br> undo [#]| **UNDO**<br>By default, undo the last operation.  However, if an undo stack line number is given, as displayed with the `list undo` command, undo will restore the stack back to that point.  Please keep in mind that if you restore back to a previous undo state, later undo states will be discarded.  You can't go back. Typically, however, `u` undo is used to simply undo the previous action|
