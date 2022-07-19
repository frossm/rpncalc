<img align="right" width="20%" src="../Images/Operands.png">

# Operands

Operands are the standard symbols used for basic mathematics on numbers.  The following is the list of operands supported by RPNCalc.  These are also supported by the NumOps shortcut described below.  

With the exception of the addition (`+`) and multiplication (`*`) operand the order is important.  It's faily intuitive when you look at it in the calculator, but refer to the following table if needed.

|Operand|Math|Description|
|-------|----|-------|
|+  |`ADDITION`| Add `line2` and `line`|
|-  |`SUBTRACTION`| Subtract `line1` from `line2`|
|\* |`MULTIPLICATION`| Multiply `line1` and `line2`|
|/  |`DIVISION`| Divide `line2` by `line1`|
|^  |`POWER`| Take `line2` to the power of `line1`|

For example:

`3` `[Enter]` `2` `[Enter]` `-`  will execute 3 - 2 and will yield `1`

`2` `[Enter]` `3` `[Enter]` `-` will execute 2 - 3 and will yield `-1`

Hint: We'll discuss commands in later chapters, but the Swap command `s` will swap the top two items in the stack (`line1` and `line2`).  Swap will also swap any to line numbers provided.  Jump to the `Calculator Commands` chapter for more information.

## The NumOps Shortcut

RPNCalc has an important shortcut that can speed up your calculations.  You can append one of the above operands at the end of an entered number and the program will, behind the scenes, place the number on top of the stack (`line1`) and then execute the operand.  For example:

`2 [ENTER]`

`3+ [ENTER]`
 
When the second enter is pressed,  the two stack items will be removed, added together, and the result, `5`, will be added back.  It's the same as entering this the following  three commands:

`2 [ENTER]`

`3 [ENTER]`

`+ [ENTER]`

As an example of this NumOps shortcut, lets revisit our example from the introduction.

`x = SQRT((((5+3) * 8)/2) ^ 6)`

Leveraging the shortcut, this would become:

- `5`
- `3+`
- `8*`
- `2/`
- `6^`
- `SQRT`

By the way, if you were wondering the answer is 32,768.

***Please note that you can not use this NumOps shortcut with a fractional number entry***