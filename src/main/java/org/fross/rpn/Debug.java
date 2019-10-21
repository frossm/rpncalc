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

import com.diogonunes.jcdp.color.api.Ansi.FColor;

/**
 * Debug contains static methods to maintain the debug state and display
 * messages when enabled.
 * 
 * @author michael.d.fross
 *
 */
public class Debug {
	// Class Variables
	private static boolean clDebug = false;

	/**
	 * Query(): Query current state of this object's debug setting
	 * 
	 * @return
	 */
	public static boolean Query() {
		return clDebug;
	}

	/**
	 * Enable(): Turn debugging on for this object
	 */
	public static void Enable() {
		clDebug = true;
	}

	/**
	 * Disable(): Disable debugging for this object
	 */
	public static void Disable() {
		clDebug = false;
	}

	/**
	 * Print(): Print the output of the String if debugging is enabled. It displays
	 * in RED using the output module.
	 * 
	 * @param msg
	 */
	public static void Print(String msg) {
		if (clDebug == true) {
			Output.printColorln(FColor.RED, "DEBUG:  " + msg);
		}
	}
}