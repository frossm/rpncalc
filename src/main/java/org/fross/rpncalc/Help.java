/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2025 Michael Fross
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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
import org.fusesource.jansi.Ansi;

/**
 * Help(): Display the help page when users enters 'h' or '?' command.
 *
 * @author Michael Fross (michael@fross.org)
 */
public class Help {
   protected static final String HOMEPAGE="https://github.com/frossm/rpncalc";
   protected static final String USERGUIDE="https://frossm.github.io/RPNCalc-UserGuide";

   /**
    * Display(): Show help information
    */
   public static void Display() {
      int helpWidth = 80;

      // Display help header
      Output.printColor(Ansi.Color.CYAN, "\n+" + "-".repeat(helpWidth) + "+\n+");
      Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, ("RPN Calculator  v" + Main.VERSION)));
      Output.printColor(Ansi.Color.CYAN, "+\n+");
      Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, Main.COPYRIGHT));
      Output.printColorln(Ansi.Color.CYAN, "+\n+" + "-".repeat(helpWidth) + "+");
      Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "RPNCalc is the command line Reverse Polish Notation calculator"));
      Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, HOMEPAGE));
      Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, USERGUIDE));

      Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
      Output.printColorln(Ansi.Color.WHITE, " -D       Start in debug mode. Same as using the 'debug' command");
      Output.printColorln(Ansi.Color.WHITE, " -l       Load a saved named stack or create the stack if it doesn't exist");
      Output.printColorln(Ansi.Color.WHITE, " -h | ?   Show this help information");
      Output.printColorln(Ansi.Color.WHITE, " -v       Display version information as well as latest GitHub release");
      Output.printColorln(Ansi.Color.WHITE, " -L       Display the program usage license. Same as the 'license' command");
      Output.printColorln(Ansi.Color.WHITE, " -z       Disable colorized output");

      Output.printColorln(Ansi.Color.YELLOW, "\nOperands:");
      Output.printColorln(Ansi.Color.WHITE, " +    Addition:  Add last two stack elements");
      Output.printColorln(Ansi.Color.WHITE, " -    Subtraction:  Subtract row 1 from row 2");
      Output.printColorln(Ansi.Color.WHITE, " *    Multiplication:  Multiply last two stack items");
      Output.printColorln(Ansi.Color.WHITE, " /    Division:  Divide line2 by line1");
      Output.printColorln(Ansi.Color.WHITE, " ^    Power:  Calculate line2 to the power of line1");

      Output.printColorln(Ansi.Color.YELLOW, "\nCalculator Commands:");
      Output.printColorln(Ansi.Color.WHITE, " aa [keep]      Add all stack items. Adding 'keep' will retain existing items");
      Output.printColorln(Ansi.Color.WHITE, " abs            Take the absolute value of line1");
      Output.printColorln(Ansi.Color.WHITE, " avg [keep]     Replace stack items with average of values. 'keep' will retain stack");
      Output.printColorln(Ansi.Color.WHITE, " c              Clear the screen and empty current stack");
      Output.printColorln(Ansi.Color.WHITE, " cl[ean]        Clear screen and keep the stack values");
      Output.printColorln(Ansi.Color.WHITE, " copy [#]       Copy line1 or the provided line number and add it to the stack");
      Output.printColorln(Ansi.Color.WHITE, " d [#] [#-#]    Delete line1, the line number provided, or a range of lines provided");
      Output.printColorln(Ansi.Color.WHITE, " dice XdY       Roll a Y sided die X times. Default is 1d6");
      Output.printColorln(Ansi.Color.WHITE, " down           Shift the stack down moving the Line1 to the bottom of the stack");
      Output.printColorln(Ansi.Color.WHITE, " echo message   Display the provided message prior to the next stack display");
      Output.printColorln(Ansi.Color.WHITE, " fact           Take a factorial of line1. Decimals will be dropped");
      Output.printColorln(Ansi.Color.WHITE, " f              Flip the sign of the element at line1");
      Output.printColorln(Ansi.Color.WHITE, " int            Convert line1 to an integer. No rounding is performed");
      Output.printColorln(Ansi.Color.WHITE, " lr [add] [x]   Linear Regression. 'add' puts next value on stk. 'x' shows y value at that x");
      Output.printColorln(Ansi.Color.WHITE, " log | log10    Calculate the natural (base e) or base10 logarithm");
      Output.printColorln(Ansi.Color.WHITE, " min | max      Add the minimum or maximum stack value to the stack");
      Output.printColorln(Ansi.Color.WHITE, " median [keep]  Replace stack with median value. 'keep' will retain stack");
      Output.printColorln(Ansi.Color.WHITE, " mod            Modulus. Perform a division and return the remainder");
      Output.printColorln(Ansi.Color.WHITE, " sort a|d       Sorts the stack in an ascending or descending manner");
      Output.printColorln(Ansi.Color.WHITE, " sqrt           Perform a square root of the line1 value");
      Output.printColorln(Ansi.Color.WHITE, " rand [L] [H]   Random integer between L and H inclusive.  Default is 1-100");
      Output.printColorln(Ansi.Color.WHITE, " repeat [Num]   Repeat the last command Num times.  Default is 1");
      Output.printColorln(Ansi.Color.WHITE, " round [n]      Round to n decimal places. Default is 0 decimals");
      Output.printColorln(Ansi.Color.WHITE, " s [#] [#]      Swap the last two elements in the stack or the lines provided");
      Output.printColorln(Ansi.Color.WHITE, " sd [keep]      Standard deviation of stack items. 'keep' will retain stack");
      Output.printColorln(Ansi.Color.WHITE, " u [STACK #]    Undo last action or back to the undo stack defined in 'list undo'");
      Output.printColorln(Ansi.Color.WHITE, " up             Shift the stack up. Line1 becomes Line2, last stack items becomes Line1");

      Output.printColorln(Ansi.Color.YELLOW, "\nConversions:");
      Output.printColorln(Ansi.Color.WHITE, " to%            Convert line1 from a number to a percent (0.715 -> 71.5%)");
      Output.printColorln(Ansi.Color.WHITE, " from%          Convert line1 from a percent to a number (23.5% -> 0.235)");
      Output.printColorln(Ansi.Color.WHITE, " frac [base]    Display as a fraction with min provided base. Default base is 64th");
      Output.printColorln(Ansi.Color.WHITE, " deg2rad        Convert line1 from degrees to radians");
      Output.printColorln(Ansi.Color.WHITE, " rad2deg        Convert line1 from radians to degrees");
      Output.printColorln(Ansi.Color.WHITE, " in2mm          Convert line1 from inches into millimeters");
      Output.printColorln(Ansi.Color.WHITE, " mm2in          Convert line1 from millimeters to inches");
      Output.printColorln(Ansi.Color.WHITE, " in2ft          Convert line1 from inches into feet");
      Output.printColorln(Ansi.Color.WHITE, " ft2in          Convert line1 from feet to inches");
      Output.printColorln(Ansi.Color.WHITE, " gram2oz        Convert line1 from grams to US ounces");
      Output.printColorln(Ansi.Color.WHITE, " oz2gram        Convert line1 from US ounces to grams");
      Output.printColorln(Ansi.Color.WHITE, " kg2lb          Convert line1 from kilograms to US pounds");
      Output.printColorln(Ansi.Color.WHITE, " lb2kg          Convert line1 from US pounds to kilograms");

      Output.printColorln(Ansi.Color.YELLOW, "\nTrigonometry Functions:");
      Output.printColorln(Ansi.Color.WHITE, " sin|cos|tan [rad]    Trig Functions: Angle is in degrees unless rad is provided");
      Output.printColorln(Ansi.Color.WHITE, " asin|acos|atan [rad] Trig Functions: Result is in degrees unless rad is provided");
      Output.printColorln(Ansi.Color.WHITE, " hypot                Returns the hypotenuse length using line1 and line2 as the legs");

      Output.printColorln(Ansi.Color.YELLOW, "\nMemory Commands:");
      Output.printColorln(Ansi.Color.WHITE, " mem [X] add       Add line1 to memory slot X. Default slot is 0");
      Output.printColorln(Ansi.Color.WHITE, " mem [X] clr       Clear memory from slot X. Default slot0");
      Output.printColorln(Ansi.Color.WHITE, " mem [X] copy      Copy number from memory slot X. Default slot is 0");
      Output.printColorln(Ansi.Color.WHITE, " mem addall        Add all stack items to memory slots");
      Output.printColorln(Ansi.Color.WHITE, " mem clearall      Clear all memory slots");
      Output.printColorln(Ansi.Color.WHITE, " mem copyall       Copy all memory items onto the stack");

      Output.printColorln(Ansi.Color.YELLOW, "\nConstants:");
      Output.printColorln(Ansi.Color.WHITE, " eulersnum         Add Euler's exponential growth number (e)");
      Output.printColorln(Ansi.Color.WHITE, " eulersconst       Add Euler's mathematical constant (y)");
      Output.printColorln(Ansi.Color.WHITE, " phi               Add the Golden Ratio (phi)");
      Output.printColorln(Ansi.Color.WHITE, " pi                Add Archimede's constant (PI)");
      Output.printColorln(Ansi.Color.WHITE, " sol               Add the Speed of Light (c)");

      Output.printColorln(Ansi.Color.YELLOW, "\nUser Defined Functions:");
      Output.printColorln(Ansi.Color.WHITE, " func del NAME     Delete named user defined function");
      Output.printColorln(Ansi.Color.WHITE, " func delall       Delete all user defined functions");
      Output.printColorln(Ansi.Color.WHITE, " func export       Export the currently defined functions to a file");
      Output.printColorln(Ansi.Color.WHITE, " func import       Import functions from a file and append them to current ones");
      Output.printColorln(Ansi.Color.WHITE, " record on         Turn on command recording");
      Output.printColorln(Ansi.Color.WHITE, " record off        Disable recording");

      Output.printColorln(Ansi.Color.YELLOW, "\nConfiguration Options:");
      Output.printColorln(Ansi.Color.WHITE, " reset             Resets configurations set with 'set' back to defaults");
      Output.printColorln(Ansi.Color.WHITE, " set               Display current values of configurations you can set");
      Output.printColorln(Ansi.Color.WHITE, " set align l|d|r   Set alignment of the stack display to 'l'eft, 'd'ecimal, or 'r'ight");
      Output.printColorln(Ansi.Color.WHITE, " set browser FILE  Set the full path (using slashes) to the web browser");
      Output.printColorln(Ansi.Color.WHITE, " set memslots NUM  Set the number of memory slots");
      Output.printColorln(Ansi.Color.WHITE, " set width NUM     Set the width of the display to num");

      Output.printColorln(Ansi.Color.YELLOW, "\nOperational Commands:");
      Output.printColorln(Ansi.Color.WHITE, " debug             Toggle DEBUG mode on/off");
      Output.printColorln(Ansi.Color.WHITE, " h|?               Show this help information.  Either key will work");
      Output.printColorln(Ansi.Color.WHITE, " export FILE       Export the current stack to the provided file (one number per line)");
      Output.printColorln(Ansi.Color.WHITE, " hp | homepage     Open up the configured browser to the RPNCalc Home Page");
      Output.printColorln(Ansi.Color.WHITE, " import FILE       Replace current stack with file contents (one number per line)");
      DisplayListCommands(Ansi.Color.WHITE);
      Output.printColorln(Ansi.Color.WHITE, " load NAME         Load (or create if needed) a named stack");
      Output.printColorln(Ansi.Color.WHITE, " rev               Reverse the order of the items in the stack");
      Output.printColorln(Ansi.Color.WHITE, " ss                Swap primary and secondary stack");
      Output.printColorln(Ansi.Color.WHITE, " ug | userguide    Open up the configured browser to the RPNCalc User Guide");
      Output.printColorln(Ansi.Color.WHITE, " license           Display the software usage license");
      Output.printColorln(Ansi.Color.WHITE, " ver               Display current RPNCalc version & latest version on GitHub");
      Output.printColorln(Ansi.Color.WHITE, " x | exit          Exit Calculator");
      Output.printColorln(Ansi.Color.WHITE, " cx| clearexit     Clear the stack and then exit");

   }

   /**
    * DisplayListCommands(): Show valid list commands elsewhere as needed
    */
   public static void DisplayListCommands(Ansi.Color clr) {
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
      Output.printColorln(Ansi.Color.WHITE, "RPNCalc Version: v" + Main.VERSION);
      Output.printColorln(Ansi.Color.CYAN, Main.COPYRIGHT);
      Output.printColorln(Ansi.Color.WHITE, "\nLatest Release on GitHub: " + GitHub.updateCheck("rpncalc"));
      Output.printColorln(Ansi.Color.CYAN, "HomePage: https://github.com/frossm/rpncalc");
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

      Output.printColorln(Ansi.Color.CYAN, licenseText);
   }
}
