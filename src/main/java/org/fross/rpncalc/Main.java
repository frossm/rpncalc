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

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.fross.library.Debug;
import org.fross.library.Format;
import org.fross.library.GitHub;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;

/**
 * Main - Main program execution class
 * 
 * @author Michael Fross (michael@fross.org)
 *
 */
public class Main {
	// Class Constants (or pseudo constants)
	public static final String PROPERTIES_FILE = "app.properties";
	public static final int PROGRAM_MINIMUM_WIDTH = 46;
	public static final int CONFIG_DEFAULT_PROGRAM_WIDTH = 80;
	public static final int CONFIG_DEFAULT_MEMORY_SLOTS = 10;
	public static final String CONFIG_DEFAULT_ALIGNMENT = "l";

	// Class Variables
	public static String VERSION;
	public static String COPYRIGHT;
	static LineReader scanner = LineReaderBuilder.builder().build();
	static boolean ProcessCommandLoop = true;
	static StackObj calcStack = new StackObj();
	static StackObj calcStack2 = new StackObj();

	// Configuration Values
	static int configProgramWidth = 80;
	static int configMemorySlots = 10;
	static String configAlignment = "l";

	/**
	 * Display Version(): Show the program version, copyright, and latest GitHub release
	 * 
	 */
	public static void DisplayVersion() {
		Output.printColorln(Ansi.Color.WHITE, "RPNCalc Version: v" + Main.VERSION);
		Output.printColorln(Ansi.Color.CYAN, Main.COPYRIGHT);
		Output.printColorln(Ansi.Color.WHITE, "\nLatest Release on GitHub: " + GitHub.updateCheck("rpncalc"));
		Output.printColorln(Ansi.Color.CYAN, "HomePage: https://github.com/frossm/rpncalc");
	}

	/**
	 * DisplayStatusLine(): Display the last line of the header and the separator line. This is a separate function given it also
	 * inserts the loaded stack and spaces everything correctly.
	 * 
	 */
	public static void DisplayStatusLine() {
		// Format the number of memory slots used
		String sfMem = String.format("Mem:%02d", StackMemory.QueryInUseMemorySlots());

		// Format the undo level to 2 digits. Can't image I'd need over 99 undo levels
		String sfUndo = String.format("Undo:%02d", calcStack.undoSize());

		// Determine how many dashes to use after remove space for the undo and stack name
		int numDashes = configProgramWidth - 2 - sfMem.length() - sfUndo.length() - calcStack.queryStackName().length() - 11;

		// [Recording] appears if it's turned on. Make room if it's enabled
		if (UserFunctions.recordingIsEnabled() == true)
			numDashes -= 12;

		// Print the StatusLine dashes
		Output.printColor(Ansi.Color.CYAN, "+");
		Output.printColor(Ansi.Color.CYAN, "-".repeat(numDashes));

		// Print the StatusLine Data in chunks to be able to better control color output
		if (UserFunctions.recordingIsEnabled() == true) {
			Output.printColor(Ansi.Color.CYAN, "[");
			Output.printColor(Ansi.Color.RED, "Recording");
			Output.printColor(Ansi.Color.CYAN, "]-");
		}
		Output.printColor(Ansi.Color.CYAN, "[");
		Output.printColor(Ansi.Color.WHITE, sfMem);
		Output.printColor(Ansi.Color.CYAN, "]-[");
		Output.printColor(Ansi.Color.WHITE, sfUndo);
		Output.printColor(Ansi.Color.CYAN, "]-[");
		Output.printColor(Ansi.Color.WHITE, calcStack.queryStackName() + ":" + StackManagement.QueryCurrentStackNum());
		Output.printColor(Ansi.Color.CYAN, "]-");
		Output.printColorln(Ansi.Color.CYAN, "+");
	}

	/*
	 * Main(): Start of program and holds main command loop
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String cmdInput = "";		// What the user enters in totality
		String cmdInputCmd = "";	// The first field - the command
		String cmdInputParam = "";	// The remaining string - Parameters
		Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config"); // Persistent configuration settings

		// Process application level properties file
		// Update properties from Maven at build time:
		// Ref: https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
			COPYRIGHT = "Copyright (C) " + prop.getProperty("Application.inceptionYear") + "-" + org.fross.library.Date.getCurrentYear() + " by Michael Fross";
		} catch (IOException ex) {
			Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
		}

		// Add default values to the persistent configuration items if none exist
		if (prefConfig.get("alignment", "none") == "none") {
			prefConfig.put("alignment", CONFIG_DEFAULT_ALIGNMENT);
		}
		if (prefConfig.getInt("programwidth", -1) == -1) {
			prefConfig.putInt("programwidth", CONFIG_DEFAULT_PROGRAM_WIDTH);
		}
		if (prefConfig.getInt("memoryslots", -1) == -1) {
			prefConfig.putInt("memoryslots", CONFIG_DEFAULT_MEMORY_SLOTS);
		}

		// Set configuration variables from preferences
		configProgramWidth = prefConfig.getInt("programwidth", CONFIG_DEFAULT_PROGRAM_WIDTH);
		configMemorySlots = prefConfig.getInt("memoryslots", CONFIG_DEFAULT_MEMORY_SLOTS);
		configAlignment = prefConfig.get("alignment", CONFIG_DEFAULT_ALIGNMENT);

		// Process Command Line Options
		CommandLineArgs.ProcessCommandLine(args);

		// Display some useful information about the environment if in Debug Mode
		Debug.displaySysInfo();
		Output.debugPrint("Command Line Options");
		Output.debugPrint("  -D:  " + Debug.query());
		Output.debugPrint("  -l:  " + calcStack.queryStackName());
		Output.debugPrint("  -w:  " + configProgramWidth);
		Output.debugPrint("  -m:  " + configMemorySlots);
		Output.debugPrint("  Color Enabled: " + Output.queryColorEnabled());

		// Restore the items in the memory slots during startup
		StackMemory.RestoreMemSlots();

		// Display the initial program header information
		Output.printColorln(Ansi.Color.CYAN, "+" + "-".repeat(configProgramWidth - 2) + "+");
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(configProgramWidth, "RPN Calculator  v" + VERSION, "|", "|"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(configProgramWidth, COPYRIGHT, "|", "|"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(configProgramWidth, "Enter command 'h' for help details", "|", "|"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(configProgramWidth, "", "|", "|"));

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			int maxDigitsBeforeDecimal = 0;
			int maxLenOfNumbers = 0;

			// Display the dashed status line
			DisplayStatusLine();

			// Loop through the stack and count the max digits before the decimal for use with the decimal
			// alignment mode & overall length for right alignment mode
			for (int k = 0; k < calcStack.size(); k++) {
				int decimalIndex = Format.Comma(calcStack.get(k)).indexOf(".");
				// If current stack item has more digits ahead of decimal make that the max - commas are included.
				if (maxDigitsBeforeDecimal < decimalIndex) {
					maxDigitsBeforeDecimal = decimalIndex;
				}

				// Determine the length of the longest item in the stack for right alignment
				if (Format.Comma(calcStack.get(k)).length() > maxLenOfNumbers) {
					maxLenOfNumbers = Format.Comma(calcStack.get(k)).length();
				}
			}

			Output.debugPrint("Alignment: Max digits before the decimal: " + maxDigitsBeforeDecimal);
			Output.debugPrint("Alignment: Max length of longest item in stack: " + maxLenOfNumbers);

			// Display the current stack contents
			for (int i = 0; i < calcStack.size(); i++) {

				// Display Stack Row Number
				String sn = String.format("%02d:  ", calcStack.size() - i);
				Output.printColor(Ansi.Color.CYAN, sn);

				// Configure the alignment
				if (configAlignment.compareTo("d") == 0) {
					// Put in spaces to align the decimals
					int decimalLocation = Format.Comma(calcStack.get(i)).indexOf(".");
					for (int k = 0; k < maxDigitsBeforeDecimal - decimalLocation; k++) {
						Output.print(" ");
					}
					sn = Format.Comma(calcStack.get(i));

				} else if (configAlignment.compareTo("r") == 0) {
					sn = String.format("%" + maxLenOfNumbers + "s", Format.Comma(calcStack.get(i)));

				} else {
					sn = Format.Comma(calcStack.get(i));
				}

				Output.printColorln(Ansi.Color.WHITE, sn);
			}

			// Input command from user
			try {
				cmdInput = scanner.readLine("\n>> ");
			} catch (UserInterruptException ex) {
				// User entered Ctrl-c so exit the program gracefully
				cmdInput = "exit";
			} catch (Exception ex) {
				Output.fatalError("Could not read user input\n" + ex.getMessage(), 5);
			}

			// Break each line entered into a command and a parameter string
			try {
				String[] ci = cmdInput.toLowerCase().trim().split("\\s+", 2);
				cmdInputCmd = ci[0];
				cmdInputParam = ci[1];

			} catch (ArrayIndexOutOfBoundsException e) {
				// Ignore if there is no command or parameter entered
				if (cmdInputCmd.isEmpty()) {
					Output.debugPrint("Blank line entered");
					continue;
				}
			}
			Output.debugPrint("Input Entered: '" + cmdInput + "'  |  Command: '" + cmdInputCmd + "'  |  Parameter: '" + cmdInputParam + "'");

			// If recording is enabled, send the user input to be recorded
			if (UserFunctions.recordingIsEnabled() == true) {
				UserFunctions.RecordCommand(cmdInputCmd + " " + cmdInputParam);
			}

			// Call the parser to send the command to the correct function to execute
			CommandParser.Parse(calcStack, calcStack2, cmdInput, cmdInputCmd, cmdInputParam);

			// Clear input parameters before we start again
			cmdInputCmd = "";
			cmdInputParam = "";

		} // End While Loop

		// Save the items in the memory slots to the preferences system
		StackMemory.SaveMemSlots();

		// Save the primary and secondary stacks to the preferences system
		StackManagement.SaveStack(calcStack, "1");
		StackManagement.SaveStack(calcStack2, "2");

	}
}