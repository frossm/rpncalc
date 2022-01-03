/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2022 Michael Fross
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *           
 ******************************************************************************/
package org.fross.rpncalc;

import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackManagement {
	// Class Constants
	protected static final String PREFS_PATH = "/org/fross/rpn/stacks";

	// Class Variables
	private static Preferences prefs = Preferences.userRoot().node(PREFS_PATH);
	private static String currentLoadedStack = "default";
	private static int currentStackNum = 1;

	/**
	 * SaveStack(): Save the provided stack into the preferences system
	 * 
	 * @param stk       - Stack to Save
	 * @param stackSlot - Stack Save slot number. Should be default, 1, or 2.
	 */
	public static void SaveStack(Stack<Double> stk, String stackSlot) {
		Output.debugPrint("SaveStack: " + PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Override the default stack location with the provided one
		Preferences p = Preferences.userRoot().node(PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Lets clear out any stack prefs that may exist
		try {
			p.clear();
		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not clear current preferences in Stack #" + stackSlot);
			Output.printColorln(Ansi.Color.RED, e.getMessage());
		}

		// Save number of elements to key StackElements
		p.putInt("StackElements", (int) stk.size());

		// Loop through each member of the stack and save it to the preferences
		for (int i = 0; i <= (int) stk.size() - 1; i++) {
			Output.debugPrint("  - Saving #" + (stk.size() - i) + ":  " + stk.elementAt(i));
			p.putDouble("Stack" + i, stk.elementAt(i));
		}

	}

	/**
	 * RestoreStack(): Read the stack, as designated by the stack slot (1 or 2) from the preferences
	 * system
	 * 
	 * @param stackSlot - The slot (default, 1, or 2) to pull from.
	 * @return
	 */
	public static Stack<Double> RestoreStack(String stackSlot) {
		Output.debugPrint("RestoreStack: " + PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);

		// Override the default stack location with the provided one
		prefs = Preferences.userRoot().node(PREFS_PATH + "/" + QueryLoadedStack() + "/" + stackSlot);
		int numElements = prefs.getInt("StackElements", 0);
		Stack<Double> stk = new Stack<Double>();

		Output.debugPrint("Restoring Stack:");
		for (int i = 0; i <= numElements - 1; i++) {
			stk.push(prefs.getDouble("Stack" + i, 0.0));
			Output.debugPrint("  - Restoring #" + (numElements - i) + ":  " + stk.elementAt(i));
		}

		return stk;
	}

	/**
	 * QueryCurrentStackNum(): Return the current active stack number
	 * 
	 * @return
	 */
	public static int QueryCurrentStackNum() {
		return currentStackNum;
	}

	/**
	 * Toggle CurrentStackNum(): Toggle to the next stack
	 * 
	 */
	public static void ToggleCurrentStackNum() {
		if (currentStackNum == 1)
			currentStackNum = 2;
		else
			currentStackNum = 1;
	}

	/**
	 * QueryStacks(): Return a string array of all current stacks
	 * 
	 * @return
	 */
	public static String[] QueryStacks() {
		String[] stacks = {};
		Preferences prefsQuery = Preferences.userRoot().node(PREFS_PATH);

		try {
			stacks = prefsQuery.childrenNames();
		} catch (BackingStoreException ex) {
			Output.printColor(Ansi.Color.RED, "Error Reading Stacks from Java Preferences");
		}

		return (stacks);
	}

	/**
	 * QueryLoadedStack(): Returns the name of the current stack that is in use The loaded stack name is
	 * important for saving and restoring data from the preferences system
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

}
