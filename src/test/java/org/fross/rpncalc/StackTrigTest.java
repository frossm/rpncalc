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
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.20791, stk.peek());
		assertEquals(1, stk.size());

		// Testing Sine - Radians
		stk.clear();
		stk.push(1.2);
		StackTrig.cmdTrig(stk, "sin", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.93204, stk.peek());
		assertEquals(1, stk.size());

		// Testing Cosine - Degrees
		stk.clear();
		stk.push(12.0);
		StackTrig.cmdTrig(stk, "cos", "");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.97815, stk.peek());
		assertEquals(1, stk.size());

		// Testing Cosine - Radians
		stk.clear();
		stk.push(1.2);
		StackTrig.cmdTrig(stk, "cos", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.36236, stk.peek());
		assertEquals(1, stk.size());

		// Testing Tangent - Degrees
		stk.clear();
		stk.push(23.0);
		StackTrig.cmdTrig(stk, "tan", "");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.42447, stk.peek());
		assertEquals(1, stk.size());

		// Testing Tangent - Radians
		stk.clear();
		stk.push(16.0);
		StackTrig.cmdTrig(stk, "tan", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.30063, stk.peek());
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
		StackCommands.cmdRound(stk, "5");
		assertEquals(7.06527, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcSine - Radians
		stk.clear();
		stk.push(.123);
		StackTrig.cmdArcTrig(stk, "asin", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(0.12331, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcCosine - Degrees
		stk.clear();
		stk.push(.345);
		StackTrig.cmdArcTrig(stk, "acos", "");
		StackCommands.cmdRound(stk, "5");
		assertEquals(69.8182, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcCosine - Radians
		stk.clear();
		stk.push(.345);
		StackTrig.cmdArcTrig(stk, "acos", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(1.21856, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcTangent - Degrees
		stk.clear();
		stk.push(2.123);
		StackTrig.cmdArcTrig(stk, "atan", "");
		StackCommands.cmdRound(stk, "5");
		assertEquals(64.77808, stk.peek());
		assertEquals(1, stk.size());

		// Testing ArcTangent - Radians
		stk.clear();
		stk.push(2.123);
		StackTrig.cmdArcTrig(stk, "atan", "rad");
		StackCommands.cmdRound(stk, "5");
		assertEquals(1.13059, stk.peek());
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
		
		stk.push(8.123);
		stk.push(4.789);
		StackTrig.cmdHypotenuse(stk);
		StackCommands.cmdRound(stk, "5");
		assertEquals(9.42962, stk.peek());
		assertEquals(2, stk.size());

	}

}
