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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class MathTest {
	/**
	 * Test method for {@link org.fross.rpncalc.Math#Add(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testAdd() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);

		assertEquals(5.80245, Math.Parse("+", stk).peek());
		assertEquals(1, stk.size());

		stk.push(-1.1);
		Math.Parse("+", stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(4.70245, stk.peek());
		assertEquals(1, stk.size());
	};

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Subtract(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testSubtract() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);

		assertEquals(-3.33333, Math.Parse("-", stk).peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Multiply(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testMultiply() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);

		Math.Parse("*", stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(5.63933, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Divide(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testDivide() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);

		Math.Parse("/", stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.27027, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Power(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testPower() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);

		Math.Parse("^", stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(2.61829, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#GreatestCommonDivisor(long, long)}.
	 */
	@Test
	void testGreatestCommonDivisor() {

		assertEquals(10, Math.GreatestCommonDivisor(90, 100));
		assertEquals(3, Math.GreatestCommonDivisor(123, 456));
		assertEquals(3, Math.GreatestCommonDivisor(123, 225));
		assertEquals(35, Math.GreatestCommonDivisor(35, 70));
		assertEquals(2, Math.GreatestCommonDivisor(36, 70));

	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#isNumeric(java.lang.String)}.
	 */
	@Test
	void testIsNumeric() {
		assertTrue(Math.isNumeric("432123434232334.325474458"));
		assertTrue(Math.isNumeric("-123.4567"));
		assertTrue(Math.isNumeric("1"));
		assertTrue(Math.isNumeric(".000002"));
		assertFalse(Math.isNumeric("-123.4a567"));
		assertFalse(Math.isNumeric("Nope"));
		assertFalse(Math.isNumeric("1x22"));
		assertFalse(Math.isNumeric("--123"));
		assertFalse(Math.isNumeric(" "));
		assertFalse(Math.isNumeric(""));
		assertFalse(Math.isNumeric("!"));
		assertFalse(Math.isNumeric("-"));

	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Mean(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testMeanIfStackObjIsProvided() {
		StackObj stk = new StackObj();

		// Add test data to stack
		stk.push(1.23456);
		stk.push(4.56789);
		stk.push(10.234);
		stk.push(12.1354);
		stk.push(-1.23);

		assertEquals(5.38837, Math.Mean(stk));
	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Mean(java.lang.Double[])}.
	 */
	@Test
	void testMeanIfDoubleArrayIsProvided() {
		Double[] arry = { 1.23456, 4.56789, 10.234, 12.1354, -1.23 };

		assertEquals(5.38837, Math.Mean(arry));
	}

}
