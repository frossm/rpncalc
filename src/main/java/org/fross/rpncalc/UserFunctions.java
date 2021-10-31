/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2021 Michael Fross
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

import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class UserFunctions {
	// Class Constants
	protected static final String PREFS_PATH_FUNCTIONS = "/org/fross/rpn/functions";

	// Class Variables
	static boolean recordingEnabled = false;
	static ArrayList<String> recording = new ArrayList<>();

	/**
	 * cmdRecord(): Processes record on/off request from user
	 * 
	 * @param args
	 */
	public static void cmdRecord(String args) {
		// Determine the record command that was given
		try {
			if (args.toLowerCase().trim().startsWith("on")) {
				recordingEnabled = true;
			} else if (args.toLowerCase().trim().startsWith("off")) {
				recordingEnabled = false;
				SaveRecordingToPrefs();
			} else {
				Output.printColorln(Ansi.Color.RED, "ERROR: Illegal argument for record.  Please see help");
				return;
			}
		} catch (StringIndexOutOfBoundsException ex) {
			// User did not enter in a command
			Output.printColorln(Ansi.Color.RED, "ERROR: An argument is requirement for record comamnd.  Please see help");
		}

	}

	/**
	 * cmdFunction(): Allow users to run or delete functions
	 * 
	 * @param args
	 */
	public static void cmdFunction(String args) {
		String command[] = args.toLowerCase().trim().split("\\s", 2);

		try {
			if (command[0].startsWith("del")) {
				try {
					FunctionDelete(command[1]);
				} catch (ArrayIndexOutOfBoundsException ex) {
					Output.printColorln(Ansi.Color.RED, "ERROR: 'function del' requires a valid function name to delete");
					return;
				}
			} else if (command[0].startsWith("run")) {
				try {
					FunctionRun(command[1]);
				} catch (ArrayIndexOutOfBoundsException ex) {
					Output.printColorln(Ansi.Color.RED, "ERROR: 'function run' requires a valid function name to execute");
					return;
				}

			} else {
				Output.printColorln(Ansi.Color.RED, "ERROR: Illegal argument for function command.  Please see help");
			}
		} catch (StringIndexOutOfBoundsException ex) {
			// User did not enter in a command
			Output.printColorln(Ansi.Color.RED, "ERROR: An argument is requirement for function comamnd.  Please see help");
		}
	}

	/**
	 * RecordingEnabled(): Return true if recording is turned on
	 * 
	 * @return
	 */
	public static boolean RecordingEnabled() {
		return recordingEnabled;
	}

	/**
	 * RecordCommand(): Save the user's input to the recording
	 * 
	 * @param arg
	 */
	public static void RecordCommand(String arg) {
		// Ignore the following commands from recording
		String[] ignore = { "list", "debug", "ver", "h", "?", "record", "function", "cx", "x", "exit" };

		// If the command starts with an ignored item, just return before adding it to the recording
		for (int i = 0; i < ignore.length; i++) {
			if (arg.startsWith(ignore[i]) == true) {
				return;
			}
		}

		Output.debugPrint("Adding '" + arg.trim().toLowerCase() + "' to recording");
		recording.add(arg.trim().toLowerCase());
		Output.debugPrint("Current Recording: " + recording.toString());
	}

	/**
	 * RemoveItemFromRecording(): Remove the value at index from the recording Most likely used if user
	 * inputs an invalid command
	 * 
	 * @param index
	 */
	public static void RemoveItemFromRecording(int index) {
		// Validate index
		try {
			if (index >= 0 && index > (recording.size() - 1)) {
				recording.remove(index);
			}
		} catch (Exception ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Can't remove recorded item from index at position: " + index);
		}
	}

	/**
	 * RemoveItemFromRecording(): If no argument is given, remove the last item
	 */
	public static void RemoveItemFromRecording() {
		recording.remove(recording.size() - 1);
	}

	/**
	 * SaveRecordingToPrefs(): When you stop a recording, save it to the preferences system
	 * 
	 */
	public static void SaveRecordingToPrefs() {
		// Request a name for the user function. No name cancels the save
		Output.printColor(Ansi.Color.YELLOW, "Please enter the name of this function. No name will cancel the recording\n>> ");
		String functionName = Main.scanner.nextLine();
		Output.debugPrint("Function's Name set to: '" + functionName + "'");

		// If no name is given, cancel the recording information
		if (functionName.isBlank() == true) {
			Output.printColorln(Ansi.Color.YELLOW, "Canceling the recording");
			recording.clear();
			return;
		}

		// Save the recording and clear it
		Output.debugPrint("Save Recordings: " + PREFS_PATH_FUNCTIONS + "/" + functionName);
		Preferences p = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + functionName);

		// Delete any existing function items with the same name
		try {
			p.clear();
		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not clear '" + functionName + "' prior to savings");
		}

		// Save the recording into the preference
		p.putInt("FunctionSteps", recording.size());
		for (int i = 0; i < recording.size(); i++) {
			p.put("Step" + i, recording.get(i));
		}

		// Erase the recording as it's saved
		recording.clear();
	}

	/**
	 * FunctionDelete(): Delete a stored function
	 * 
	 * @param fname
	 */
	public static void FunctionDelete(String fname) {
		// Verify user defined function exists
		if (FunctionExists(fname) == false) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + fname + "' is not a valid user defined function");
			return;
		}

		// Remove the user defined function
		Preferences pChild = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + fname);
		try {
			pChild.removeNode();

		} catch (BackingStoreException e) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not remove the function named: " + fname);
		}
	}

	/**
	 * FunctionRun(): Execute the user defined function provided
	 * 
	 * @param func
	 */
	public static void FunctionRun(String functionName) {
		// Verify user defined function exists
		if (FunctionExists(functionName) == false) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + functionName + "' is not a valid user defined function");
			return;
		}

		Preferences pChild = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + functionName);
		Output.printColorln(Ansi.Color.CYAN, "Executing User Defined Function: '" + functionName + "'");

		// Loop through the steps in the function and send them to be executed
		for (int i = 0; i < Integer.parseInt(pChild.get("FunctionSteps", "Error")); i++) {
			String fullCommand = pChild.get("Step" + i, "Error");
			String command = "";
			String param = "";

			try {
				String[] ci = fullCommand.toLowerCase().trim().split("\\s+", 2);
				command = ci[0];
				param = ci[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				// Ignore if there is no command or parameter entered
				if (command.isEmpty()) {
					Output.debugPrint("Blank line entered");
					continue;
				}
			}

			Output.debugPrint("   Step" + i + ":  " + pChild.get("Step" + i, "Error"));
			CommandParser.Parse(fullCommand, command, param);
		}

	}

	/**
	 * FunctionExists(): Returns true if the user defined function exists in the preferences system
	 * 
	 * @param fname
	 * @return
	 */
	public static boolean FunctionExists(String fname) {
		Preferences pParent = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS);
		boolean childExists = false;

		// Verify that the user defined function exists
		try {
			for (String child : pParent.childrenNames()) {
				if (child.equals(fname)) {
					childExists = true;
				}
			}
		} catch (BackingStoreException e1) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not read from Java preferences");
		}

		return childExists;
	}

}
