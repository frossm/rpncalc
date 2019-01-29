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

/* Current Defined Preferences:
 *   MoneyMode/Boolean:  Determine if rpn should start in money mode
 *   Persistent/Boolean:  Should the calc keep it's stack persistent between runs
 *   Stack/Double:  A saved list of everything in the stack
 */

package org.fross.rpn;

import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Prefs {
	// Class Constants
	private static final String PREFS_PATH = "/org/fross/rpn";

	// Class Variables
	private static Preferences prefs = Preferences.userRoot().node(PREFS_PATH);

	public static boolean QueryBoolean(String key) {
		return prefs.getBoolean(key, false);
	}

	public static Double QueryDouble(String key) {
		return prefs.getDouble(key, 0);
	}

	public static void Set(String key, boolean value) {
		prefs.putBoolean(key, value);
	}

	public static void Set(String key, int value) {
		prefs.putInt(key, value);
	}

	public static void Set(String key, double value) {
		prefs.putDouble(key, value);
	}

	/**
	 * SaveStack Save the provided stack into the preferences system
	 * 
	 * @param stk       - Stack to Save
	 * @param stackSlot - Stack Save slot number. Should be 1 or 2.
	 */
	public static void SaveStack(Stack<Double> stk) {
		Debug.Print("StackSave");

		// Lets clear out any stack prefs that may exist
		try {
			prefs.clear();
		} catch (BackingStoreException e) {
			Output.Red("ERROR: Could not clear current preferences in Stack #1");
			Output.Red(e.getMessage());
		}

		// Save number of elements to key StackElements
		prefs.putInt("StackElements", (int) stk.size());

		// Loop through each member of the stack and save it to the preferences
		for (int i = 0; i <= stk.size() - 1; i++) {
			Debug.Print("  - Saving #" + i + ":  " + stk.elementAt(i));
			Set("Stack" + i, stk.elementAt(i));
		}

	}

	/**
	 * RestoreStack Read the stack, as designated by the stack slot (1 or 2) from
	 * the prefs system
	 * 
	 * @param stackSlot - The slot (1 or 2) to pull from.
	 * @return
	 */
	public static Stack<Double> RestoreStack() {
		int numElements = prefs.getInt("StackElements", 0);
		Stack<Double> stk = new Stack<Double>();

		Debug.Print("Restoring Stack:");
		for (int i = 0; i <= numElements - 1; i++) {
			stk.push(prefs.getDouble("Stack" + i, 0.0));
			Debug.Print("  - Restoring #" + i + ":  " + stk.elementAt(i));
		}

		return stk;
	}
}