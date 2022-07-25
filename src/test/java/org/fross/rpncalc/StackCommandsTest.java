/**
 * 
 */
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdAbsoluteValue(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdAbsoluteValue() {
		StackObj stk = new StackObj();

		stk.push(123.0);
		StackCommands.cmdAbsoluteValue(stk);
		assertEquals(123.0, stk.pop());
		assertEquals(0, stk.size());

		stk.push(-123.0);
		StackCommands.cmdAbsoluteValue(stk);
		assertEquals(123.0, stk.pop());
		assertEquals(0, stk.size());
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
		assertEquals(3.45, stk.pop());
		assertEquals(0, stk.size());
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

		// Copy line1 item
		StackCommands.cmdCopy(stk, "");
		assertEquals(6, stk.size());
		assertEquals(5.67, stk.get(stk.size() - 1));
		assertEquals(5.67, stk.get(stk.size() - 2));

		// Copy line4 item
		StackCommands.cmdCopy(stk, "4");
		assertEquals(7, stk.size());
		assertEquals(3.45, stk.get(stk.size() - 1));
		assertEquals(5.67, stk.get(stk.size() - 2));
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

		// Delete top stack item (line1)
		StackCommands.cmdDelete(stk, "");
		assertEquals(4, stk.size());
		assertEquals(4.56, stk.peek());

		// Delete line 3
		StackCommands.cmdDelete(stk, "3");
		assertEquals(3, stk.size());
		assertEquals(4.56, stk.peek());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdDice(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdDice() {
		StackObj stk = new StackObj();

		StackCommands.cmdDice(stk, "6d10");
		assertEquals(6, stk.size());

		StackCommands.cmdDice(stk, "4d10");
		assertEquals(10, stk.size());
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

		StackCommands.cmdAbsoluteValue(stk);
		assertEquals(123.0, stk.peek());
		assertEquals(1, stk.size());
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
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdLog10(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdLog10() {
		StackObj stk = new StackObj();

		stk.push(123.456);
		StackCommands.cmdLog10(stk);
		assertEquals(2.0915122016277716, stk.pop());
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
		assertEquals(5.67, stk.peek());
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
		assertEquals(1.23, stk.peek());
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
		assertEquals(0.9900000000000002, stk.pop());

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

//	/**
//	 * Test method for {@link org.fross.rpncalc.StackCommands#cmdRandom(org.fross.rpncalc.StackObj, java.lang.String)}.
//	 */
//	@Test
//	void testCmdRandom() {
//		fail("Not yet implemented");
//	}

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
