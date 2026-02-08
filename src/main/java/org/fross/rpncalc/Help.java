/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 * Copyright (c) 2011-2026 Michael Fross
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ------------------------------------------------------------------------------*/
package org.fross.rpncalc;

import org.fross.library.Format;
import org.fross.library.GitHub;
import org.fross.library.Output;

/**
 * Help(): Display the help page when users enters 'h' or '?' command.
 *
 * @author Michael Fross (michael@fross.org)
 */
public class Help {
   protected static final String HOMEPAGE = "https://github.com/frossm/rpncalc";
   protected static final String USERGUIDE = "https://frossm.github.io/RPNCalc-UserGuide";

   /**
    * Display(): Show help information
    */
   public static void Display() {
      int helpWidth = 80;

      // Display help header
      Output.printColor(Output.CYAN, "\n+" + "-".repeat(helpWidth) + "+\n+");
      Output.printColor(Output.WHITE, Format.CenterText(helpWidth, ("RPN Calculator  v" + Main.VERSION)));
      Output.printColor(Output.CYAN, "+\n+");
      Output.printColor(Output.WHITE, Format.CenterText(helpWidth, Main.COPYRIGHT));
      Output.printColorln(Output.CYAN, "+\n+" + "-".repeat(helpWidth) + "+");
      Output.printColorln(Output.CYAN, Format.CenterText(helpWidth, "RPNCalc is the command line Reverse Polish Notation calculator"));
      Output.printColorln(Output.CYAN, Format.CenterText(helpWidth, HOMEPAGE));
      Output.printColorln(Output.CYAN, Format.CenterText(helpWidth, USERGUIDE));

      Output.printColorln(Output.YELLOW, "\nCommand Line Options:");
      Output.printColorln(Output.WHITE, " -D       Start in debug mode. Same as using the 'debug' command");
      Output.printColorln(Output.WHITE, " -l       Load a saved named stack or create the stack if it doesn't exist");
      Output.printColorln(Output.WHITE, " -h | ?   Show this help information");
      Output.printColorln(Output.WHITE, " -v       Display version information as well as latest GitHub release");
      Output.printColorln(Output.WHITE, " -L       Display the program usage license. Same as the 'license' command");
      Output.printColorln(Output.WHITE, " -z       Disable colorized output");

      Output.printColorln(Output.YELLOW, "\nOperands:");
      Output.printColorln(Output.WHITE, " +    Addition:  Add last two stack elements");
      Output.printColorln(Output.WHITE, " -    Subtraction:  Subtract row 1 from row 2");
      Output.printColorln(Output.WHITE, " *    Multiplication:  Multiply last two stack items");
      Output.printColorln(Output.WHITE, " /    Division:  Divide line2 by line1");
      Output.printColorln(Output.WHITE, " ^    Power:  Calculate line2 to the power of line1");

      Output.printColorln(Output.YELLOW, "\nCalculator Commands:");
      Output.printColorln(Output.WHITE, " aa [keep]      Add all stack items. Adding 'keep' will retain existing items");
      Output.printColorln(Output.WHITE, " abs            Take the absolute value of line1");
      Output.printColorln(Output.WHITE, " avg [keep]     Replace stack items with average of values. 'keep' will retain stack");
      Output.printColorln(Output.WHITE, " c              Clear the screen and empty current stack");
      Output.printColorln(Output.WHITE, " cl[ean]        Clear screen and keep the stack values");
      Output.printColorln(Output.WHITE, " copy [#]       Copy line1 or the provided line number and add it to the stack");
      Output.printColorln(Output.WHITE, " d [#] [#-#]    Delete line1, the line number provided, or a range of lines provided");
      Output.printColorln(Output.WHITE, " dice XdY       Roll a Y sided die X times. Default is 1d6");
      Output.printColorln(Output.WHITE, " down           Shift the stack down moving the Line1 to the bottom of the stack");
      Output.printColorln(Output.WHITE, " echo message   Display the provided message prior to the next stack display");
      Output.printColorln(Output.WHITE, " fact           Take a factorial of line1. Decimals will be dropped");
      Output.printColorln(Output.WHITE, " f              Flip the sign of the element at line1");
      Output.printColorln(Output.WHITE, " int            Convert line1 to an integer. No rounding is performed");
      Output.printColorln(Output.WHITE, " lr [add] [x]   Linear Regression. 'add' puts next value on stk. 'x' shows y value at that x");
      Output.printColorln(Output.WHITE, " log | log10    Calculate the natural (base e) or base10 logarithm");
      Output.printColorln(Output.WHITE, " min | max      Add the minimum or maximum stack value to the stack");
      Output.printColorln(Output.WHITE, " median [keep]  Replace stack with median value. 'keep' will retain stack");
      Output.printColorln(Output.WHITE, " mod            Modulus. Perform a division and return the remainder");
      Output.printColorln(Output.WHITE, " sort a|d       Sorts the stack in an ascending or descending manner");
      Output.printColorln(Output.WHITE, " sqrt           Perform a square root of the line1 value");
      Output.printColorln(Output.WHITE, " rand [L] [H]   Random integer between L and H inclusive.  Default is 1-100");
      Output.printColorln(Output.WHITE, " repeat [Num]   Repeat the last command Num times.  Default is 1");
      Output.printColorln(Output.WHITE, " round [n]      Round to n decimal places. Default is 0 decimals");
      Output.printColorln(Output.WHITE, " s [#] [#]      Swap the last two elements in the stack or the lines provided");
      Output.printColorln(Output.WHITE, " sd [keep]      Standard deviation of stack items. 'keep' will retain stack");
      Output.printColorln(Output.WHITE, " u [STACK #]    Undo last action or back to the undo stack defined in 'list undo'");
      Output.printColorln(Output.WHITE, " up             Shift the stack up. Line1 becomes Line2, last stack items becomes Line1");

      Output.printColorln(Output.YELLOW, "\nConversions:");
      Output.printColorln(Output.WHITE, " frac [base]      Display as a fraction with min provided base. Default base is 64th");
      Output.printColorln(Output.WHITE, " convert FROM TO  Convert the amount provided from one unit to another");
      Output.printColorln(Output.WHITE, " convertunits     Display the supported units available to convert");

      Output.printColorln(Output.YELLOW, "\nTrigonometry Functions:");
      Output.printColorln(Output.WHITE, " sin|cos|tan [rad]    Trig Functions: Angle is in degrees unless rad is provided");
      Output.printColorln(Output.WHITE, " asin|acos|atan [rad] Trig Functions: Result is in degrees unless rad is provided");
      Output.printColorln(Output.WHITE, " hypot                Returns the hypotenuse length using line1 and line2 as the legs");

      Output.printColorln(Output.YELLOW, "\nMemory Commands:");
      Output.printColorln(Output.WHITE, " mem [X] add       Add line1 to memory slot X. Default slot is 0");
      Output.printColorln(Output.WHITE, " mem [X] clr       Clear memory from slot X. Default slot0");
      Output.printColorln(Output.WHITE, " mem [X] copy      Copy number from memory slot X. Default slot is 0");
      Output.printColorln(Output.WHITE, " mem addall        Add all stack items to memory slots");
      Output.printColorln(Output.WHITE, " mem clearall      Clear all memory slots");
      Output.printColorln(Output.WHITE, " mem copyall       Copy all memory items onto the stack");

      Output.printColorln(Output.YELLOW, "\nConstants:");
      Output.printColorln(Output.WHITE, " eulersnum         Add Euler's exponential growth number (e)");
      Output.printColorln(Output.WHITE, " eulersconst       Add Euler's mathematical constant (y)");
      Output.printColorln(Output.WHITE, " phi               Add the Golden Ratio (phi)");
      Output.printColorln(Output.WHITE, " pi                Add Archimede's constant (PI)");
      Output.printColorln(Output.WHITE, " sol               Add the Speed of Light (c)");

      Output.printColorln(Output.YELLOW, "\nUser Defined Functions:");
      Output.printColorln(Output.WHITE, " func del NAME     Delete named user defined function");
      Output.printColorln(Output.WHITE, " func delall       Delete all user defined functions");
      Output.printColorln(Output.WHITE, " func export       Export the currently defined functions to a file");
      Output.printColorln(Output.WHITE, " func import       Import functions from a file and append them to current ones");
      Output.printColorln(Output.WHITE, " record on         Turn on command recording");
      Output.printColorln(Output.WHITE, " record off        Disable recording");

      Output.printColorln(Output.YELLOW, "\nConfiguration Options:");
      Output.printColorln(Output.WHITE, " reset             Resets configurations set with 'set' back to defaults");
      Output.printColorln(Output.WHITE, " set               Display current values of configurations you can set");
      Output.printColorln(Output.WHITE, " set align l|d|r   Set alignment of the stack display to 'l'eft, 'd'ecimal, or 'r'ight");
      Output.printColorln(Output.WHITE, " set browser FILE  Set the full path (using slashes) to the web browser");
      Output.printColorln(Output.WHITE, " set memslots NUM  Set the number of memory slots");
      Output.printColorln(Output.WHITE, " set width NUM     Set the width of the display to num");

      Output.printColorln(Output.YELLOW, "\nOperational Commands:");
      Output.printColorln(Output.WHITE, " debug             Toggle DEBUG mode on/off");
      Output.printColorln(Output.WHITE, " h|?               Show this help information.  Either key will work");
      Output.printColorln(Output.WHITE, " export FILE       Export the current stack to the provided file (one number per line)");
      Output.printColorln(Output.WHITE, " hp | homepage     Open up the configured browser to the RPNCalc Home Page");
      Output.printColorln(Output.WHITE, " import FILE       Replace current stack with file contents (one number per line)");
      DisplayListCommands(Output.WHITE);
      Output.printColorln(Output.WHITE, " load NAME         Load (or create if needed) a named stack");
      Output.printColorln(Output.WHITE, " rev               Reverse the order of the items in the stack");
      Output.printColorln(Output.WHITE, " ss                Swap primary and secondary stack");
      Output.printColorln(Output.WHITE, " ug | userguide    Open up the configured browser to the RPNCalc User Guide");
      Output.printColorln(Output.WHITE, " license           Display the software usage license");
      Output.printColorln(Output.WHITE, " ver               Display current RPNCalc version & latest version on GitHub");
      Output.printColorln(Output.WHITE, " x | exit          Exit Calculator");
      Output.printColorln(Output.WHITE, " cx| clearexit     Clear the stack and then exit");

   }

   /**
    * DisplayListCommands(): Show valid list commands elsewhere as needed
    */
   public static void DisplayListCommands(int clr) {
      Output.printColorln(clr, " list func         Display the detailed user defined functions");
      Output.printColorln(clr, " list mem          Display contents of the memory slots");
      Output.printColorln(clr, " list stacks       Show the list of saved stacks");
      Output.printColorln(clr, " list undo         Show the current undo stack");
      Output.printColorln(clr, " list commands     Show the command history");
   }

   /**
    * Display Version(): Show the program version, copyright, and latest GitHub release
    */
   public static void DisplayVersion() {
      Output.printColorln(Output.WHITE, "RPNCalc Version: v" + Main.VERSION);
      Output.printColorln(Output.CYAN, Main.COPYRIGHT);
      Output.printColorln(Output.WHITE, "\nLatest Release on GitHub: " + GitHub.updateCheck("rpncalc"));
      Output.printColorln(Output.CYAN, "HomePage: https://github.com/frossm/rpncalc");
   }

   /**
    * DisplayLicense(): Simply display the software usage license and return
    */
   public static void DisplayLicense() {
      String licenseText = "MIT License\n\n"

            + "Copyright (c) 2013-" + org.fross.library.Date.getCurrentYear() + " Michael Fross\n\n"

            + "Permission is hereby granted, free of charge, to any person obtaining a copy\n" + "of this software and associated documentation files (the \"Software\"), to deal\n" + "in the Software without restriction, including without limitation the rights\n" + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" + "copies of the Software, and to permit persons to whom the Software is\n" + "furnished to do so, subject to the following conditions:\n\n"

            + "The above copyright notice and this permission notice shall be included in all\n" + "copies or substantial portions of the Software.\n\n"

            + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" + "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" + "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" + "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" + "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" + "SOFTWARE.\n";

      Output.printColorln(Output.CYAN, licenseText);
   }
}
