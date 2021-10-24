/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2021 Michael Fross
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

import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackOperations {
	/**
	 * StackSwapItems(): Swap two elements in the stack
	 * 
	 * Approach: Empty the stack into an array. Replace the existing values with the swapped values.
	 * Then recreate the stack.
	 * 
	 * @param stk
	 * @param item1
	 * @param item2
	 * @return
	 */
	public static Stack<Double> StackSwapItems(Stack<Double> stk, int item1, int item2) {
		int stkSize = stk.size();
		Double tempArray[] = new Double[stkSize];
		Double value1;
		Double value2;

		// Populate the array with the contents of the stack
		Output.debugPrint("Size of Stack is: " + stkSize);
		for (int i = 0; i < stkSize; i++) {
			// System.out.println("i = " + i);
			Output.debugPrint("Backup: Array[" + i + "] = " + stk.peek());
			tempArray[i] = stk.pop();
		}

		// Grab the initial values
		value1 = tempArray[item1];
		value2 = tempArray[item2];

		// Swap with the new values
		tempArray[item1] = value2;
		tempArray[item2] = value1;

		// Recreate the stack
		for (int i = stkSize - 1; i >= 0; i--) {
			Output.debugPrint("Restore: Array[" + i + "] = " + tempArray[i] + " -> Stack");
			stk.push(tempArray[i]);
		}

		return (stk);
	}

	/**
	 * cmdList(): Sub commands to list are:
	 * 
	 * stacks: List the current list of saved stacks from the preferences system
	 * 
	 * mem: List the current values in the memory stacks
	 * 
	 * undo: List the contents of the undo stack which shows previous stack states
	 */
	public static void cmdList(String arg) {
		switch (arg.toLowerCase()) {
		case "stacks":
		case "stack":
			String[] stks = Prefs.QueryStacks();

			Output.printColorln(Ansi.Color.YELLOW, "\n-Saved Stacks" + "-".repeat(Main.PROGRAMWIDTH - 13));
			for (int i = 0; i < stks.length; i++) {
				String sn = String.format("%02d:  %s", i, stks[i]);
				Output.printColorln(Ansi.Color.CYAN, sn);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.PROGRAMWIDTH) + "\n");
			break;

		case "mem":
			Output.printColorln(Ansi.Color.YELLOW, "\n-Memory Slots" + "-".repeat(Main.PROGRAMWIDTH - 13));
			for (int i = 0; i < StackMemory.memorySlots.length; i++) {
				Output.printColorln(Ansi.Color.CYAN, "Slot #" + i + ": " + StackMemory.memorySlots[i]);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.PROGRAMWIDTH) + "\n");
			break;

		case "undo":
			Output.printColorln(Ansi.Color.YELLOW, "\n-Undo Stack" + "-".repeat(Main.PROGRAMWIDTH - 11));
			for (int i = 0; i < Main.undoStack.size(); i++) {
				String sn = String.format("%02d:  %s", i + 1, Main.undoStack.get(i));
				Output.printColorln(Ansi.Color.CYAN, sn);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.PROGRAMWIDTH) + "\n");
			break;

		default:
			Output.printColorln(Ansi.Color.RED, "Error:  Unknown list command '" + arg + "'");
		}
	}

	/**
	 * cmdLoad(stackToLoad): Load the named stack after saving current stack to prefs
	 * 
	 * @param stackToLoad
	 */
	public static void cmdLoad(String stackToLoad) {
		// Save current Stack
		Prefs.SaveStack(Main.calcStack, "1");
		Prefs.SaveStack(Main.calcStack2, "2");

		// Set new stack
		Output.debugPrint("Loading new stack: '" + stackToLoad + "'");
		Prefs.SetLoadedStack(stackToLoad);

		// Load new stack
		Main.calcStack = Prefs.RestoreStack("1");
		Main.calcStack2 = Prefs.RestoreStack("2");
	}

	/**
	 * cmdSwapStack(): Swap the primary and secondary stacks
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdSwapStack() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Swapping primary and secondary stack");
		Stack<Double> calcStackTemp = (Stack<Double>) Main.calcStack.clone();
		Main.calcStack = (Stack<Double>) Main.calcStack2.clone();
		Main.calcStack2 = (Stack<Double>) calcStackTemp.clone();
		Prefs.ToggleCurrentStackNum();
	}

	/**
	 * cmdDebug(): Toggle debug setting
	 * 
	 */
	public static void cmdDebug() {
		if (Debug.query()) {
			Debug.disable();
			Output.printColorln(Ansi.Color.RED, "Debug Disabled");
		} else {
			Debug.enable();
			Output.debugPrint("Debug Enabled");
		}
	}

	/**
	 * cmdAlign(alignment): Set display alignment to l(eft), r(ight), or d(ecimal)
	 * 
	 * @param al
	 */
	public static void cmdAlign(char al) {
		// Validate we have one of the right values
		if (al != 'l' && al != 'd' && al != 'r') {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must provide an alignment value of 'l'eft, 'd'ecimal, or 'r'ight");
		} else {
			Output.debugPrint("Setting display alignment to: " + al);
			Main.displayAlignment = al;
		}
	}

}
