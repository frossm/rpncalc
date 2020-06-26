/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE file for additional details
 *           
 ******************************************************************************/
package org.fross.rpncalc;

import org.fusesource.jansi.Ansi;
import org.fross.library.Output;

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
		Output.printColorln(Ansi.Color.YELLOW, "\n+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.WHITE, "+              RPN Calculator v" + Main.VERSION + "  Help Document               +");
		Output.printColorln(Ansi.Color.WHITE, "+      " + Main.COPYRIGHT + "      +");
		Output.printColorln(Ansi.Color.YELLOW, "+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.CYAN, "          RPNCalc is a simple reverse polish notation calculator");
		Output.printColorln(Ansi.Color.CYAN, "                   https://github.com/frossm/rpncalc");

		Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
		Output.printColorln(Ansi.Color.WHITE, " -l       Load saved named stack. Create the stack if it does not exist");
		Output.printColorln(Ansi.Color.WHITE, " -D       Start in debug mode.  Same as using the 'debug' command");
		Output.printColorln(Ansi.Color.WHITE, " -a [lrd] Alignment of number stack. (l)eft, (r)ight, or (d)ecmimal. Default is left");
		Output.printColorln(Ansi.Color.WHITE, " -v       Display version information and exit program");
		
		Output.printColorln(Ansi.Color.YELLOW, "\nOperands:");
		Output.printColorln(Ansi.Color.WHITE, " +      Addition:  Add last two stack elements");
		Output.printColorln(Ansi.Color.WHITE, " -      Subtraction: Subtract last element from previous element");
		Output.printColorln(Ansi.Color.WHITE, " *      Multiplication: Muliply last two stack items");
		Output.printColorln(Ansi.Color.WHITE, " /      Division: Divide second to last item by last stack item");
		Output.printColorln(Ansi.Color.WHITE, " ^      Power:  Calculate second to last item to the power of the last item");
		Output.printColorln(Ansi.Color.WHITE, " %      Percent: Convert last stack item into a percentage (multiply by 0.01)");

		Output.printColorln(Ansi.Color.YELLOW, "\nCommands:");
		Output.printColorln(Ansi.Color.WHITE, " u            Undo last action");
		Output.printColorln(Ansi.Color.WHITE, " f            Flip the sign of the last stack element");
		Output.printColorln(Ansi.Color.WHITE, " c            Clear everything from the visible stack");
		Output.printColorln(Ansi.Color.WHITE, " d [#]        Delete the last item in the stack or the line number provided");
		Output.printColorln(Ansi.Color.WHITE, " s [#] [#]    Swap the last two elments in the stack or the elements provided");
		Output.printColorln(Ansi.Color.WHITE, " copy         Copy the item at the top of the stack");
		Output.printColorln(Ansi.Color.WHITE, " pi           Insert the value of PI onto the end of the stack");
		Output.printColorln(Ansi.Color.WHITE, " sqrt         Perform a square root on the last stack number");
		Output.printColorln(Ansi.Color.WHITE, " ss           Swap primary and secondary stack");
		Output.printColorln(Ansi.Color.WHITE, " sin|cos|tan  Calculate the trigonometry function");
		Output.printColorln(Ansi.Color.WHITE, " rand [L] [H] Random number between X and Y inclusive.  Default is 1-100");
		Output.printColorln(Ansi.Color.WHITE, " listundo     Show the current undo stack");
		Output.printColorln(Ansi.Color.WHITE, " frac [base]  Display as a fraction with min provide base.  Default is 64th");
		Output.printColorln(Ansi.Color.WHITE, " dice XdY     Roll a Y sided die X times.  Default is 1d6");

		Output.printColorln(Ansi.Color.WHITE, "\n load         Load or create a named stack");
		Output.printColorln(Ansi.Color.WHITE, " a [lrd]      Set display alignment to be (l)eft, (r)ight, or (d)ecmial");
		Output.printColorln(Ansi.Color.WHITE, " debug        Toggle DEBUG mode on/off");
		Output.printColorln(Ansi.Color.WHITE, " ver          Display the current version");
		Output.printColorln(Ansi.Color.WHITE, " h|?          Show this help information.  Either key will work.");
		Output.printColorln(Ansi.Color.WHITE, " x|exit       Exit Calculator.  'cx' will clear before exiting\n");

		Output.printColorln(Ansi.Color.WHITE, "Note: You can place an operand at the end of a number and execute in one step.");
		Output.printColorln(Ansi.Color.WHITE, "For Example:  To add two numbers:   2 <enter> 3+ <enter>   will produce 5.\n");
	}
}
