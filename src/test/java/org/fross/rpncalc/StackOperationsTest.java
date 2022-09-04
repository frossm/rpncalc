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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class StackOperationsTest {

	/**
	 * Testing turning debug on and off
	 */
	@Test
	void testCmdDebug() {
		assertFalse(Debug.query());
		StackOperations.cmdDebug();
		assertTrue(Debug.query());
		StackOperations.cmdDebug();
		assertFalse(Debug.query());
	}

	/**
	 * Test reversing the stack with the 'rev' command
	 */
	@Test
	void testReverseCommand() {
		StackObj stk = new StackObj();

		// Load the stack
		stk.push(1.0);
		stk.push(2.0);
		stk.push(3.0);
		stk.push(4.0);
		stk.push(5.0);

		// Check the current stack
		assertEquals(5, stk.size());
		assertEquals(1.0, stk.get(0));
		assertEquals(2.0, stk.get(1));
		assertEquals(3.0, stk.get(2));
		assertEquals(4.0, stk.get(3));
		assertEquals(5.0, stk.get(4));

		// Reverse the stack items
		StackOperations.cmdReverse(stk);

		// Validate they are reversed
		assertEquals(5, stk.size());
		assertEquals(5.0, stk.get(0));
		assertEquals(4.0, stk.get(1));
		assertEquals(3.0, stk.get(2));
		assertEquals(2.0, stk.get(3));
		assertEquals(1.0, stk.get(4));
	}

	/**
	 * Testing the various 'set' commands
	 * 
	 * 'set mem' slots tested in StackMemory method test
	 * 
	 * 'reset' tested here as well
	 */
	@Test
	void testCmdSet() {
		Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");

		// set Width
		StackOperations.cmdSet("width 100");
		assertEquals(100, Main.configProgramWidth);
		assertEquals("100", prefConfig.get("programwidth", ""));

		// set Align
		StackOperations.cmdSet("alignment d");
		assertEquals("d", Main.configAlignment);
		assertEquals("d", prefConfig.get("alignment", ""));

		StackOperations.cmdSet("alignment r");
		assertEquals("r", Main.configAlignment);
		assertEquals("r", prefConfig.get("alignment", ""));

		StackOperations.cmdSet("alignment l");
		assertEquals("l", Main.configAlignment);
		assertEquals("l", prefConfig.get("alignment", ""));

		StackOperations.cmdReset();
		assertEquals(80, Main.configProgramWidth);
		assertEquals("80", prefConfig.get("programwidth", ""));
		assertEquals("l", Main.configAlignment);
		assertEquals("l", prefConfig.get("alignment", ""));
	}

	/**
	 * Test method to create a new test stack, add values to it, and check that you can load it
	 */
	@Test
	void testLoadCommand() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		// Clear out any saves items in the junittest saved stack if it exists
		try {
			Preferences.userRoot().node(StackManagement.PREFS_PATH + "/junittest").removeNode();
		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, e.getMessage());
		}

		// Set the stack names
		stk1.setStackNameAndRestore("junittest", "1");
		stk2.setStackNameAndRestore("junittest", "2");

		// Fill up the stacks
		for (Double i = 1.0; i <= 10.00; i++) {
			stk1.push(i);
			stk2.push(i + 100.00);
		}

		// Save the stacks to the registry
		StackManagement.SaveStack(stk1, "1");
		StackManagement.SaveStack(stk2, "2");

		// Load the junittest stack and make sure the name is queried successfully
		CommandParser.Parse(stk1, stk2, "load junittest", "load", "junittest");
		assertEquals("junittest", stk1.queryStackName());
		assertEquals("junittest", stk2.queryStackName());

		// stk1: Check to make sure the numbers are accurate by adding them up
		StackCommands.cmdAddAll(stk1, "keep");
		assertEquals(11, stk1.size());
		assertEquals(55, stk1.pop());

		// stk2: Check to make sure the numbers are accurate by adding them up
		StackCommands.cmdAddAll(stk2, "keep");
		assertEquals(11, stk2.size());
		assertEquals(1055, stk2.pop());

		// Load default stack
		StackOperations.cmdLoad(stk1, stk2, "default");
		assertEquals("default", stk1.queryStackName());

		// Load the junittest stack again
		StackOperations.cmdLoad(stk1, stk2, "junittest");
		assertEquals("junittest", stk1.queryStackName());

		// stk1: Check to make sure the numbers are accurate by adding them up
		StackCommands.cmdAddAll(stk1, "keep");
		assertEquals(11, stk1.size());
		assertEquals(55, stk1.pop());

		// stk2: Check to make sure the numbers are accurate by adding them up
		StackCommands.cmdAddAll(stk2, "keep");
		assertEquals(11, stk2.size());
		assertEquals(1055, stk2.pop());

		// Cleanup by removing the junittest stack from the preferences system
		try {
			Preferences.userRoot().node(StackManagement.PREFS_PATH + "/junittest").removeNode();
		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, e.getMessage());
		}
	}

}
