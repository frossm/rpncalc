/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: GNU General Public License v3.
 *           http://www.gnu.org/licenses/gpl-3.0.html
 *           
 ******************************************************************************/
package org.fross.rpn;

import java.io.Console;
import java.util.Stack;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import gnu.getopt.Getopt;

/**
 * Main - Main program execution class
 * 
 * @author michael.d.fross
 *
 */
public class Main {

	// Class Constants
	public static final String VERSION = "2019.02.22";

	/**
	 * Main(): Start of program and holds main command loop
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Console con = null;
		Stack<Double> calcStack = new Stack<Double>();
		Stack<Double> calcStack2 = new Stack<Double>();
		boolean ProcessCommandLoop = true;
		int optionEntry;

		// Init the console used for command input
		con = System.console();
		if (con == null) {
			Output.printError("FATAL ERROR:  Could not initialize OS Console for data input");
			System.exit(1);
		}

		// Process Command Line Options and set flags where needed
		Getopt optG = new Getopt("DirSize", args, "Dl:h?");
		while ((optionEntry = optG.getopt()) != -1) {
			switch (optionEntry) {
			case 'D': // Debug Mode
				Debug.Enable();
				break;
			case 'l':
				Prefs.SetLoadedStack(String.valueOf(optG.getOptarg()));
				break;
			case '?': // Help
			case 'h':
			default:
				Output.printError("Unknown Command Line Option -" + optG.getOptarg() + "'");
				Help.Display();
				System.exit(0);
				break;
			}
		}

		// Display some useful information about the environment if in Debug Mode
		Debug.Print("System Information:");
		Debug.Print(" - class.path:     " + System.getProperty("java.class.path"));
		Debug.Print("  - java.home:      " + System.getProperty("java.home"));
		Debug.Print("  - java.vendor:    " + System.getProperty("java.vendor"));
		Debug.Print("  - java.version:   " + System.getProperty("java.version"));
		Debug.Print("  - os.name:        " + System.getProperty("os.name"));
		Debug.Print("  - os.version:     " + System.getProperty("os.version"));
		Debug.Print("  - os.arch:        " + System.getProperty("os.arch"));
		Debug.Print("  - user.name:      " + System.getProperty("user.name"));
		Debug.Print("  - user.home:      " + System.getProperty("user.home"));
		Debug.Print("  - user.dir:       " + System.getProperty("user.dir"));
		Debug.Print("  - file.separator: " + System.getProperty("file.separator"));
		Debug.Print("  - library.path:   " + System.getProperty("java.library.path"));
		Debug.Print("\nCommand Line Options");
		Debug.Print("  -D:  " + Debug.Query());
		Debug.Print("  -l:  " + Prefs.QueryLoadedStack());

		// Pull the existing stacks from the preferences if they exist
		calcStack = Prefs.RestoreStack("1");
		calcStack2 = Prefs.RestoreStack("2");

		// Display output header information
		Output.printColorln(FColor.CYAN, "+----------------------------------------------------------------------+");
		Output.printColorln(FColor.CYAN,
				"|                           RPN Calculator                 v" + VERSION + " |");
		Output.printColorln(FColor.CYAN, "|           Written by Michael Fross.  All rights reserved             |");
		Output.printColorln(FColor.CYAN, "|                 Enter command 'h' for help details                   |");
		Output.displayDashedNameLine();

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			String cmdInput = null;

			// Display the current stack
			for (int i = 0; i <= calcStack.size() - 1; i++) {
				// Display Stack Number
				String sn = String.format("%02d:   ", calcStack.size() - i);
				Output.printColor(FColor.CYAN, sn);
				// Display Stack Value
				Output.printColorln(FColor.WHITE, Math.Comma(calcStack.get(i)));
			}

			// Input command/number from user
			Output.printColor(FColor.YELLOW, "\n>>  ");
			cmdInput = con.readLine();

			// Toggle Debug Mode
			if (cmdInput.matches("[Dd][Ee][Bb][Uu][Gg]")) {
				if (Debug.Query()) {
					Debug.Disable();
					Output.printColorln(FColor.RED, "Debug Disabled");
				} else {
					Debug.Enable();
					Debug.Print("Debug Enabled");
				}

				// Load a new stack into the calculator
			} else if (cmdInput.matches("^[Ll][Oo][Aa][Dd] .+")) {
				// Save current Stack
				Prefs.SaveStack(calcStack, "1");
				Prefs.SaveStack(calcStack2, "2");

				// Set new stack
				String newStack = cmdInput.toString().substring(5);
				Debug.Print("Loading new stack: '" + newStack + "'");
				Prefs.SetLoadedStack(newStack);

				// Load new stack
				calcStack = Prefs.RestoreStack("1");
				calcStack2 = Prefs.RestoreStack("2");

				// Process Help
			} else if (cmdInput.matches("^[Hh?]")) {
				Debug.Print("Displaying Help");
				Help.Display();

				// Process Exit
			} else if (cmdInput.matches("^[Xx]")) {
				Debug.Print("Exiting Command Loop");
				ProcessCommandLoop = false;

				// Process Clear
			} else if (cmdInput.matches("^[Cc]")) {
				Debug.Print("Clearing Stack");
				calcStack.clear();

				// Delete last stack item
			} else if (cmdInput.matches("^[Dd]")) {
				Debug.Print("Deleting Last Stack Item");
				try {
					if (!calcStack.isEmpty())
						calcStack.pop();
				} catch (Exception e) {
					Output.printError("Could not delete last stack item");
					Output.printError(e.getMessage());
				}

				// Flip lasts two elements on the stack
			} else if (cmdInput.matches("^[Ff]")) {
				Debug.Print("Flipping last two elements in the stack");

				if (calcStack.size() < 2) {
					Output.printError("Two elements are needed for flip");
				} else {
					Double temp1 = calcStack.pop();
					Double temp2 = calcStack.pop();
					calcStack.push(temp1);
					calcStack.push(temp2);
				}

				// Change sign of last stack element
			} else if (cmdInput.matches("^[Ss]")) {
				Debug.Print("Changing sign of last stack element");
				if (!calcStack.isEmpty())
					calcStack.push(calcStack.pop() * -1);

				// Swap primary and secondary stack
			} else if (cmdInput.matches("^[Ss][Ss]")) {
				Debug.Print("Swapping primary and secondary stack");
				Stack<Double> calcStackTemp = (Stack<Double>) calcStack.clone();
				calcStack = (Stack<Double>) calcStack2.clone();
				calcStack2 = (Stack<Double>) calcStackTemp.clone();
				Prefs.ToggleCurrentStackNum();

				// Perform a square root of the last item on the stack
			} else if (cmdInput.matches("^[Ss][Qq][Rr][Tt]")) {
				Debug.Print("Taking the square root of the last stack item");
				Math.SquareRoot(calcStack);

				// Operand entered
			} else if (cmdInput.matches("[\\*\\+\\-\\/\\^\\%]")) {
				Debug.Print("CalcStack has " + calcStack.size() + " elements");
				Debug.Print("Operand entered: '" + cmdInput.charAt(0) + "'");
				// Verify stack contains at least two elements
				if (calcStack.size() >= 2) {
					calcStack = Math.Parse(cmdInput.charAt(0), calcStack);
				} else {
					Output.printError("Two numbers are required for this operation");
				}

				// Number entered, add to stack. Blank line will trigger so skip if !blank
			} else if (!cmdInput.isEmpty() && cmdInput.matches("^-?\\d*\\.?\\d*")) {
				Debug.Print("Adding number onto the stack");
				calcStack.push(Double.valueOf(cmdInput));

				// Handle numbers with a single operand at the end (a NumOp)
			} else if (cmdInput.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				Debug.Print("CalcStack has " + calcStack.size() + " elements");
				// Verify stack contains at least one element
				if (calcStack.size() >= 1) {
					char TempOp = cmdInput.charAt(cmdInput.length() - 1);
					String TempNum = cmdInput.substring(0, cmdInput.length() - 1);
					Debug.Print("NumOp Found: Num= '" + TempNum + "'");
					Debug.Print("NumOp Found: Op = '" + TempOp + "'");
					calcStack.push(Double.valueOf(TempNum));
					calcStack = Math.Parse(TempOp, calcStack);
				} else {
					Output.printError("One number is required for this NumOp function");
				}

				// Display an error if the entry matched none of the above
			} else {
				Output.printColorln(FColor.RED, "Input Error: '" + cmdInput + "'");
			}

			// Display DashLine
			Output.displayDashedNameLine();

		}

		// Save the primary and secondary stacks
		Prefs.SaveStack(calcStack, "1");
		Prefs.SaveStack(calcStack2, "2");
	}

} // End Program
