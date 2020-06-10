/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2020.  All rights reserved.
 *  
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE.txt file for additional details
 *           
 ******************************************************************************/
package org.fross.rpn;

import java.util.Stack;
import org.fusesource.jansi.Ansi;
import org.fross.library.Output;

/**
 * Math: The math class contains the methods to parse the operands entered and perform the math
 * tasks. It was done strictly so it will be easily to grow the list of functions at a later date.
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
	public static Stack<Double> Parse(String op, Stack<Double> stk) {
		Stack<Double> result = new Stack<Double>();

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
	public static Stack<Double> Add(Stack<Double> stk) {
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
	public static Stack<Double> Subtract(Stack<Double> stk) {
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
	public static Stack<Double> Multiply(Stack<Double> stk) {
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
	public static Stack<Double> Divide(Stack<Double> stk) {
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
	public static Stack<Double> Power(Stack<Double> stk) {
		Double power = stk.pop();
		Double base = stk.pop();
		Output.debugPrint("Base=" + base + "   Power=" + power);
		stk.push(java.lang.Math.pow(base, power));
		return stk;
	}

	/**
	 * SquareRoot(): Take the square root of the last stack item
	 * 
	 * @param stk
	 * @return
	 */
	public static Stack<Double> SquareRoot(Stack<Double> stk) {
		stk.push(java.lang.Math.sqrt(stk.pop()));
		return stk;
	}

	/**
	 * GreatestCommonDivisor(): Return the largest common number divisible into both numbers. Used in
	 * rpn for fraction reduction.
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

} // END MATH CLASS
