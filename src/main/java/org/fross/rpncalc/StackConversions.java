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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackConversions {
	// Class Constants
	public static final int DEFAULT_DENOMINATOR = 64;  // Default Smallest Fraction Denominator

	/**
	 * cmdConvertMM(): Assumes Line1 is in inches and converts to millimeters
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdConvert2MM() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Pop off the last value, convert, and push it back
		Main.calcStack.push(Main.calcStack.pop() * 25.4);
	}

	/**
	 * cmdConvertIN(): Assumes Line1 is in millimeters and converts to inches
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void cmdConvert2IN() {
		// Save to undo stack
		Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

		// Pop off the last value, convert, and push it back
		Main.calcStack.push(Main.calcStack.pop() / 25.4);
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

}
