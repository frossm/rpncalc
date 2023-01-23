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
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class StackCommandsTest {

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdAddAll(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdAddAll() {
		StackObj stk = new StackObj();
		stk.push(1.23);
		stk.push(2.34);
		stk.push(3.45);
		stk.push(4.56);
		stk.push(5.67);

		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(6, stk.size());
		assertEquals(17.25, stk.peek());

		StackCommands.cmdAddAll(stk, "");
		StackCommands.cmdRound(stk, "6");
		assertEquals(34.5, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdAbsoluteValue(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdAbsoluteValue() {
		StackObj stk = new StackObj();

		stk.push(123.456);
		StackCommands.cmdAbsoluteValue(stk);
		assertEquals(123.456, stk.peek());
		assertEquals(1, stk.size());

		stk.push(-789.123);
		StackCommands.cmdAbsoluteValue(stk);
		assertEquals(789.123, stk.peek());
		assertEquals(2, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdAverage(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdAverage() {
		StackObj stk = new StackObj();

		stk.push(1.23);
		stk.push(2.34);
		stk.push(3.45);
		stk.push(4.56);
		stk.push(5.67);

		StackCommands.cmdAverage(stk, "keep");
		assertEquals(3.45, stk.pop());
		assertEquals(5, stk.size());

		StackCommands.cmdAverage(stk, "");
		assertEquals(3.45, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdCopy(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdCopy() {
		StackObj stk = new StackObj();

		stk.push(1.23);
		stk.push(2.34);
		stk.push(3.45);
		stk.push(4.56);
		stk.push(5.67);
		stk.push(-1000.001);

		// Copy line1
		StackCommands.cmdCopy(stk, "");
		assertEquals(7, stk.size());
		assertEquals(-1000.001, stk.get(stk.size() - 1));
		assertEquals(-1000.001, stk.get(stk.size() - 2));
		assertEquals(5.67, stk.get(stk.size() - 3));
		assertEquals(1.23, stk.get(0));

		// Copy line4
		StackCommands.cmdCopy(stk, "4");
		assertEquals(8, stk.size());
		assertEquals(4.56, stk.pop());
		assertEquals(-1000.001, stk.pop());
		assertEquals(-1000.001, stk.pop());
		assertEquals(5.67, stk.pop());
		assertEquals(4.56, stk.pop());
		assertEquals(3.45, stk.pop());
		assertEquals(1.23, stk.get(0));
	}

	/**
	 * Test delete command no parameter. Should delete line1
	 * 
	 */
	@Test
	void testCmdDeleteNoParm() {
		StackObj stk = new StackObj();

		stk.push(5.0);
		stk.push(4.0);
		stk.push(3.0);
		stk.push(2.0);
		stk.push(1.0);

		// Delete line1 by not providing a line number
		StackCommands.cmdDelete(stk, "");
		assertEquals(4, stk.size());
		assertEquals(2, stk.peek());
		assertEquals(5, stk.get(0));
		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(5, stk.size());
		assertEquals(14, stk.peek());

		// Delete all items one at a time
		StackCommands.cmdDelete(stk, "");
		assertEquals(4, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(3, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(2, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(1, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(0, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(0, stk.size());
	}

	/**
	 * Test delete with a single line number given
	 * 
	 */
	@Test
	void testCmdDelete1Line() {
		StackObj stk = new StackObj();

		stk.push(5.0);
		stk.push(4.0);
		stk.push(3.0);
		stk.push(2.0);
		stk.push(1.0);

		// Delete line 3
		StackCommands.cmdDelete(stk, "3");
		assertEquals(4, stk.size());
		assertEquals(1, stk.peek());
		assertEquals(5, stk.get(0));
		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(5, stk.size());
		assertEquals(12, stk.peek());

		// Delete Line 5
		StackCommands.cmdDelete(stk, "5");
		assertEquals(4, stk.size());
		assertEquals(12, stk.peek());
		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(5, stk.size());
		assertEquals(19, stk.peek());
	}

	/**
	 * Test delete by providing a range of lines to delete
	 * 
	 */
	@Test
	void testCmdDeleteRange() {
		StackObj stk = new StackObj();

		stk.push(5.0);
		stk.push(4.0);
		stk.push(3.0);
		stk.push(2.0);
		stk.push(1.0);

		StackCommands.cmdDelete(stk, "2-4");
		assertEquals(2, stk.size());
		assertEquals(1, stk.peek());
		assertEquals(5, stk.get(0));
		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(3, stk.size());
		assertEquals(6, stk.peek());

		// Undo the above changes and reset for next test
		StackCommands.cmdUndo(stk, "1");

		StackCommands.cmdDelete(stk, "5 - 2");
		assertEquals(1, stk.size());
		assertEquals(1, stk.peek());
		assertEquals(1, stk.get(0));
		StackCommands.cmdAddAll(stk, "keep");
		assertEquals(1, stk.size());
		assertEquals(1, stk.peek());

		// Delete last line
		StackCommands.cmdDelete(stk, "1-1");
		assertEquals(0, stk.size());
		StackCommands.cmdDelete(stk, "");
		assertEquals(0, stk.size());
	}

	/**
	 * Test delete by sending illegal parameters. The size shouldn't change
	 */
	@Test
	void testCmdDeleteParmError() {
		StackObj stk = new StackObj();

		stk.push(5.0);
		stk.push(4.0);
		stk.push(3.0);
		stk.push(2.0);
		stk.push(1.0);

		// Send a bunch of incorrect parameters
		StackCommands.cmdDelete(stk, "x");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "5x");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "5-52a");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "-1");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "4-1000");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "200-1");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "keep");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "-1 * 5");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "1-6");
		assertEquals(5, stk.size());
		StackCommands.cmdDelete(stk, "5-0");
		assertEquals(5, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdDice(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdDice() {
		StackObj stk = new StackObj();

		// Roll some bones and the stack counts should equal the number of rolls
		StackCommands.cmdDice(stk, "10D4");
		assertEquals(10, stk.size());

		StackCommands.cmdDice(stk, "6d10");
		assertEquals(16, stk.size());

		StackCommands.cmdDice(stk, "500d100");
		assertEquals(516, stk.size());

		// Illegal parameter syntax
		StackCommands.cmdDice(stk, "d1");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "11a12");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "10-2");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "ddd");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "10d");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "6");
		assertEquals(516, stk.size());
		StackCommands.cmdDice(stk, "10d0");
		assertEquals(516, stk.size());
	}

	/**
	 * Test the 'dice' command and ensure none of the rolls are within the range
	 * 
	 */
	@Test
	void testDieRollsAreWithinRange() {
		StackObj stk = new StackObj();

		// Roll 5000 d10 dice
		StackCommands.cmdDice(stk, "5000d10");
		for (int i = 0; i < stk.size(); i++) {
			if (stk.get(i) < 1 || stk.get(i) > 10) {
				fail();
			}
		}
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdFlipSign(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdFlipSign() {
		StackObj stk = new StackObj();

		stk.push(123.0);

		StackCommands.cmdFlipSign(stk);
		assertEquals(-123.0, stk.peek());
		assertEquals(1, stk.size());

		StackCommands.cmdFlipSign(stk);
		assertEquals(123.0, stk.peek());
		assertEquals(1, stk.size());

		StackCommands.cmdFlipSign(stk);
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdInteger(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdInteger() {
		StackObj stk = new StackObj();

		stk.push(123.456);
		StackCommands.cmdInteger(stk);
		assertEquals(123.0, stk.pop());

		stk.push(0.999);
		StackCommands.cmdInteger(stk);
		assertEquals(0, stk.pop());

		stk.push(101.0);
		StackCommands.cmdInteger(stk);
		assertEquals(101, stk.pop());
	}

	/**
	 * Testing the linear regression results
	 * 
	 */
	@Test
	void testCmdLinearRegression() {
		StackObj stk = new StackObj();

		// Test #1
		stk.push(11.00);
		stk.push(9.00);
		stk.push(22.00);
		stk.push(15.00);
		stk.push(20.00);
		stk.push(10.00);
		stk.push(35.00);
		StackCommands.cmdLinearRegression(stk);
		StackCommands.cmdRound(stk, "4");
		assertEquals(8, stk.size());
		assertEquals(27.7143, stk.pop());

		// Test #2
		stk.clear();
		stk.push(-5.67);
		stk.push(0.0);
		stk.push(3.45);
		stk.push(9.01);
		stk.push(12.0);
		stk.push(15.0);
		stk.push(6.99);
		stk.push(11.11);
		stk.push(22.22);
		stk.push(3.0);
		StackCommands.cmdLinearRegression(stk);
		StackCommands.cmdRound(stk, "6");
		assertEquals(11, stk.size());
		assertEquals(16.671333, stk.pop());
		
		// Test #3
		stk.clear();
		stk.push(29.11);
		stk.push(11.22);
		stk.push(-18.33);
		stk.push(50.44);
		stk.push(-44.55);
		stk.push(49.66);
		stk.push(5.77);
		stk.push(-34.88);
		stk.push(40.99);
		stk.push(3.00);
		StackCommands.cmdLinearRegression(stk);
		StackCommands.cmdRound(stk, "6");
		assertEquals(11, stk.size());
		assertEquals(4.271333, stk.pop());

	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdLog(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdLog() {
		StackObj stk = new StackObj();

		stk.push(123.456);
		StackCommands.cmdLog(stk);
		assertEquals(4.815884817283264, stk.pop());

		stk.push(123.456);
		StackCommands.cmdLog(stk);
		assertEquals(4.815884817283264, stk.pop());

		stk.push(12332.12333);
		StackCommands.cmdLog(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(9.41996, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdLog10(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdLog10() {
		StackObj stk = new StackObj();

		stk.push(9.41996);
		StackCommands.cmdLog10(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(.97405, stk.pop());

		stk.push(1588.963);
		StackCommands.cmdLog10(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(3.20111, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdMaximum(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdMaximum() {
		StackObj stk = new StackObj();

		stk.push(4.56);
		stk.push(1.23);
		stk.push(5.67);
		stk.push(2.34);
		stk.push(4.56);
		stk.push(3.45);

		StackCommands.cmdMaximum(stk);
		assertEquals(7, stk.size());
		assertEquals(5.67, stk.peek());

		stk.push(5.68);
		StackCommands.cmdMaximum(stk);
		assertEquals(9, stk.size());
		assertEquals(5.68, stk.peek());
		assertEquals(5.68, stk.get(stk.size() - 1));
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdMinimum(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdMinimum() {
		StackObj stk = new StackObj();

		stk.push(4.56);
		stk.push(1.23);
		stk.push(5.67);
		stk.push(2.34);
		stk.push(4.56);
		stk.push(3.45);

		StackCommands.cmdMinimum(stk);
		assertEquals(7, stk.size());
		assertEquals(1.23, stk.peek());

		stk.push(1.22);
		StackCommands.cmdMinimum(stk);
		assertEquals(9, stk.size());
		assertEquals(1.22, stk.peek());
		assertEquals(1.22, stk.get(stk.size() - 1));
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdModulus(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdModulus() {
		StackObj stk = new StackObj();

		stk.push(5.67);
		stk.push(2.34);

		StackCommands.cmdModulus(stk);
		StackCommands.cmdRound(stk, "2");
		assertEquals(0.99, stk.pop());

		stk.push(88.0);
		stk.push(5.0);
		StackCommands.cmdModulus(stk);
		assertEquals(3.0, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdPercent(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdPercent() {
		StackObj stk = new StackObj();

		stk.push(2.34);
		StackCommands.cmdPercent(stk);
		StackCommands.cmdRound(stk, "4");
		assertEquals(.0234, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdRandom(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdRandom() {
		StackObj stk = new StackObj();

		// Generate 500 numbers between 1 and 10 inclusive
		for (int i = 0; i < 500; i++) {
			StackCommands.cmdRandom(stk, "1 10");
		}

		// Ensure there are 500 numbers and they are all in the correct range
		assertEquals(500, stk.size());
		for (int i = 0; i < 500; i++) {
			if (stk.get(i) < 1 || stk.get(i) > 100) {
				fail();
			}
		}
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdRound(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdRound() {
		StackObj stk = new StackObj();

		stk.push(2.34567);

		StackCommands.cmdRound(stk, "");
		assertEquals(2, stk.pop());

		stk.push(2.34567);
		StackCommands.cmdRound(stk, "2");

		assertEquals(2.35, stk.pop());

		stk.push(2.34567);
		StackCommands.cmdRound(stk, "4");
		assertEquals(2.3457, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdSwapElements(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdSwapElements() {
		StackObj stk = new StackObj();

		stk.push(1.0);
		stk.push(2.0);
		stk.push(3.0);
		stk.push(4.0);
		stk.push(5.0);

		// Swap line1 and line2
		StackCommands.cmdSwapElements(stk, "");
		assertEquals(4, stk.pop());

		// Swap line3 and line3
		StackCommands.cmdSwapElements(stk, "3 2");
		assertEquals(5, stk.pop());
		assertEquals(2, stk.pop());
		assertEquals(3, stk.pop());
		assertEquals(1, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdSqrt(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdSqrt() {
		StackObj stk = new StackObj();

		stk.push(25.0);
		stk.push(12.5);

		StackCommands.cmdSqrt(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(3.53553, stk.pop());

		StackCommands.cmdSqrt(stk);
		assertEquals(5, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdStdDeviation(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdStdDeviation() {
		StackObj stk = new StackObj();

		stk.push(10.0);
		stk.push(12.0);
		stk.push(23.0);
		stk.push(23.0);
		stk.push(16.0);
		stk.push(23.0);
		stk.push(21.0);
		stk.push(16.0);

		StackCommands.cmdStdDeviation(stk, "keep");
		assertEquals(9, stk.size());
		assertEquals(4.898979485566356, stk.pop());

		StackCommands.cmdStdDeviation(stk, "");
		assertEquals(1, stk.size());
		assertEquals(4.898979485566356, stk.pop());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdUndo(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdUndo() {
		StackObj stk = new StackObj();
		Double[] testData = { 10.0, 12.0, 23.0, 23.0, 16.0, 23.0, 21.0, 16.0 };

		for (Double i : testData) {
			stk.saveUndo();
			stk.push(i);
		}

		StackCommands.cmdUndo(stk, "");
		assertEquals(7, stk.size());
		assertEquals(21, stk.peek());

		StackCommands.cmdUndo(stk, "3");
		assertEquals(2, stk.size());
		assertEquals(12, stk.peek());
	}

}
