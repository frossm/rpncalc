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

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class StackConversionsTest {

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdConvertIN2MM(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdConvertIN2MM() {
		StackObj stk = new StackObj();

		stk.push(31.6);
		StackConversions.cmdConvertIN2MM(stk);
		assertEquals(802.64, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdConvertMM2IN(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdConvertMM2IN() {
		StackObj stk = new StackObj();

		stk.push(666.0);
		StackConversions.cmdConvertMM2IN(stk);
		StackCommands.cmdRound(stk, "4");
		assertEquals(26.2205, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdRad2Deg(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdRad2Deg() {
		StackObj stk = new StackObj();

		stk.push(4.321);
		StackConversions.cmdRad2Deg(stk);
		StackCommands.cmdRound(stk, "4");
		assertEquals(247.5751, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdDeg2Rad(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdDeg2Rad() {
		StackObj stk = new StackObj();

		stk.push(189.6);
		StackConversions.cmdDeg2Rad(stk);
		StackCommands.cmdRound(stk, "4");
		assertEquals(3.3091, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdFraction(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdFraction() {
		StackObj stk = new StackObj();

		// Test positive numbers with different bases
		stk.push(71.046875);
		assertEquals(71.046875, stk.peek());

		String[] result = StackConversions.cmdFraction(stk, "");
		assertEquals("71 3/64", result[3]);

		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("71 1/16", result[3]);

		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("71 0/1", result[3]);

		// Test negative numbers with different bases
		stk.push(-123.456);
		assertEquals(-123.456, stk.peek());

		result = StackConversions.cmdFraction(stk, "");
		assertEquals("-123 29/64", result[3]);

		result = StackConversions.cmdFraction(stk, "44");
		assertEquals("-123 5/11", result[3]);

		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("-123 15/32", result[3]);

		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-123 1/2", result[3]);

	}

	/**
	 * Test Grams to Ounces conversion
	 */
	@Test
	void testGram2Oz() {
		StackObj stk = new StackObj();

		stk.push(1234.567);
		StackConversions.cmdGram2Oz(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(43.54812, stk.pop());

		stk.push(-22.2);
		StackConversions.cmdGram2Oz(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.78308, stk.pop());
	}

	/**
	 * Test Ounces to grams conversion
	 */
	@Test
	void testOz2Gram() {
		StackObj stk = new StackObj();

		stk.push(12.345);
		StackConversions.cmdOz2Gram(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(349.97486, stk.pop());

		stk.push(-0.0221);
		StackConversions.cmdOz2Gram(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.62652, stk.pop());
	}

	/**
	 * Test Kilograms to US pounds conversion
	 */
	@Test
	void testKg2Lbs() {
		StackObj stk = new StackObj();

		stk.push(123.321);
		StackConversions.cmdKg2Lbs(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(271.87627, stk.pop());

		stk.push(-0.369);
		StackConversions.cmdKg2Lbs(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.81351, stk.pop());
	}

	/**
	 * Test US Pounds to Kilograms conversion
	 */
	@Test
	void testLbs2Kg() {
		StackObj stk = new StackObj();

		stk.push(456.654);
		StackConversions.cmdLbs2Kg(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(207.13477, stk.pop());

		stk.push(-0.987654);
		StackConversions.cmdLbs2Kg(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.44799, stk.pop());
	}

}
