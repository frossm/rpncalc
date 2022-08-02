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
		CommandParser.Parse(stk1,  stk2,  "ss", "ss", "");
		assertEquals(0, stk1.size());
		CommandParser.Parse(stk1,  stk2,  "ss", "ss", "");
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
