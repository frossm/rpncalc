/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2023 Michael Fross
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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

/**
 * Math: The math class contains the methods to parse the operands entered and perform the math tasks. It was done strictly
 * so it will be easily to grow the list of functions at a later date.
 * 
 * @author Michael Fross (michael@fross.org)
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
		BigDecimal b = stk.pop();
		BigDecimal a = stk.pop();
		BigDecimal result = a.add(b, stk.mc);

		Output.debugPrint("Adding: " + a.toString() + " + " + b.toString() + " = " + result.toString());
		stk.push(result);
		return stk;
	}

	/**
	 * Subtract(): Subtract the last item from the previous item on the provided stack
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Subtract(StackObj stk) {
		BigDecimal b = stk.pop();
		BigDecimal a = stk.pop();
		BigDecimal result = a.subtract(b, stk.mc);

		Output.debugPrint("Subtracting: " + a.toString() + " - " + b.toString() + " = " + result.toString());
		stk.push(result);
		return stk;
	}

	/**
	 * Multiply(): Multiply the last two items on the provided stack
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Multiply(StackObj stk) {
		BigDecimal b = stk.pop();
		BigDecimal a = stk.pop();
		BigDecimal result = a.multiply(b, stk.mc);

		Output.debugPrint("Multiplying: " + a.toString() + " * " + b.toString() + " = " + result.toString());
		stk.push(result);
		return stk;
	}

	/**
	 * Divide(): Divide the 2nd to the last stack item by the last
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Divide(StackObj stk) {
		// Ensure we don't divide by zero
		if (stk.peek().compareTo(BigDecimal.ZERO) == 0) {
			Output.printColorln(Ansi.Color.RED, "Dividing by zero is not allowed. You could wind up going back in time...");
			return stk;
		}

		BigDecimal b = stk.pop();
		BigDecimal a = stk.pop();
		BigDecimal result = BigDecimal.ZERO;

		try {
			result = a.divide(b, MathContext.DECIMAL128);
		} catch (ArithmeticException ex) {
			Output.printColorln(Ansi.Color.RED, "Error dividing " + a + " / " + b);
		} catch (NullPointerException ex) {
			Output.printColorln(Ansi.Color.RED, "Error dividing " + a + " / " + b);
		}

		Output.debugPrint("Dividing: " + a.toString() + " / " + b.toString() + " = " + result.toString());
		stk.push(result);
		return stk;
	}

	/**
	 * Power(): The second to the last item in the stack to the power of the last item
	 * 
	 * @param stk
	 * @return
	 */
	public static StackObj Power(StackObj stk) {
		BigDecimal power = stk.pop();
		BigDecimal base = stk.pop();
		BigDecimal result = base.pow(power.intValue(), stk.mc);

		Output.debugPrint(base.toString() + " ^ " + power.toString() + " = " + result.toString());

		// Warn user the decimal has been dropped
		// TODO: Should make this more international at some point
		if (power.toPlainString().contains(".")) {
			Output.printColorln(Ansi.Color.CYAN, "Warning: The decimal portion of '" + power.toString() + "' has been dropped for the calculation");
		}

		stk.push(result);
		return stk;
	}

	/**
	 * factorial(): Return the factorial of the provided integer
	 * 
	 * @param num
	 * @return
	 */
	public static BigDecimal Factorial(BigDecimal num) {
		BigDecimal result = BigDecimal.ONE;

		for (BigDecimal factor = new BigDecimal("2"); factor.compareTo(num) <= 0; factor = factor.add(BigDecimal.ONE)) {
			result = result.multiply(factor, MathContext.UNLIMITED);
		}

		return result;
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
	public static BigInteger GreatestCommonDivisor(BigInteger n1, BigInteger n2) {
		Output.debugPrint("Finding Greatest Common Divisor between: '" + n1.toString() + "' and '" + n2.toString() + "'");

		if (n2.compareTo(BigInteger.ZERO) == 0) {
			return n1;
		}
		return GreatestCommonDivisor(n2, n1.remainder(n2));
	}

	/**
	 * isNumeric(): Return true or false if provided string is a number
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			new BigDecimal(str);
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
	public static BigDecimal mean(StackObj stk) {
		BigDecimal totalCounter = BigDecimal.ZERO;
		int size = stk.size();

		// Add up the numbers in the stack
		for (int i = 0; i < size; i++) {
			totalCounter = totalCounter.add(stk.get(i), stk.mc);
		}

		// Return the average
		return (totalCounter.divide(new BigDecimal(String.valueOf(size)), MathContext.DECIMAL128));

	}

	/**
	 * mean(): Return the mean from the numbers in a stack of doubles or a double array
	 * 
	 * @param arry
	 * @return
	 */
	public static BigDecimal mean(Double[] arry) {
		StackObj stk = new StackObj();

		// Convert array into a stack then call Mean again
		for (int i = 0; i < arry.length; i++) {
			stk.push(arry[i]);
		}

		return (mean(stk));
	}

	/**
	 * mean(): Return the mean from the numbers in a stack of doubles or a double array
	 * 
	 * @param arry
	 * @return
	 */
	public static BigDecimal mean(BigDecimal[] arry) {
		StackObj stk = new StackObj();

		// Convert array into a stack then call Mean again
		for (int i = 0; i < arry.length; i++) {
			stk.push(arry[i]);
		}

		return (mean(stk));
	}

	/**
	 * median(): Return the median value of the stack items. If odd number of items, just return the middle item. If even, take
	 * the average of the two numbers closest to the middle
	 * 
	 */
	public static BigDecimal median(StackObj stk) {
		BigDecimal result = new BigDecimal("0.0");

		// Save current calcStack to the undoStack
		stk.saveUndo();

		// sort the stack
		stk.sort("descending");

		try {
			if (stk.size() % 2 == 0) {
				// Even number of items
				int lowerIndex = Integer.valueOf(stk.size() / 2);
				int upperIndex = Integer.valueOf(stk.size() / 2 + 1);

				Output.debugPrint("Median: UpperIndex=" + upperIndex + "  |  LowerIndex=" + lowerIndex);
				result = (stk.get(lowerIndex - 1).add(stk.get(upperIndex - 1), stk.mc)).divide(new BigDecimal("2"), MathContext.DECIMAL128);

			} else {
				// Odd number of items
				result = stk.get(stk.size() / 2);
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not calculate the median");
			return BigDecimal.ZERO;
		}

		// Undo the sort to get back to the original stack order
		StackCommands.cmdUndo(stk, String.valueOf(stk.undoSize()));

		return result;
	}

}