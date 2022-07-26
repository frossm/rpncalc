/**
 * 
 */
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.prefs.Preferences;

import org.fross.library.Debug;
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

}
