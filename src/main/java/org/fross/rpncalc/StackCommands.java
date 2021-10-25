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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackCommands {
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
		Output.debugPrint("Cleaning Screen");
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
		int lineToDelete;

		// Ensure we have at least one item on the stack
		if (Main.calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "There must be at least one item on the stack to delete");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine line to delete by looking at arg
		try {
			lineToDelete = Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			if (!arg.isBlank()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number provided: '" + arg + "'");
				return;
			} else {
				lineToDelete = 1;
			}
		}
		Output.debugPrint("Line to Delete: " + lineToDelete);

		try {
			// Ensure the number entered is is valid
			if (lineToDelete < 1 || lineToDelete > Main.calcStack.size()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineToDelete);
			} else {
				// Finally we can remove the item from the stack
				Main.calcStack.remove(Main.calcStack.size() - lineToDelete);
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

			Main.calcStack = StackOperations.StackSwapItems(Main.calcStack, (item1 - 1), (item2) - 1);
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
	public static void cmdModulus() {
		// Ensure we have at least 2 items on the stack
		if (Main.calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least two items on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());
		
		// Perform the division and push the result onto the stack
		Double b = Main.calcStack.pop();
		Double a = Main.calcStack.pop();
		Output.debugPrint("Modulus: " + a + " % " + b + " = " + (a % b));
		Main.calcStack.push(a % b);
	}

	/**
	 * cmdAverage(): Calculate the average of the stack items
	 * 
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdAverage(String arg) {
		// Ensure we have enough numbers on the stack
		if (Main.calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  Average requires at least two items on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine if we should keep or clear the stack
		boolean keepFlag = false;
		try {
			// Just check if the provided command starts with 'k'. That should be enough
			if (arg.toLowerCase().charAt(0) == 'k') {
				keepFlag = true;
			}
		} catch (StringIndexOutOfBoundsException ex) {
			keepFlag = false;
		}

		// Calculate the mean
		Double mean = Math.Mean(Main.calcStack);

		// If we are not going to keep the stack (the default) clear it
		if (keepFlag == false)
			Main.calcStack.clear();

		// Add the average to the stack
		Main.calcStack.push(mean);
	}

	/**
	 * cmdStdDeviation(): Calculate the Standard Deviation of the stack items
	 * 
	 * Reference: https://www.mathsisfun.com/data/standard-deviation-formulas.html
	 * 
	 * @param arg
	 */
	@SuppressWarnings("unchecked")
	public static void cmdStdDeviation(String arg) {
		// Ensure we have enough numbers on the stack
		if (Main.calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  Standard Deviation requires at least two items on the stack");
			return;
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Determine if we should keep or clear the stack
		boolean keepFlag = false;
		try {
			// Just check if the provided command starts with 'k'. That should be enough
			if (arg.toLowerCase().charAt(0) == 'k') {
				keepFlag = true;
			}
		} catch (StringIndexOutOfBoundsException ex) {
			keepFlag = false;
		}

		// Step1: Get the mean
		Double mean1 = Math.Mean(Main.calcStack);
		Output.debugPrint("Inital mean of the numbers: " + mean1);

		// Step2: For each number: subtract the mean from the number and square the result
		Double[] stdArray = new Double[Main.calcStack.size()];
		for (int i = 0; i < Main.calcStack.size(); i++) {
			stdArray[i] = java.lang.Math.pow((Main.calcStack.get(i) - mean1), 2);
		}

		// Step3: Work out the mean of those squared differences
		Double mean2 = Math.Mean(stdArray);
		Output.debugPrint("Secondary mean of (number-mean)^2: " + mean2);

		if (keepFlag == false)
			Main.calcStack.clear();

		// Step4: Take the square root of that result and push onto the stack
		Double result = java.lang.Math.sqrt(mean2);
		Main.calcStack.push(result);
	}

	/**
	 * cmdCopy(): Copy the item at the top of the stack or the line number provided
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdCopy(String arg) {
		int lineNum = 1;

		// Ensure we have at least one number to copy
		if (Main.calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "Error: The stack must contain at least one number to copy");
			return;
		}

		// Determine line number to copy
		try {
			lineNum = Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			if (!arg.isBlank()) {
				Output.printColorln(Ansi.Color.RED, "ERROR:  '" + arg + "' is not a valid line number");
				return;
			}
		}

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		Output.debugPrint("Copying line" + lineNum + " to line1");

		// Copy the provided number if it's valid
		try {
			// Ensure the number entered is is valid
			if (lineNum < 1 || lineNum > Main.calcStack.size()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineNum);
			} else {
				// Perform the copy
				Main.calcStack.push(Main.calcStack.get(Main.calcStack.size() - lineNum));
			}
		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing line number for element copy: '" + lineNum + "'");
			Output.debugPrint(e.getMessage());
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
	 * cmdInteger(): Take the integer value of the top stack item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdInteger() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the integer of " + Main.calcStack.peek());
			String stackItemString = Main.calcStack.pop().toString();
			int stackItemInt = Integer.parseInt(stackItemString.substring(0, stackItemString.indexOf(".")));
			Main.calcStack.add(stackItemInt * 1.0);
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdAbsoluteValue(): Take the absolute value of the top stack item
	 */
	@SuppressWarnings("unchecked")
	public static void cmdAbsoluteValue() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		if (Main.calcStack.size() >= 1) {
			Output.debugPrint("Taking the absolute value of " + Main.calcStack.peek());

			Double value = Main.calcStack.pop();
			if (value < 0) {
				Main.calcStack.add(value * -1);
			} else {
				Main.calcStack.add(value);
			}
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdMinimum(): Add minimum value in the stack to the top of the stack
	 */
	@SuppressWarnings("unchecked")
	public static void cmdMinimum() {
		Double lowestValue = Double.MAX_VALUE;

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Loop through the stack and look for the lowest value
		for (int i = 0; i < Main.calcStack.size(); i++) {
			if (Main.calcStack.get(i) < lowestValue)
				lowestValue = Main.calcStack.get(i);
		}

		// Add lowest value to the stack
		Output.printColorln(Ansi.Color.CYAN, "Minimum value added to stack: " + lowestValue);
		Main.calcStack.push(lowestValue);
	}

	/**
	 * cmdMaximum(): Add minimum value in the stack to the top of the stack
	 */
	@SuppressWarnings("unchecked")
	public static void cmdMaximum() {
		Double largestValue = Double.MIN_VALUE;

		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Loop through the stack and look for the largest value
		for (int i = 0; i < Main.calcStack.size(); i++) {
			if (Main.calcStack.get(i) > largestValue)
				largestValue = Main.calcStack.get(i);
		}

		// Add lowest value to the stack
		Output.printColorln(Ansi.Color.CYAN, "Maximum value added to stack: " + largestValue);
		Main.calcStack.push(largestValue);
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
		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing low and high parameters.  Low: '" + low + "' High: '" + high + "'");
			Output.printColorln(Ansi.Color.RED, "See usage information in the help page");
			return;
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

}
