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

/**
 * Math: The math class contains the methods to parse the operands entered and perform the math tasks. It was done
 * strictly so it will be easily to grow the list of functions at a later date.
 * 
 * @author michael.d.fross
 *
 */
public class Math {
	/**
	 * Parse Take an operand and a stack and call the right math function.
	 * 
	 * @param op  - Operand to process
	 * @param stk - Stack containing the list of Doubles to process
	 * @return
	 */
	public static StackObj Parse(String op, StackObj stk) {
		StackObj result = new StackObj();

		// Addition
		switch (op) {
		case "+":
			result = Add(stk);
			break;

		case "-":
			result = Subtract(stk);
			break;

		case "*":
			result = Multiply(stk);
			break;

		case "/":
			result = Divide(stk);
			break;

		case "^":
			result = Power(stk);
			break;

		default:
			Output.printColorln(Ansi.Color.RED, "ERROR:  Illegal Operand Sent to Math.Parse(): '" + op + "'");
		}

		return result;
	}

	/**
	 * Add(): Add the last two numbers on the provided stack
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Add(StackObj stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		Output.debugPrint("Adding: " + a + " + " + b + " = " + (a + b));
		stk.push(a + b);
		return stk;
	}

	/**
	 * Subtract(): Subtract the last item from the previous item on the provided stack
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Subtract(StackObj stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		Output.debugPrint("Subtracting: " + a + " - " + b + " = " + (a - b));
		stk.push(a - b);
		return stk;
	}

	/**
	 * Multiply(): Multiply the last two items on the provided stack
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Multiply(StackObj stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		Output.debugPrint("Multiplying: " + a + " * " + b + " = " + (a * b));
		stk.push(a * b);
		return stk;
	}

	/**
	 * Divide(): Divide the 2nd to the last stack item by the last
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Divide(StackObj stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		Output.debugPrint("Dividing: " + a + " / " + b + " = " + (a / b));
		stk.push(a / b);
		return stk;
	}

	/**
	 * Power(): The second to the last item in the stack to the power of the last item
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Power(StackObj stk) {
		Double power = stk.pop();
		Double base = stk.pop();
		Output.debugPrint("Base=" + base + "   Power=" + power);
		stk.push(java.lang.Math.pow(base, power));
		return stk;
	}

	/**
	 * GreatestCommonDivisor(): Return the largest common number divisible into both numbers. Used in rpncalc for fraction
	 * reduction.
	 * 
	 * https://www.baeldung.com/java-greatest-common-divisor
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static long GreatestCommonDivisor(long n1, long n2) {
		// Output.debugPrint("Finding Greatest Common Divisor between: '" + n1 + "' and '" + n2 + "'");
		if (n2 == 0) {
			return n1;
		}
		return GreatestCommonDivisor(n2, n1 % n2);
	}

	/**
	 * isNumeric(): Return true or false if provided string is a number
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * mean(): Return the mean from the numbers in a stack of doubles or a double array
	 * 
	 * @param stk
	 * @return
	 */
	public static Double Mean(StackObj stk) {
		Double totalCounter = 0.0;
		int size = stk.size();

		// Add up the numbers in the stack
		for (int i = 0; i < size; i++) {
			totalCounter += stk.get(i);
		}

		// Return the average
		return (totalCounter / size);

	}

	/**
	 * mean(): Return the mean from the numbers in a stack of doubles or a double array
	 * 
	 * @param arry
	 * @return
	 */
	public static Double Mean(Double[] arry) {
		StackObj stk = new StackObj();

		// Convert array into a stack then call Mean again
		for (int i = 0; i < arry.length; i++) {
			stk.push(arry[i]);
		}

		return (Mean(stk));
	}

}