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

public class Math {
	/**
	 * Comma Return a string with comma separators at the correct intervals.
	 * Supports decimal places and a negative sign.
	 * 
	 * @param num - Number in need of commas
	 * @return
	 */
	public static String Comma(Double num) {
		DecimalFormat myFormatter = null;

		try {
			myFormatter = new DecimalFormat("###,###.########");
		} catch (Exception Ex) {
			Output.Red("ERROR Adding Commas to numbers:\n" + Ex.getMessage());
		}

		return (myFormatter.format(num).toString());
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
		default:
			Output.Red("ERROR: Illegal Operand Sent to Math.Parse()");
		}

		return result;
	}

	// Process Add
	public static Stack<Double> Add(Stack<Double> stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		stk.push(a + b);
		return stk;
	}

	// Process Subtract
	public static Stack<Double> Subtract(Stack<Double> stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		stk.push(a - b);
		return stk;
	}

	// Process Multiply
	public static Stack<Double> Multiply(Stack<Double> stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		stk.push(a * b);
		return stk;
	}

	// Process Divide
	public static Stack<Double> Divide(Stack<Double> stk) {
		Double b = stk.pop();
		Double a = stk.pop();
		stk.push(a / b);
		return stk;
	}

	public static Stack<Double> Power(Stack<Double> stk) {
		Double power = stk.pop();
		Double base = stk.pop();
		Debug.Print("Base=" + base + "   Power=" + power);
		stk.push(java.lang.Math.pow(base, power));
		return stk;
	}

	public static Stack<Double> Percent(Stack<Double> stk) {
		stk.push(stk.pop() * 0.01);
		return stk;
	}
}
