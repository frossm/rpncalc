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
class StackTrigTest {

	/**
	 * Test method for
	 * {@link org.fross.rpncalc.StackTrig#cmdTrig(org.fross.rpncalc.StackObj, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testCmdTrig() {
		StackObj stk = new StackObj();

		// Testing Sine - Degrees
		stk.push(12.0);
		StackTrig.cmdTrig(stk, "sin", "");
		assertEquals(0.20791169081775934, stk.peek());
		assertEquals(1, stk.size());

		// Testing Sine - Radians
		stk.clear();
		stk.push(1.2);
		StackTrig.cmdTrig(stk, "sin", "rad");
		assertEquals(0.9320390859672263, stk.peek());
		assertEquals(1, stk.size());

		// Testing Cosine - Degrees
		stk.clear();
		stk.push(12.0);
		StackTrig.cmdTrig(stk, "cos", "");
		assertEquals(0.9781476007338057, stk.peek());
		assertEquals(1, stk.size());

		// Testing Cosine - Radians
		stk.clear();
		stk.push(1.2);
		StackTrig.cmdTrig(stk, "cos", "rad");
		assertEquals(0.3623577544766736, stk.peek());
		assertEquals(1, stk.size());

		// Testing Tangent - Degrees
		stk.clear();
		stk.push(23.0);
		StackTrig.cmdTrig(stk, "tan", "");
		assertEquals(0.42447481620960476, stk.peek());
		assertEquals(1, stk.size());

		// Testing Tangent - Radians
		stk.clear();
		stk.push(16.0);
		StackTrig.cmdTrig(stk, "tan", "rad");
		assertEquals(0.3006322420239034, stk.peek());
		assertEquals(1, stk.size());

	}

	/**
	 * Test method for
	 * {@link org.fross.rpncalc.StackTrig#cmdArcTrig(org.fross.rpncalc.StackObj, java.lang.String, java.lang.String)}.
	 */
	@Test
	void testCmdArcTrig() {
		StackObj stk = new StackObj();

		// Testing ArcSine - Degrees
		stk.push(0.123);
		StackTrig.cmdArcTrig(stk, "asin", "");
		assertEquals(7.065272930650028, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcSine - Radians
		stk.clear();
		stk.push(.123);
		StackTrig.cmdArcTrig(stk, "asin", "rad");
		assertEquals(0.12331227519187199, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcCosine - Degrees
		stk.clear();
		stk.push(.345);
		StackTrig.cmdArcTrig(stk, "acos", "");
		assertEquals(69.8182042331226, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcCosine - Radians
		stk.clear();
		stk.push(.345);
		StackTrig.cmdArcTrig(stk, "acos", "rad");
		assertEquals(1.218557541697832, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcTangent - Degrees
		stk.clear();
		stk.push(2.123);
		StackTrig.cmdArcTrig(stk, "atan", "");
		assertEquals(64.77808452780363, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcTangent - Radians
		stk.clear();
		stk.push(2.123);
		StackTrig.cmdArcTrig(stk, "atan", "rad");
		assertEquals(1.1305908581453696, stk.peek());
		assertEquals(1, stk.size());

	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackTrig#cmdHypotenuse(org.fross.rpncalc.StackObj)}.
	 */
	@Test
	void testCmdHypotenuse() {
		StackObj stk = new StackObj();

		stk.push(3.0);
		stk.push(4.0);
		StackTrig.cmdHypotenuse(stk);

		assertEquals(5.0, stk.peek());
		assertEquals(1, stk.size());
	}

}
