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
/* Leverages the JCDP Color library:  
 *   https://github.com/dialex/JCDP
 *   http://dialex.github.io/JCDP/javadoc/
 *   <!-- https://mvnrepository.com/artifact/com.diogonunes/JCDP -->
*/

package org.fross.rpn;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

public class Output {
	/**
	 * PrintColor: Print to the console with the provided foreground color
	 * Acceptable ColorNames: FColor.BLUE, FColor.CYAN, FColor.GREEN,
	 * FColor.MAGENTA, FColor.NONE, FColor.RED, FColor.WHITE, FColor.YELLOW
	 * 
	 * @param Color
	 * @param msg
	 */
	public static void PrintColor(FColor clr, String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(clr).build();
		cp.setAttribute(Attribute.LIGHT);
		cp.println(msg);
		cp.clear();
	}

	/**
	 * PrintColorNNL: Print to the console with NoNewLine. The provided foreground
	 * color Acceptable ColorNames: FColor.BLUE, FColor.CYAN, FColor.GREEN,
	 * FColor.MAGENTA, FColor.NONE, FColor.RED, FColor.WHITE, FColor.YELLOW
	 * 
	 * @param Color
	 * @param msg
	 */
	public static void PrintColorNNL(FColor clr, String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(clr).build();
		cp.setAttribute(Attribute.LIGHT);
		cp.print(msg);
		cp.clear();
	}

	/**
	 * Print: Basic System.out.println call. It's here so out text output can go
	 * through this function.
	 * 
	 * @param msg
	 */
	public static void Print(String msg) {
		System.out.println(msg);
	}

	/**
	 * DisplayDashedNameLine(): Display the last line of the header and the separater
	 * line. This is a separate function given it also inserts the loaded stack and
	 * spaced everything correctly.
	 */
	public static void DisplayDashedNameLine() {
		int DesiredDashes = 70;

		// Display the Loaded Stack into Dash line. 70 dashes w/o the name
		Output.PrintColorNNL(FColor.CYAN, "+");
		int numDashes = DesiredDashes - Prefs.QueryLoadedStack().length() - 2;
		for (int i = 0; i < numDashes; i++) {
			Output.PrintColorNNL(FColor.CYAN, "-");
		}
		Output.PrintColor(FColor.CYAN, "[" + Prefs.QueryLoadedStack() + "]+");
	}
}