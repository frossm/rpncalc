/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE.txt file for additional details
 *           
 ******************************************************************************/
package org.fross.rpn;

import org.fusesource.jansi.Ansi;
import org.fross.library.Output;

/**
 * Help(): Display the help page when users enters 'h' command.
 * 
 * @author michael.d.fross
 *
 */
public class Help {
	/**
	 * Display(): Prints help in color using the JCDP library in the output module.
	 */
	public static void Display() {
		Output.printColorln(Ansi.Color.YELLOW,
				"\n+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.WHITE, "+              RPN Calculator v" + Main.VERSION + "  Help Document               +");
		Output.printColorln(Ansi.Color.YELLOW, "+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.CYAN, "            RPN is a simple reverse polish notation calculator");
		Output.printColorln(Ansi.Color.CYAN, "                     https://github.com/frossm/rpn");

		Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
		Output.printColorln(Ansi.Color.WHITE, " -l     Load saved named stack. Create the stack if it does not exist");
		Output.printColorln(Ansi.Color.WHITE, " -D     Start in debug mode.  Same as using the 'debug' command");
		Output.printColorln(Ansi.Color.WHITE, " -a     Alignment.  Can be either (l)eft, (r)ight, or (d)ecmimal. Default is left.");

		Output.printColorln(Ansi.Color.YELLOW, "\nOperands:");
		Output.printColorln(Ansi.Color.WHITE, " +      Addition:  Add last two stack elements");
		Output.printColorln(Ansi.Color.WHITE, " -      Subtraction: Subtract last element from previous element");
		Output.printColorln(Ansi.Color.WHITE, " *      Multiplication: Muliply last two stack items");
		Output.printColorln(Ansi.Color.WHITE, " /      Division: Divide second to last item by last stack item");
		Output.printColorln(Ansi.Color.WHITE, " ^      Power:  Calculate second to last item to the power of the last item");
		Output.printColorln(Ansi.Color.WHITE, " %      Percent: Turn the last stack item into a percentage (multiplied by 0.01)");

		Output.printColorln(Ansi.Color.YELLOW, "\nCommands:");
		Output.printColorln(Ansi.Color.WHITE, " listundo  Show the current undo stack");
		Output.printColorln(Ansi.Color.WHITE, " u         Undo.  Replaces current stack with one from before previous operation");
		Output.printColorln(Ansi.Color.WHITE, " s [#] [#] Swap the last two elments in the stack or the elements provided");
		Output.printColorln(Ansi.Color.WHITE, " c         Clear everything from the visible stack");
		Output.printColorln(Ansi.Color.WHITE, " d #]      Delete the last item in the stack or, optionally, the line number provided");
		Output.printColorln(Ansi.Color.WHITE, " f         Flip the sign of the last stack element");
		Output.printColorln(Ansi.Color.WHITE, " copy      Copy the item at the top of the stack");
		Output.printColorln(Ansi.Color.WHITE, " pi        Insert the value of PI onto the end of the stack");
		Output.printColorln(Ansi.Color.WHITE, " sqrt      Perform a square root on the last stack number\n");
		Output.printColorln(Ansi.Color.WHITE, " ss        Swap primary stack to secondary.  You can swap them back at a later time");
		Output.printColorln(Ansi.Color.WHITE, " load      Load a saved named stack. It will be created if it doesn't exist");
		Output.printColorln(Ansi.Color.WHITE, " a [lrd]   Set display alignment to be (l)eft, (r)ight, or (d)ecmial");
		Output.printColorln(Ansi.Color.WHITE, " debug     Toggle DEBUG mode on/off");
		Output.printColorln(Ansi.Color.WHITE, " ver       Display the current version");
		Output.printColorln(Ansi.Color.WHITE, " h|?       Show this help information.  Either key will work.");
		Output.printColorln(Ansi.Color.WHITE, " x         Exit Calculator\n");

		Output.printColorln(Ansi.Color.WHITE, "Note: You can place an operand at the end of a number and execute in one step.");
		Output.printColorln(Ansi.Color.WHITE, "For Example:  To add two numbers:   2 <enter> 3+ <enter>   will produce 5.\n");
	}
}
