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
		assertEquals(26.220472440944885, stk.peek());
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
		assertEquals(247.57506327602871, stk.peek());
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
		assertEquals(3.3091442617812485, stk.peek());
		assertEquals(1, stk.size());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackConversions#cmdFraction(org.fross.rpncalc.StackObj, java.lang.String)}.
	 */
	@Test
	void testCmdFraction() {
		StackObj stk = new StackObj();

		stk.push(71.046875);
		String[] result = StackConversions.cmdFraction(stk, "");
		assertEquals("71 3/64", result[3]);
		assertEquals(71.046875, stk.peek());

	}

}
