/**
 * 
 */
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.beust.jcommander.JCommander;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class CommandLineArgsTest {

	/**
	 * Test method for {@link org.fross.rpncalc.CommandLineArgs#ProcessCommandLine(java.lang.String[])}.
	 */
	@Test
	void testShortCommandLineArgs() {
		// Test Short Options
		String[] argv1 = { "-D", "-z", "-h", "-v", "-l", "LoadFile.txt" };

		CommandLineArgs cli = new CommandLineArgs();
		JCommander jc = new JCommander();
		jc.setProgramName("RPNCalc");
		
		jc = JCommander.newBuilder().addObject(cli).build();
		jc.parse(argv1);

		assertTrue(cli.clDebug);
		assertTrue(cli.clNoColor);
		assertTrue(cli.clVersion);
		assertTrue(cli.clHelp);
		assertEquals("LoadFile.txt", cli.clLoad);
	}
	
	@Test
	void testLongCommandLineArgs() {
		// Test Long Options
		String[] argv2 = { "--debug", "--no-color", "--help", "--version", "--load", "LongLoadFile.txt" };
		
		CommandLineArgs cli = new CommandLineArgs();
		JCommander jc = new JCommander();
		jc.setProgramName("RPNCalc");
		
		jc = JCommander.newBuilder().addObject(cli).build();
		jc.parse(argv2);

		assertTrue(cli.clDebug);
		assertTrue(cli.clNoColor);
		assertTrue(cli.clVersion);
		assertTrue(cli.clHelp);
		assertEquals("LongLoadFile.txt", cli.clLoad);
	}
	
	@Test
	void testMixedCommandLineArgs() {
		// Test Mix of Options
		String[] argv3 = { "--no-color", "-?", "--load", "MixedLoadFile.txt" };
		
		CommandLineArgs cli = new CommandLineArgs();
		JCommander jc = new JCommander();
		jc.setProgramName("RPNCalc");
		
		jc = JCommander.newBuilder().addObject(cli).build();
		jc.parse(argv3);

		assertFalse(cli.clDebug);
		assertTrue(cli.clNoColor);
		assertFalse(cli.clVersion);
		assertTrue(cli.clHelp);
		assertEquals("MixedLoadFile.txt", cli.clLoad);
	}
}