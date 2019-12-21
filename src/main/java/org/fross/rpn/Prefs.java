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

/* Current Defined Preferences:
 *   MoneyMode/Boolean:  Determine if rpn should start in money mode
 *   Persistent/Boolean:  Should the calc keep it's stack persistent between runs
 *   Stack/Double:  A saved list of everything in the stack
 */

package org.fross.rpn;

import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.fusesource.jansi.Ansi;

/**
 * Prefs: Holds the logic and calls to the java preferences system. Used to save
 * and restore the stacks between sessions.
 * 
 * @author michael.d.fross
 *
 */
public class Prefs {
	// Class Constants
	private static final String PREFS_PATH = "/org/fross/rpn/stacks";

	// Class Variables
	private static Preferences prefs = Preferences.userRoot().node(PREFS_PATH);
	private static String currentLoadedStack = "default";
	private static int currentStackNum = 1;

	public static int QueryCurrentStackNum() {
		return currentStackNum;
	}

	public static void ToggleCurrentStackNum() {
		if (currentStackNum == 1)
			currentStackNum = 2;
		else
			currentStackNum = 1;
	}

	/**
	 * QueryLoadedStack(): Returns the name of the current stack that is in use The
	 * loaded stack name is important for saving and restoring data from the prefs
	 * system
	 * 
	 * @return
	 */
	public static String QueryLoadedStack() {
		return currentLoadedStack;
	}

	/**
	 * SetLoadedStack(): Name the current stack that is being used
	 * 
	 * @param s
	 */
	public static void SetLoadedStack(String s) {
		currentLoadedStack = s;
	}

	/**
	 * QueryBoolean(): Returns a boolean preference item
	 * 
	 * @param key
	 * @return
	 */
	public static boolean QueryBoolean(String key) {
		return prefs.getBoolean(key, false);
	}

	/**
	 * QueryDouble(): Returns a Double preference item
	 * 
	 * @param key
	 * @return
	 */
	public static Double QueryDouble(String key) {
		return prefs.getDouble(key, 0);
	}

	/**
	 * Set Sets a boolean preference
	 * 
	 * @param key
	 * @param value
	 */
	public static void Set(String key, boolean value) {
		prefs.putBoolean(key, value);
	}

	/**
	 * Set(): Sets a integer preference
	 * 
	 * @param key
	 * @param value
	 */
	public static void Set(String key, int value) {
		prefs.putInt(key, value);
	}

	/**
	 * Set(): Sets a double preference
	 * 
	 * @param key
	 * @param value
	 */
	public static void Set(String key, double value) {
		prefs.putDouble(key, value);
	}

	/**
	 * SaveStack(): Save the provided stack into the preferences system
	 * 
	 * @param stk       - Stack to Save
	 * @param stackSlot - Stack Save slot number. Should be default, 1, or 2.
	 */
	public static void SaveStack(Stack<Double> stk, String stackSlot) {
		Debug.Print("SaveStack: " + PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Override the default stack location with the provided one
		prefs = Preferences.userRoot().node(PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Lets clear out any stack prefs that may exist
		try {
			prefs.clear();
		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not clear current preferences in Stack #1");
			Output.printColorln(Ansi.Color.RED, e.getMessage());
		}

		// Save number of elements to key StackElements
		prefs.putInt("StackElements", (int) stk.size());

		// Loop through each member of the stack and save it to the preferences
		for (int i = 0; i <= stk.size() - 1; i++) {
			Debug.Print("  - Saving #" + (stk.size() - i) + ":  " + stk.elementAt(i));
			Set("Stack" + i, stk.elementAt(i));
		}

	}

	/**
	 * RestoreStack(): Read the stack, as designated by the stack slot (1 or 2) from
	 * the preferences system
	 * 
	 * @param stackSlot - The slot (default, 1, or 2) to pull from.
	 * @return
	 */
	public static Stack<Double> RestoreStack(String stackSlot) {
		Debug.Print("RestoreStack: " + PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Override the default stack location with the provided one
		prefs = Preferences.userRoot().node(PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);
		int numElements = prefs.getInt("StackElements", 0);
		Stack<Double> stk = new Stack<Double>();

		Debug.Print("Restoring Stack:");
		for (int i = 0; i <= numElements - 1; i++) {
			stk.push(prefs.getDouble("Stack" + i, 0.0));
			Debug.Print("  - Restoring #" + (numElements - i) + ":  " + stk.elementAt(i));
		}

		return stk;
	}

}