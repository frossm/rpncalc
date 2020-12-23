/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2020 Michael Fross
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.Stack;

import org.fross.library.Debug;
import org.fross.library.Format;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

import gnu.getopt.Getopt;

/**
 * Main - Main program execution class
 * 
 * @author michael.d.fross
 *
 */
public class Main {

	// Class Constants (or pseudo constants)
	public static final String PROPERTIES_FILE = "app.properties";
	public static int PROGRAMWIDTH = 70;
	public static String VERSION;
	public static String COPYRIGHT;

	// Class Variable
	@SuppressWarnings("rawtypes")
	static Stack<Stack> undoStack = new Stack<Stack>();
	static Stack<Double> calcStack = new Stack<Double>();
	static Stack<Double> calcStack2 = new Stack<Double>();
	static char displayAlignment = 'l';

	/**
	 * DisplayStatusLine(): Display the last line of the header and the separator line. This is a
	 * separate function given it also inserts the loaded stack and spaced everything correctly.
	 * 
	 */
	public static void DisplayStatusLine() {
		// Format the number of memory slots used
		String sfMem = String.format("Mem:%02d", StackOps.QueryInUseMemorySlots());

		// Format the undo level to 2 digits. Can't image I'd need over 99 undo levels
		String sfUndo = String.format("Undo:%02d", Main.undoStack.size());

		// Determine how many dashes to use after remove space for the undo and stack name
		int numDashes = PROGRAMWIDTH - 2 - sfMem.length() - sfUndo.length() - Prefs.QueryLoadedStack().length() - 11;

		// Print the StatusLine dashes
		Output.printColor(Ansi.Color.CYAN, "+");
		Output.printColor(Ansi.Color.CYAN, "-".repeat(numDashes));

		// Print the StatusLine Data in chunks to be able to better control color output
		Output.printColor(Ansi.Color.CYAN, "[");
		Output.printColor(Ansi.Color.WHITE, sfMem);
		Output.printColor(Ansi.Color.CYAN, "]-[");
		Output.printColor(Ansi.Color.WHITE, sfUndo);
		Output.printColor(Ansi.Color.CYAN, "]-[");
		Output.printColor(Ansi.Color.WHITE, Prefs.QueryLoadedStack() + ":" + Prefs.QueryCurrentStackNum());
		Output.printColor(Ansi.Color.CYAN, "]-");
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
		String cmdInput = "";		// What the user enters
		String cmdInputCmd = "";	// The first field. The command.
		String cmdInputParam = "";	// The remaining string. Parameters

		// Process application level properties file
		// Update properties from Maven at build time:
		// https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
			COPYRIGHT = "Copyright (C) " + prop.getProperty("Application.inceptionYear") + "-" + org.fross.library.Date.getCurrentYear() + " by Michael Fross";
		} catch (IOException ex) {
			Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
		}

		// Process Command Line Options and set flags where needed
		Getopt optG = new Getopt("RPNCalc", args, "Dl:a:m:w:vz?");
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

			case 'm':
				StackOps.SetMaxMemorySlots(optG.getOptarg());
				break;

			case 'w':
				try {
					int newSize = Integer.parseInt(optG.getOptarg());
					if (newSize < COPYRIGHT.length()) {
						Output.printColorln(Ansi.Color.RED, "Error.  Minimum width is " + (COPYRIGHT.length() + 4) + ". Setting width to that value.");
						PROGRAMWIDTH = COPYRIGHT.length() + 4;
					} else {
						PROGRAMWIDTH = newSize;
					}
				} catch (NumberFormatException ex) {
					Output.fatalError("Incorrect width value provided: '" + optG.getOptarg() + "'", 2);
				}
				break;

			case 'v': // Version Display
				Output.printColorln(Ansi.Color.YELLOW, "Version: v" + VERSION);
				Output.printColorln(Ansi.Color.CYAN, COPYRIGHT);
				System.exit(0);
				break;

			case 'z': // Disable colorized output
				Output.enableColor(false);
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
		Debug.displaySysInfo();
		Output.debugPrint("Command Line Options");
		Output.debugPrint("  -D:  " + Debug.query());
		Output.debugPrint("  -l:  " + Prefs.QueryLoadedStack());
		Output.debugPrint("  -a:  " + displayAlignment);

		// Pull the existing stacks from the preferences if they exist
		calcStack = Prefs.RestoreStack("1");
		calcStack2 = Prefs.RestoreStack("2");
		Output.debugPrint("Elements in the Stack: " + calcStack.size());

		// Display the initial program header information
		Output.printColorln(Ansi.Color.CYAN, "+" + "-".repeat(PROGRAMWIDTH - 2) + "+");
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(PROGRAMWIDTH, "RPN Calculator  v" + VERSION, "|", "|"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(PROGRAMWIDTH, COPYRIGHT, "|", "|"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(PROGRAMWIDTH, "Enter command 'h' for help details", "|", "|"));

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			int maxDigitsBeforeDecimal = 0;

			// Display the dashed status line
			DisplayStatusLine();

			// Loop through the stack and count the max digits before the decimal for use with the decimal
			// alignment mode
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

				// Display Stack Row Number
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

			// Break each line entered into a command and a parameter string
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

			// Main switch statement to process user input and call the correct functions
			switch (cmdInputCmd) {

			/*********************************************
			 * Calculator Commands
			 *********************************************/
			// Undo
			case "undo":
			case "u":
				StackOps.cmdUndo();
				break;

			// Flip Sign
			case "flip":
			case "f":
				StackOps.cmdFlipSign();
				break;

			// Clear Screen and Stack
			case "clear":
			case "c":
				StackOps.cmdClear();
				break;

			// Clean the screen and redisplay the stack
			case "clean":
			case "cl":
				StackOps.cmdClean();
				break;

			// Delete
			case "delete":
			case "del":
			case "d":
				// If Parameter is empty, delete the value on the top of the stack
				if (cmdInputParam.isEmpty())
					StackOps.cmdDelete("1");
				else
					StackOps.cmdDelete(cmdInputParam);
				break;

			// Percent
			case "%":
				Output.debugPrint("Create a percent by dividing by 100");
				undoStack.push((Stack<Double>) calcStack.clone());
				calcStack.push(calcStack.pop() / 100);
				break;

			// Swap Elements in a stack
			case "swap":
			case "s":
				StackOps.cmdSwapElements(cmdInputParam);
				break;

			// Square Root
			case "sqrt":
				StackOps.cmdSqrt();
				break;

			// Round
			case "round":
				StackOps.cmdRound(cmdInputParam);
				break;

			// AddAll
			case "aa":
				StackOps.cmdAddAll(cmdInputParam);
				break;

			// Modulus
			case "mod":
				StackOps.cmdMod();
				break;

			// Copy Item
			case "copy":
				StackOps.cmdCopy();
				break;

			// Natural (base e) Logarithm
			case "log":
				StackOps.cmdLog();
				break;

			// Base10 Logarithm
			case "log10":
				StackOps.cmdLog10();
				break;

			// Random Number Generation
			case "rand":
			case "random":
				StackOps.cmdRandom(cmdInputParam);
				break;

			// Dice
			case "dice":
				StackOps.cmdDice(cmdInputParam);
				break;

			// Fraction
			case "frac":
			case "fraction":
				StackOps.cmdFraction(cmdInputParam);
				break;

			/*********************************************
			 * Trigonometry Functions
			 *********************************************/
			// Radians
			case "rad":
			case "radian":
				StackOps.cmdRadian();
				break;

			case "deg":
			case "degree":
				StackOps.cmdDegree();
				break;

			// Trigonometry Functions
			case "tan":
			case "sin":
			case "cos":
				StackOps.cmdTrig(cmdInputCmd, cmdInputParam);
				break;

			// Arc-Trigonometry Functions
			case "atan":
			case "asin":
			case "acos":
				StackOps.cmdArcTrig(cmdInputCmd, cmdInputParam);
				break;

			// Hypotenuse
			case "hypot":
			case "hypotenuse":
				StackOps.cmdHypotenuse();
				break;

			/*********************************************
			 * Memory Functions
			 *********************************************/
			case "memory":
			case "mem":
				StackOps.cmdMem(cmdInputParam);
				break;

			/*********************************************
			 * Constants
			 *********************************************/
			// Add PI
			case "pi":
				undoStack.push((Stack<Double>) calcStack.clone());
				Output.printColorln(Ansi.Color.CYAN, "PI to the stack");
				calcStack.add(java.lang.Math.PI);
				break;

			// Add PHI also known as The Golden Ratio
			case "phi":
				undoStack.push((Stack<Double>) calcStack.clone());
				Output.printColorln(Ansi.Color.CYAN, "Phi, the golden ratio, added to the stack");
				calcStack.add(1.61803398874989);
				break;

			// Euler's number
			case "euler":
				undoStack.push((Stack<Double>) calcStack.clone());
				Output.printColorln(Ansi.Color.CYAN, "Euler's number (e) to the stack");
				calcStack.add(2.7182818284590452353602874713527);
				break;

			/*********************************************
			 * Operational Commands
			 *********************************************/
			// List
			// Supported commands are "stacks" | "mem" | "undo"
			case "list":
				StackOps.cmdList(cmdInputParam);
				break;

			// Load
			case "load":
				StackOps.cmdLoad(cmdInputParam);
				break;

			// Swap Stack
			case "ss":
				StackOps.cmdSwapStack();
				break;

			// Debug Toggle
			case "debug":
				StackOps.cmdDebug();
				break;

			// Display Alignment
			case "a":
				try {
					StackOps.cmdAlign(cmdInputParam.charAt(0));
				} catch (StringIndexOutOfBoundsException ex) {
					Output.printColorln(Ansi.Color.RED, "ERROR: Must provide an alignment value of 'l'eft, 'd'ecimal, or 'r'ight");
				}
				break;

			// Version
			case "ver":
			case "version":
				Output.printColorln(Ansi.Color.YELLOW, "Version: v" + VERSION);
				Output.printColorln(Ansi.Color.CYAN, COPYRIGHT);
				break;

			// Help
			case "h":
			case "?":
			case "help":
				Help.Display();
				break;

			// Clear & Exit
			case "cx":
				Main.calcStack.clear();
				Output.debugPrint("Exiting Command Loop");
				ProcessCommandLoop = false;
				break;

			// Exit
			case "x":
			case "exit":
				Output.debugPrint("Exiting Command Loop");
				ProcessCommandLoop = false;
				break;

			/*********************************************
			 * Operands
			 *********************************************/
			case "+":
			case "-":
			case "*":
			case "/":
			case "^":
				StackOps.cmdOperand(cmdInputCmd);
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

					Output.debugPrint("Adding number '" + cmdInputCmd + "' onto the stack");
					calcStack.push(Double.valueOf(cmdInputCmd));

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
