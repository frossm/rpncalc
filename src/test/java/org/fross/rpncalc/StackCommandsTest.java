/**
 * 
 */
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
		assertEquals(34.49999999999999, stk.peek());
		StackCommands.cmdRound(stk, "1");
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
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdDelete(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdDelete() {
		StackObj stk = new StackObj();

		stk.push(1.23);
		stk.push(2.34);
		stk.push(3.45);
		stk.push(4.56);
		stk.push(5.67);

		// Delete top stack item (line1) by not providing a line number
		StackCommands.cmdDelete(stk, "");
		assertEquals(4, stk.size());
		assertEquals(4.56, stk.peek());
		assertEquals(1.23, stk.get(0));

		// Delete line 3
		StackCommands.cmdDelete(stk, "3");
		assertEquals(3, stk.size());
		assertEquals(4.56, stk.peek());
		assertEquals(4.56, stk.get(2));

		// Remove top of stack item again
		StackCommands.cmdDelete(stk, "");
		assertEquals(2, stk.size());
		assertEquals(3.45, stk.peek());
		assertEquals(1.23, stk.get(0));
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdDice(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdDice() {
		StackObj stk = new StackObj();

		StackCommands.cmdDice(stk, "10d4");
		assertEquals(10, stk.size());

		StackCommands.cmdDice(stk, "6d10");
		assertEquals(16, stk.size());

		StackCommands.cmdDice(stk, "4d10");
		assertEquals(20, stk.size());
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
		for (int i=0; i < 500; i++) {
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
