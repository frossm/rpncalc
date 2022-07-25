/**
 * 
 */
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	void testProcessCommandLine() {
		String[] argv1 = { "-D", "-z", "-h", "-v", "-l", "LoadFile.txt" };

		CommandLineArgs cli = new CommandLineArgs();
		JCommander jc = new JCommander();

		// Test Short Options
		jc.setProgramName("RPNCalc");
		jc = JCommander.newBuilder().addObject(cli).build();
		jc.parse(argv1);

		assertEquals(true, cli.clDebug);
		assertEquals(true, cli.clNoColor);
		assertEquals(true, cli.clVersion);
		assertEquals(true, cli.clHelp);
		assertEquals("LoadFile.txt", cli.clLoad);

	}
}