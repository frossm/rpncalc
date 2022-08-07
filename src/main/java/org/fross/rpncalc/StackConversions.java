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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackConversions {
	// Class Constants
	public static final int DEFAULT_DENOMINATOR = 64;  // Default Smallest Fraction Denominator

	/**
	 * cmdConvertMM(): Assumes Line1 is in inches and converts to millimeters
	 * 
	 */
	public static void cmdConvertIN2MM(StackObj calcStack) {
		// Verify at least one elements exists
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "Error: There must be at least 1 element on the stack to convert");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Pop off the last value, convert, and push it back
		calcStack.push(calcStack.pop() * 25.4);
	}

	/**
	 * cmdConvertIN(): Assumes Line1 is in millimeters and converts to inches
	 * 
	 */
	public static void cmdConvertMM2IN(StackObj calcStack) {
		// Verify at least one elements exists
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "Error: There must be at least 1 element on the stack to convert");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Pop off the last value, convert, and push it back
		calcStack.push(calcStack.pop() / 25.4);
	}

	/**
	 * cmdFraction(): Display the last stack item as a fraction with a minimum base of the provided number. For example, sending
	 * 64 would produce a fraction of 1/64th but will be reduced if possible.
	 * 
	 * @param param
	 */
	public static String[] cmdFraction(StackObj calcStack, String param) {
		String[] outputString = { "", "", "", "" };

		// Verify we have an item on the stack
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least one item on the stack");
			return outputString;
		}

		// The base to convert the fraction to. For example, 64 = 1/64th
		int denominator = DEFAULT_DENOMINATOR;

		// If a denominator is provided, use it instead of the default
		try {
			if (!param.isEmpty())
				denominator = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + param + "' is not a valid denominator");
			return outputString;
		}

		// Determine the integer portion of the number
		int integerPart = (int) java.lang.Math.floor(calcStack.peek());

		// Determine the fractional portion as an double
		double decimalPart = calcStack.peek() - integerPart;

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
		outputString[0] = "\n" + stackHeader + "-".repeat(Main.configProgramWidth - stackHeader.length());
		outputString[1] = " " + calcStack.peek() + " is approximately '" + integerPart + " " + numerator + "/" + denominator + "'";
		outputString[2] = "-".repeat(Main.configProgramWidth) + "\n";
		outputString[3] = integerPart + " " + numerator + "/" + denominator;

		return outputString;
	}

	/**
	 * cmdDegree(): Convert line1 from radians to degrees
	 * 
	 * Formula: degrees = radians * (180 / PI)
	 */
	public static void cmdRad2Deg(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Pull the value, convert and push back
		calcStack.push(calcStack.pop() * (180 / java.lang.Math.PI));
	}

	/**
	 * cmdRadian(): Convert line1 from degrees to radians.
	 * 
	 * Formula: radians = degrees (PI/180)
	 */
	public static void cmdDeg2Rad(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Pull the value, convert and push back
		calcStack.push(calcStack.pop() * (java.lang.Math.PI / 180));
	}

}
