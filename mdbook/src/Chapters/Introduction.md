<p align="center"> <img width="1024" src ="https://github.com/frossm/rpncalc/raw/master/graphics/ReadmeHeader.jpg"> </p> 

# INTRODUCTION
RPNCalc is the command-line based [Reverse Polish Notation (RPN) calculator](https://en.wikipedia.org/wiki/Reverse_Polish_notation).  
RPN calculators make it very simple to do complex calculations, especially if there are
parentheses involved.  In essence, you enter your numbers first, then the operator.

So, to add 2 and 3 to get 5, you would first add the number 2 to the stack:  `2 [Enter]`.  Then 
you would add 3 to the stack (pushing the 2 to the second position):  `3 [Enter]`.  Now, to add 
them you would enter: `+ [Enter]` and RPNCalc would remove the 2 and 3 from the stack, add them, and push 5 back onto the stack.

The following example can be tricky to get your head around in a traditional calculator but is quite easy with RPN.  The chapter `What is an RPN Calculator` 
will go through solving this step by step.  ***(Spoiler warning: just start in the middle and work your way out.)***

`x = SQRT(((((5+3) * 8)/2) * (2+1)) ^ 2)`

RPN is based on a Last In First Out (LIFO) stack framework which sounds complicated, but makes intuitive sense when you use it.  
You can think of it as a stack of plates.  When you put one on the top of the stack, everyone other one shifts down by one. On an RPN calculator, 
there is no equal sign, but there is an enter key to put a value onto the stack.  The chapter on `Stacks` talks more about how this works with RPNCalc.

This guide also has chapters on the various functions provided by the calculator and how to use them.  If you find something that is unclear, or 
could benefit from an example or two, please let me know.  Contact information is below.

If you have not heard of reverse Polish notation, or just have a passion for various calculator notations (and seriously, who doesn't?), you can read more about RPN on
[Wikipedia](https://en.wikipedia.org/wiki/Reverse_Polish_notation) or a (very) quick summary in the `What is an RPN Calculator` chapter.


## My Brief History of RPNCalc

<img align="right" width="150" src ="https://github.com/frossm/rpncalc/raw/master/graphics/PostIt-200x200.jpg">

I studied engineering in [college](https://purdue.edu/) and used the [HP 15C](https://hpcalcs.com/product/hp-15c-collectors-edition) calculator (I think that might date 
me...) I grew to love RPN and over the years I've used various GUI RPN 
calculators.  However, I failed to find a simple command line version that I liked.  Therefore, I decided to write one back in 2011.  

One key goal was to make it extensible, so I could easily add new features, constants, and 
capabilities.  A second goal was to allow it to run everywhere I needed it to run, which is 
mostly Windows and Linux.  Therefore, it's written in Java, it should run just about anywhere 
Java is supported.  And, while this calculator doesn't have ***every*** function a complex 
scientific calculator would have, it has the basics well covered (along with a few oddball 
functions - I'm looking at you `dice`)

Lastly, I didn't want to have a big uninstall/install task to upgrade to a new version.  With RPNCalc, it's just one JAR file.  You just run it 
(`java -jar rpncalc.jar`).  Nothing to install or uninstall.  If you want to remove it, just delete the `rpncalc.jar` file. You can now also use the SNAP version 
on supported operating systems which means you don't even need to install Java as it's built into the SNAP package.


## Contact Options & Links

The home page for RPNCalc is located on [GitHub](https://github.com/frossm/rpncalc).  It's open source with a **very** open usage license.

- Report issues or enhancement requests, please post at the RPNCalc [issues page](https://github.com/frossm/rpncalc/issues)
- For RPNCalc discussions, you can post on the [Discussion page](https://github.com/frossm/rpncalc/discussions)
- The RPNCalc [Snap Store Page](https://snapcraft.io/rpncalc)
- You can also reach me directly at `rpncalc@fross.org`
- Checkout my other open source programs on [GitHub](https://github.com/frossm)


## The In-Line Help from RPNCalc

From within the program, entering `h`, `help` or `?` will show the program help screen. The help can also be viewed by starting RPNCalc with the `-h` or `-?` command line switch (i.e. `rpncalc -h`)

**Please note I do not update this screenshot with every release so it may be a bit dated.  
See the actual program for the up-to-date help screen.**

The (mostly) current RPNCalc help screen:<br>

<img align="center" width="100%" src="https://github.com/frossm/rpncalc/raw/master/graphics/ScreenShot1.jpg">
<img align="center" width="100%" src="https://github.com/frossm/rpncalc/raw/master/graphics/ScreenShot2.jpg">