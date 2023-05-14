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
class CommandParserTest {

	/**
	 * Parser is just a big switch statement and takes commands entered and runs the specific method. The key part to test is the
	 * 'default:' portion where numbers, fractions, and NumOps are entered
	 *
	 **/

	// Test standard number inputs
	@Test
	void testNormalNumberEntry() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		// Fraction Input
		stk1.clear();
		CommandParser.Parse(stk1, stk2, "123", "123", "");
		assertEquals(123, stk1.get(0));
		assertEquals(1, stk1.size());

		CommandParser.Parse(stk1, stk2, "123", "123", "");
		assertEquals(123, stk1.get(0));
		assertEquals(2, stk1.size());

		CommandParser.Parse(stk1, stk2, "123", "123", "");
		assertEquals(123, stk1.get(0));
		assertEquals(3, stk1.size());
	}

	// Test fractional inputs
	@Test
	void testFractionInput() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		// Fraction Input
		stk1.clear();
		CommandParser.Parse(stk1, stk2, "1/8", "1/8", "");
		assertEquals(.125, stk1.peek());
		assertEquals(1, stk1.size());

		CommandParser.Parse(stk1, stk2, "47/88", "47/88", "");
		StackCommands.cmdRound(stk1,  "4");
		assertEquals(0.5341, stk1.peek());
		assertEquals(2, stk1.size());

		CommandParser.Parse(stk1, stk2, "1 3/16", "1", "3/16");
		assertEquals(1.1875, stk1.peek());
		assertEquals(3, stk1.size());
		
		CommandParser.Parse(stk1, stk2, "-4 1/64", "-4", "1/64");
		StackCommands.cmdRound(stk1,  "4");
		assertEquals(-3.9844, stk1.peek());
		assertEquals(4, stk1.size());
	}

	@Test
	void testNumOp() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		stk1.clear();
		CommandParser.Parse(stk1, stk2, "-123.456", "-123.456", "");
		assertEquals(1, stk1.size());
		assertEquals(-123.456, stk1.peek());
		
		CommandParser.Parse(stk1, stk2, "2+", "2+", "");
		assertEquals(1, stk1.size());
		assertEquals(-121.456, stk1.peek());
		
		CommandParser.Parse(stk1, stk2, "16.61-", "16.61-", "");
		assertEquals(1, stk1.size());
		assertEquals(-138.066, stk1.peek());
		
		CommandParser.Parse(stk1, stk2, "-2.2*", "-2.2*", "");
		assertEquals(1, stk1.size());
		assertEquals(303.7452, stk1.peek());
		
		CommandParser.Parse(stk1, stk2, "10/", "10/", "");
		assertEquals(1, stk1.size());
		assertEquals(30.37452, stk1.peek());
		
		CommandParser.Parse(stk1, stk2, "2^", "2^", "");
		assertEquals(1, stk1.size());
		StackCommands.cmdRound(stk1,  "4");
		assertEquals(922.6115, stk1.peek());
		
		assertEquals(1, stk1.size());
		StackCommands.cmdRound(stk1, "4");
		assertEquals(922.6115, stk1.peek());
	}

}
