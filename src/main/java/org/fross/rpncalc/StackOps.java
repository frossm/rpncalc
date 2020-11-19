/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2020 Michael Fross
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackOps {
	// Class Constants
	public static final int DEFAULT_DENOMINATOR = 64;  // Default Smallest Fraction Denominator
	private static final int DEFAULT_MEMORY_SLOTS = 10;	 // Number of memory slots available for mem command

	// Class Variables
	private static Double[] memorySlots = new Double[DEFAULT_MEMORY_SLOTS];

	/**
	 * SetMaxMemorySlots(): Sets the number of memory slot available to be used
	 * 
	 * @param numSlots
	 */
	public static void SetMaxMemorySlots(String slots) {
		try {
			int numSlots = Integer.parseInt(slots);

			// Ensure we always have at least one memory slot
			if (numSlots >= 1) {
				memorySlots = new Double[numSlots];
			} else {
				Output.printColorln(Ansi.Color.RED, "Error: There must be at least 1 memory slot.  Setting to 1.");
				memorySlots = new Double[1];
			}

		} catch (NumberFormatException ex) {
			Output.fatalError("Could not set the number of memory slots to '" + slots + "'", 4);
		}
	}

	/**
	 * QueryInUseMemorySlots(): Return the number of memory slots being used
	 * 
	 * @return
	 */
	public static int QueryInUseMemorySlots() {
		int inUseCounter = 0;

		for (int i = 0; i < memorySlots.length; i++) {
			if (memorySlots[i] != null)
				inUseCounter++;
		}

		return inUseCounter;
	}

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
	 * cmdClean(): Clean the screen by clearing it and then showing existing stack
	 */
	public static void cmdClean() {
		Output.debugPrint("Cleanning Screen");
		// Rather than printing several hundred new lines, use the JANSI clear screen
		Output.clearScreen();
	}

	/**
	 * cmdDelete(): Delete the provided item from the stack
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdDelete(String arg) {
		int lineToDelete = 0;
		try {
			lineToDelete = Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			Output.printColorln(Ansi.Color.RED, "Line number provided can not be deleted: '" + arg + "'");
			return;
		}

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
		Main.calcStack.push(java.lang.Math.sqrt(Main.calcStack.pop()));
	}

	/**
	 * cmdRound(): Round to the provided decimal place. If none is provided round to the nearest integer
	 * 
	 * Reference: https://www.baeldung.com/java-round-decimal-number
	 * 
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdRound(String arg) {
		int decimalPlaces = 0;
		BigDecimal bd;

		// Ensure we have something on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least one item on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Convert the arg to the number of decimal places
		try {
			decimalPlaces = Integer.parseInt(arg);
			// Ensure a negative number is not provdied for decimal points to round
			if (decimalPlaces <= 0) {
				Output.printColorln(Ansi.Color.RED, "ERROR:  '" + arg + "' not a valid number of decimal places");
				return;
			}

		} catch (NumberFormatException ex) {
			if (arg.isBlank()) {
				decimalPlaces = 0;
			} else {
				// Error out for any non-valid characters
				Output.printColorln(Ansi.Color.RED, "ERROR:  '" + arg + "' not a valid number of decimal places");
				return;
			}
		}

		// Round the top of stack item and return that result to the stack
		bd = new BigDecimal(Main.calcStack.pop());
		bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
		Main.calcStack.push(bd.doubleValue());
	}

	/**
	 * cmdAddAll(): Add everything on the stack together and return the result to the stack
	 * 
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdAddAll(String arg) {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine if we should keep or clear the stack upon adding
		boolean keepFlag = false;
		try {
			// Just check if the provided command starts with 'k'. That should be enough
			if (arg.toLowerCase().charAt(0) == 'k') {
				keepFlag = true;
			}
		} catch (StringIndexOutOfBoundsException ex) {
			keepFlag = false;
		}

		// Counter to hold the accumulating total
		Double totalCounter = 0.0;

		// If the 'keep' flag was sent, get the stack items instead of using pop
		if (keepFlag == true) {
			for (int i = 0; i < Main.calcStack.size(); i++) {
				totalCounter += Main.calcStack.get(i);
			}
		} else {
			// Loop through the stack items popping them off until there is nothing left
			while (Main.calcStack.empty() == false) {
				totalCounter += Main.calcStack.pop();
			}
		}

		// Add result back to the stack
		Main.calcStack.push(totalCounter);
	}

	/**
	 * cmdMod(): Divide and place the modulus onto the stack
	 */
	@SuppressWarnings("unchecked")
	public static void cmdMod() {
		// Ensure we have something on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least one item on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());
		Double b = Main.calcStack.pop();
		Double a = Main.calcStack.pop();
		Output.debugPrint("Modulus: " + a + " % " + b + " = " + (a % b));
		Main.calcStack.push(a % b);
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
	 * cmdLog(): Take the natural (base e) logarithm
	 */
	@SuppressWarnings("unchecked")
	public static void cmdLog() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the natural logarithm of " + Main.calcStack.peek());
			Main.calcStack.add(java.lang.Math.log(Main.calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdLog10(): Take base10 logarithm
	 */
	@SuppressWarnings("unchecked")
	public static void cmdLog10() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the base 10 logarithm of " + Main.calcStack.peek());
			Main.calcStack.add(java.lang.Math.log10(Main.calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
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
			Output.printColorln(Ansi.Color.RED, "ERROR:\n" + e.getMessage());
		}

		// Display Debug Output
		Output.debugPrint("Generating Random number between " + low + " and " + high);

		// Verify that the low number <= the high number
		if (low > high) {
			Output.printColorln(Ansi.Color.RED, "ERROR: the first number much be less than or equal to the high number");
			return;
		}

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
			Output.printColorln(Ansi.Color.RED, "ERROR:\n" + e.getMessage());
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
		int denominator = DEFAULT_DENOMINATOR;

		// If a denominator is provided, use it instead of the default
		try {
			if (!param.isEmpty())
				denominator = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + param + "' is not a valid denominator");
			return;
		}

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
		String stackHeader = "-Fraction (1/" + (denominator * gcd) + ")";
		Output.printColorln(Ansi.Color.YELLOW, "\n" + stackHeader + "-".repeat(Main.PROGRAMWIDTH - stackHeader.length()));
		Output.printColorln(Ansi.Color.WHITE, " " + Main.calcStack.peek() + " is approximately '" + integerPart + " " + numerator + "/" + denominator + "'");
		Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.PROGRAMWIDTH) + "\n");
	}

	/**
	 * cmdRadian(): Convert line1 from degrees to radians.
	 * 
	 * Formula: radians = degrees (PI/180)
	 */
	@SuppressWarnings("unchecked")
	public static void cmdRadian() {
		// Ensure we have something on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Pull the value, convert and push back
		Main.calcStack.push(Main.calcStack.pop() * (java.lang.Math.PI / 180));
	}

	/**
	 * cmdDegree(): Convert line1 from radians to degrees
	 * 
	 * Formula: degrees = radians * (180 / PI)
	 */
	@SuppressWarnings("unchecked")
	public static void cmdDegree() {
		// Ensure we have something on the stack
		if (Main.calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Pull the value, convert and push back
		Main.calcStack.push(Main.calcStack.pop() * (180 / java.lang.Math.PI));
	}

	/**
	 * cmdTrig(): Calculate the trig functions. There was so much overlap in the functions I
	 * consolidated
	 * 
	 * @param cmd
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdTrig(String cmd, String arg) {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Double angle = null;

		// Ensure we have at least one value on the stack
		if (Main.calcStack.size() >= 1) {
			try {
				angle = Main.calcStack.pop();

				// Calculations are done in radians. Convert if 'rad' is not provided as a parameter
				if (arg.toLowerCase().charAt(0) != 'r') {
					Output.printColorln(Ansi.Color.RED, "ERROR: unknown " + cmd + " parameter: '" + arg + "'");
					Main.calcStack.push(angle);
					return;
				}
			} catch (StringIndexOutOfBoundsException ex) {
				angle = java.lang.Math.toRadians(angle);
			}

		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
			return;
		}

		// Push the result back onto the stack
		switch (cmd) {
		case "tan":
			Main.calcStack.add(java.lang.Math.tan(angle));
			break;

		case "sin":
			Main.calcStack.add(java.lang.Math.sin(angle));
			break;

		case "cos":
			Main.calcStack.add(java.lang.Math.cos(angle));
			break;

		default:
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not understand trig command: '" + cmd + "'");
			return;
		}
	}

	/**
	 * cmdArcTrig(): Calculate the arc trig functions. There was so much overlap in the functions I
	 * consolidated
	 * 
	 * @param cmd
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdArcTrig(String cmd, String arg) {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Double result = null;
		Double originalValue = null;

		// Ensure we have at least one value on the stack
		if (Main.calcStack.size() >= 1) {
			originalValue = Main.calcStack.peek();

			// Calculate the arc trig function
			switch (cmd) {
			case "asin":
				result = java.lang.Math.asin(Main.calcStack.pop());
				break;

			case "acos":
				result = java.lang.Math.acos(Main.calcStack.pop());
				break;

			case "atan":
				result = java.lang.Math.atan(Main.calcStack.pop());
				break;

			default:
				Output.printColorln(Ansi.Color.RED, "ERROR: Could not understand trig command: '" + cmd + "'");
				Main.calcStack.push(originalValue);
				return;
			}
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
			return;
		}

		try {
			// Display value in degrees or if 'rad' is a parameter, as radians
			if (arg.toLowerCase().charAt(0) == 'r') {
				Main.calcStack.push(result);
			} else {
				Main.calcStack.push(originalValue);
				Output.printColorln(Ansi.Color.RED, "ERROR: unknown " + cmd + " parameter: '" + arg + "'");
			}

		} catch (StringIndexOutOfBoundsException ex) {
			Main.calcStack.push(java.lang.Math.toDegrees(result));
		}
	}

	/**
	 * cmdHypotenuse(): Calculates the hypotenuse by pulling the top two stack items and using them as
	 * the triangle legs
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdHypotenuse() {
		// Ensure we have something on the stack
		if (Main.calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be two items on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Pop the two values and push the hypotenuse back onto the stack
		Main.calcStack.push(java.lang.Math.hypot(Main.calcStack.pop(), Main.calcStack.pop()));
	}

	/**
	 * cmdMem(): Manage the memory slots
	 * 
	 * @param cmd
	 */
	@SuppressWarnings("unchecked")
	public static void cmdMem(String arg) {
		String[] argParse = null;
		int memSlot = 0;

		// Parse the command string provided. If we can't create an integer from the first
		// arg then no stack number was provided
		try {
			argParse = arg.split(" ");
			memSlot = Integer.parseInt(argParse[0]);
		} catch (NumberFormatException ex) {
			// No slot number provided, just the command. Set Slot to 0 and it will re-parse below
			arg = "0 " + arg;
		}

		// Now that we have a memory slot (0 was inserted above if none was provided) we can get to work
		try {
			argParse = arg.split(" ");
			memSlot = Integer.parseInt(argParse[0]);

			Output.debugPrint("Argument Parsing: Memory Slot Selected: " + memSlot);
			Output.debugPrint("Argument Parsing: Memory Command: " + argParse[1]);

			// Ensure provided slot is within range
			if (memSlot < 0 || memSlot >= memorySlots.length) {
				Output.printColorln(Ansi.Color.RED, "ERROR: Memory Slot Number must be between 0 and " + (memorySlots.length - 1));
				return;
			}

			// Execute provided memory command
			switch (argParse[1].toLowerCase()) {
			// Add the last stack item in the memory slot
			case "add":
				// Ensure there is a value to save to the memory slot
				if (Main.calcStack.size() >= 1) {
					Output.printColorln(Ansi.Color.CYAN, "Adding '" + Main.calcStack.peek() + "' to Memory Slot #" + memSlot);
					memorySlots[memSlot] = Main.calcStack.peek();
				} else {
					Output.printColorln(Ansi.Color.RED, "ERROR: There must be at least one value on the stack");
				}
				break;

			// Clear the provided slot's value
			case "clr":
			case "clear":
				Output.printColorln(Ansi.Color.CYAN, "Clearing Memory Slot #" + memSlot);
				memorySlots[memSlot] = null;
				break;

			case "clrall":
			case "clearall":
				Output.printColorln(Ansi.Color.CYAN, "Clearing All Memory Slots");
				for (int i = 0; i < memorySlots.length; i++) {
					memorySlots[i] = null;
				}
				break;

			// Copy the value from the memory slot provided back onto the stack
			case "copy":
			case "recall":
				// Save to undo stack
				Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

				Output.printColorln(Ansi.Color.CYAN, "Copying values from Memory Slot #" + memSlot);
				if (memorySlots[memSlot] != null)
					Main.calcStack.add(memorySlots[memSlot]);
				else
					Output.printColorln(Ansi.Color.RED, "Memory Slot #" + memSlot + " is empty");
				break;

			default:
				// Slot was valid number, but unknown mem command
				Output.printColorln(Ansi.Color.RED, "ERROR: Unknown memory command: '" + argParse[1] + "'");
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "Error parsing mem command: 'mem " + arg + "'  See help for mem command usage");
		}
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
			for (int i = 0; i < memorySlots.length; i++) {
				Output.printColorln(Ansi.Color.CYAN, "Slot #" + i + ": " + memorySlots[i]);
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

} // END CLASS
