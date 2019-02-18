/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: GNU General Public License v3.
 *           http://www.gnu.org/licenses/gpl-3.0.html
 *           
 ******************************************************************************/
package org.fross.rpn;

import com.diogonunes.jcdp.color.api.Ansi.FColor;

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
		Output.PrintColor(FColor.YELLOW, "\n+----------------------------------------------------------------------+");
		Output.PrintColor(FColor.YELLOW, "-                   RPN Calculator Help Document                       -");
		Output.PrintColor(FColor.YELLOW, "+----------------------------------------------------------------------+");
		Output.PrintColor(FColor.WHITE, "            RPN is a simple reverse polish notation calculator");
		Output.PrintColor(FColor.WHITE, "               https://bitbucket.org/frossm/rpn/src/default\n");

		Output.PrintColor(FColor.YELLOW, "Command Line Options:");
		Output.PrintColor(FColor.WHITE, " -l     Load saved named stack. Create the stack if it does not exist");
		Output.PrintColor(FColor.WHITE, " -D     Start in debug mode.  Same as using the 'debug' command\n");

		Output.PrintColor(FColor.YELLOW, "Operands:");
		Output.PrintColor(FColor.WHITE, " +      Addition:  Add last two stack elements");
		Output.PrintColor(FColor.WHITE, " -      Subtraction: Subtract last element from previous element");
		Output.PrintColor(FColor.WHITE, " *      Multiplication: Muliply last two stack items");
		Output.PrintColor(FColor.WHITE, " /      Division: Divide second to last item by last stack item");
		Output.PrintColor(FColor.WHITE, " ^      Power:  Calculate second to last item to the power of the last item");
		Output.PrintColor(FColor.WHITE,
				" %      Percent: Turn the last stack item into a percentage (multiplied by 0.01)");
		Output.PrintColor(FColor.WHITE, " q      Perform a square root on the last stack number\n");

		Output.PrintColor(FColor.YELLOW, "Commands:");
		Output.PrintColor(FColor.WHITE, " s      Change sign of last element");
		Output.PrintColor(FColor.WHITE, " c      Clear everything from the visible stack");
		Output.PrintColor(FColor.WHITE, " d      Delete the last item in the stack");
		Output.PrintColor(FColor.WHITE, " f      Flip last two stack items");
		Output.PrintColor(FColor.WHITE,
				" ss     Swap primary stack to secondary.  You can swap them back at a later time");
		Output.PrintColor(FColor.WHITE, " load   Load a saved named stack. It will be created if it doesn't exist");
		Output.PrintColor(FColor.WHITE, " debug  Toggle DEBUG mode on/off");
		Output.PrintColor(FColor.WHITE, " h|?    Show this help information.  Either key will work.");
		Output.PrintColor(FColor.WHITE, " x      Exit Calculator\n");

		Output.PrintColor(FColor.WHITE, "Note: You can place an operand at the end of a number and execute in one step.");
		Output.PrintColor(FColor.WHITE, "For Example:  To add two numbers:   2 <enter> 3+ <enter>   will produce 5.\n");
	}
}