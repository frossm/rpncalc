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

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackCommands {
	/**
	 * cmdAddAll(): Add everything on the stack together and return the result to the stack
	 * 
	 * @param arg
	 */
	public static void cmdAddAll(StackObj calcStack, String arg) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
			for (int i = 0; i < calcStack.size(); i++) {
				totalCounter += calcStack.get(i);
			}
		} else {
			// Loop through the stack items popping them off until there is nothing left
			while (calcStack.isEmpty() == false) {
				totalCounter += calcStack.pop();
			}
		}

		// Add result back to the stack
		calcStack.push(totalCounter);
	}

	/**
	 * cmdAbsoluteValue(): Take the absolute value of the top stack item
	 */
	public static void cmdAbsoluteValue(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		if (calcStack.size() >= 1) {
			Output.debugPrint("Taking the absolute value of " + calcStack.peek());

			Double value = calcStack.pop();
			if (value < 0) {
				calcStack.push(value * -1);
			} else {
				calcStack.push(value);
			}
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdAverage(): Calculate the average of the stack items
	 * 
	 * @param arg
	 */
	public static void cmdAverage(StackObj calcStack, String arg) {
		// Ensure we have enough numbers on the stack
		if (calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  Average requires at least two items on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
		Double mean = Math.Mean(calcStack);

		// If we are not going to keep the stack (the default) clear it
		if (keepFlag == false)
			calcStack.clear();

		// Add the average to the stack
		calcStack.push(mean);
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
	 * cmdClear(): Clear the current stack and the screen
	 */
	public static void cmdClear(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrint("Clearing Stack");
		calcStack.clear();

		// Rather than printing several hundred new lines, use the JANSI clear screen
		Output.clearScreen();
	}

	/**
	 * cmdCopy(): Copy the item at the top of the stack or the line number provided
	 * 
	 */
	public static void cmdCopy(StackObj calcStack, String arg) {
		int lineNum = 1;

		// Ensure we have at least one number to copy
		if (calcStack.size() < 1) {
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

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrint("Copying line" + lineNum + " to line1");

		// Copy the provided number if it's valid
		try {
			// Ensure the number entered is is valid
			if (lineNum < 1 || lineNum > calcStack.size()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineNum);
			} else {
				// Perform the copy
				calcStack.push(calcStack.get(calcStack.size() - lineNum));
			}
		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing line number for element copy: '" + lineNum + "'");
			Output.debugPrint(e.getMessage());
		}
	}

	/**
	 * cmdDelete(): Delete the provided item from the stack
	 * 
	 * @param item
	 */
	public static void cmdDelete(StackObj calcStack, String arg) {
		int lineToDelete;

		// Ensure we have at least one item on the stack
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "There must be at least one item on the stack to delete");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
			if (lineToDelete < 1 || lineToDelete > calcStack.size()) {
				Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineToDelete);
			} else {
				// Finally we can remove the item from the stack
				calcStack.remove(calcStack.size() - lineToDelete);
			}

		} catch (Exception e) {
			Output.printColorln(Ansi.Color.RED, "Error parsing line number for element delete: '" + lineToDelete + "'");
			Output.debugPrint(e.getMessage());
		}
	}

	/**
	 * cmdDice(XdY): Roll a Y sided die X times and add the result to the stack.
	 * 
	 * @param param
	 */
	public static void cmdDice(StackObj calcStack, String param) {
		int die = 6;
		int rolls = 1;

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
			calcStack.push((double) new java.util.Random().nextInt(die) + 1);
		}

	}

	/**
	 * cmdFlipSign(): Change the sign of the last element in the stack
	 * 
	 */
	public static void cmdFlipSign(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "Error: There must be at least one item on the stack to flip it's sign");
		} else {
			Output.debugPrint("Changing sign of last stack element");
			calcStack.push(calcStack.pop() * -1);
		}
	}

	/**
	 * cmdInteger(): Take the integer value of the top stack item
	 */
	public static void cmdInteger(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		if (calcStack.size() >= 1) {
			Output.debugPrint("Taking the integer of " + calcStack.peek());
			String stackItemString = calcStack.pop().toString();
			int stackItemInt = Integer.parseInt(stackItemString.substring(0, stackItemString.indexOf(".")));
			calcStack.push(stackItemInt * 1.0);
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdLog(): Take the natural (base e) logarithm
	 */
	public static void cmdLog(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		if (calcStack.size() >= 1) {
			Output.debugPrint("Taking the natural logarithm of " + calcStack.peek());
			calcStack.push(java.lang.Math.log(calcStack.pop()));

		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdLog10(): Take base10 logarithm
	 */
	public static void cmdLog10(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		if (calcStack.size() >= 1) {
			Output.debugPrint("Taking the base 10 logarithm of " + calcStack.peek());
			calcStack.push(java.lang.Math.log10(calcStack.pop()));
		} else {
			Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
		}
	}

	/**
	 * cmdMaximum(): Add minimum value in the stack to the top of the stack
	 */
	public static void cmdMaximum(StackObj calcStack) {
		Double largestValue = Double.MIN_VALUE;

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Loop through the stack and look for the largest value
		for (int i = 0; i < calcStack.size(); i++) {
			if (calcStack.get(i) > largestValue)
				largestValue = calcStack.get(i);
		}

		// Add lowest value to the stack
		Output.printColorln(Ansi.Color.CYAN, "Maximum value added to stack: " + largestValue);
		calcStack.push(largestValue);
	}

	/**
	 * cmdMinimum(): Add minimum value in the stack to the top of the stack
	 */
	public static void cmdMinimum(StackObj calcStack) {
		Double lowestValue = Double.MAX_VALUE;

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Loop through the stack and look for the lowest value
		for (int i = 0; i < calcStack.size(); i++) {
			if (calcStack.get(i) < lowestValue)
				lowestValue = calcStack.get(i);
		}

		// Add lowest value to the stack
		Output.printColorln(Ansi.Color.CYAN, "Minimum value added to stack: " + lowestValue);
		calcStack.push(lowestValue);
	}

	/**
	 * cmdMod(): Divide and place the modulus onto the stack
	 */
	public static void cmdModulus(StackObj calcStack) {
		// Ensure we have at least 2 items on the stack
		if (calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least two items on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Perform the division and push the result onto the stack
		Double b = calcStack.pop();
		Double a = calcStack.pop();
		Output.debugPrint("Modulus: " + a + " % " + b + " = " + (a % b));
		calcStack.push(a % b);
	}

	/**
	 * cmdOperand(): An operand was entered such as + or -
	 * 
	 */
	public static void cmdOperand(StackObj calcStack, String op) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrint("CalcStack has " + calcStack.size() + " elements");
		Output.debugPrint("Operand entered: '" + op + "'");

		// Verify stack contains at least two elements
		if (calcStack.size() >= 2) {
			Math.Parse(op, calcStack);
		} else {
			Output.printColorln(Ansi.Color.RED, "Two numbers are required for this operation");
		}

	}

	/**
	 * cmdPercent(): Turn a number into a percent by dividing by 100
	 * 
	 */
	public static void cmdPercent(StackObj calcStack) {
		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrint("Create a percent by dividing by 100");
		calcStack.push(calcStack.pop() / 100);
	}

	/**
	 * cmdRandom(): Produce a random number between the Low and High values provided. If there are no parameters, produce
	 * the number between 1 and 100.
	 * 
	 * @param param
	 */
	public static void cmdRandom(StackObj calcStack, String param) {
		int low = 1;
		int high = 100;
		int randomNumber = 0;

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
		Output.debugPrint("Generating Random number between " + low + " and " + high + "(inclusive of both)");

		// Verify that the low number <= the high number
		if (low > high) {
			Output.printColorln(Ansi.Color.RED, "ERROR: the first number much be less than or equal to the high number");
			return;
		}

		// Generate the random number. Rand function will generate 0-9 for random(10)
		// so add 1 to the high so we include the high number in the results
		randomNumber = new java.util.Random().nextInt((high + 1) - low) + low;

		// Add result to the calculator stack
		calcStack.push((double) randomNumber);
	}

	/**
	 * cmdRound(): Round to the provided decimal place. If none is provided round to the nearest integer
	 * 
	 * Reference: https://www.baeldung.com/java-round-decimal-number
	 * 
	 * @param arg
	 */
	public static void cmdRound(StackObj calcStack, String arg) {
		int decimalPlaces = 0;
		BigDecimal bd;

		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least one item on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Convert the arg to the number of decimal places
		try {
			decimalPlaces = Integer.parseInt(arg);
			// Ensure a negative number is not provided for decimal points to round
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
		bd = new BigDecimal(calcStack.pop());
		bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
		calcStack.push(bd.doubleValue());
	}

	/**
	 * cmdSwapElements(): Swap the provided elements within the stack
	 * 
	 * @param param
	 */
	public static void cmdSwapElements(StackObj calcStack, String param) {
		// Default is to swap last two stack items
		int item1 = 1;
		int item2 = 2;

		// Verify at least two elements exist
		if (calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "Error: There must be at least 2 elements on the stack to swap");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
		if (item1 < 1 || item1 > calcStack.size() || item2 < 1 || item2 > calcStack.size()) {
			Output.printColorln(Ansi.Color.RED, "Invalid element entered.  Must be between 1 and " + calcStack.size());
		} else {
			Output.debugPrint("Swapping line" + item1 + " and line" + item2 + " stack items");
			StackOperations.StackSwapItems(calcStack, (item1 - 1), (item2) - 1);
		}
	}

	/**
	 * cmdSqrt(): Take the square root of the number at the top of the stack
	 * 
	 */
	public static void cmdSqrt(StackObj calcStack) {
		// Verify we have an item on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrint("Taking the square root of the last stack item");
		calcStack.push(java.lang.Math.sqrt(calcStack.pop()));
	}

	/**
	 * cmdStdDeviation(): Calculate the Standard Deviation of the stack items
	 * 
	 * Reference: https://www.mathsisfun.com/data/standard-deviation-formulas.html
	 * 
	 * @param arg
	 */
	public static void cmdStdDeviation(StackObj calcStack, String arg) {
		// Ensure we have enough numbers on the stack
		if (calcStack.size() < 2) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  Standard Deviation requires at least two items on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

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
		Double mean1 = Math.Mean(calcStack);
		Output.debugPrint("Inital mean of the numbers: " + mean1);

		// Step2: For each number: subtract the mean from the number and square the result
		Double[] stdArray = new Double[calcStack.size()];
		for (int i = 0; i < calcStack.size(); i++) {
			stdArray[i] = java.lang.Math.pow((calcStack.get(i) - mean1), 2);
		}

		// Step3: Work out the mean of those squared differences
		Double mean2 = Math.Mean(stdArray);
		Output.debugPrint("Secondary mean of (number-mean)^2: " + mean2);

		if (keepFlag == false)
			calcStack.clear();

		// Step4: Take the square root of that result and push onto the stack
		Double result = java.lang.Math.sqrt(mean2);
		calcStack.push(result);
	}

	/**
	 * cmdUndo(): Undo last change be restoring the last stack from the undo stack
	 * 
	 */
	public static void cmdUndo(StackObj calcStack, String arg) {
		Output.debugPrint("Undoing command");
		int lineNum = 0;	// Undo line number NOT the position on the stack. That is one less.

		// Determine if a line number was provided
		try {
			lineNum = Integer.parseInt(arg);

			// Ensure number provided as > 0 and less than the size of the undo stack
			if (lineNum <= 0 || lineNum > calcStack.undoSize()) {
				Output.printColorln(Ansi.Color.RED, "An invalid undo line number entered: '" + arg + "'");
				return;
			}
		} catch (NumberFormatException ex) {
			if (arg.isEmpty()) {
				// No number was provided, use the top stack item
				lineNum = calcStack.undoSize();
			} else {
				Output.printColorln(Ansi.Color.RED, "An invalid undo line number entered: '" + arg + "'");
				return;
			}
		}

		Output.debugPrint("  - Restoring back to line number: " + lineNum);

		if (calcStack.undoSize() >= 1) {
			// Save current calcStack to the undoStack
			calcStack.saveUndo();

			// Clear the stack items after lineNum
			for (int i = (calcStack.undoSize() - 1); i >= lineNum - 1; i--) {
				Output.debugPrint("  - Removing later undo stack at line " + (i + 1) + " / position: " + i + ":  " + calcStack.undoGet(i));
				calcStack.undoRemove(i);
			}
		} else {
			Output.printColorln(Ansi.Color.RED, "Error: Already at oldest change");
		}
	}

}
