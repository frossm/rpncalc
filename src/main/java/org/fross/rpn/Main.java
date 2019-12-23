/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE.txt file for additional details
 *           
 ******************************************************************************/
package org.fross.rpn;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;
import org.fusesource.jansi.Ansi;
import gnu.getopt.Getopt;
import org.fross.library.Debug;
import org.fross.library.Output;
import org.fross.library.Format;;

/**
 * Main - Main program execution class
 * 
 * @author michael.d.fross
 *
 */
public class Main {

	// Class Constants
	public static String VERSION;
	public static final String PROPERTIES_FILE = "rpn.properties";

	/**
	 * DisplayDashedNameLine(): Display the last line of the header and the separator line. This is a
	 * separate function given it also inserts the loaded stack and spaced everything correctly.
	 */
	public static void displayDashedNameLine() {
		int DesiredDashes = 70;

		// Display the Loaded Stack into Dash line. 70 dashes w/o the name
		Output.printColor(Ansi.Color.CYAN, "+");
		int numDashes = DesiredDashes - Prefs.QueryLoadedStack().length() - 4;
		for (int i = 0; i < numDashes; i++) {
			Output.printColor(Ansi.Color.CYAN, "-");
		}
		Output.printColor(Ansi.Color.YELLOW, "[" + Prefs.QueryLoadedStack() + ":" + Prefs.QueryCurrentStackNum() + "]");
		Output.printColorln(Ansi.Color.CYAN, "+");
	}

	/**
	 * Main(): Start of program and holds main command loop
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Console con = null;
		char displayAlignment = 'l';
		Stack<Double> calcStack = new Stack<Double>();
		Stack<Double> calcStack2 = new Stack<Double>();
		Stack<Double> undoStack = new Stack<Double>();
		boolean ProcessCommandLoop = true;
		int optionEntry;

		// Process application level properties file
		// Update properties from Maven at build time:
		// https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
		} catch (IOException ex) {
			Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
		}

		// Initialize the console used for command input
		con = System.console();
		if (con == null) {
			Output.printColorln(Ansi.Color.RED, "FATAL :  Could not initialize OS Console for data input");
			System.exit(1);
		}

		// Process Command Line Options and set flags where needed
		Getopt optG = new Getopt("rpn", args, "Dl:a:h?");
		while ((optionEntry = optG.getopt()) != -1) {
			switch (optionEntry) {
			case 'D': // Debug Mode
				Debug.enable();
				break;
			case 'l':
				Prefs.SetLoadedStack(String.valueOf(optG.getOptarg()));
				break;
			case 'a':
				if (optG.getOptarg().charAt(0) == 'r') {
					Output.debugPrint("RIGHT alignment selected");
					displayAlignment = 'r';
				} else if (optG.getOptarg().charAt(0) == 'd') {
					Output.debugPrint("DECIMAL alignment selected");
					displayAlignment = 'd';
				} else if (optG.getOptarg().charAt(0) == 'l') {
					Output.debugPrint("LEFT alignment selected");
					displayAlignment = 'l';
				} else {
					Output.printColorln(Ansi.Color.RED, "ERROR: The -a alignment must be either a 'l', 'r', or 'd'");
					Help.Display();
					System.exit(0);
					break;
				}
				break;
			case '?': // Help
			case 'h':
				Help.Display();
				System.exit(0);
				break;
			default:
				Output.printColorln(Ansi.Color.RED, "ERROR: Unknown Command Line Option: '" + (char) optionEntry + "'");
				Help.Display();
				System.exit(0);
				break;
			}
		}

		// Display some useful information about the environment if in Debug Mode
		Output.debugPrint("System Information:");
		Output.debugPrint(" - class.path:     " + System.getProperty("java.class.path"));
		Output.debugPrint("  - java.home:      " + System.getProperty("java.home"));
		Output.debugPrint("  - java.vendor:    " + System.getProperty("java.vendor"));
		Output.debugPrint("  - java.version:   " + System.getProperty("java.version"));
		Output.debugPrint("  - os.name:        " + System.getProperty("os.name"));
		Output.debugPrint("  - os.version:     " + System.getProperty("os.version"));
		Output.debugPrint("  - os.arch:        " + System.getProperty("os.arch"));
		Output.debugPrint("  - user.name:      " + System.getProperty("user.name"));
		Output.debugPrint("  - user.home:      " + System.getProperty("user.home"));
		Output.debugPrint("  - user.dir:       " + System.getProperty("user.dir"));
		Output.debugPrint("  - file.separator: " + System.getProperty("file.separator"));
		Output.debugPrint("  - library.path:   " + System.getProperty("java.library.path"));
		Output.debugPrint("\nCommand Line Options");
		Output.debugPrint("  -D:  " + Debug.query());
		Output.debugPrint("  -l:  " + Prefs.QueryLoadedStack());
		Output.debugPrint("  -a:  " + displayAlignment);

		// Pull the existing stacks from the preferences if they exist
		calcStack = Prefs.RestoreStack("1");
		calcStack2 = Prefs.RestoreStack("2");
		Output.debugPrint("Elements in the Stack: " + calcStack.size());

		// Populate the undoStack with the contents of the current stack
		undoStack = (Stack<Double>) calcStack.clone();

		// Display output header information
		Output.printColorln(Ansi.Color.CYAN, "+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.CYAN, "|                           RPN Calculator                 v" + VERSION + " |");
		Output.printColorln(Ansi.Color.CYAN, "|           Written by Michael Fross.  All rights reserved             |");
		Output.printColorln(Ansi.Color.CYAN, "|                 Enter command 'h' for help details                   |");

		displayDashedNameLine();

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			String cmdInput = null;
			int maxDigitsBeforeDecimal = 0;

			// Loop through the stack and count the max digits before the decimal for use
			// with the decimal alignment mode
			for (int k = 0; k < calcStack.size(); k++) {
				int decimalIndex = calcStack.get(k).toString().indexOf(".");
				// If current stack item has more digits ahead of decimal make that the max
				if (maxDigitsBeforeDecimal < decimalIndex) {
					maxDigitsBeforeDecimal = decimalIndex;
				}
			}
			Output.debugPrint("Max Digits before decimal point: " + maxDigitsBeforeDecimal);

			// Display the current stack
			for (int i = 0; i <= calcStack.size() - 1; i++) {

				// Display Stack Number
				String sn = String.format("%02d:  ", calcStack.size() - i);
				Output.printColor(Ansi.Color.CYAN, sn);

				// Configure the alignment based on the -a: option
				if (displayAlignment == 'd') {
					// Put in spaces to align the decimals
					int decimalLocation = Format.Comma(calcStack.get(i)).indexOf(".");
					for (int k = 0; k < maxDigitsBeforeDecimal - decimalLocation + 1; k++) {
						Output.print(" ");
					}
					sn = Format.Comma(calcStack.get(i));

				} else if (displayAlignment == 'r') {
					// Add a few extra digits to maxDigitsBeforeDecimal account for commas
					sn = String.format("%" + (maxDigitsBeforeDecimal + 5) + "s", Format.Comma(calcStack.get(i)));

				} else {
					sn = Format.Comma(calcStack.get(i));
				}

				// Output.printColorln(Ansi.Color.WHITE, Math.Comma(calcStack.get(i)));
				Output.printColorln(Ansi.Color.WHITE, sn);
			}

			// Input command/number from user
			Output.printColor(Ansi.Color.YELLOW, "\n>>  ");
			cmdInput = con.readLine();

			// Toggle Debug Mode
			if (cmdInput.matches("[Dd][Ee][Bb][Uu][Gg]")) {
				if (Debug.query()) {
					Debug.disable();
					Output.printColorln(Ansi.Color.RED, "Debug Disabled");
				} else {
					Debug.enable();
					Output.debugPrint("Debug Enabled");
				}

				//////////////////////////////////////////////////////////////////
				// Load a new stack into the calculator
				// Command: load
			} else if (cmdInput.matches("^load .+")) {
				// Save current Stack
				Prefs.SaveStack(calcStack, "1");
				Prefs.SaveStack(calcStack2, "2");

				// Set new stack
				String newStack = cmdInput.toString().substring(5);
				Output.debugPrint("Loading new stack: '" + newStack + "'");
				Prefs.SetLoadedStack(newStack);

				// Load new stack
				calcStack = Prefs.RestoreStack("1");
				calcStack2 = Prefs.RestoreStack("2");

				//////////////////////////////////////////////////////////////////
				// Display program version
				// Command: ver
			} else if (cmdInput.matches("^ver.*")) {
				Output.printColorln(Ansi.Color.RED, "Version: v" + VERSION);

				//////////////////////////////////////////////////////////////////
				// Display help information
				// Command: h or ?
			} else if (cmdInput.matches("^[Hh?]")) {
				Output.debugPrint("Displaying Help");
				Help.Display();

				//////////////////////////////////////////////////////////////////
				// Process Exit
				// Command: x or q
			} else if (cmdInput.matches("^[Xx]") || cmdInput.matches("^[Qq]")) {
				Output.debugPrint("Exiting Command Loop");
				ProcessCommandLoop = false;

				/////////////////////////////////////////////////////////////////
				// Change display alignment
				// Command: ar, al, or ad
			} else if (cmdInput.matches("^[Aa][Rr]") || cmdInput.matches("^[Aa][Ll]") || cmdInput.matches("^[Aa][Dd]")) {
				Output.debugPrint("Setting display alignment to: " + cmdInput.toLowerCase().charAt(1));
				displayAlignment = cmdInput.toLowerCase().charAt(1);

				//////////////////////////////////////////////////////////////////
				// Show Undo Stack
				// Command: queryundo
			} else if (cmdInput.matches("^queryundo")) {
				Output.printColorln(Ansi.Color.CYAN, "Current undoStack:");
				for (int j = 0; j <= undoStack.size() - 1; j++) {
					String sn = String.format("%02d:   ", undoStack.size() - j);
					Output.printColor(Ansi.Color.CYAN, sn);
					Output.printColorln(Ansi.Color.CYAN, Format.Comma(undoStack.get(j)));
				}

				//////////////////////////////////////////////////////////////////
				// Process Undo
				// Command: u
			} else if (cmdInput.matches("^[Uu]")) {
				Output.debugPrint("Undoing last command");

				// Backup current stack so we can undo the undo
				Stack<Double> calcStackTemp = (Stack<Double>) calcStack.clone();

				// Replace current stack with the undo stack
				calcStack = (Stack<Double>) undoStack.clone();

				// Add original stack to the undo stack
				undoStack = (Stack<Double>) calcStackTemp.clone();

				//////////////////////////////////////////////////////////////////
				// Clear stack and screen
				// Command: c
			} else if (cmdInput.matches("^[Cc]")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Clearing Stack");
				calcStack.clear();

				// Rather than printing several hundred new lines, use the JANSI clear screen
				Output.clearScreen();

				//////////////////////////////////////////////////////////////////
				// Delete a stack item
				// Command: d
			} else if (cmdInput.matches("^[Dd].*")) {
				// Default to deleting the top of the stack
				int lineToDelete = 0;

				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				// Determine the line number to delete
				try {
					if (cmdInput.substring(1).trim().length() == 0) {
						lineToDelete = 1;
					} else {
						lineToDelete = Integer.parseInt(cmdInput.substring(1).trim());
					}

					// Ensure the number entered is is valid
					if (lineToDelete < 1 || lineToDelete > calcStack.size()) {
						Output.printColorln(Ansi.Color.RED, "Invalid line number entered: " + lineToDelete);
					} else {
						Output.debugPrint("Deleting line number: " + lineToDelete);
						calcStack = StackOps.StackDeleteItem(calcStack, (lineToDelete - 1));
					}

				} catch (Exception e) {
					Output.printColorln(Ansi.Color.RED, "Error parsing line number for element delete: '" + cmdInput.substring(1).trim() + "'");
					Output.debugPrint(e.getMessage());
				}

				//////////////////////////////////////////////////////////////////
				// Perform a square root of the last item on the stack
				// Command: sqrt
			} else if (cmdInput.matches("^sqrt")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Taking the square root of the last stack item");
				Math.SquareRoot(calcStack);

				//////////////////////////////////////////////////////////////////
				// Swap primary and secondary stack
				// Command: ss
			} else if (cmdInput.matches("^[Ss][Ss]")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Swapping primary and secondary stack");
				Stack<Double> calcStackTemp = (Stack<Double>) calcStack.clone();
				calcStack = (Stack<Double>) calcStack2.clone();
				calcStack2 = (Stack<Double>) calcStackTemp.clone();
				Prefs.ToggleCurrentStackNum();

				//////////////////////////////////////////////////////////////////
				// Swap two elements on the stack
				// Command: s
			} else if (cmdInput.matches("^[Ss].*")) {
				int item1 = 1;
				int item2 = 2;

				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				// Determine the source and destination elements
				try {
					if (cmdInput.substring(1).trim().length() != 0) {
						item1 = Integer.parseInt(cmdInput.substring(1).trim().split("\\s")[0]);
						item2 = Integer.parseInt(cmdInput.substring(1).trim().split("\\s")[1]);
					}
				} catch (Exception e) {
					Output.printColorln(Ansi.Color.RED, "Error parsing line number for stack swap: '" + cmdInput.substring(1).trim() + "'");
				}

				// Make sure the numbers are valid
				if (item1 < 1 || item1 > calcStack.size() || item2 < 1 || item2 > calcStack.size()) {
					Output.printColorln(Ansi.Color.RED, "Invalid element entered.  Must be between 1 and " + calcStack.size());
				} else {
					Output.debugPrint("Swapping #" + item1 + " and #" + item2 + " stack items");

					calcStack = StackOps.StackSwapItems(calcStack, (item1 - 1), (item2) - 1);
				}

				//////////////////////////////////////////////////////////////////
				// Flip the sign of last stack element
				// Command: f
			} else if (cmdInput.matches("^[Ff]")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Changing sign of last stack element");
				if (!calcStack.isEmpty())
					calcStack.push(calcStack.pop() * -1);

				//////////////////////////////////////////////////////////////////
				// Copy the item at the top of the stack
				// Command: copy
			} else if (cmdInput.matches("^copy")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Copying the item at the top of the stack");
				if (calcStack.size() >= 1) {
					calcStack.add(calcStack.lastElement());
				} else {
					Output.printColorln(Ansi.Color.RED, "ERROR: Must be an item in the stack to copy it");
				}

				//////////////////////////////////////////////////////////////////
				// Add the value of PI onto the stack
				// Command: pi
			} else if (cmdInput.matches("^pi")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Adding PI to the end of the stack");
				calcStack.add(java.lang.Math.PI);

				//////////////////////////////////////////////////////////////////
				// Operand entered
				//
			} else if (cmdInput.matches("[\\*\\+\\-\\/\\^\\%]")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("CalcStack has " + calcStack.size() + " elements");
				Output.debugPrint("Operand entered: '" + cmdInput.charAt(0) + "'");
				// Verify stack contains at least two elements
				if (calcStack.size() >= 2) {
					calcStack = Math.Parse(cmdInput.charAt(0), calcStack);
				} else {
					Output.printColorln(Ansi.Color.RED, "Two numbers are required for this operation");
				}

				//////////////////////////////////////////////////////////////////
				// Number entered, add to stack. Blank line will trigger so skip if !blank
				//
			} else if (!cmdInput.isEmpty() && cmdInput.matches("^-?\\d*\\.?\\d*")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("Adding number onto the stack");
				calcStack.push(Double.valueOf(cmdInput));

				//////////////////////////////////////////////////////////////////
				// Handle numbers with a single operand at the end (a NumOp)
				//
			} else if (cmdInput.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				Output.debugPrint("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Output.debugPrint("CalcStack has " + calcStack.size() + " elements");
				// Verify stack contains at least one element
				if (calcStack.size() >= 1) {
					char TempOp = cmdInput.charAt(cmdInput.length() - 1);
					String TempNum = cmdInput.substring(0, cmdInput.length() - 1);
					Output.debugPrint("NumOp Found: Num= '" + TempNum + "'");
					Output.debugPrint("NumOp Found: Op = '" + TempOp + "'");
					calcStack.push(Double.valueOf(TempNum));
					calcStack = Math.Parse(TempOp, calcStack);
				} else {
					Output.printColorln(Ansi.Color.RED, "One number is required for this NumOp function");
				}

				//////////////////////////////////////////////////////////////////
				// A blank line is OK, just do nothing
				//
			} else if (cmdInput.matches("")) {
				Output.debugPrint("Blank line entered");

				//////////////////////////////////////////////////////////////////
				// Display an error if the entry matched none of the above
			} else {
				Output.printColorln(Ansi.Color.RED, "Input : '" + cmdInput + "'");
			}

			// Display DashLine
			displayDashedNameLine();

		}

		// Save the primary and secondary stacks
		Prefs.SaveStack(calcStack, "1");
		Prefs.SaveStack(calcStack2, "2");
	}

} // End Program