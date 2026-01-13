/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2026 Michael Fross
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
 * ------------------------------------------------------------------------------*/
package org.fross.rpncalc;

import org.fross.library.Debug;
import org.fross.library.Format;
import org.fross.library.Output;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Main - Main program execution class
 *
 * @author Michael Fross (michael@fross.org)
 */
public class Main {
   // Class Constants (or pseudo constants)
   public static final String PROPERTIES_FILE = "app.properties";
   public static final int PROGRAM_MINIMUM_WIDTH = 46;
   public static final int CONFIG_DEFAULT_PROGRAM_WIDTH = 80;
   public static final int CONFIG_DEFAULT_MEMORY_SLOTS = 10;
   public static final String CONFIG_DEFAULT_ALIGNMENT = "l";
   public static final int LINE_NUMBER_DIGITS = 2;
   public static final String INPUT_PROMPT = ">> ";

   // Class Variables
   public static String VERSION;
   public static String COPYRIGHT;
   public static Terminal terminal = null;
   public static LineReader lineReader = null;
   static boolean ProcessCommandLoop = true;
   static final StackObj calcStack = new StackObj();
   static final StackObj calcStack2 = new StackObj();

   // Configuration Values
   static int configProgramWidth = CONFIG_DEFAULT_PROGRAM_WIDTH;
   static int configMemorySlots = CONFIG_DEFAULT_MEMORY_SLOTS;
   static String configAlignment = CONFIG_DEFAULT_ALIGNMENT;

   /*
    * Main(): Start of program and holds main command loop
    *
    * @param args
    */
   public static void main(String[] args) {
      String cmdInput = "";         // What the user enters in totality
      String cmdInputCmd = "";      // The first field - the command
      String cmdInputParam = "";    // The remaining string - Parameters
      Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config"); // Persistent configuration settings

      // Silence the following JLine warning line when you start
      // WARNING: Unable to create a system terminal, creating a dumb terminal
      java.util.logging.LogManager.getLogManager().reset();
      java.util.logging.Logger.getLogger("org.jline").setLevel(java.util.logging.Level.OFF);

      // Force JLine to assume the terminal supports ANSI color and movement
      System.setProperty("org.jline.terminal.type", "xterm-256color");

      // Create a terminal used for input and output with JLine
      try {
         // This will print the actual reason (like "Missing library" or "Access Denied") to the console
         System.setProperty("org.jline.terminal.debug", "true");
         terminal = TerminalBuilder.builder()
               .system(true)
               .build();

         // Let Output and Input classes know which terminal to use
         Output.setTerminal(terminal);
         UserInput.setTerminal(terminal);


      } catch (IOException ex) {
         // Note: Since terminal failed, we use System.out as a fallback
         System.out.println("Unable to create a terminal. Visuals will be impacted.");
      }

      // Process application level properties file
      // Update properties from the build configuration at build time:
      // Ref: https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
      try {
         InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
         Properties prop = new Properties();
         prop.load(iStream);
         VERSION = prop.getProperty("Application.version");
         COPYRIGHT = "Copyright (C) " + prop.getProperty("Application.inceptionYear") + "-" + org.fross.library.Date.getCurrentYear() + " by Michael Fross";
      } catch (IOException ex) {
         Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
      }

      // Add default values to the persistent configuration items if none exist
      if (prefConfig.get("alignment", "none").equals("none")) {
         prefConfig.put("alignment", CONFIG_DEFAULT_ALIGNMENT);
      }
      if (prefConfig.getInt("programwidth", -1) == -1) {
         prefConfig.putInt("programwidth", CONFIG_DEFAULT_PROGRAM_WIDTH);
      }
      if (prefConfig.getInt("memoryslots", -1) == -1) {
         prefConfig.putInt("memoryslots", CONFIG_DEFAULT_MEMORY_SLOTS);
      }

      // Set configuration variables from preferences
      configProgramWidth = prefConfig.getInt("programwidth", CONFIG_DEFAULT_PROGRAM_WIDTH);
      configMemorySlots = prefConfig.getInt("memoryslots", CONFIG_DEFAULT_MEMORY_SLOTS);
      configAlignment = prefConfig.get("alignment", CONFIG_DEFAULT_ALIGNMENT);

      // Process Command Line Options
      CommandLineArgs.ProcessCommandLine(args);

      // Display some useful information about the environment if in Debug Mode
      Debug.displaySysInfo();
      Output.debugPrintln("Command Line Options");
      Output.debugPrintln("  - DebugModeOn:   " + Debug.query());
      Output.debugPrintln("  - StackName:     " + calcStack.queryStackName());
      Output.debugPrintln("  - Program Width: " + configProgramWidth);
      Output.debugPrintln("  - Memory Slots:  " + configMemorySlots);
      Output.debugPrintln("  - Color Enabled: " + Output.queryColorEnabled());

      // Restore the items in the memory slots during startup
      StackMemory.RestoreMemSlots();

      // Display the initial program header information
      Output.printColorln(Output.CYAN, "+" + "-".repeat(configProgramWidth - 2) + "+");
      Output.printColorln(Output.CYAN, Format.CenterText(configProgramWidth, "RPN Calculator  v" + VERSION, "|", "|"));
      Output.printColorln(Output.CYAN, Format.CenterText(configProgramWidth, COPYRIGHT, "|", "|"));
      Output.printColorln(Output.CYAN, Format.CenterText(configProgramWidth, "Enter command 'h' for help details", "|", "|"));
      Output.printColorln(Output.CYAN, Format.CenterText(configProgramWidth, "", "|", "|"));

      // Start Main Command Loop
      while (ProcessCommandLoop) {
         int maxDigitsBeforeDecimal = 0;
         int maxLenOfNumbers = 0;

         // Display the dashed status line
         Display.ShowStatusLine(calcStack);

         // Loop through the stack and count the max digits before the decimal for use with the decimal
         // alignment mode & overall length for right alignment mode
         for (int i = 0; i < calcStack.size(); i++) {
            int decimalIndex;
            String currentStackItem;

            // Add a comma to the current stack item if it's not in scientific notation
            if (calcStack.getAsString(i).toLowerCase().contains("e")) {
               currentStackItem = calcStack.getAsString(i);
            } else {
               currentStackItem = Display.Comma(calcStack.get(i));
            }

            // Determine where the decimal point is located. If no decimal exists (-1) assume it's at the end
            decimalIndex = Display.queryDecimalIndex(currentStackItem);

            // If current stack item has more digits ahead of decimal make that the max - commas are included.
            if (maxDigitsBeforeDecimal < decimalIndex) {
               maxDigitsBeforeDecimal = decimalIndex;
            }

            // Determine the length of the longest item in the stack for right alignment
            if (currentStackItem.length() > maxLenOfNumbers) {
               maxLenOfNumbers = currentStackItem.length();
            }

         }

         // Output information for alignment debugging
         Output.debugPrintln("Alignment: Max digits before the decimal: " + maxDigitsBeforeDecimal);
         Output.debugPrintln("Alignment: Max length of longest item in stack: " + maxLenOfNumbers);

         // Display the current stack contents
         for (int i = 0; i < calcStack.size(); i++) {
            String currentStackItem;

            // Add commas to the number if it's not in scientific notation
            if (calcStack.getAsString(i).toLowerCase().contains("e")) {
               currentStackItem = calcStack.getAsString(i).toLowerCase();
            } else {
               currentStackItem = Display.Comma(calcStack.get(i));
            }

            // Display Stack Row Number without a newline
            String stkLineNumber = String.format("%0" + LINE_NUMBER_DIGITS + "d:  ", calcStack.size() - i);
            Output.printColor(Output.CYAN, stkLineNumber);

            // DECIMAL ALIGNMENT: Insert spaces before number to align to the decimal point
            if (configAlignment.compareTo("d") == 0) {
               int decimalIndex = Display.queryDecimalIndex(currentStackItem);

               // Output the spaces in front so the decimals align
               Output.print(" ".repeat(maxDigitsBeforeDecimal - decimalIndex));
            }

            // RIGHT ALIGNMENT: Insert spaces before number to right align
            if (configAlignment.compareTo("r") == 0) {
               Output.print(" ".repeat(maxLenOfNumbers - currentStackItem.length()));
            }

            // Now that the spaces are inserted (for decimal/right) display the number
            Output.printColorln(Output.WHITE, currentStackItem.trim());
         }

         // Input command from user
         try {
            cmdInput = UserInput.GetInput("\n" + INPUT_PROMPT);

         } catch (UserInterruptException ex) {
            // User entered Ctrl-C so exit the program gracefully by placing the "exit" command as the input
            cmdInput = "exit";
            Output.printColorln(Output.CYAN, "Ctrl-C Detected. Exiting RPNCalc...");

         } catch (Exception ex) {
            // Should never get this error...
            Output.fatalError("Could not read user input\n" + ex.getMessage(), 5);
         }

         // If nothing was entered, stop processing and request new input
         if (cmdInput.isEmpty()) {
            Output.debugPrintln("Blank line entered");
            continue;
         }

         // Break each entered line (cmdInput) into a command (cmdInputCmd) and a parameter (cmdInputParam) string
         try {
            // Remove any commas from the string allowing for numbers such as "12,123" to be entered
            // TODO: Should make this more international at some point
            cmdInput = cmdInput.replaceAll(",", "");

            // Break the string into a command (cmdInputCmd) and a parameter (cmdInputParam)
            String[] ci = cmdInput.trim().split("\\s+", 2);
            cmdInputCmd = ci[0];
            cmdInputParam = ci[1];

         } catch (ArrayIndexOutOfBoundsException e) {
            // TODO: Ignore this error as it will trigger if just a command is entered with no parameter. There must be a better way...
         } catch (Exception e) {
            Output.printColorln(Output.RED, "ERROR: Problem parsing the command: '" + cmdInput + "' into command and arguments");
         }

         // While in debug mode, show the entered text along with the broken up command and parameter
         Output.debugPrintln("Full cmdInput: '" + cmdInput + "'  |  cmdInputCmd: '" + cmdInputCmd + "'  |  cmdInputParam: '" + cmdInputParam + "'");

         // Save the entered command to the command history
         CommandHistory.addCommand(cmdInput, cmdInputCmd, cmdInputParam);

         // If recording is enabled, send the user input to be recorded
         if (UserFunctions.recordingIsEnabled()) {
            UserFunctions.RecordCommand(cmdInputCmd + " " + cmdInputParam);
         }

         // Call the parser to send the command to the correct function to execute
         CommandParser.Parse(calcStack, calcStack2, cmdInput, cmdInputCmd, cmdInputParam);

         // Clear input parameters before we start again with the next command
         cmdInputCmd = "";
         cmdInputParam = "";

      } // End While Command Loop

      // If recording is on, complete the recording off process before exiting
      if (UserFunctions.recordingIsEnabled()) {
         Output.printColorln(Output.YELLOW, "\nRecording is in progress:");
         UserFunctions.cmdRecord("off");
      }

      // Save the items in the memory slots to the preferences system
      StackMemory.SaveMemSlots();

      // Save the primary and secondary stacks to the preferences system
      StackManagement.SaveStack(calcStack, "1");
      StackManagement.SaveStack(calcStack2, "2");

   }
}