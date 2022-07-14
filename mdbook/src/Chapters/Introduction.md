<p align="center"> <img width="1024" src ="https://github.com/frossm/rpncalc/raw/master/graphics/ReadmeHeader.jpg"> </p> 

# INTRODUCTION
RPNCalc is the command-line based Reverse Polish Notation (RPN) calculator.  RPN calculators make it very simple to do complex calculations, especially if there are parentheses involved.  For a quick example, consider solving for x with the following:

`x = SQRT((((5+3) * 8)/2) ^ 6)`

This can be tricky with a traditional calculator.  However, with a RPN Calculator, to solve for x you would start on the inner calculation and work outwards by entering the following:

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

(This can also be dramatically shortened using the `NumOps` shortcut - see the `Operands` chapter)

RPN is based on a Last In First Out (LIFO) stack framework which sounds complicated, but makes intuitive sense when you use it.  On a RPN calculator, there is no equal sign, but there is an enter key to put a value onto the stack.  

So, in the above example, when you enter **`5 [ENTER]`** , 5 is put on top of the stack.  You then enter **`3 [ENTER]`** which add that to the top.  5 is pushed down to 2nd.  When **`+`** is entered, the top two stack items are removed from the stack, added, and the result is placed on the top.  I'll talk more about stacks and how RPNCalc uses them in future chapters.

If you have not heard of reverse Polish notation, or just have a passion for various calculator notations (and seriously, who doesn't?), you can read more about RPN on [Wikipedia](https://en.wikipedia.org/wiki/Reverse_Polish_notation).


## A Brief History of RPNCalc

Over the years I've used various RPN calculators, but I failed to find a simple command line version that I liked.  Therefore, I decided to write one back in 2011.  One key goal was to make it extensible so I could easily add new features, constants, and capabilities.  A second goal was to allow it to run everywhere I needed it to run, which is mostly Windows and Linux.  Therefore it's written in Java, it should run just about anywhere Java is supported.  And, while this calculator doesn't have **every** function a complex calculator would have, it has the basics covered.

If there are ideas, suggestions, or complaints, drop me an email at RPNCalc@fross.org or leave a comment in the GitHub issues tracker.  I am continually adding new capabilities to it when the need arises.

## The Current Help Page from RPNCalc

This can be achieved by entered `h` or `help` from within the program or running `rpncalc -h` from the command line.

<br>

<img align="center" width="100%" src="https://github.com/frossm/rpncalc/raw/master/graphics/ScreenShot.jpg">