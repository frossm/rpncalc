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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
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
	public static String INCEPTIONYEAR;
	public static final String PROPERTIES_FILE = "app.properties";

	// Class Variable
	@SuppressWarnings("rawtypes")
	static Stack<Stack> undoStack = new Stack<Stack>();
	static Stack<Double> calcStack = new Stack<Double>();
	static Stack<Double> calcStack2 = new Stack<Double>();
	static char displayAlignment = 'l';

	/**
	 * DisplayDashedNameLine(): Display the last line of the header and the separator line. This is a
	 * separate function given it also inserts the loaded stack and spaced everything correctly.
	 */
	public static void displayDashedNameLine() {
		int dashLineLength = 70;

		// Display the Loaded Stack into Dash line. 70 dashes w/o the name
		Output.printColor(Ansi.Color.CYAN, "+");

		// Determine how many dashes to use after remove space for the undo and stack
		// name
		int numDashes = dashLineLength - 10 - Prefs.QueryLoadedStack().length() - 4;

		// Print the dashes
		for (int i = 0; i < numDashes; i++) {
			Output.printColor(Ansi.Color.CYAN, "-");
		}

		// Format the undo level to 2 digits. Can't image I'd need over 99 undo levels
		String sf = String.format("%02d", Main.undoStack.size());

		// Print the Undo header information
		Output.printColor(Ansi.Color.CYAN, "[");
		Output.printColor(Ansi.Color.YELLOW, "Undo:" + sf);
		Output.printColor(Ansi.Color.CYAN, "]-[");
		Output.printColor(Ansi.Color.YELLOW, Prefs.QueryLoadedStack() + ":" + Prefs.QueryCurrentStackNum());
		Output.printColor(Ansi.Color.CYAN, "]");
		Output.printColorln(Ansi.Color.CYAN, "+");
	}

	/**
	 * Main(): Start of program and holds main command loop
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean ProcessCommandLoop = true;
		int optionEntry;
		String cmdInput = "";
		String cmdInputCmd = "";
		String cmdInputParam = "";

		// Process application level properties file
		// Update properties from Maven at build time:
		// https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
			INCEPTIONYEAR = prop.getProperty("Application.inceptionYear");
		} catch (IOException ex) {
			Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
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

		// Display output header information
		Output.printColorln(Ansi.Color.CYAN, "+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.CYAN, "|                           RPN Calculator                 v" + VERSION + " |");
		Output.printColorln(Ansi.Color.CYAN,
				"|      Copyright " + INCEPTIONYEAR + "-" + org.fross.library.Date.getCurrentYear() + " by Michael Fross.  All rights reserved      |");
		Output.printColorln(Ansi.Color.CYAN, "|                 Enter command 'h' for help details                   |");

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			int maxDigitsBeforeDecimal = 0;

			// Display the dashed status line
			displayDashedNameLine();

			// Loop through the stack and count the max digits before the decimal for use
			// with the decimal alignment mode
			for (int k = 0; k < calcStack.size(); k++) {
				int decimalIndex = Format.Comma(calcStack.get(k)).indexOf(".");
				// If current stack item has more digits ahead of decimal make that the max.
				// Commas are included.
				if (maxDigitsBeforeDecimal < decimalIndex) {
					maxDigitsBeforeDecimal = decimalIndex;
				}
			}

			// Display the current stack
			for (int i = 0; i < calcStack.size(); i++) {

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
					// Add a few extra digits to maxDigitsBeforeDecimal
					sn = String.format("%" + (maxDigitsBeforeDecimal + 7) + "s", Format.Comma(calcStack.get(i)));

				} else {
					sn = Format.Comma(calcStack.get(i));
				}

				// Output.printColorln(Ansi.Color.WHITE, Math.Comma(calcStack.get(i)));
				Output.printColorln(Ansi.Color.WHITE, sn);
			}

			// Input command from user
			Output.printColor(Ansi.Color.YELLOW, "\n>>  ");
			cmdInput = scanner.nextLine();

			// Break each entered line into a command and parameters
			try {
				String[] ci = cmdInput.toLowerCase().trim().split("\\s+", 2);
				cmdInputCmd = ci[0];
				cmdInputParam = ci[1];
				Output.debugPrint("Entered: '" + cmdInput + "'  Command: '" + cmdInputCmd + "' Parameter: '" + cmdInputParam + "'");

			} catch (ArrayIndexOutOfBoundsException e) {
				// Ignore if there is no command or parameter entered
				Output.debugPrint("Entered: '" + cmdInput + "'  Command: '" + cmdInputCmd + "' Parameter: '" + cmdInputParam + "'");
				if (cmdInputCmd.isEmpty()) {
					Output.debugPrint("Blank line entered");
					continue;
				}
			}

			// Main switch to process user input into functions
			switch (cmdInputCmd) {

			// Debug Toggle
			case "debug":
				StackOps.cmdDebug();
				break;

			// Version
			case "ver":
			case "version":
				Output.printColorln(Ansi.Color.YELLOW, "Version: v" + VERSION);
				break;

			// Load
			case "load":
				StackOps.cmdLoad(cmdInputParam);
				break;

			// Exit
			case "x":
			case "exit":
				Output.debugPrint("Exiting Command Loop");
				ProcessCommandLoop = false;
				break;

			// Display Alignment
			case "a":
				StackOps.cmdAlign(cmdInputParam.charAt(0));
				break;

			// ListUndo
			case "listundo":
				StackOps.cmdListUndo(undoStack);
				break;

			// Undo
			case "u":
				StackOps.cmdUndo();
				break;

			// Clear Screen and Stack
			case "c":
				StackOps.cmdClear();
				break;

			// Delete
			case "d":
				// If Param is empty, assume delete top of stack item
				if (cmdInputParam.isEmpty())
					StackOps.cmdDelete(1);
				else
					StackOps.cmdDelete(Integer.parseInt(cmdInputParam));
				break;

			// Square Root
			case "sqrt":
				StackOps.cmdSqrt();
				break;

			// Swap Stack
			case "ss":
				StackOps.cmdSwapStack();
				break;

			// Random Number Generation
			case "rand":
			case "random":
				StackOps.cmdRandom(cmdInputParam);
				break;

			// Fraction
			case "frac":
			case "fraction":
				StackOps.cmdFraction(cmdInputParam);
				break;

			// Dice
			case "dice":
				StackOps.cmdDice(cmdInputParam);
				break;

			// Swap Elements in a stack
			case "s":
				StackOps.cmdSwapElements(cmdInputParam);
				break;

			// Flip Sign
			case "f":
				StackOps.cmdFlipSign();
				break;

			// Copy Item
			case "copy":
				StackOps.cmdCopy();
				break;

			// Add PI
			case "pi":
				undoStack.push((Stack<Double>) calcStack.clone());
				Output.debugPrint("Adding PI to the end of the stack");
				calcStack.add(java.lang.Math.PI);
				break;

			// Percent
			case "%":
				Output.debugPrint("Create a percent by dividing by 100");
				undoStack.push((Stack<Double>) calcStack.clone());
				calcStack.push(calcStack.pop() / 100);
				break;

			// Operand
			case "+":
			case "-":
			case "*":
			case "/":
			case "^":
				StackOps.cmdOperand(cmdInputCmd);
				break;

			case "h":
			case "?":
			case "help":
				Help.Display();
				break;

			default:
				// Check for a fraction. If number entered contains a '/' but it's not at the
				// end, then it must be a fraction.
				if (cmdInput.contains("/") && !cmdInput.substring(cmdInput.length() - 1).matches("/")) {
					long fracInteger = 0;
					double fracDecimalEquiv = 0.0;

					// If there wasn't an integer entered, move the fraction to the parameter
					// variable
					if (cmdInputCmd.contains("/")) {
						cmdInputParam = cmdInputCmd;
					} else {
						fracInteger = Long.parseLong(cmdInputCmd);
					}

					double fracTop = Double.parseDouble(cmdInputParam.substring(0, cmdInputParam.indexOf('/')));
					double fracBottom = Double.parseDouble(cmdInputParam.substring(cmdInputParam.indexOf('/') + 1));

					// Divide the fraction and get a decimal equivalent
					fracDecimalEquiv = fracTop / fracBottom;

					// Simply convert the fraction to a decimal and add it to the stack
					Output.debugPrint("Fraction Entered: '" + cmdInput + "' Decimal: " + (fracInteger + fracDecimalEquiv));

					// Add the decimal number to the stack and continue with next command
					calcStack.add(fracInteger + fracDecimalEquiv);

					// Number entered, add to stack.
				} else if (cmdInputCmd.matches("^-?\\d*\\.?\\d*")) {
					// Save to Undo stack
					undoStack.push((Stack<Double>) calcStack.clone());

					Output.debugPrint("Adding number onto the stack");
					calcStack.push(Double.valueOf(cmdInput));

					// Handle numbers with a single operand at the end (a NumOp)
				} else if (cmdInputCmd.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
					// Save to Undo stack
					undoStack.push((Stack<Double>) calcStack.clone());

					Output.debugPrint("CalcStack has " + calcStack.size() + " elements");
					// Verify stack contains at least one element
					if (calcStack.size() >= 1) {
						String TempOp = cmdInputCmd.substring(cmdInputCmd.length() - 1, cmdInputCmd.length());
						String TempNum = cmdInput.substring(0, cmdInput.length() - 1);
						Output.debugPrint("NumOp Found: Num= '" + TempNum + "'");
						Output.debugPrint("NumOp Found: Op = '" + TempOp + "'");
						calcStack.push(Double.valueOf(TempNum));
						calcStack = Math.Parse(TempOp, calcStack);
					} else {
						Output.printColorln(Ansi.Color.RED, "One number is required for this NumOp function");
					}

				} else {
					Output.printColorln(Ansi.Color.RED, "Unknown Command: '" + cmdInput + "'");
				}
				break;
			}

			// Clear input parameters before we start again
			cmdInputCmd = "";
			cmdInputParam = "";

		} // End While Loop

		// Close the scanner
		scanner.close();

		// Save the primary and secondary stacks
		Prefs.SaveStack(calcStack, "1");
		Prefs.SaveStack(calcStack2, "2");

	} // End Main

} // End Class