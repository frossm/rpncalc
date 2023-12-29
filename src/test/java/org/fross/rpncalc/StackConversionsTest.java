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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class StackConversionsTest {
	/**
	 * Test converting from a percent to a number
	 * 
	 * Example: 34.5% -> .345
	 */
	@Test
	void testCmdFromPercent() {
		StackObj stk = new StackObj();

		stk.push("2.34");
		StackConversions.cmdFromPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("0.02340", stk.pop().toString());

		stk.push(-44.9873);
		StackConversions.cmdFromPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("-0.44987", stk.pop().toString());

		stk.push(-154.321E10);
		StackConversions.cmdFromPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("-15432100000.00000", stk.pop().toString());

		stk.push(1.1E6);
		StackConversions.cmdFromPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("11000.00000", stk.pop().toString());
	}

	/**
	 * Test converting from a number to a percent
	 * 
	 * Example: .345 -> 34.5%
	 */
	@Test
	void testCmdToPercent() {
		StackObj stk = new StackObj();

		stk.push(71.2345);
		StackConversions.cmdToPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(7123.45, stk.pop().doubleValue());

		stk.push(-44.987);
		StackConversions.cmdToPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-4498.7, stk.pop().doubleValue());

		stk.push(-154.321E10);
		StackConversions.cmdToPercent(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("-154321000000000.00000", stk.pop().toString());

		stk.push(1.1E6);
		StackConversions.cmdToPercent(stk);
		assertEquals("110000000.0", stk.pop().toString());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdConvertIN2MM(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdConvertIN2MM() {
		StackObj stk = new StackObj();

		stk.push(31.6);
		StackConversions.cmdConvertIN2MM(stk);
		assertEquals(802.64, stk.peek().doubleValue());
		assertEquals(1, stk.size());

		stk.push(1.234e12);
		StackConversions.cmdConvertIN2MM(stk);
		assertEquals("3.13436E+13", stk.peek().toString());
		assertEquals(2, stk.size());

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
		assertEquals(26.2205, stk.peek().doubleValue());
		assertEquals(1, stk.size());

		stk.push(1.234e12);
		StackConversions.cmdConvertMM2IN(stk);
		StackCommands.cmdRound(stk, "10");
		assertEquals("48582677165.3543307087", stk.peek().toEngineeringString());
		assertEquals(2, stk.size());
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
		assertEquals(247.5751, stk.peek().doubleValue());
		assertEquals(1, stk.size());

		stk.push(1.234e12);
		StackConversions.cmdRad2Deg(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("70702991919143.58288", stk.peek().toEngineeringString());
		assertEquals(2, stk.size());
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
		assertEquals(3.3091, stk.peek().doubleValue());
		assertEquals(1, stk.size());

		stk.push(1.234e12);
		StackConversions.cmdDeg2Rad(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals("21537362969.61003", stk.peek().toEngineeringString());
		assertEquals(2, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdFraction(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdFraction() {
		StackObj stk = new StackObj();

		// Test #1
		stk.clear();
		stk.push(71.046875);
		assertEquals(71.046875, stk.peek().doubleValue());
		// 64
		String[] result = StackConversions.cmdFraction(stk, "64");
		assertEquals("71 3/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("71 1/16", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("71 1/16", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("71", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("71", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("71", result[3]);

		// Test #2
		stk.clear();
		stk.push(4.99);
		assertEquals(4.99, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("4 63/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("5", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("5", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("5", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("5", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("5", result[3]);

		// Test #3
		stk.clear();
		stk.push(23.212121);
		assertEquals(23.212121, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("23 7/32", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("23 7/32", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("23 3/16", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("23 1/4", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("23 1/4", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("23", result[3]);

		// Test #3
		stk.clear();
		stk.push(71.7272);
		assertEquals(71.7272, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("71 47/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("71 23/32", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("71 3/4", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("71 3/4", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("71 3/4", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("71 1/2", result[3]);

		// Test with negative numbers
		// Test #-1
		stk.clear();
		stk.push(-71.046875);
		assertEquals(-71.046875, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("-71 3/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("-71 1/16", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("-71 1/16", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-71", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("-71", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-71", result[3]);

		// Test #-2
		stk.clear();
		stk.push(-4.99);
		assertEquals(-4.99, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("-4 63/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("-5", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("-5", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-5", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("-5", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-5", result[3]);

		// Test #3
		stk.clear();
		stk.push(-23.212121);
		assertEquals(-23.212121, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("-23 7/32", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("-23 7/32", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("-23 3/16", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-23 1/4", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("-23 1/4", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-23", result[3]);

		// Test #3
		stk.clear();
		stk.push(-71.7272);
		assertEquals(-71.7272, stk.peek().doubleValue());
		// 64
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("-71 47/64", result[3]);
		// 32
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("-71 23/32", result[3]);
		// 16
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("-71 3/4", result[3]);
		// 8
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-71 3/4", result[3]);
		// 4
		result = StackConversions.cmdFraction(stk, "4");
		assertEquals("-71 3/4", result[3]);
		// 2
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-71 1/2", result[3]);

		// Misc
		stk.clear();
		stk.push(71.1);
		result = StackConversions.cmdFraction(stk, "16");
		assertEquals("71 1/8", result[3]);
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("71", result[3]);

		stk.clear();
		stk.push(14.77);
		result = StackConversions.cmdFraction(stk, "32");
		assertEquals("14 25/32", result[3]);

		// Test negative numbers with different bases
		stk.clear();
		stk.push(-123.456);
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-123 1/2", result[3]);

		stk.push(-123.456);
		result = StackConversions.cmdFraction(stk, "");
		assertEquals("-123 29/64", result[3]);

		// -41 33/34
		stk.push(-41.970588235);
		result = StackConversions.cmdFraction(stk, "2");
		assertEquals("-42", result[3]);

		// -12.123123123123123
		stk.push(-12.123123123123123);
		result = StackConversions.cmdFraction(stk, "64");
		assertEquals("-12 1/8", result[3]);
		result = StackConversions.cmdFraction(stk, "8");
		assertEquals("-12 1/8", result[3]);

		stk.clear();
		stk.push(1.2e12 / 2.2e14);
		assertEquals("0.005454545454545455", stk.peek().toString());
		result = StackConversions.cmdFraction(stk, "1024");
		assertEquals("0 3/512", result[3]);

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
		assertEquals(43.54807, stk.pop().doubleValue());

		stk.push(-22.2);
		StackConversions.cmdGram2Oz(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.78308, stk.pop().doubleValue());

		stk.push(44.55e12);
		StackConversions.cmdGram2Oz(stk);
		StackCommands.cmdRound(stk, "2");
		assertEquals("1571455004853.81", stk.pop().toEngineeringString());
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
		assertEquals(349.97486, stk.pop().doubleValue());

		stk.push(-0.0221);
		StackConversions.cmdOz2Gram(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.62652, stk.pop().doubleValue());

		stk.push(44.55e12);
		StackConversions.cmdOz2Gram(stk);
		StackCommands.cmdRound(stk, "2");
		assertEquals("1262971255218750.00", stk.pop().toEngineeringString());
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
		assertEquals(271.87627, stk.pop().doubleValue());

		stk.push(-0.369);
		StackConversions.cmdKg2Lbs(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.81351, stk.pop().doubleValue());

		stk.push(4.321e18);
		StackConversions.cmdKg2Lbs(stk);
		StackCommands.cmdRound(stk, "2");
		assertEquals("9526174348797800000.00", stk.pop().toEngineeringString());

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
		assertEquals(207.13477, stk.pop().doubleValue());

		stk.push(-0.987654);
		StackConversions.cmdLbs2Kg(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(-0.44799, stk.pop().doubleValue());

		stk.push(4.321e18);
		StackConversions.cmdLbs2Kg(stk);
		StackCommands.cmdRound(stk, "2");
		assertEquals("1959972630770000000.00", stk.pop().toEngineeringString());
	}

}
