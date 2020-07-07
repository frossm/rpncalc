/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE file for additional details
 *           
 ******************************************************************************/
package org.fross.rpncalc;

import java.util.Stack;
import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackOps {
	// Class Constants
	public static final int MAX_DENOMINATOR = 64; // Smallest Fraction Denominator

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
		// Verify we have an item on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Taking the square root of the last stack item");
		Math.SquareRoot(Main.calcStack);
	}
	
	/**
	 * cmdMod():  Divide and place the modulus onto the stack
	 */
	public static void cmdMod() {
		Double b = Main.calcStack.pop();
		Double a = Main.calcStack.pop();
		Output.debugPrint("Modulus: " + a + " % " + b + " = " + (a % b));
		Main.calcStack.push(a % b);
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

		// Verify that the low number <= the high number
		if (low > high) {
			Output.printColorln(Ansi.Color.RED, "Error: the first number much be less than or equal to the high number");
			return;
		}

		// Generate the random number. Rand function will generate 0-9 for random(10)
		// so add 1 to the high so we include the high number in the results
		randomNumber = new java.util.Random().nextInt((high + 1) - low) + low;

		// Add result to the calculator stack
		Main.calcStack.push((double) randomNumber);
	}

	/**
	 * cmdFraction(): Display the last stack item as a fraction with a minimum base of the provided
	 * number. For example, sending 64 would produce a fraction of 1/64th but will be reduced if
	 * possible.
	 * 
	 * @param param
	 */
	public static void cmdFraction(String param) {
		// Make sure the stack is not empty
		// Verify we have an item on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// The base to convert the fraction to. For example, 64 = 1/64th
		int denominator = MAX_DENOMINATOR;

		// If no denominator is provided, use it instead of the default
		if (!param.isEmpty())
			denominator = Integer.parseInt(param);

		// Determine the integer portion of the number
		int integerPart = (int) java.lang.Math.floor(Main.calcStack.peek());

		// Determine the fractional portion as an double
		double decimalPart = Main.calcStack.peek() - integerPart;

		// Convert to a fraction with provided base
		long numerator = java.lang.Math.round(decimalPart * denominator);

		// Get the Greatest Common Divisor so we can simply the fraction
		long gcd = Math.GreatestCommonDivisor(numerator, denominator);

		Output.debugPrint("Greatest Common Divisor for " + numerator + " and " + denominator + " is " + gcd);

		// Simply the fraction
		numerator /= gcd;
		denominator /= gcd;

		// Output the fractional display
		Output.printColorln(Ansi.Color.YELLOW, "---Fraction (1/" + (denominator * gcd) + ")-----------------------");
		Output.printColorln(Ansi.Color.WHITE, " " + Main.calcStack.peek() + " is approximately '" + integerPart + " " + numerator + "/" + denominator + "'");
		Output.printColorln(Ansi.Color.YELLOW, "-----------------------------------------\n");
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

		// Verify that the entered numbers are valid
		if (die <= 0) {
			Output.printColorln(Ansi.Color.RED, "ERROR: die must have greater than zero sides");
			return;
		} else if (rolls < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR: You have to specify at least 1 roll");
			return;
		}

		// Roll them bones
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
			Output.printColorln(Ansi.Color.RED, "ERROR:\n" + e.getMessage());
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

	/**
	 * cmdTangent(): Take the tangent of the last stack item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdTangent() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the Tangent of " + Main.calcStack.peek());
			Main.calcStack.add(java.lang.Math.tan(Main.calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdSin(): Take the sin of the last stack item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdSin() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the Sin of " + Main.calcStack.peek());
			Main.calcStack.add(java.lang.Math.sin(Main.calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdCos(): Take the Cos of the last stack item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdCos() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the Cosine of " + Main.calcStack.peek());
			Main.calcStack.add(java.lang.Math.cos(Main.calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

} // END CLASS
