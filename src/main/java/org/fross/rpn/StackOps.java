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

import java.util.Stack;
import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackOps {

	/**
	 * StackDeleteItem(): Delete a stack element
	 * 
	 * @param stk
	 * @param lineToDelete
	 * @return
	 */
	public static Stack<Double> StackDeleteItem(Stack<Double> stk, int elementToDelete) {
		Stack<Double> tempStack = new Stack<Double>();

		// Copy the elements in the stack to a temporary stack except for the one to
		// delete
		try {
			for (int i = 0; i <= elementToDelete; i++) {
				if (i != elementToDelete) {
					Output.debugPrint("Moving line:    #" + (i + 1) + " [" + stk.peek() + "] to a temp stack");
					tempStack.push(stk.pop());
				} else {
					Output.debugPrint("Skipping Line:  #" + (i + 1) + " [" + stk.peek() + "] as it's being deleted");
					stk.pop();
				}
			}
		} catch (Exception ex) {
			Output.debugPrint(ex.getMessage());
		}

		// Copy the elements in the temp stack back to the main one
		try {
			while (tempStack.size() > 0) {
				Output.debugPrint("Restore Value:  " + tempStack.peek());
				stk.push(tempStack.pop());
			}
		} catch (Exception ex) {
			Output.debugPrint(ex.getMessage());
		}

		return (stk);
	}

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
	 * cmdAlign(alignment): Set display alignment to l(eft), r(ight), or d(ecimal)
	 * 
	 * @param al
	 */
	public static void cmdAlign(char al) {
		Output.debugPrint("Setting display alignment to: " + al);
		Main.displayAlignment = al;
	}

	/**
	 * cmdListUndo(StackToDisplay): Display the current undo stack
	 * 
	 * @param undoStk
	 */
	@SuppressWarnings("rawtypes")
	public static void cmdListUndo(Stack<Stack> undoStk) {
		Output.printColorln(Ansi.Color.YELLOW, "-Undo Stack:-------------------------------");
		for (int i = 0; i < undoStk.size(); i++) {
			String sn = String.format("%02d:  %s", i + 1, undoStk.get(i));
			Output.printColorln(Ansi.Color.CYAN, sn);
		}
		Output.printColorln(Ansi.Color.YELLOW, "-------------------------------------------");
	}

	/**
	 * cmdUndo(): Undo last change be restoring the last stack from the undo stack
	 */
	@SuppressWarnings("unchecked")
	public static void cmdUndo() {
		Output.debugPrint("Undoing last command");

		if (Main.undoStack.size() >= 1) {
			// Replace current stack with the last one on the undo stack
			Main.calcStack = (Stack<Double>) Main.undoStack.pop().clone();
		} else {
			Output.printColorln(Ansi.Color.RED, "Error: Already at oldest change");
		}
	}

	/**
	 * cmdClear(): Clear the current stack and the screen
	 */
	@SuppressWarnings("unchecked")
	public static void cmdClear() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Clearing Stack");
		Main.calcStack.clear();

		// Rather than printing several hundred new lines, use the JANSI clear screen
		Output.clearScreen();
	}

	/**
	 * cmdDelete(): Delete the provided item from the stack
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdDelete(int lineToDelete) {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine the line number to delete
		Output.debugPrint("Line to Delete: " + lineToDelete);
		try {
			// Ensure the number entered is is valid
			if (lineToDelete < 1 || lineToDelete > Main.calcStack.size()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineToDelete);
			} else {
				Output.debugPrint("Deleting line number: " + lineToDelete);
				Main.calcStack = StackOps.StackDeleteItem(Main.calcStack, (lineToDelete - 1));
			}

		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing line number for element delete: '" + lineToDelete + "'");
			Output.debugPrint(e.getMessage());
		}
	}

	/**
	 * cmdSqrt(): Take the square root of the number at the top of the stack
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdSqrt() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Taking the square root of the last stack item");
		Math.SquareRoot(Main.calcStack);
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
	 * cmdRandom(): Produce a random number between the Low and High values provided. If there are no
	 * parameters, produce the number between 0 and 1.
	 * 
	 * @param param
	 */
	@SuppressWarnings("unchecked")
	public static void cmdRandom(String param) {
		int low = 1;
		int high = 100;
		int randomNumber = 0;

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Parse out the low and high numbers
		try {
			if (!param.isEmpty()) {
				low = Integer.parseInt(param.substring(0).trim().split("\\s")[0]);
				high = Integer.parseInt(param.substring(0).trim().split("\\s")[1]);
			}
		} catch (NumberFormatException e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing low and high parameters.  Low: '" + low + "' High: '" + high + "'");
			return;
		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error:\n" + e.getMessage());
		}

		// Display Debug Output
		Output.debugPrint("Generating Random number between " + low + " and " + high);

		// Generate the random number. Rand function will generate 0-9 for random(10)
		// so add 1 to the high so we include the high number in the results
		randomNumber = new java.util.Random().nextInt((high + 1) - low) + low;

		// Add result to the calculator stack
		Main.calcStack.push((double) randomNumber);
	}

	/**
	 * cmdDice(XdY): Roll a Y sided die X times and add the result to the stack.
	 * 
	 * @param param
	 */
	@SuppressWarnings("unchecked")
	public static void cmdDice(String param) {
		int die = 6;
		int rolls = 1;

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Parse out the die sides and rolls
		try {
			if (!param.isEmpty()) {
				rolls = Integer.parseInt(param.substring(0).trim().split("[Dd]")[0]);
				die = Integer.parseInt(param.substring(0).trim().split("[Dd]")[1]);
			}
		} catch (NumberFormatException e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing die and rolls.  Rolls: '" + rolls + "' Die: '" + die + "'");
			return;
		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error:\n" + e.getMessage());
		}

		// Display Debug Output
		Output.debugPrint("Rolls: '" + rolls + "' Die: '" + die + "'");

		for (int i = 0; i < rolls; i++) {
			Main.calcStack.push((double) new java.util.Random().nextInt(die) + 1);
		}

	}

	/**
	 * cmdSwapElements(): Swap the provided elements within the stack
	 * 
	 * @param param
	 */
	@SuppressWarnings("unchecked")
	public static void cmdSwapElements(String param) {
		// Default is to swap last two stack items
		int item1 = 1;
		int item2 = 2;

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine the source and destination elements
		try {
			if (!param.isEmpty()) {
				item1 = Integer.parseInt(param.substring(0).trim().split("\\s")[0]);
				item2 = Integer.parseInt(param.substring(0).trim().split("\\s")[1]);
			}

		} catch (NumberFormatException e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing line number for stack swap: '" + item1 + "' and '" + item2 + "'");
			return;

		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error:\n" + e.getMessage());
		}

		// Make sure the numbers are valid
		if (item1 < 1 || item1 > Main.calcStack.size() || item2 < 1 || item2 > Main.calcStack.size()) {
			Output.printColorln(Ansi.Color.RED, "Invalid element entered.  Must be between 1 and " + Main.calcStack.size());
		} else {
			Output.debugPrint("Swapping #" + item1 + " and #" + item2 + " stack items");

			Main.calcStack = StackOps.StackSwapItems(Main.calcStack, (item1 - 1), (item2) - 1);
		}
	}

	/**
	 * cmdFlipSign(): Change the sign of the last element in the stack
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdFlipSign() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Changing sign of last stack element");
		if (!Main.calcStack.isEmpty())
			Main.calcStack.push(Main.calcStack.pop() * -1);
	}

	/**
	 * cmdCopy(): Copy the item at the top of the stack
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdCopy() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Copying the item at the top of the stack");
		if (Main.calcStack.size() >= 1) {
			Main.calcStack.add(Main.calcStack.lastElement());
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be an item in the stack to copy it");
		}
	}

	/**
	 * cmdOperand(): An operand was entered such as + or -
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdOperand(String Op) {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("CalcStack has " + Main.calcStack.size() + " elements");
		Output.debugPrint("Operand entered: '" + Op + "'");
		// Verify stack contains at least two elements
		if (Main.calcStack.size() >= 2) {
			Main.calcStack = Math.Parse(Op, Main.calcStack);
		} else {
			Output.printColorln(Ansi.Color.RED, "Two numbers are required for this operation");
		}

	}

}
