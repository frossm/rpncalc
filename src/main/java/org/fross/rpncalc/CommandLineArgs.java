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

import org.fross.library.Debug;
import org.fross.library.Output;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class CommandLineArgs {
	// ---------------------------------------------------------------------------------------------
	// Define command line options that can be used
	// ---------------------------------------------------------------------------------------------

	@Parameter(names = { "-h", "-?", "--help" }, help = true, description = "Display RPNCalc help and exit")
	protected boolean clHelp = false;

	@Parameter(names = { "-z", "--no-color" }, description = "Disable colorized output")
	protected boolean clNoColor = false;

	@Parameter(names = { "-v", "--version" }, description = "Show current program version and latest release on GitHub")
	protected boolean clVersion = false;

	@Parameter(names = { "-D", "--debug" }, description = "Turn on Debug mode to display extra program information")
	protected boolean clDebug = false;

	@Parameter(names = { "-l", "--load" }, description = "Load saved stack file")
	protected String clLoad = "";

	@Parameter(names = { "-L", "--license" }, description = "Display program usage license")
	protected boolean clLicense = false;

	// ---------------------------------------------------------------------------------------------
	// Process command line parameters with the following methods
	// ---------------------------------------------------------------------------------------------
	public static void ProcessCommandLine(String[] argv) {
		CommandLineArgs cli = new CommandLineArgs();
		JCommander jc = new JCommander();

		// JCommander parses the command line
		try {
			jc.setProgramName("RPNCalc");
			jc = JCommander.newBuilder().addObject(cli).build();
			jc.parse(argv);
		} catch (ParameterException ex) {
			System.out.println(ex.getMessage());
			jc.usage();
			System.exit(0);
		}

		// ---------------------------------------------------------------------------------------------
		// Process the parsed command line options
		// ---------------------------------------------------------------------------------------------
		// Debug Switch
		if (cli.clDebug == true)
			Debug.enable();

		// Set the stack name and restore stack from Preferences
		if (!cli.clLoad.isBlank()) {
			Main.calcStack.setStackNameAndRestore(cli.clLoad, "1");
			Main.calcStack2.setStackNameAndRestore(cli.clLoad, "2");
		} else {
			Main.calcStack.setStackNameAndRestore("default", "1");
			Main.calcStack2.setStackNameAndRestore("default", "2");
		}

		// Version Display
		if (cli.clVersion == true) {
			Help.DisplayVersion();
			System.exit(0);
		}

		// License Display
		if (cli.clLicense == true) {
			Help.DisplayLicense();
			System.exit(0);
		}

		// Disable Colorized Output Switch
		if (cli.clNoColor == true) {
			Output.enableColor(false);
		}

		// Show Help and Exit
		if (cli.clHelp == true) {
			Help.Display();
			System.exit(0);
		}

	}

}