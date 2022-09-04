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
class CommandParserTest {

	/**
	 * Test method for
	 * {@link org.fross.rpncalc.CommandParser#Parse(org.fross.rpncalc.StackObj, org.fross.rpncalc.StackObj, java.lang.String, java.lang.String, java.lang.String)}.
	 *
	 * Parser is just a big switch statement and takes commands entered and runs the specific method. The key part to test is the
	 * 'default:' portion where numbers, fractions, or NumOps are entered
	 *
	 */
	@Test
	void testParsingOfNumbersFractionsNumOp() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		// Standard numbers
		CommandParser.Parse(stk1, stk2, "123", "123", "");
		CommandParser.Parse(stk1, stk2, "456.789", "456.789", "");
		assertEquals(2, stk1.size());

		// Swap the stack and the size should be 0
		CommandParser.Parse(stk1, stk2, "ss", "ss", "");
		assertEquals(0, stk1.size());

		// Swap it back and we should be at 2 again
		CommandParser.Parse(stk1, stk2, "ss", "ss", "");
		assertEquals(2, stk1.size());

		// Fraction Input
		stk1.clear();
		CommandParser.Parse(stk1, stk2, "1/8", "1/8", "");
		assertEquals(1, stk1.size());
		assertEquals(.125, stk1.get(0));

		// NumOp
		stk1.clear();
		CommandParser.Parse(stk1, stk2, "123", "123", "");
		CommandParser.Parse(stk1, stk2, "2+", "2+", "");
		CommandParser.Parse(stk1, stk2, "16-", "16-", "");
		CommandParser.Parse(stk1, stk2, "2*", "2*", "");
		CommandParser.Parse(stk1, stk2, "10/", "10/", "");
		CommandParser.Parse(stk1, stk2, ".12^", ".12^", "");
		assertEquals(1, stk1.size());
		StackCommands.cmdRound(stk1, "5");
		assertEquals(1.44749, stk1.get(0));
	}

}
