<img align="right" width="175" src="../Images/WhatIsRPN.jpg">

# What is an RPN Calculator?

For me, the way I thought about using RPN back in the early 1990s was to approach it with `start on the inside and work your way out`.  Just look at the inner most calculation, and start working yourself back out.  It also helps if you also understand the order of operations for the operands.

### Order of Operations - *PEMA*

|Order|Abbr|Description|
|:---:|:--:|-----------|
|#1|P|Parentheses First|
|#2|E|Exponents - Powers and Square Roots, etc.|
|#3|M|Multiplication and Division (left-to-right)|
|#4|A|Addition and Subtraction (left-to-right)|

### Walkthrough of the Example from the Introduction Page

`x = SQRT( ((((5+3) * 8)/2) * (2+1)) ^ 2 )`

This can be tricky with a traditional calculator.  However, with a RPN Calculator, to solve for x you would start on the inner calculation and work outwards.  Looking at the equation, `5 + 3` is about the lowest so lets start there. The approach will be work back up until you get to a peer of the same "level".  Then do the `2+1`. We'll walk through each step and explain what's happening.  Go ahead and start RPNCalc and follow along - it's much easier to do it and see the stack rather than just reading it here.

|Command|Explanation|
|-------|-----------|
|`c [Enter]`| Clear the stack if there is anything on it.  Not really needed, but lets start off clean|
|`5 [Enter]`| The top of the stack (`line1`) contains a 5|
|`3 [Enter]`| 3 has been added to the top of the stack (`line1`) pushing 5 to `line2`|
|`+ [Enter]`| 3 and 5 have been removed from the stack, added, and 8 pushed to `line1`|
|`8 [Enter]`| 8 is added to `line1`, pushing the other 8 to `line2`|
|`* [Enter]`| Remove 8 and 8 from the stack, multiply, and push 64 to `line1`|
|`2 [Enter]`| Add 2 to the stack pushing down 64|
|`/ [Enter]`| Divide 64 (`line2`) by 2 (`line1`) and replace those numbers with 32|
|`2 [Enter]`| Add 2 to the stack|
|`1 [Enter]`| Add 1 to the stack|
|`+ [Enter]`| Add 2 and 1.  They will be removed and 3 will be pushed to `line`|
|`* [Enter]`| Multiply the 3 to the 32 you had in `line1` resulting in 96|
|`2 [Enter]`| Add 2 to the stack|
|`^ [Enter]`| Take `line2` to the power of `line1` resulting in 9,216|
|`SQRT [Enter]`| Takes the square root of `line1` pushing the result to the stack|

The result is 96 which ends up on the top (`line1`) of an otherwise empty stack.  I'll talk more about stacks and how RPNCalc uses them in future chapters.

*By the way, this can also be dramatically shortened using the `NumOps` shortcut - see the `Operands` chapter for more information.*


### Traditional RPN Notation Limitations in RPNCalc

Please note that RPNCalc doesn't implement every RPN convention and there are a few as of yet unimplemented items.

For example, this won't work in RPNCalc:

`10 + 10 + 10` can't be solved by entering `10 [Enter] 10 [Enter] 10 [Enter] ++ [Enter]` as RPNCalc only allows one operand per command entry.  You'll need an `[Enter]` between the last two `+` signs.  Using the `NumOp` shortcut, this is an efficient way to solve that equation:

`10 [Enter] 10+ [Enter] 10+ [Enter]`

<hr>

## Wikipedia:

The following is directly from [Wikipedia](https://en.wikipedia.org/wiki/Reverse_Polish_notation).  There is a lot more information there, but here are a few highlights.

### Explanation

In reverse Polish notation, the operators follow their operands; for instance, to add 3 and 4 together, one would write `3 4 +` rather than `3 + 4`. If there are multiple operations, operators are given immediately after their final operands (often an operator takes two operands, in which case the operator is written after the second operand); so the expression written `3 − 4 + 5` in conventional notation would be written `3 4 − 5 +` in reverse Polish notation: 4 is first subtracted from 3, then 5 is added to it. An advantage of reverse Polish notation is that it removes the need for parentheses that are required by infix notation. While `3 − 4 × 5` can also be written `3 − (4 × 5)`, that means something quite different from `(3 − 4) × 5`. In reverse Polish notation, the former could be written `3 4 5 × −`, which unambiguously means `3 (4 5 ×) −` which reduces to `3 20 −` (which can further be reduced to -17); the latter could be written `3 4 − 5 ×` (or `5 3 4 − ×`, if keeping similar formatting), which unambiguously means `(3 4 −) 5 ×`.

### Practical implications

In comparison, testing of reverse Polish notation with algebraic notation, reverse Polish has been found to lead to faster calculations, for two reasons. The first reason is that reverse Polish calculators do not need expressions to be parenthesized, so fewer operations need to be entered to perform typical calculations. Additionally, users of reverse Polish calculators made fewer mistakes than for other types of calculators. Later research clarified that the increased speed from reverse Polish notation may be attributed to the smaller number of keystrokes needed to enter this notation, rather than to a smaller cognitive load on its users. However, anecdotal evidence suggests that reverse Polish notation is more difficult for users to learn than algebraic notation.