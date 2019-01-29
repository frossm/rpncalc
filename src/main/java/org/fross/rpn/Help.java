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

public class Help {
	public static void Display() {
		Output.Yellow("\n+----------------------------------------------------------------------+");
		Output.Yellow("-                   RPN Calculator Help Document   -");
		Output.Yellow("+----------------------------------------------------------------------+");
		Output.Yellow("RPN is a simply reverse polish notation calculator\n");
		
		Output.Yellow("Operands:");
		Output.White(" +     Addition:  Add last two stack elements");
		Output.White(" -     Subtraction: Subtract last element from previous element");
		Output.White(" *     Multiplication: Muliply last two stack items");
		Output.White(" /     Division: Divide second to last item by last stack item");
		Output.White(" ^     Power:  Calculate send to last item to the power of the last item");
		Output.White(" %     Turn last stack item into a % (multiplied by 0.01)");
		
		Output.Yellow("\nCommands:");
		Output.White(" s     Change sign of last element");
		Output.White(" c     Clear everything from the visible stack");
		Output.White(" d     Delete the last item in the stack");
		Output.White(" f     Flip last two stack items");
		Output.White(" ss    Swap primary stack to secondary.  You can swap them back at a later time");
		Output.White("debug  Toggle DEBUG mode on/off");
		Output.White(" h|?   Show this help information.  Either key will work.");
		Output.White(" x|q   Exit Calculator\n");
		
		Output.White("Note, you can couple an operand at the end of a number to save time.");
		Output.White("For Example.   Add two numbers:  2 <enter> 3+ <enter> to get 5\n");
	}
}