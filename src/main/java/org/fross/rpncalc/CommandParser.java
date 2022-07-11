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

import java.util.Stack;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class CommandParser {

	/**
	 * Parse(): Take the user input and send it to the executing function
	 * 
	 * @param cmdInputCmd
	 * @param cmdInputParam
	 */
	@SuppressWarnings("unchecked")
	public static void Parse(String cmdInput, String cmdInputCmd, String cmdInputParam) {
		// Main switch statement to process user input and call the correct functions
		switch (cmdInputCmd) {

		/*******************************************************************************
		 * Stack Commands
		 ******************************************************************************/
		// Undo
		case "undo":
		case "u":
			StackCommands.cmdUndo(cmdInputParam);
			break;

		// Flip Sign
		case "flip":
		case "f":
			StackCommands.cmdFlipSign();
			break;

		// Clear Screen and Stack
		case "clear":
		case "c":
			StackCommands.cmdClear();
			break;

		// Clean the screen and redisplay the stack
		case "clean":
		case "cl":
			StackCommands.cmdClean();
			break;

		// Delete
		case "del":
		case "d":
		case "drop":
			StackCommands.cmdDelete(cmdInputParam);
			break;

		// Percent
		case "%":
			StackCommands.cmdPercent();
			break;

		// Swap Elements in a stack
		case "swap":
		case "s":
			StackCommands.cmdSwapElements(cmdInputParam);
			break;

		// Square Root
		case "sqrt":
			StackCommands.cmdSqrt();
			break;

		// Round
		case "round":
			StackCommands.cmdRound(cmdInputParam);
			break;

		// AddAll
		case "aa":
			StackCommands.cmdAddAll(cmdInputParam);
			break;

		// Modulus
		case "mod":
			StackCommands.cmdModulus();
			break;

		// Average
		case "mean":
		case "average":
		case "avg":
			StackCommands.cmdAverage(cmdInputParam);
			break;

		// Standard Deviation
		case "sd":
			StackCommands.cmdStdDeviation(cmdInputParam);
			break;

		// Copy Item
		case "copy":
		case "dup":
			StackCommands.cmdCopy(cmdInputParam);
			break;

		// Natural (base e) Logarithm
		case "log":
			StackCommands.cmdLog();
			break;

		// Base10 Logarithm
		case "log10":
			StackCommands.cmdLog10();
			break;

		// Integer
		case "int":
			StackCommands.cmdInteger();
			break;

		// Absolute Value
		case "abs":
			StackCommands.cmdAbsoluteValue();
			break;

		// Minimum Value
		case "min":
			StackCommands.cmdMinimum();
			break;

		// Maximum Value
		case "max":
			StackCommands.cmdMaximum();
			break;

		// Random Number Generation
		case "rand":
		case "random":
			StackCommands.cmdRandom(cmdInputParam);
			break;

		// Dice
		case "dice":
			StackCommands.cmdDice(cmdInputParam);
			break;

		/*******************************************************************************
		 * Stack Conversions
		 ******************************************************************************/
		// Fraction
		case "frac":
		case "fraction":
			StackConversions.cmdFraction(cmdInputParam);
			break;

		// Convert inches to millimeters
		case "in2mm":
		case "2mm":
			StackConversions.cmdConvert2MM();
			break;

		// Convert millimeters to inches
		case "mm2in":
		case "2in":
			StackConversions.cmdConvert2IN();
			break;

		// Convert to Radians
		case "deg2rad":
		case "2rad":
			StackConversions.cmdRadian();
			break;

		// Convert to Degrees
		case "rad2deg":
		case "2deg":
			StackConversions.cmdDegree();
			break;

		/*******************************************************************************
		 * Stack Trigonometry Functions
		 ******************************************************************************/
		// Trigonometry Functions
		case "tan":
		case "sin":
		case "cos":
			StackTrig.cmdTrig(cmdInputCmd, cmdInputParam);
			break;

		// Arc-Trigonometry Functions
		case "atan":
		case "asin":
		case "acos":
			StackTrig.cmdArcTrig(cmdInputCmd, cmdInputParam);
			break;

		// Hypotenuse
		case "hypot":
		case "hypotenuse":
			StackTrig.cmdHypotenuse();
			break;

		/*******************************************************************************
		 * Stack Memory Functions
		 ******************************************************************************/
		case "memory":
		case "mem":
			StackMemory.cmdMem(cmdInputParam);
			break;

		/*******************************************************************************
		 * Constants
		 ******************************************************************************/
		// Add PI
		case "pi":
			StackConstants.cmdPI();
			break;

		// Add PHI also known as The Golden Ratio
		case "phi":
			StackConstants.cmdPHI();
			break;

		// Euler's number
		case "euler":
			StackConstants.cmdEuler();
			break;

		// Speed of light
		case "sol":
		case "speedoflight":
			StackConstants.cmdSpeedOfLight();
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
		// Supported commands are "stacks" | "mem" | "undo"
		case "list":
			StackOperations.cmdList(cmdInputParam);
			break;

		// Load
		case "load":
			StackOperations.cmdLoad(cmdInputParam);
			break;

		// Import stack from disk
		case "import":
			StackOperations.LoadStackFromDisk(cmdInputParam);
			break;

		// Swap Stack
		case "ss":
			StackOperations.cmdSwapStack();
			break;

		// Debug Toggle
		case "debug":
			StackOperations.cmdDebug();
			break;

		// Display Alignment
		case "a":
			try {
				StackOperations.cmdAlign(cmdInputParam.charAt(0));
			} catch (StringIndexOutOfBoundsException ex) {
				Output.printColorln(Ansi.Color.RED, "ERROR: Must provide an alignment value of 'l'eft, 'd'ecimal, or 'r'ight");
			}
			break;

		// Version
		case "ver":
		case "version":
			Output.printColorln(Ansi.Color.YELLOW, "Version: v" + Main.VERSION);
			Output.printColorln(Ansi.Color.CYAN, Main.COPYRIGHT);
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
			Main.ProcessCommandLoop = false;
			break;

		// Exit
		case "x":
		case "exit":
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
			StackCommands.cmdOperand(cmdInputCmd);
			break;

		default:
			// Determine if the command is a user defined function
			// Verify user defined function exists
			if (UserFunctions.FunctionExists(cmdInput) == true) {
				Output.debugPrint("Executing User Defined Function: '" + cmdInput + "'");
				UserFunctions.FunctionRun(cmdInput);

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

					// Save to Undo stack
					Main.undoStack.push((Stack<Double>) Main.calcStack.clone());
					
					// Add the decimal number to the stack and continue with next command
					Main.calcStack.add(fracInteger + fracDecimalEquiv);
					
				} catch (NumberFormatException ex) {
					Output.printColorln(Ansi.Color.RED, "Illegal Fraction Entered: '" + cmdInput + "'");
					break;
				}

				// Number entered, add to stack.
			} else if (cmdInputCmd.matches("^-?\\d*\\.?\\d*")) {
				// Save to Undo stack
				Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

				Output.debugPrint("Adding number '" + cmdInputCmd + "' onto the stack");
				Main.calcStack.push(Double.valueOf(cmdInputCmd));

				// Handle numbers with a single operand at the end (a NumOp)
			} else if (cmdInputCmd.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				// Save to Undo stack
				Main.undoStack.push((Stack<Double>) Main.calcStack.clone());

				Output.debugPrint("CalcStack has " + Main.calcStack.size() + " elements");
				// Verify stack contains at least one element
				if (Main.calcStack.size() >= 1) {
					String TempOp = cmdInputCmd.substring(cmdInputCmd.length() - 1, cmdInputCmd.length());
					String TempNum = cmdInput.substring(0, cmdInput.length() - 1);
					Output.debugPrint("NumOp Found: Num= '" + TempNum + "'");
					Output.debugPrint("NumOp Found: Op = '" + TempOp + "'");
					Main.calcStack.push(Double.valueOf(TempNum));
					Main.calcStack = Math.Parse(TempOp, Main.calcStack);
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
