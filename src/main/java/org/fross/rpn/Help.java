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
		Output.White("+----------------------------------------------------------------------+");
		Output.White("-   RPN Calculator Help Document   -");
		Output.White("+----------------------------------------------------------------------+");
		Output.White("RPN is a reverse polish notation calculator\n");
		Output.White("s    Sign Change             c   Clear Stack");
		Output.White("d    Delete Last in Stack    f   Flip last two");
		Output.White("ss   Save Stack              sr  Stack Restore");
		Output.White("x|q  Exit Calculator         h|?  This Help\n");
	}
}