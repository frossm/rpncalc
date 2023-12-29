/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2011-2024 Michael Fross
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
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackConversions {
	// Class Constants
	public static final BigInteger DEFAULT_FRACTION_DENOMINATOR = new BigInteger("64");  // Default Smallest Fraction Denominator

	/**
	 * cmdFromPercent(): Convert a percentage to a number by multiplying by .01
	 * 
	 * Example: 23.5% converts to .235
	 * 
	 */
	public static void cmdFromPercent(StackObj calcStack) {
		// Ensure we have enough numbers on the stack
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  This operation requires at least one item on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrintln("Create a percent by multiplying by 0.01");
		calcStack.push(calcStack.pop().multiply(new BigDecimal("0.01")));
	}

	/**
	 * cmdToPercent(): Convert a number to a percentage by multiplying by 100
	 * 
	 * Example: 0.715 converts to 71.5%
	 * 
	 */
	public static void cmdToPercent(StackObj calcStack) {
		// Ensure we have enough numbers on the stack
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  This operation requires at least one item on the stack");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		Output.debugPrintln("Create a percent by multiplying by 100");
		calcStack.push(calcStack.pop().multiply(new BigDecimal("100")));
	}

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
		calcStack.push(calcStack.pop().multiply(new BigDecimal("25.4")));
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
		calcStack.push(calcStack.pop().divide(new BigDecimal("25.4"), MathContext.DECIMAL128));
	}

	/**
	 * cmdFraction(): Display the last stack item as a fraction with a minimum base of the provided number. For example, sending
	 * 64 would produce a fraction of 1/64th but will be reduced if possible.
	 * 
	 * @param param
	 */
	public static String[] cmdFraction(StackObj calcStack, String param) {
		String[] outputString = { "", "", "", "" };
		boolean negativeNumber = false;

		// Verify we have an item on the stack
		if (calcStack.size() < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There must be at least one item on the stack");
			return outputString;
		}

		// Set the last stack item as the startingNumber
		BigDecimal startingNumber = calcStack.peek();

		// If starting number is negative, set a variable then remove the negative sign
		if (startingNumber.signum() < 0) {
			negativeNumber = true;
			startingNumber = startingNumber.abs();
		}

		// The base to convert the fraction to. For example, 64 = 1/64th
		BigInteger denominator = DEFAULT_FRACTION_DENOMINATOR;

		// If a denominator is provided, use it instead of the default
		try {
			if (!param.isEmpty())
				denominator = new BigInteger(param);
		} catch (NumberFormatException ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + param + "' is not a valid denominator");
			return outputString;
		}

		// Round the decimal number to the right denominator
		// If My_Dim != ((Floor((My_Dim * 16) + 0.5)) / 16) (For 1/16 as the denominator)
		// Reference: https://community.ptc.com/t5/3D-Part-Assembly-Design/Rounding-decimals-to-the-nearest-fraction/td-p/657420
		BigDecimal roundedNumber = BigDecimal.ZERO;
		try {
			roundedNumber = startingNumber.multiply(new BigDecimal(denominator)).add(new BigDecimal("0.5")).setScale(0, RoundingMode.FLOOR);
			;
			roundedNumber = roundedNumber.divide(new BigDecimal(denominator), MathContext.DECIMAL128);
		} catch (ArithmeticException ex) {
			Output.printColorln(Ansi.Color.RED, "Error calculating the rounded fraction\n" + ex.getMessage());
		}

		// Determine the integer portion of the number
		BigInteger integerPart = roundedNumber.toBigInteger();

		// Need to make the decimal a fraction my multiplying by the 10ths.
		// Determine the number of decimals places and tale 10^scale
		BigDecimal scaleMultiplier = BigDecimal.TEN.pow(roundedNumber.scale());

		// Numerator = decimal portion * scale so we have an integer
		// Decimal = the scale
		// Example: 0.25 becomes 25/100 | 0.123 becomes 123/1000
		BigDecimal numeratorTemp = roundedNumber.subtract(new BigDecimal(integerPart));
		numeratorTemp = numeratorTemp.multiply(scaleMultiplier);
		BigInteger numerator = numeratorTemp.toBigInteger();
		denominator = scaleMultiplier.toBigInteger();

		// Get the Greatest Common Divisor so we can simply the fraction
		BigInteger gcd = Math.GreatestCommonDivisor(numerator, denominator);
		Output.debugPrintln("Greatest Common Divisor for " + numerator.toString() + " and " + denominator.toString() + " is " + gcd.toString());

		// Simply the fraction
		numerator = numerator.divide(gcd);
		denominator = denominator.divide(gcd);

		// If starting number was negative, set it as negative again
		if (negativeNumber == true) {
			integerPart = integerPart.multiply(new BigInteger("-1"));
		}

		// Output the fractional display
		// If there is no fractional result, remove it so we don't see '0/1'
		String stackHeader = "-Fraction (Granularity: 1/" + (param) + ")";
		String result = integerPart + " " + ((numerator.compareTo(BigInteger.ZERO) == 0) ? "" : numerator + "/" + denominator);

		// Header Top
		outputString[0] = "\n" + stackHeader + "-".repeat(Main.configProgramWidth - stackHeader.length());
		// Results
		outputString[1] = " " + calcStack.peek().setScale(5, RoundingMode.HALF_UP) + " is approximately '" + result.trim() + "'";
		// Header Bottom
		outputString[2] = "-".repeat(Main.configProgramWidth) + "\n";
		// Results for testing
		outputString[3] = result.trim();

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
		Double conversionFactor = 180 / java.lang.Math.PI;
		calcStack.push(calcStack.pop().multiply(new BigDecimal(String.valueOf(conversionFactor))));
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
		Double conversionFactor = java.lang.Math.PI / 180;
		calcStack.push(calcStack.pop().multiply(new BigDecimal(String.valueOf(conversionFactor))));
	}

	/**
	 * cmdGram2Oz(): Convert line1 from grams to ounces
	 * 
	 * There are 1/28.349523125 ounces per gram
	 * 
	 * @param calcStack
	 */
	public static void cmdGram2Oz(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Make the conversion
		calcStack.push(calcStack.pop().divide(new BigDecimal("28.349523125"), MathContext.DECIMAL128));
	}

	/**
	 * cmdOz2Gram(): Convert line1 from grams to ounces
	 * 
	 * There are 28.349523125 grams per ounce
	 * 
	 * @param calcStack
	 */
	public static void cmdOz2Gram(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Make the conversion
		calcStack.push(calcStack.pop().multiply(new BigDecimal("28.349523125")));
	}

	/**
	 * cmdKg2Lb(): Convert line1 from kilograms to US pounds
	 * 
	 * There are 2.2046226218 pounds per kilogram
	 * 
	 * @param calcStack
	 */
	public static void cmdKg2Lbs(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Make the conversion
		calcStack.push(calcStack.pop().multiply(new BigDecimal("2.2046226218"), MathContext.UNLIMITED));
	}

	/**
	 * cmdLb2Kg(): Convert line1 from US pounds to kilograms
	 * 
	 * There are 0.45359237 kilograms per US pound
	 * 
	 * @param calcStack
	 */
	public static void cmdLbs2Kg(StackObj calcStack) {
		// Ensure we have something on the stack
		if (calcStack.isEmpty()) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  There are no items on the stack.");
			return;
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		// Make the conversion
		calcStack.push(calcStack.pop().multiply(new BigDecimal("0.45359237")));
	}
}
