/**
 * 
 */
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
	 * Test method for {@link org.fross.rpncalc.StackOperations#cmdDebug()}.
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
	 * Test method for {@link org.fross.rpncalc.StackOperations#cmdSet(java.lang.String)}.
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
	 * Test method to create a new test stack, add values to it, and check that you can load
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
