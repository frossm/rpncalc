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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

		assertEquals(5.80245, Math.Add(stk).peek());
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

		assertEquals(-3.33333, Math.Subtract(stk).peek());
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

		assertEquals(5.639334278400001, Math.Multiply(stk).peek());
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

		assertEquals(0.270269205256694, Math.Divide(stk).peek());
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

		assertEquals(2.6182895397375354, Math.Power(stk).peek());
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
		assertEquals(true, Math.isNumeric("-123.4567"));
		assertEquals(true, Math.isNumeric("1"));
		assertEquals(true, Math.isNumeric(".000002"));
		assertEquals(false, Math.isNumeric("-123.4a567"));
		assertEquals(false, Math.isNumeric("Nope"));
		assertEquals(false, Math.isNumeric("1x22"));
		assertEquals(false, Math.isNumeric("--123"));
		assertEquals(false, Math.isNumeric(" "));
		assertEquals(false, Math.isNumeric(""));

	}

	/**
	 * Test method for {@link org.fross.rpncalc.Math#Mean(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testMeanStackObj() {
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
	void testMeanDoubleArray() {
		Double[] arry = { 1.23456, 4.56789, 10.234, 12.1354, -1.23 };

		assertEquals(5.38837, Math.Mean(arry));
	}

}
