/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2021 Michael Fross
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
 ******************************************************************************/
package org.fross.rpncalc;

import org.fross.library.Format;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

/**
 * Help(): Display the help page when users enters 'h' or '?' command.
 * 
 * @author michael.d.fross
 *
 */
public class Help {
	/**
	 * Display(): Show help information
	 */
	public static void Display() {
		int helpWidth = 80;

		Output.printColor(Ansi.Color.CYAN, "\n+" + "-".repeat(helpWidth) + "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, ("RPN Calculator  v" + Main.VERSION), "", ""));
		Output.printColor(Ansi.Color.CYAN, "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, Main.COPYRIGHT, "", ""));
		Output.printColorln(Ansi.Color.CYAN, "+\n+" + "-".repeat(helpWidth) + "+");
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "RPNCalc is a command line Reverse Polish Notation calculator", "", ""));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "https://github.com/frossm/rpncalc", "", ""));

		Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
		Output.printColorln(Ansi.Color.WHITE, " -l       Load saved named stack. Create the stack if it does not exist");
		Output.printColorln(Ansi.Color.WHITE, " -D       Start in debug mode.  Same as using the 'debug' command");
		Output.printColorln(Ansi.Color.WHITE, " -a [lrd] Alignment of numbers. (l)eft, (r)ight, or (d)ecmimal. Default: left");
		Output.printColorln(Ansi.Color.WHITE, " -m num   Set the number of memory slots.  Default value is 10");
		Output.printColorln(Ansi.Color.WHITE, " -w num   Set Width of header / status line.  Default is 70 characters");
		Output.printColorln(Ansi.Color.WHITE, " -v       Display version information as well as latest GitHub release");
		Output.printColorln(Ansi.Color.WHITE, " -z       Disable colorized output");

		Output.printColorln(Ansi.Color.YELLOW, "\nOperands:");
		Output.printColorln(Ansi.Color.WHITE, " +    Addition:  Add last two stack elements");
		Output.printColorln(Ansi.Color.WHITE, " -    Subtraction: Subtract row 1 from row 2");
		Output.printColorln(Ansi.Color.WHITE, " *    Multiplication: Muliply last two stack items");
		Output.printColorln(Ansi.Color.WHITE, " /    Division: Divide row 2 by row 1");
		Output.printColorln(Ansi.Color.WHITE, " ^    Power:  Calculate row 2 to the power of row 1");

		Output.printColorln(Ansi.Color.YELLOW, "\nCalculator Commands:");
		Output.printColorln(Ansi.Color.WHITE, " u            Undo last action");
		Output.printColorln(Ansi.Color.WHITE, " f            Flip the sign of the last element");
		Output.printColorln(Ansi.Color.WHITE, " c            Clear the screen and empty current stack");
		Output.printColorln(Ansi.Color.WHITE, " clean        Clear screen but save the stack values");
		Output.printColorln(Ansi.Color.WHITE, " d [#]        Delete the last item in the stack or the row number provided");
		Output.printColorln(Ansi.Color.WHITE, " s [#] [#]    Swap the last two elments in the stack or the rows provided");
		Output.printColorln(Ansi.Color.WHITE, " %            Convert last stack item into a percentage by multipling by 0.01");
		Output.printColorln(Ansi.Color.WHITE, " sqrt         Perform a square root");
		Output.printColorln(Ansi.Color.WHITE, " round [n]    Round to n decimal places.  Default is 0 decimals");
		Output.printColorln(Ansi.Color.WHITE, " aa [keep]    Add all stack items. Adding Keep will keep added elements");
		Output.printColorln(Ansi.Color.WHITE, " mod          Modulus. Perform a division and return the remainder");
		Output.printColorln(Ansi.Color.WHITE, " copy         Copy the last stack item");
		Output.printColorln(Ansi.Color.WHITE, " log|log10    Calculate the natural (base e) or base10 logarithm");
		Output.printColorln(Ansi.Color.WHITE, " rand [L] [H] Random number between X and Y inclusive.  Default is 1-100");
		Output.printColorln(Ansi.Color.WHITE, " dice XdY     Roll a Y sided die X times.  Default is 1d6");

		Output.printColorln(Ansi.Color.YELLOW, "\nConversions:");
		Output.printColorln(Ansi.Color.WHITE, " frac [base]  Display as a fraction with min provided base.  Default is 64th");
		Output.printColorln(Ansi.Color.WHITE, " mm           Assumes line1 is in inches and converts to millimeters");
		Output.printColorln(Ansi.Color.WHITE, " in           Assumes line1 is in millimeters and converts to inches");
		Output.printColorln(Ansi.Color.WHITE, " rad | deg    Converts line1 to radians or degrees");

		Output.printColorln(Ansi.Color.YELLOW, "\nTrigonometry Functions:");
		Output.printColorln(Ansi.Color.WHITE, " sin|cos|tan [rad]    Trig Functions: Angle in degrees unless rad is provided");
		Output.printColorln(Ansi.Color.WHITE, " asin|acos|atan [rad] Trig Functions: Result in degrees unless rad is provided");
		Output.printColorln(Ansi.Color.WHITE, " hypot        Returns the hypotenuse using line1 and line2 as the legs");

		Output.printColorln(Ansi.Color.YELLOW, "\nMemory Commands:");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] add   Add last stack item to memory Slot X. Default is 0");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] copy  Copy number for memory Slot X. Default is 0");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] clr   Clear memory Slot X. Default is 0");
		Output.printColorln(Ansi.Color.WHITE, " mem clearall  Clear all memory slots");

		Output.printColorln(Ansi.Color.YELLOW, "\nConstants:");
		Output.printColorln(Ansi.Color.WHITE, " pi        Add PI to the stack");
		Output.printColorln(Ansi.Color.WHITE, " phi       Add the Golden Ratio (phi) to the stack");
		Output.printColorln(Ansi.Color.WHITE, " euler     Add Euler's number (e) to the stack");

		Output.printColorln(Ansi.Color.YELLOW, "\nOperational Commands:");
		Output.printColorln(Ansi.Color.WHITE, " list stacks  Show the list of saved stacks");
		Output.printColorln(Ansi.Color.WHITE, " list mem     Display contents of the memory slots");
		Output.printColorln(Ansi.Color.WHITE, " list undo    Show the current undo stack");
		Output.printColorln(Ansi.Color.WHITE, " ss           Swap primary and secondary stack");
		Output.printColorln(Ansi.Color.WHITE, " load         Load (or create if needed) a named stack");
		Output.printColorln(Ansi.Color.WHITE, " a [lrd]      Set display alignment to be (l)eft, (r)ight, or (d)ecmial");
		Output.printColorln(Ansi.Color.WHITE, " debug        Toggle DEBUG mode on/off");
		Output.printColorln(Ansi.Color.WHITE, " ver          Display the current version");
		Output.printColorln(Ansi.Color.WHITE, " h|?          Show this help information.  Either key will work.");
		Output.printColorln(Ansi.Color.WHITE, " x|exit       Exit Calculator.  'cx' will clear before exiting");

		Output.printColorln(Ansi.Color.YELLOW, "\nNotes:");
		Output.printColorln(Ansi.Color.WHITE, "  - You can place an operand at the end of a number & execute in one step.");
		Output.printColorln(Ansi.Color.WHITE, "    Example adding two numbers:   2 <enter> 3+ <enter>   will produce 5.");
		Output.printColorln(Ansi.Color.CYAN, "  - See GitHub homepage (listed above) for additional usage instructions\n");
	}
}
