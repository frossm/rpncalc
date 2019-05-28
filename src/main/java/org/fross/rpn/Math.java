/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: GNU General Public License v3.
 *           http://www.gnu.org/licenses/gpl-3.0.html
 *           
 ******************************************************************************/
package org.fross.rpn;

import java.text.DecimalFormat;
import java.util.Stack;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

/**
 * Math: The math class contains the methods to parse the operands entered and
 * perform the math tasks. It was done strictly so it will be easily to grow the
 * list of functions at a later date.
 * 
 * @author michael.d.fross
 *
 */
public class Math {
	/**
	 * Comma Return a string with comma separators at the correct intervals.
	 * Supports decimal places and a negative sign.
	 * 
	 * @param num - Number in need of commas
	 * @return
	 */
	public static String Comma(Double num) {
		DecimalFormat df = null;

		try {
			df = new DecimalFormat("#,###,###.00#######");
		} catch (Exception Ex) {
			Output.printColorln(FColor.RED, "ERROR Adding Commas to numbers:\n" + Ex.getMessage());
		}

		return (String.valueOf(df.format(num)));
	}

	/**
	 * Parse Take an operand and a stack and call the right math function.
	 * 
	 * @param op  - Operand to process
	 * @param stk - Stack containing the list of Doubles to process
	 * @return
	 */
	public static Stack<Double> Parse(char op, Stack<Double> stk) {
		Stack<Double> result = new Stack<Double>();

		// Addition
		switch (op) {
		case '+':
			result = Add(stk);
			break;
		case '-':
			result = Subtract(stk);
			break;
		case '*':
			result = Multiply(stk);
			break;
		case '/':
			result = Divide(stk);
			break;
		case '^':
			result = Power(stk);
			break;
		case '%':
			result = Percent(stk);
			break;
		default:
			Output.printColorln(FColor.RED, "ERROR: Illegal Operand Sent to Math.Parse(): '" + op + "'");
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
		Debug.Print("Adding: " + a + " + " + b + " = " + (a + b));
		stk.push(a + b);
		return stk;
	}

	/**
	 * Subtract(): Subtract the last item from the previous item on the provided
	 * stack
	 * 
	 * @param stk
	 * @return
	 */
	public static Stack<Double> Subtract(Stack<Double> stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		Debug.Print("Subtracting: " + a + " - " + b + " = " + (a - b));
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
		Debug.Print("Multiplying: " + a + " * " + b + " = " + (a * b));
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
		Debug.Print("Dividing: " + a + " / " + b + " = " + (a / b));
		stk.push(a / b);
		return stk;
	}

	/**
	 * Power(): The second to the last item in the stack to the power of the last
	 * item
	 * 
	 * @param stk
	 * @return
	 */
	public static Stack<Double> Power(Stack<Double> stk) {
		Double power = stk.pop();
		Double base = stk.pop();
		Debug.Print("Base=" + base + "   Power=" + power);
		stk.push(java.lang.Math.pow(base, power));
		return stk;
	}

	/**
	 * Percent(): Simply multiply the last stack item by 0.01
	 * 
	 * @param stk
	 * @return
	 */
	public static Stack<Double> Percent(Stack<Double> stk) {
		stk.push(stk.pop() * 0.01);
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

}
