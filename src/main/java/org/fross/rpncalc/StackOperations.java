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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.fross.library.Debug;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackOperations {
	/**
	 * cmdDebug(): Toggle debug setting
	 * 
	 */
	public static void cmdDebug() {
		if (Debug.query()) {
			Debug.disable();
			Output.printColorln(Ansi.Color.RED, "Debug Disabled");
		} else {
			Debug.enable();
			Output.debugPrint("Debug Enabled");
		}
	}

	/**
	 * cmdList(): Sub commands to list are:
	 * 
	 * stacks: List the current list of saved stacks from the preferences system
	 * 
	 * mem: List the current values in the memory stacks
	 * 
	 * undo: List the contents of the undo stack which shows previous stack states
	 */
	public static void cmdList(StackObj calcStack, String arg) {
		switch (arg.toLowerCase()) {
		case "stacks":
		case "stack":
			String[] stks = StackManagement.QueryStacks();

			Output.printColorln(Ansi.Color.YELLOW, "\n-Saved Stacks" + "-".repeat(Main.configProgramWidth - 13));
			for (int i = 0; i < stks.length; i++) {
				String sn = String.format("%02d:  %s", i, stks[i]);
				Output.printColorln(Ansi.Color.CYAN, sn);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
			break;

		case "mem":
			Output.printColorln(Ansi.Color.YELLOW, "\n-Memory Slots" + "-".repeat(Main.configProgramWidth - 13));
			for (int i = 0; i < StackMemory.memorySlots.length; i++) {
				Output.printColorln(Ansi.Color.CYAN, "Slot #" + i + ": " + StackMemory.memorySlots[i]);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
			break;

		case "undo":
			Output.printColorln(Ansi.Color.YELLOW,
					"\n-Undo Stack for Stack #: " + StackManagement.QueryCurrentStackNum() + "-".repeat(Main.configProgramWidth - 26));
			for (int i = 0; i < calcStack.undoSize(); i++) {
				String sn = String.format("%02d:  %s", i + 1, calcStack.undoGet(i));
				Output.printColorln(Ansi.Color.CYAN, sn);
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
			break;

		case "function":
		case "func":
			try {
				Output.printColorln(Ansi.Color.YELLOW, "\n-User Defined Functions" + "-".repeat(Main.configProgramWidth - 23));
				Preferences p = Preferences.userRoot().node(UserFunctions.PREFS_PATH_FUNCTIONS);

				// Loop through each function (child of the root) and display the details
				for (String functionName : p.childrenNames()) {
					Output.printColorln(Ansi.Color.WHITE, functionName);
					Preferences functionPref = Preferences.userRoot().node(UserFunctions.PREFS_PATH_FUNCTIONS + "/" + functionName);
					for (int i = 0; i < Integer.parseInt(functionPref.get("FunctionSteps", "Error")); i++) {
						Output.printColorln(Ansi.Color.CYAN, "   Step " + i + ":  " + functionPref.get("Step" + i, "Error"));
					}
					Output.println("");
				}
			} catch (Exception ex) {
				Output.printColorln(Ansi.Color.RED, "Error reading preferences system");
				return;
			}
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
			break;

		default:
			Output.printColorln(Ansi.Color.RED, "Error:  Unknown list command '" + arg + "'");
		}
	}

	/**
	 * cmdLoad(stackToLoad): Load the named stack after saving current stack to prefs
	 * 
	 * @param stackToLoad
	 */
	public static void cmdLoad(StackObj calcStack, StackObj calcStack2, String stackToLoad) {
		// Save current Stack
		StackManagement.SaveStack(calcStack, "1");
		StackManagement.SaveStack(calcStack2, "2");

		// Set new stack
		Output.debugPrint("Loading new stack: '" + stackToLoad + "'");
		calcStack.setStackNameAndRestore(stackToLoad, "1");
		calcStack2.setStackNameAndRestore(stackToLoad, "2");
	}

	/**
	 * set(): Set configuration Options
	 * 
	 */
	public static void cmdSet(String arg) {
		Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");
		String[] argParse = null;
		String command = "";
		String value = "";

		// If there is not a parameter provided, display the current values and return
		if (arg.isBlank()) {
			Output.printColorln(Ansi.Color.YELLOW, "\n-Configuration Values" + "-".repeat(Main.configProgramWidth - 21));
			Output.printColorln(Ansi.Color.CYAN, "Width:    " + Main.configProgramWidth + "\t|  Sets the program width in characters");
			Output.printColorln(Ansi.Color.CYAN, "Align:    " + Main.configAlignment + "\t|  Set display alignment. Values: (l)eft, (d)ecimal, (r)ight");
			Output.printColorln(Ansi.Color.CYAN, "MemSlots: " + Main.configMemorySlots + "\t|  Sets number of available memory slots");
			Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
			return;
		}

		// Parse the provided argument into a command and value
		try {
			argParse = arg.split(" ");
			command = argParse[0];
			value = argParse[1].toLowerCase();

			Output.debugPrint("Set Command: '" + command + "'");
			Output.debugPrint("Set Value:   '" + value + "'");

			switch (command.toLowerCase()) {
			case "align":
			case "alignment":
				if (value.compareTo("l") != 0 && value.compareTo("d") != 0 && value.compareTo("r") != 0) {
					Output.printColorln(Ansi.Color.RED, "Alignment can only be 'l'eft, 'd'ecimal, or 'r'ight. See help for usage");
					return;
				}
				Main.configAlignment = value;
				Output.debugPrint("Saving Alignment value to preferences");
				prefConfig.put("alignment", value);
				Output.printColorln(Ansi.Color.CYAN, "Alignment set to '" + value + "'");
				break;

			case "width":
				if (Integer.parseInt(value) < Main.PROGRAM_MINIMUM_WIDTH) {
					Output.printColorln(Ansi.Color.RED, "Error.  Minimum width is " + Main.PROGRAM_MINIMUM_WIDTH + ". Setting width to that value.");
					value = "" + Main.PROGRAM_MINIMUM_WIDTH;
				}
				Main.configProgramWidth = Integer.parseInt(value);
				Output.debugPrint("Saving Program Width value to preferences");
				prefConfig.putInt("programwidth", Integer.parseInt(value));
				Output.printColorln(Ansi.Color.CYAN, "Program Width set to '" + value + "'");
				break;

			case "mem":
			case "memslots":
			case "memoryslots":
				if (StackMemory.SetMaxMemorySlots(value) == true) {
					Output.printColorln(Ansi.Color.CYAN, "Memory Slots set to '" + value + "'");
				}
				break;

			default:
				Output.printColorln(Ansi.Color.RED, "ERROR: Unknown set command: '" + command + "'");
				return;
			}

		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "Error parsing set command: 'set " + arg + "'  See help for set command usage");
			return;
		}
	}

	/**
	 * reset(): Resets the configuration variables back to default
	 */
	public static void cmdReset() {
		Output.printColorln(Ansi.Color.CYAN, "Alignment, Width, and Memory slots reset to default values");
		Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");

		// Reset Alignment
		prefConfig.put("alignment", Main.CONFIG_DEFAULT_ALIGNMENT);
		Main.configAlignment = Main.CONFIG_DEFAULT_ALIGNMENT;

		// Reset Width
		prefConfig.putInt("programwidth", Main.CONFIG_DEFAULT_PROGRAM_WIDTH);
		Main.configProgramWidth = Main.CONFIG_DEFAULT_PROGRAM_WIDTH;

		// Reset the number of Memory Slots
		StackMemory.SetMaxMemorySlots(Main.CONFIG_DEFAULT_MEMORY_SLOTS + "");
		prefConfig.putInt("memoryslots", Main.CONFIG_DEFAULT_MEMORY_SLOTS);
		Main.configMemorySlots = Main.CONFIG_DEFAULT_MEMORY_SLOTS;
	}

	/**
	 * cmdReverse(): Reverse all of the elements in the stack. Last becomes first and first becomes last
	 */
	public static void cmdReverse(StackObj calcStack) {
		Output.debugPrint("Reversing all items in the stack");

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		StackObj tempStack = new StackObj();
		int stackSize = calcStack.size();

		// Reverse the items as we add them to a temporary stack
		for (int i = 0; i < stackSize; i++) {
			tempStack.push(calcStack.pop());
		}

		// Copy from the temporary stack back to the calcStack
		for (int i = 0; i < stackSize; i++) {
			calcStack.push(tempStack.get(i));
		}
	}

	/**
	 * cmdSwapStack(): Swap the primary and secondary stacks
	 * 
	 */
	public static void cmdSwapStack(StackObj calcStack, StackObj calcStack2) {
		Output.debugPrint("Swapping primary and secondary stack");

		// Swap the stacks from the objects into their new homes
		StackObj tempStack = new StackObj();

		tempStack.stackName = calcStack.stackName;
		tempStack.calcStack = calcStack.calcStack;
		tempStack.undoStack = calcStack.undoStack;

		calcStack.stackName = calcStack2.stackName;
		calcStack.calcStack = calcStack2.calcStack;
		calcStack.undoStack = calcStack2.undoStack;

		calcStack2.stackName = tempStack.stackName;
		calcStack2.calcStack = tempStack.calcStack;
		calcStack2.undoStack = tempStack.undoStack;

		// Update the current stack number for the status line refresh
		StackManagement.ToggleCurrentStackNum();
	}

	/**
	 * LoadStackFromDisk(): Load the contents of a file into the stack. The file should have one stack item per line
	 * 
	 * @param arg
	 */
	public static void importStackFromDisk(StackObj calcStack, String arg) {
		String fileName = arg.trim();

		try {
			// Verify the filename provided is a file and can be read
			if (new File(fileName).canRead() && new File(fileName).isFile()) {
				// Read lines from the file into the ArrayList
				ArrayList<String> linesRead = new ArrayList<>(Files.readAllLines(Paths.get(fileName)));

				// Convert the strings to double values
				for (int i = 0; i < linesRead.size(); i++) {
					calcStack.push(Double.parseDouble(linesRead.get(i)));
				}

			} else {
				throw new IOException();
			}
		} catch (IOException ex) {
			Output.printColorln(Ansi.Color.RED, "Error: Could not read from the file '" + fileName + "'");

		} catch (NumberFormatException ex) {
			Output.printColorln(Ansi.Color.RED,
					"The data in '" + fileName + "' can't be read as it is not in the correct format.\nThe import file format is simply one number per line");
		}

		// Save current calcStack to the undoStack
		calcStack.saveUndo();

		calcStack.clear();
	}

	/**
	 * StackSwapItems(): Swap two elements in the stack
	 * 
	 * Approach: Empty the stack into an array. Replace the existing values with the swapped values. Then recreate the stack.
	 * 
	 * @param stk
	 * @param item1
	 * @param item2
	 * @return
	 */
	public static StackObj StackSwapItems(StackObj stk, int item1, int item2) {
		int stkSize = stk.size();
		Double tempArray[] = new Double[stkSize];
		Double value1;
		Double value2;

		// Populate the array with the contents of the stack
		Output.debugPrint("Size of Stack is: " + stkSize);
		for (int i = 0; i < stkSize; i++) {
			Output.debugPrint("Backup: Array[" + i + "] = " + stk.peek());
			tempArray[i] = stk.pop();
		}

		// Grab the initial values
		value1 = tempArray[item1];
		value2 = tempArray[item2];

		// Swap with the new values
		tempArray[item1] = value2;
		tempArray[item2] = value1;

		// Recreate the stack
		for (int i = stkSize - 1; i >= 0; i--) {
			Output.debugPrint("Restore: Array[" + i + "] = " + tempArray[i] + " -> Stack");
			stk.push(tempArray[i]);
		}

		return (stk);
	}

}