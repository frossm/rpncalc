/*
 * rpn - A Reverse Polish Notation calculator
 *
 * Copyright 2011 by Michael Fross
 * All Rights Reserved
 *
 */
package org.fross.rpn;

import gnu.getopt.Getopt;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.*;
import java.util.Stack;
import java.util.EmptyStackException;

public class Main {

	// Constants
	public static final String VERSION = "1.3.0";
	public static final String PREF_STACK = "Stack";
	public static final String PREF_MONEYMODE = "MoneyMode";
	// Class Variables
	public static boolean clDebug = false;
	private static Stack LogStack = new Stack();
	private static double MemoryVar = 0;

	/**
	 * Main application program.  RPN is note an objective oriented application
	 * and is still fairly traditional.  Main does the initialization and
	 * starts the command input, the heart of the program.  This loop takes
	 * input, analyzes what was done and call out to perform math functions or
	 * handles the commands within it.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Console Con = null;
		String CommandInput = null;
		String prefStack = null;
		boolean prefMoneyMode = false;
		boolean clPersistentStack = false;
		boolean ProcessCommandLoop = true;
		int OptionEntry;
		Stack CalcStack = new Stack();


		System.out.println("+----------------------------------------------------------------------+");
		System.out.println("|                           RPN Calculator                      v" + VERSION + " |");
		System.out.println("|           Written by Michael Fross.  All rights reserved.            |");
		System.out.println("+----------------------------------------------------------------------+");
		LogStack.push("RPN Calculator v" + VERSION);
		LogStack.push("--<Begin Logging>--------------------------------");


		// Initialize the console used for command input
		Con = System.console();
		if (Con == null) {
			System.out.println("ERROR:  Could not initialize OS Console");
			System.exit(1);
		}

		// Process Command Line Options and set flags where needed
		Getopt OptG = new Getopt("DirSize", args, "Dph?");
		while ((OptionEntry = OptG.getopt()) != -1) {
			switch (OptionEntry) {
				case 'D':											// Debug Mode
					clDebug = true;
					break;
				case 'p':											// User Persistent Stack
					clPersistentStack = true;
					break;
				case '?':											// Help
				case 'h':
				default:
					Usage();
					System.exit(0);
					break;
			}
		}

		// Display some useful information about the environment if in Debug Mode
		Main.DebugPrint("DEBUG: System Information:");
		Main.DebugPrint("DEBUG:   - class.path:     " + System.getProperty("java.class.path"));
		Main.DebugPrint("DEBUG:   - java.home:      " + System.getProperty("java.home"));
		Main.DebugPrint("DEBUG:   - java.vendor:    " + System.getProperty("java.vendor"));
		Main.DebugPrint("DEBUG:   - java.version:   " + System.getProperty("java.version"));
		Main.DebugPrint("DEBUG:   - os.name:        " + System.getProperty("os.name"));
		Main.DebugPrint("DEBUG:   - os.version:     " + System.getProperty("os.version"));
		Main.DebugPrint("DEBUG:   - os.arch:        " + System.getProperty("os.arch"));
		Main.DebugPrint("DEBUG:   - user.name:      " + System.getProperty("user.name"));
		Main.DebugPrint("DEBUG:   - user.home:      " + System.getProperty("user.home"));
		Main.DebugPrint("DEBUG:   - user.dir:       " + System.getProperty("user.dir"));
		Main.DebugPrint("DEBUG:   - file.separator: " + System.getProperty("file.separator"));
		Main.DebugPrint("DEBUG:   - library.path:   " + System.getProperty("java.library.path"));

		Main.DebugPrint("\nDEBUG: Command Line Options");
		Main.DebugPrint("DEBUG:   -D:  " + clDebug);
		Main.DebugPrint("DEBUG:   -p:  " + clPersistentStack);

		// Read settings / Stack from preferences and load it if desired
		Preferences prefs = Preferences.userRoot().node("/org/fross/rpn");
		if (clPersistentStack == true) {
			prefMoneyMode = prefs.getBoolean(PREF_MONEYMODE, false);
			prefStack = prefs.get(PREF_STACK, "");

			Main.DebugPrint("DEBUG: Preference Stack:     '" + prefStack + "'");
			Main.DebugPrint("DEBUG: Preference MoneyMode: '" + prefMoneyMode + "'");

			if (!prefStack.isEmpty()) {
				CalcStack.push(Double.valueOf(prefStack));
				LogStack.push(Double.valueOf(prefStack) + "(From Persistent Cache");
			}
		}


		// Display top help for comamnds
		UsageInLoop();


		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			System.out.println("+----------------------------------------------------------------------+");

			// Display Stack
			for (int i = 0; i < CalcStack.size(); i++) {
				// Display current Stack.  Use Money Mode or Normal based on user preference
				if (prefMoneyMode == true) {
					System.out.println(" " + Number.MoneyFormat(CalcStack.get(i).toString()));
				} else {
					System.out.println(" " + Number.Comma(CalcStack.get(i).toString()));
				}
			}

			///////////////////////////////////////////////////////////////////
			// Input Command String from User
			if (prefMoneyMode == false) {
				CommandInput = Con.readLine("\n>> ");
			} else {
				CommandInput = Con.readLine("\n$ ");
			}

			///////////////////////////////////////////////////////////////////
			// Process Help
			if (CommandInput.matches("^[Hh]")) {
				UsageInLoop();

				///////////////////////////////////////////////////////////////////
				// Process Clear Stack
			} else if (CommandInput.matches("^[Cc]")) {
				Main.DebugPrint("DEBUG:  Clearing Stack");
				CalcStack = new Stack();
				LogStack.push("<Clearing Stack>");
				Main.DumpStackToLog(CalcStack);

				///////////////////////////////////////////////////////////////////
				// Process Last Item Delete
			} else if (CommandInput.matches("^[Dd]")) {
				Main.DebugPrint("DEBUG:  Deleting Last Item On Stack");
				if (!CalcStack.isEmpty()) {
					LogStack.push("<Delete Last Number: " + CalcStack.peek() + ">");
					CalcStack.pop();
					Main.DumpStackToLog(CalcStack);
				}

				///////////////////////////////////////////////////////////////////
				// Process Exit Program
			} else if (CommandInput.matches("^[XxQq]")) {
				Main.DebugPrint("DEBUG:  Exiting Command Loop");
				ProcessCommandLoop = false;

				///////////////////////////////////////////////////////////////////
				// Process Output Logging
			} else if (CommandInput.matches("^[Ll]")) {
				String FileName = null;
				String CurrentDir = System.getProperty("user.dir");
				String DefaultFileName = CurrentDir + System.getProperty("file.separator") + "RPN.Log";

				Main.DebugPrint("DEBUG:  Dumping Stack Log");

				if (LogStack.size() == 0) {
					System.out.println("** ERROR:  Stack is empty.  Nothing to Log!");
				} else {
					try {
						System.out.println("\nDefault Logfile is '" + DefaultFileName + "'");
						FileName = Con.readLine("Enter Export Filename (Return for Default): ");
						if (FileName.isEmpty()) {
							FileName = DefaultFileName;
						}

						Main.DebugPrint("DEBUG:  Output Filename is: '" + FileName + "'");
						FileWriter LogFile = new FileWriter(FileName);

						// Cycle Through Stack and Write to Output File
						for (int i = 0; i < LogStack.size(); i++) {
							Main.DebugPrint(i + ": " + LogStack.get(i));
							LogFile.write(i + ":   " + LogStack.get(i) + "\n");
						}

						// Close file
						LogFile.close();

					} catch (IOException Ex) {
						System.out.println("Error:  Could not write to output log file: '" + FileName + "'");
					}
				}

				///////////////////////////////////////////////////////////////////
				// Process Flip Last Two Items In Stack
			} else if (CommandInput.matches("^[Ff]")) {
				Main.DebugPrint("DEBUG:  Flip Command Entered");
				if (CalcStack.size() < 2) {
					System.out.println("ERROR:  Two elements are needed for flip");
				} else {
					Object Temp1 = CalcStack.pop();
					Object Temp2 = CalcStack.pop();
					CalcStack.push(Temp1);
					CalcStack.push(Temp2);
					LogStack.push("<Flipping Last Two Numbers>");
					Main.DumpStackToLog(CalcStack);
				}

				///////////////////////////////////////////////////////////////////
				// Process Sign Change
			} else if (CommandInput.matches("^[Ss]")) {
				Main.DebugPrint("DEBUG:  Sign Command Entered");
				if (!CalcStack.isEmpty()) {
					LogStack.push("<Sign Change for " + CalcStack.peek() + ">");
					double Temp = Double.valueOf(CalcStack.pop().toString());
					CalcStack.push(Temp * -1);
					Main.DumpStackToLog(CalcStack);
				}

				///////////////////////////////////////////////////////////////////
				// Process Money Mode Toggle
			} else if (CommandInput.matches("^[Mm]")) {
				if (prefMoneyMode == false) {
					Main.DebugPrint("DEBUG:  MoneyMode Now On");
					prefMoneyMode = true;
				} else {
					Main.DebugPrint("DEBUG:  MoneyMode Now Off");
					prefMoneyMode = false;
				}

				///////////////////////////////////////////////////////////////////
				// Process Operand Entry
			} else if (CommandInput.matches("[\\*\\+\\-\\/\\^]")) {
				Main.DebugPrint("DEBUG:  Operand Entered");
				CalcStack = Number.SimpleMath(CommandInput.charAt(0), CalcStack);
				LogStack.add(CommandInput.charAt(0));
				Main.DumpStackToLog(CalcStack);


				///////////////////////////////////////////////////////////////////
				// Process Number Entered. Convert to double first
			} else if (!CommandInput.isEmpty() && CommandInput.matches("^-?\\d*(\\.)?\\d*")) {
				Main.DebugPrint("DEBUG:  Number Entered.  Adding to Stack");
				CalcStack.push(Double.valueOf(CommandInput));
				LogStack.add(CalcStack.peek());

				
				///////////////////////////////////////////////////////////////////
				// Handle numbers with a single opperand at the end (a NumOp)
			} else if (CommandInput.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				char TempOp = CommandInput.charAt(CommandInput.length() - 1);
				String TempNum = CommandInput.substring(0, CommandInput.length() - 1);
				Main.DebugPrint("DEBUG:  NumOp Found: Op = '" + TempOp + "'");
				Main.DebugPrint("DEBUG:  NumOp Found: Num= '" + TempNum + "'");
				LogStack.push(TempNum);
				LogStack.push(TempOp);
				CalcStack.push(Double.valueOf(TempNum));
				CalcStack = Number.SimpleMath(TempOp, CalcStack);
				Main.DumpStackToLog(CalcStack);

				
				///////////////////////////////////////////////////////////////////
				// Adds the last number in the stack into memory
			} else if (CommandInput.matches("^[Mm][Aa]")) {
				Main.DebugPrint("DEBUG:  Adding '" + CalcStack.peek().toString() + "' to MEMORY value");
				MemoryVar += Double.valueOf(CalcStack.peek().toString());
				
				
				///////////////////////////////////////////////////////////////////
				// Subtracts the last number in the stack into memory
			} else if (CommandInput.matches("^[Mm][Ss]")) {
				Main.DebugPrint("DEBUG:  Subtracting '" + CalcStack.peek().toString() + "' from MEMORY value");
				MemoryVar -= Double.valueOf(CalcStack.peek().toString());
				
				
				///////////////////////////////////////////////////////////////////
				// Clears the Memory Variable
			} else if (CommandInput.matches("^[Mm][Cc]")) {
				Main.DebugPrint("DEBUG:  Clearning MEMORY variable");
				MemoryVar = 0;
				
				
				///////////////////////////////////////////////////////////////////
				// Reads the memory and places it at the end of the stack
			} else if (CommandInput.matches("^[Mm][Rr]")) {
				CalcStack.add(MemoryVar);
				
				
				///////////////////////////////////////////////////////////////////
				// Display an error if I didnt' understand the input
			} else if (!CommandInput.isEmpty()) {
				System.out.println("** Input Error: '" + CommandInput + "' **");
			}

		} // End Command Input Loop

		// Save Preferences
		if (clPersistentStack == true) {
			try {
				prefs.putBoolean(PREF_MONEYMODE, prefMoneyMode);
				prefs.put(PREF_STACK, CalcStack.pop().toString());
			} catch (NumberFormatException Ex) {
				// Don't update peferences if there are issues.
			} catch (EmptyStackException Ex) {
				// If the Stack is empty, empty the preference
				prefs.remove(PREF_STACK);
			}
		}

		// Exit Main Program
	}

	/**
	 * UsageInLoop:  Display usage information inside of the command input loop
	 */
	public static void UsageInLoop() {
		System.out.println("\n+----------------------------------------------------------------------+");
		System.out.println("|Commands:                                                             |");
		System.out.println("|  c  - Clear Stack      d  - Del last item     f  - Flip last 2 items |");
		System.out.println("|  s  - Change Sign      l  - Dump Stack Log    m  - Toggle Money Mode |");
		System.out.println("|  ma - Memory Add       ms - Memory subtrct    mc - Memory Clear      |");
		System.out.println("|  mr - Memory Recall    x  - Exit              h  - Help              |");
	}

	/**
	 * Usage:  Display rpn.jar program usage.  This is basically the command line
	 * parameters.
	 */
	public static void Usage() {
		System.out.println("\nUsage:   java -jar rpn.jar [-D] -[p] -[h] -[?]\n");
		System.out.println("   -D:  Debug Mode.  Displays debug output.");
		System.out.println("   -p:  Use Persistent Stack");
		System.out.println("   -h:  Display this help information '-?' can also be used.");
	}

	/**
	 * DebugPrint:  This is a simple function that takes a strong argument
	 * and tests to see if DebugMode is on.  If it is, then display via
	 * standard out the sent string.
	 *
	 * @param Msg
	 */
	public static void DebugPrint(String Msg) {
		if (Main.clDebug == true) {
			System.out.println(Msg);
		}
	}

	/**
	 * DumpStackToLog
	 * Save a dashed line and then the full stack to the log stack.
	 * @param Stk 
	 */
	public static void DumpStackToLog(Stack Stk) {
		LogStack.add("------------------------------------------------");
		for (int i = 0; i < Stk.size(); i++) {
			LogStack.add(Double.valueOf(Stk.elementAt(i).toString()));
		}
	}
} // End Class

