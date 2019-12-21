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

/* Leverages the JCDP Color library:  
 *   https://github.com/dialex/JCDP
 *   http://dialex.github.io/JCDP/javadoc/
 *   <!-- https://mvnrepository.com/artifact/com.diogonunes/JCDP -->
*/

package org.fross.rpn;

import static org.fusesource.jansi.Ansi.*;
import org.fusesource.jansi.Ansi;

public class Output {
	/**
	 * printcolorln(): Print to the console with the provided foreground color
	 * 
	 * Allowable colors are:
	 * - Ansi.Color.BLACK
	 * - Ansi.Color.RED
	 * - Ansi.Color.GREEN
	 * - Ansi.Color.YELLOW
	 * - Ansi.Color.BLUE
	 * - Ansi.Color.MAGENTA
	 * - Ansi.Color.CYAN
	 * - Ansi.Color.WHITE
	 * - Ansi.Color.DEFAULT
	 * 
	 * @param Color
	 * @param msg
	 */
	public static void printColorln(Ansi.Color clr, String msg) {
		System.out.println(ansi().a(Attribute.INTENSITY_BOLD).fg(clr).a(msg).reset());
	}

	/**
	 * printcolor(): Print to the console without a newline
	 * 
	 * @param Color
	 * @param msg
	 */
	public static void printColor(Ansi.Color clr, String msg) {
		System.out.print(ansi().a(Attribute.INTENSITY_BOLD).fg(clr).a(msg).reset());
	}

	/**
	 * println: Basic System.out.println call. It's here so out text output can go
	 * through this function.
	 * 
	 * @param msg
	 */
	public static void println(String msg) {
		System.out.println(msg);
	}

	/**
	 * print: Basic System.out.print call. It's here so out text output can go
	 * through this function.
	 * 
	 * @param msg
	 */
	public static void print(String msg) {
		System.out.print(msg);
	}

	/**
	 * printError: Display an error message in RED preceded by "ERROR:"
	 * 
	 * @param msg
	 */
	public static void printError(String msg) {
		printColorln(Ansi.Color.RED, "ERROR:  " + msg);
	}

	/**
	 * DisplayDashedNameLine(): Display the last line of the header and the
	 * separator line. This is a separate function given it also inserts the loaded
	 * stack and spaced everything correctly.
	 */
	public static void displayDashedNameLine() {
		int DesiredDashes = 70;

		// Display the Loaded Stack into Dash line. 70 dashes w/o the name
		Output.printColor(Ansi.Color.CYAN, "+");
		int numDashes = DesiredDashes - Prefs.QueryLoadedStack().length() - 4;
		for (int i = 0; i < numDashes; i++) {
			Output.printColor(Ansi.Color.CYAN, "-");
		}
		Output.printColor(Ansi.Color.YELLOW, "[" + Prefs.QueryLoadedStack() + ":" + Prefs.QueryCurrentStackNum() + "]");
		Output.printColorln(Ansi.Color.CYAN, "+");
	}

	/**
	 * fatalerror(): Print the provided string in RED and exit the program with the
	 * error code given
	 * 
	 * @param msg
	 * @param errorcode
	 */
	public static void fatalerror(String msg, int errorcode) {
		Output.printColorln(Ansi.Color.RED, "\nFATAL ERROR: " + msg);
		System.exit(errorcode);
	}
}