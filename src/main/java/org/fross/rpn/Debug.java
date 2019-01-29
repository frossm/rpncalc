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

public class Debug {
	// Class Variables
	private static boolean clDebug = false;

	// Query current state of this object's debug setting
	public static boolean Query() {
		return clDebug;
	}

	// Turn debugging on for this object
	public static void Enable() {
		clDebug = true;
	}

	// Disable debugging for this object
	public static void Disable() {
		clDebug = false;
	}

	// Print the output of the String if debugging is enabled
	public static void Print(String msg) {
		if (clDebug == true) {
			Output.Red("DEBUG:  " + msg);
		}
	}
}