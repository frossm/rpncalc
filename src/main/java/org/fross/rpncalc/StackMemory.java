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

import java.util.prefs.Preferences;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class StackMemory {
	// Class Constants
	private static final int DEFAULT_MEMORY_SLOTS = 10;	 // Number of memory slots available for mem command

	// Class Variables
	protected static Double[] memorySlots = new Double[DEFAULT_MEMORY_SLOTS];

	/**
	 * SetMaxMemorySlots(): Sets the number of memory slot available to be used
	 * 
	 * @param numSlots
	 */
	public static void SetMaxMemorySlots(String slots) {
		try {
			int numSlots = Integer.parseInt(slots);

			// Ensure we always have at least one memory slot
			if (numSlots >= 1) {
				memorySlots = new Double[numSlots];
			} else {
				Output.printColorln(Ansi.Color.RED, "Error: There must be at least 1 memory slot.  Setting to 1.");
				memorySlots = new Double[1];
			}

		} catch (NumberFormatException ex) {
			Output.fatalError("Could not set the number of memory slots to '" + slots + "'", 4);
		}
	}

	/**
	 * QueryInUseMemorySlots(): Return the number of memory slots being used
	 * 
	 * @return
	 */
	public static int QueryInUseMemorySlots() {
		int inUseCounter = 0;

		for (int i = 0; i < memorySlots.length; i++) {
			if (memorySlots[i] != null)
				inUseCounter++;
		}

		return inUseCounter;
	}

	/**
	 * SaveMemSlots(): Save the memory slots to the preferences system
	 * 
	 */
	public static void SaveMemSlots() {
		Preferences p = Preferences.userRoot().node("/org/fross/rpn/memoryslots");

		Output.debugPrint("Saving Memory Slots:");
		try {
			// Clear out any slots in the store in prep for writing the current slots
			p.clear();

			for (int i = 0; i < StackMemory.memorySlots.length; i++) {
				if (memorySlots[i] != null) {
					Output.debugPrint("  - Slot #" + i + ":  " + memorySlots[i]);
					p.putDouble(Integer.toString(i), memorySlots[i]);
				}
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "Error: Unable to save memory slots to preferences successfully");
		}

		Output.debugPrint("");
	}

	/**
	 * RestoreMemSlots(): Restore the contents of the memory slots from the preferences system. Typically done at startup
	 * 
	 */
	public static void RestoreMemSlots() {
		Preferences p = Preferences.userRoot().node("/org/fross/rpn/memoryslots");

		Output.debugPrint("Restoring Memory Slots:");
		try {
			for (int i = 0; i < StackMemory.memorySlots.length; i++) {
				if (p.getDouble(Integer.toString(i), Double.MAX_VALUE) != Double.MAX_VALUE) {
					Output.debugPrint("  - Slot #" + i + "  " + p.getDouble(Integer.toString(i), Double.MAX_VALUE));
					memorySlots[i] = p.getDouble(Integer.toString(i), Double.MAX_VALUE);
				}
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "Error: Unable to restore memory slots from preferences");
		}

		Output.debugPrint("");
	}

	/**
	 * cmdMem(): Process mem commands from user
	 * 
	 * @param cmd
	 */
	public static void cmdMem(StackObj calcStack, String arg) {
		String[] argParse = null;
		int memSlot = 0;

		// Parse the command string provided. If we can't create an integer from the first
		// arg then no stack number was provided
		try {
			argParse = arg.split(" ");
			memSlot = Integer.parseInt(argParse[0]);
		} catch (NumberFormatException ex) {
			// No slot number provided, just the command. Set Slot to 0 and it will re-parse below
			arg = "0 " + arg;
		}

		// Now that we have a memory slot (0 was inserted above if none was provided) we can get to work
		try {
			argParse = arg.split(" ");
			memSlot = Integer.parseInt(argParse[0]);

			Output.debugPrint("Argument Parsing: Memory Slot Selected: " + memSlot);
			Output.debugPrint("Argument Parsing: Memory Command: " + argParse[1]);

			// Ensure provided slot is within range
			if (memSlot < 0 || memSlot >= memorySlots.length) {
				Output.printColorln(Ansi.Color.RED, "ERROR: Memory Slot Number must be between 0 and " + (memorySlots.length - 1));
				return;
			}

			// Execute provided memory command
			switch (argParse[1].toLowerCase()) {
			// Add the last stack item in the memory slot
			case "add":
				// Ensure there is a value to save to the memory slot
				if (calcStack.size() >= 1) {
					Output.printColorln(Ansi.Color.CYAN, "Adding '" + calcStack.peek() + "' to Memory Slot #" + memSlot);
					memorySlots[memSlot] = calcStack.peek();
				} else {
					Output.printColorln(Ansi.Color.RED, "ERROR: There must be at least one value on the stack");
				}
				break;

			// Clear the provided slot's value
			case "clr":
			case "clear":
				Output.printColorln(Ansi.Color.CYAN, "Clearing Memory Slot #" + memSlot);
				memorySlots[memSlot] = null;
				break;

			case "clrall":
			case "clearall":
				Output.printColorln(Ansi.Color.CYAN, "Clearing All Memory Slots");
				for (int i = 0; i < memorySlots.length; i++) {
					memorySlots[i] = null;
				}
				break;

			// Copy the value from the memory slot provided back onto the stack
			case "copy":
			case "recall":
				// Save current calcStack to the undoStack
				calcStack.saveUndo();

				Output.printColorln(Ansi.Color.CYAN, "Copying value from Memory Slot #" + memSlot);
				if (memorySlots[memSlot] != null)
					calcStack.push(memorySlots[memSlot]);
				else
					Output.printColorln(Ansi.Color.RED, "Memory Slot #" + memSlot + " is empty");
				break;

			// Copy everything back onto the stack. Lower number to stop of stack (line 1)
			case "copyall":
			case "recallall":
				// Save current calcStack to the undoStack
				calcStack.saveUndo();

				Output.printColorln(Ansi.Color.CYAN, "Copying all memory items to the stack");
				for (int i = memorySlots.length - 1; i >= 0; i--) {
					if (memorySlots[i] != null) {
						calcStack.push(memorySlots[i]);
					}
				}
				break;

			// Copy all of the stack items into memory slots
			case "addall":
				// Ensure we have enough memory slots and then add the values to the slots
				try {
					if (calcStack.size() <= memorySlots.length) {
						for (int i = calcStack.size() - 1; i >= 0; i--) {
							memorySlots[calcStack.size() - 1 - i] = calcStack.get(i);
						}
					} else {
						Output.printColorln(Ansi.Color.RED, "ERROR: There are not enough memory slots to hold the stack. See -m switch");
						return;
					}
				} catch (Exception ex) {
					Output.printColorln(Ansi.Color.RED, "ERROR: An uknown error occured copying stack to memory slots");
					return;
				}
				break;

			default:
				// Slot was valid number, but unknown mem command
				Output.printColorln(Ansi.Color.RED, "ERROR: Unknown memory command: '" + argParse[1] + "'.  See help");
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "Error parsing mem command: 'mem " + arg + "'  See help for mem command usage");
		}
	}
}
