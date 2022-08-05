/******************************************************************************
/ * RPNCalc
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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class CommandParser {

	/**
	 * Parse(): Take the user input and send it to the executing function
	 * 
	 * @param calcStack
	 * @param calcStack2
	 * @param cmdInput
	 * @param cmdInputCmd
	 * @param cmdInputParam
	 */
	public static void Parse(StackObj calcStack, StackObj calcStack2, String cmdInput, String cmdInputCmd, String cmdInputParam) {
		// Massive switch statement to process user input and call the correct functions
		switch (cmdInputCmd) {

		/*******************************************************************************
		 * Stack Commands
		 ******************************************************************************/
		// Undo
		case "undo":
		case "u":
			StackCommands.cmdUndo(calcStack, cmdInputParam);
			break;

		// Flip Sign
		case "flip":
		case "f":
			StackCommands.cmdFlipSign(calcStack);
			break;

		// Clear Screen and Stack
		case "clear":
		case "c":
			StackCommands.cmdClear(calcStack);
			break;

		// Clean the screen and redisplay the stack
		case "clean":
		case "cl":
			StackCommands.cmdClean();
			break;

		// Delete
		case "del":
		case "d":
			StackCommands.cmdDelete(calcStack, cmdInputParam);
			break;

		// Percent
		case "%":
			StackCommands.cmdPercent(calcStack);
			break;

		// Swap Elements in a stack
		case "swap":
		case "s":
			StackCommands.cmdSwapElements(calcStack, cmdInputParam);
			break;

		// Square Root
		case "sqrt":
			StackCommands.cmdSqrt(calcStack);
			break;

		// Round
		case "round":
			StackCommands.cmdRound(calcStack, cmdInputParam);
			break;

		// AddAll
		case "aa":
			StackCommands.cmdAddAll(calcStack, cmdInputParam);
			break;

		// Modulus
		case "mod":
			StackCommands.cmdModulus(calcStack);
			break;

		// Average
		case "mean":
		case "average":
		case "avg":
			StackCommands.cmdAverage(calcStack, cmdInputParam);
			break;

		// Standard Deviation
		case "sd":
			StackCommands.cmdStdDeviation(calcStack, cmdInputParam);
			break;

		// Copy Item
		case "copy":
		case "dup":
			StackCommands.cmdCopy(calcStack, cmdInputParam);
			break;

		// Natural (base e) Logarithm
		case "log":
			StackCommands.cmdLog(calcStack);
			break;

		// Base10 Logarithm
		case "log10":
			StackCommands.cmdLog10(calcStack);
			break;

		// Integer
		case "int":
			StackCommands.cmdInteger(calcStack);
			break;

		// Absolute Value
		case "abs":
			StackCommands.cmdAbsoluteValue(calcStack);
			break;

		// Minimum Value
		case "min":
			if (StackCommands.cmdMinimum(calcStack) == true) {
				Output.printColorln(Ansi.Color.CYAN, "Minimum value added to stack: " + calcStack.peek());
			}
			break;

		// Maximum Value
		case "max":
			if (StackCommands.cmdMaximum(calcStack) == true) {
				Output.printColorln(Ansi.Color.CYAN, "Maximum value added to stack: " + calcStack.peek());
			}
			break;

		// Random Number Generation
		case "rand":
		case "random":
			StackCommands.cmdRandom(calcStack, cmdInputParam);
			break;

		// Dice
		case "dice":
			StackCommands.cmdDice(calcStack, cmdInputParam);
			break;

		/*******************************************************************************
		 * Stack Conversions
		 ******************************************************************************/
		// Fraction
		case "frac":
		case "fraction":
			String[] outString = StackConversions.cmdFraction(calcStack, cmdInputParam);
			Output.printColorln(Ansi.Color.YELLOW, outString[0]);
			Output.printColorln(Ansi.Color.WHITE, outString[1]);
			Output.printColorln(Ansi.Color.YELLOW, outString[2]);
			break;

		// Convert inches to millimeters
		case "in2mm":
		case "2mm":
			StackConversions.cmdConvertIN2MM(calcStack);
			break;

		// Convert millimeters to inches
		case "mm2in":
		case "2in":
			StackConversions.cmdConvertMM2IN(calcStack);
			break;

		// Convert to Radians
		case "deg2rad":
		case "2rad":
			StackConversions.cmdDeg2Rad(calcStack);
			break;

		// Convert to Degrees
		case "rad2deg":
		case "2deg":
			StackConversions.cmdRad2Deg(calcStack);
			break;

		/*******************************************************************************
		 * Stack Trigonometry Functions
		 ******************************************************************************/
		// Trigonometry Functions
		case "tan":
		case "sin":
		case "cos":
			StackTrig.cmdTrig(calcStack, cmdInputCmd, cmdInputParam);
			break;

		// Arc-Trigonometry Functions
		case "atan":
		case "asin":
		case "acos":
			StackTrig.cmdArcTrig(calcStack, cmdInputCmd, cmdInputParam);
			break;

		// Hypotenuse
		case "hypot":
		case "hypotenuse":
			StackTrig.cmdHypotenuse(calcStack);
			break;

		/*******************************************************************************
		 * Stack Memory Functions
		 ******************************************************************************/
		case "memory":
		case "mem":
			StackMemory.cmdMem(calcStack, cmdInputParam);
			break;

		/*******************************************************************************
		 * Constants
		 ******************************************************************************/
		// Add PI
		case "pi":
			StackConstants.cmdPI(calcStack);
			Output.printColorln(Ansi.Color.CYAN, "The value PI added to the stack");
			break;

		// Add PHI also known as The Golden Ratio
		case "phi":
			StackConstants.cmdPHI(calcStack);
			Output.printColorln(Ansi.Color.CYAN, "Phi, the golden ratio, added to the stack");
			break;

		// Euler's number
		case "euler":
			StackConstants.cmdEuler(calcStack);
			Output.printColorln(Ansi.Color.CYAN, "Euler's number (e) to the stack");
			break;

		// Speed of light
		case "sol":
		case "speedoflight":
			StackConstants.cmdSpeedOfLight(calcStack);
			Output.printColorln(Ansi.Color.CYAN, "Speed of Light (c) added to the stack");
			break;

		/*******************************************************************************
		 * User Defined Functions
		 ******************************************************************************/
		// Turn recording on or off
		case "rec":
		case "record":
			UserFunctions.cmdRecord(cmdInputParam);
			break;

		// Allows for running or deleting functions
		case "func":
		case "function":
			UserFunctions.cmdFunction(cmdInputParam);
			break;

		/*******************************************************************************
		 * Stack Operational Commands
		 ******************************************************************************/
		// List
		case "list":
			StackOperations.cmdList(calcStack, cmdInputParam);
			break;

		// Load new or existing stack
		case "load":
			StackOperations.cmdLoad(calcStack, calcStack2, cmdInputParam);
			break;

		// Import stack from disk
		case "import":
			StackOperations.importStackFromDisk(calcStack, cmdInputParam);
			break;

		// Swap Stack
		case "ss":
			StackOperations.cmdSwapStack(calcStack, calcStack2);
			break;

		// Debug Toggle
		case "debug":
			StackOperations.cmdDebug();
			break;

		// Set configuration options
		case "set":
			StackOperations.cmdSet(cmdInputParam);
			break;

		// Reset configuration to defaults
		case "reset":
			StackOperations.cmdReset();
			break;

		// Version
		case "ver":
		case "version":
			Main.DisplayVersion();
			break;

		// Help
		case "h":
		case "?":
		case "help":
			Help.Display();
			break;

		// Exit
		case "cx":
			calcStack.clear();
		case "x":
		case "exit":
		case "quit":
			Output.debugPrint("Exiting Command Loop");
			Main.ProcessCommandLoop = false;
			break;

		/*******************************************************************************
		 * Operands
		 ******************************************************************************/
		case "+":
		case "-":
		case "*":
		case "/":
		case "^":
			StackCommands.cmdOperand(calcStack, cmdInputCmd);
			break;

		default:
			// Determine if the command is a user defined function
			// Verify user defined function exists
			if (UserFunctions.FunctionExists(cmdInput) == true) {
				Output.debugPrint("Executing User Defined Function: '" + cmdInput + "'");
				UserFunctions.FunctionRun(calcStack, calcStack2, cmdInput);

				// Check for a fraction. If number entered contains a '/' but it's not at the end, then it must be a fraction.
			} else if (cmdInput.contains("/") && !cmdInput.substring(cmdInput.length() - 1).matches("/")) {
				Output.debugPrint("Fraction has been entered");
				try {
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

					// Save current calcStack to the undoStack
					calcStack.saveUndo();

					// Add the decimal number to the stack and continue with next command
					calcStack.push(fracInteger + fracDecimalEquiv);

				} catch (NumberFormatException ex) {
					Output.printColorln(Ansi.Color.RED, "Illegal Fraction Entered: '" + cmdInput + "'");
					break;
				}

				// Number entered, add to stack.
			} else if (cmdInputCmd.matches("^-?\\d*\\.?\\d*")) {
				// Save current calcStack to the undoStack
				calcStack.saveUndo();

				Output.debugPrint("Adding number '" + cmdInputCmd + "' onto the stack");
				calcStack.push(Double.valueOf(cmdInputCmd));

				// Handle numbers with a single operand at the end (a NumOp)
			} else if (cmdInputCmd.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				// Save current calcStack to the undoStack
				calcStack.saveUndo();

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
				// If a user enters an invalid command, remove it from the recording if enabled
				if (UserFunctions.recordingEnabled == true) {
					Output.debugPrint("Removing '" + UserFunctions.recording.get(UserFunctions.recording.size() - 1) + "' from the recording");
					UserFunctions.RemoveItemFromRecording();
				}

				// Let user know a bad command was provided
				Output.printColorln(Ansi.Color.RED, "Unknown Command: '" + cmdInput + "'");

			}
			break;
		}
	}

}