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

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.fross.library.Debug;
import org.fross.library.Format;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

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
	public static final int LINE_NUMBER_DIGITS = 2;
	public static final String INPUT_PROMPT = ">> ";

	// Class Variables
	public static String VERSION;
	public static String COPYRIGHT;
	static LineReader scanner;
	static boolean ProcessCommandLoop = true;
	static final StackObj calcStack = new StackObj();
	static final StackObj calcStack2 = new StackObj();

	// Configuration Values
	static int configProgramWidth = CONFIG_DEFAULT_PROGRAM_WIDTH;
	static int configMemorySlots = CONFIG_DEFAULT_MEMORY_SLOTS;
	static String configAlignment = CONFIG_DEFAULT_ALIGNMENT;

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

		try (Terminal terminal = TerminalBuilder.builder().build()) {
			scanner = LineReaderBuilder.builder().terminal(terminal).build();
			doMain(args);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	private static void doMain(String[] args) {

		String cmdInput = "";        // What the user enters in totality
		String cmdInputCmd = "";    // The first field - the command
		String cmdInputParam = "";    // The remaining string - Parameters
		Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config"); // Persistent configuration settings

		// Add default values to the persistent configuration items if none exist
		if (prefConfig.get("alignment", "none").equals("none")) {
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
		Output.debugPrintln("Command Line Options");
		Output.debugPrintln("  - DebugModeOn:   " + Debug.query());
		Output.debugPrintln("  - StackName:     " + calcStack.queryStackName());
		Output.debugPrintln("  - Program Width: " + configProgramWidth);
		Output.debugPrintln("  - Memory Slots:  " + configMemorySlots);
		Output.debugPrintln("  - Color Enabled: " + Output.queryColorEnabled());

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
			for (int i = 0; i < calcStack.size(); i++) {
				int decimalIndex = 0;
				String currentStackItem = "";

				// Add a comma to the current stack item if it's not in scientific notation
				if (calcStack.getAsString(i).toLowerCase().contains("e")) {
					currentStackItem = calcStack.getAsString(i);
				} else {
					currentStackItem = Format.Comma(calcStack.getAsString(i));
				}

				// Determine where the decimal point is located
				decimalIndex = currentStackItem.indexOf(".");

				// If current stack item has more digits ahead of decimal make that the max - commas are included.
				if (maxDigitsBeforeDecimal < decimalIndex) {
					maxDigitsBeforeDecimal = decimalIndex;
				}

				// Determine the length of the longest item in the stack for right alignment
				if (currentStackItem.length() > maxLenOfNumbers) {
					maxLenOfNumbers = currentStackItem.length();
				}

			}

			// Uncomment to debug alignment issues
			// Output.debugPrintln("Alignment: Max digits before the decimal: " + maxDigitsBeforeDecimal);
			// Output.debugPrintln("Alignment: Max length of longest item in stack: " + maxLenOfNumbers);

			// Display the current stack contents
			for (int i = 0; i < calcStack.size(); i++) {
				String currentStackItem = "";

				// Add commas to the number if it's not in scientific notation
				if (calcStack.getAsString(i).toLowerCase().contains("e")) {
					currentStackItem = calcStack.getAsString(i).toLowerCase();
				} else {
					currentStackItem = Format.Comma(calcStack.getAsString(i).toLowerCase());
				}

				// Display Stack Row Number without a newline
				String stkLineNumber = String.format("%0" + LINE_NUMBER_DIGITS + "d:  ", calcStack.size() - i);
				Output.printColor(Ansi.Color.CYAN, stkLineNumber);

				// Decimal Alignment - insert spaces before number to align to the decimal point
				if (configAlignment.compareTo("d") == 0) {
					for (int k = 0; k < (maxDigitsBeforeDecimal - currentStackItem.indexOf(".")); k++) {
						Output.print(" ");
					}
				}

				// Right Alignment - insert spaces before number to right align
				if (configAlignment.compareTo("r") == 0) {
					for (int k = 0; k < (maxLenOfNumbers - currentStackItem.length()); k++) {
						Output.print(" ");
					}
				}

				// Now that the spaces are inserted (for decimal/right) display the number
				// currentStackItem = currentStackItem.replaceAll("\\s+$", ""); // TODO: Delete this in the future if the below trim() works
				Output.printColorln(Ansi.Color.WHITE, currentStackItem.trim());
			}

			// Input command from user
			try {
				cmdInput = scanner.readLine("\n" + INPUT_PROMPT);
			} catch (UserInterruptException ex) {
				// User entered Ctrl-c so exit the program gracefully by adding "exit" as the input
				cmdInput = "exit";
				Output.printColorln(Ansi.Color.CYAN, "Ctrl-C Detected. Exiting RPNCalc...");

			} catch (Exception ex) {
				// Should never get this error...
				Output.fatalError("Could not read user input\n" + ex.getMessage(), 5);
			}

			// Break each entered line (cmdInput) into a command (cmdInputCmd) and a parameter (cmdInputParam) string
			try {
				// Remove any commas from the string allowing for numbers such as "12,123" to be entered
				// TODO: Should make this more international at some point
				cmdInput = cmdInput.replaceAll(",", "");

				// Break the string into a command (cmdInputCmd) and a parameter (cmdInputParam)
				String[] ci = cmdInput.toLowerCase().trim().split("\\s+", 2);
				cmdInputCmd = ci[0];
				cmdInputParam = ci[1];

			} catch (ArrayIndexOutOfBoundsException e) {
				// Ignore this exception if there is no command or parameter entered
				if (cmdInputCmd.isEmpty()) {
					Output.debugPrintln("Blank line entered");
					continue;
				}
			}

			// While in debug mode, show the entered text along with the broken up command and parameter
			Output.debugPrintln(
					"Full cmdInput: '" + cmdInput + "'  |  cmdInputCommand: '" + cmdInputCmd + "'  |  cmdInputParameter: '" + cmdInputParam + "'");

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

		// If recording is on, complete the recording off process before exiting
		if (UserFunctions.recordingIsEnabled() == true) {
			Output.printColorln(Ansi.Color.YELLOW, "\nRecording is in progress:");
			UserFunctions.cmdRecord("off");
		}

		// Save the items in the memory slots to the preferences system
		StackMemory.SaveMemSlots();

		// Save the primary and secondary stacks to the preferences system
		StackManagement.SaveStack(calcStack, "1");
		StackManagement.SaveStack(calcStack2, "2");

	}
}