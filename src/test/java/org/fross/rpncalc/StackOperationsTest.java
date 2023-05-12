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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
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

	/**
	 * Test import capabilities
	 */
	@Test
	void testImport() {
		String testFileName = "target/rpncalc.import";
		Double[] testValues = { 2.34, 6.78, -2.11, 0.0, 12.12345, 4.44, -54.223, 100.001, 11.23 };

		// Create a test file
		try {
			FileWriter fw = new FileWriter(new File(testFileName.toLowerCase()));

			for (int i = 0; i < testValues.length; i++) {
				fw.write(testValues[i] + "\n");
			}

			// Remove testfile
			fw.close();

		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR:  Could not create testfile used for import testing");
			fail();
		}

		StackObj stk = new StackObj();
		StackOperations.importStackFromDisk(stk, testFileName);

		// Verify the import values match the file data
		for (int i = 0; i < testValues.length; i++) {
			Output.println("Testing Import: TestValues:" + testValues[i] + "\t| StackValues: " + stk.get(i));
			assertEquals(testValues[i], stk.get(i));
		}

		// Delete the test import file
		try {
			File file = new File(testFileName);
			file.delete();
		} catch (Exception ex) {
			Output.println("Testing Import: Issue deleting test file: ' " + testFileName + "'");
		}

	}

	/**
	 * Testing stack swapping
	 */
	@Test
	void testStackSwap() {
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
	}

}
