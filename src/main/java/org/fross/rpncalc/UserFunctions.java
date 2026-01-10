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

import org.fross.library.Output;
import org.jline.reader.UserInterruptException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserFunctions {
   // Class Constants
   protected static final String PREFS_PATH_FUNCTIONS = "/org/fross/rpn/functions";

   // Class Variables
   static boolean recordingEnabled = false;
   static ArrayList<String> recording = new ArrayList<>();

   /**
    * cmdFunction(): Allow users to manage functions
    *
    * @param args Method Parameters
    */
   public static void cmdFunction(String args) {
      String[] command = args.toLowerCase().trim().split("\\s", 2);
      String fileNameStr = "";

      try {
         switch (command[0]) {
            case "del":
               try {
                  FunctionDelete(command[1]);
               } catch (ArrayIndexOutOfBoundsException ex) {
                  Output.printColorln(Output.RED, "ERROR: 'function del' requires a valid function name to delete");
               }
               break;

            case "delall":
               Preferences p = Preferences.userRoot().node(UserFunctions.PREFS_PATH_FUNCTIONS);

               // Loop through each function (child of the root) and delete it
               try {
                  for (String functionName : p.childrenNames()) {
                     Output.debugPrintln("Removing function: " + functionName);
                     FunctionDelete(functionName);
                  }
               } catch (BackingStoreException e) {
                  Output.printColorln(Output.RED, "Error:  Could not remove the user defined functions");
               }
               break;

            case "export":
               Output.printColorln(Output.YELLOW, "Please enter the export filename. A blank name will cancel the export");
               Output.printColorln(Output.YELLOW, "Note, if needed, please use use '/' as the path separator");

               // Read the user input. If ctrl-C is entered, discard the function
               try {
                  fileNameStr = UserInput.GetInput(Main.INPUT_PROMPT);

               } catch (UserInterruptException ex) {
                  fileNameStr = "";
               } catch (Exception e) {
                  Output.fatalError("Could not read user input", 5);
               }

               // If no name is given, cancel export
               if (fileNameStr.isBlank()) {
                  Output.printColorln(Output.YELLOW, "Canceling export");
                  break;
               }

               // Output the full path/name of the output file
               Output.printColorln(Output.CYAN, "Export filename: '" + new File(fileNameStr).getAbsolutePath() + "'");

               // Output preferences as an XML file
               try {
                  FileOutputStream fos = new FileOutputStream(fileNameStr);
                  Preferences pExport = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS);
                  pExport.exportSubtree(fos);
                  fos.close();

               } catch (IOException ex) {
                  Output.printColorln(Output.RED, "Error:  RPNCalc can't write to '" + fileNameStr + "'");
               } catch (BackingStoreException ex) {
                  Output.printColorln(Output.RED, "Error:  Could not export the user defined functions (BackingStoreException)");
               } catch (IllegalStateException ex) {
                  Output.printColorln(Output.RED, "Error:  Could not export the user defined functions (IllegalStateException)");
               } catch (Exception ex) {
                  Output.printColorln(Output.RED, "Error:  Could not export the user defined functions (Exception)");
               }
               break;

            case "import":
               Output.printColorln(Output.YELLOW, "Please enter the import filename. A blank name will cancel the import");
               Output.printColorln(Output.YELLOW, "Note, if needed, please use use '/' as the path separator");

               // Read the user input. If ctrl-C is entered, discard the function
               try {
                  fileNameStr = UserInput.GetInput(Main.INPUT_PROMPT);

               } catch (UserInterruptException ex) {
                  fileNameStr = "";
               } catch (Exception e) {
                  Output.fatalError("Could not read user input", 5);
               }

               // If no name is given, cancel export
               if (fileNameStr.isBlank()) {
                  Output.printColorln(Output.YELLOW, "Canceling import");
                  break;
               }

               // Import preferences from the XML file
               try {
                  FileInputStream fis = new FileInputStream(fileNameStr);
                  Preferences pImport = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS);
                  pImport.importPreferences(fis);
                  fis.close();

                  Output.printColorln(Output.CYAN, "Import complete");

               } catch (IOException ex) {
                  Output.printColorln(Output.RED, "Error:  RPNCalc can't read from '" + fileNameStr + "'");
               } catch (IllegalStateException ex) {
                  Output.printColorln(Output.RED, "Error:  Could not import the user defined functions (IllegalStateException)");
               } catch (Exception ex) {
                  Output.printColorln(Output.RED, "Error:  Could not import the user defined functions (Exception)");
               }
               break;

            default:
               Output.printColorln(Output.RED, "ERROR: Illegal argument for function command.  Please see help");
               break;
         }

      } catch (StringIndexOutOfBoundsException ex) {
         // User did not enter a command
         Output.printColorln(Output.RED, "ERROR: An argument is requirement for function command.  Please see help");
      }
   }

   /**
    * cmdRecord(): Processes record on/off request from user
    *
    * @param args Method parameters
    */
   public static void cmdRecord(String args) {
      boolean functionNameValid = false;
      String functionName;
      String[] arguments = {};

      // Break arguments into on/off and name. If no name was given set to blank
      try {
         arguments = args.trim().toLowerCase().split(" ");
         functionName = arguments[1];

      } catch (Exception ex) {
         functionName = "";
      }

      // Determine the record command that was given
      try {
         if (arguments[0].startsWith("on")) {
            if (!recordingEnabled) {
               recordingEnabled = true;
            } else {
               Output.printColorln(Output.CYAN, "Recording is already turned on");
            }

         } else if (arguments[0].startsWith("off")) {
            if (recordingEnabled) {
               recordingEnabled = false;

               // Ensure we have something in the buffer to save. If not, just return
               if (!recording.isEmpty()) {
                  if (functionName.isBlank()) {
                     while (!functionNameValid) {
                        // Request a name for the user function
                        Output.printColorln(Output.YELLOW, "Please enter the name of this function. A blank name will cancel the recording");

                        // Read the user input. If ctrl-C is entered, discard the function
                        try {
                           functionName = UserInput.GetInput(Main.INPUT_PROMPT);
                        } catch (UserInterruptException ex) {
                           functionName = "";
                        } catch (Exception e) {
                           Output.fatalError("Could not read user input", 5);
                        }

                        // If no name is given, cancel the recording information
                        if (functionName.isBlank()) {
                           Output.printColorln(Output.YELLOW, "Discarding the recording");
                           recording.clear();
                           return;
                        }

                        // Ensure there are no spaces in the name
                        if (functionName.contains(" ")) {
                           Output.printColor(Output.RED, "Error: Spaces in function names are not allowed\n");
                           functionName = "";
                        } else {
                           functionNameValid = true;
                        }
                     }
                  }

                  // We have a valid name so save to preferences
                  SaveRecordingToPrefs(functionName);

               } else {
                  Output.printColorln(Output.CYAN, "Recording off - No valid commands were recorded");
               }
            } else {
               Output.printColorln(Output.RED, "Recording is already turned off");
            }
         } else {
            Output.printColorln(Output.RED, "ERROR: Illegal argument for record.  Must be 'on' or 'off'. Please see help");
         }
      } catch (StringIndexOutOfBoundsException ex) {
         // User did not enter a command
         Output.printColorln(Output.RED, "ERROR: An argument is requirement (on | off) for the record command.  Please see help");
      }

   }

   /**
    * FunctionDelete(): Delete a stored function
    *
    * @param fname Name of function to delete
    */
   public static void FunctionDelete(String fname) {
      // Verify user defined function exists
      if (!FunctionExists(fname)) {
         Output.printColorln(Output.RED, "ERROR: '" + fname + "' is not a valid user defined function");
         return;
      }

      // Remove the user defined function
      Preferences pChild = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + fname);
      try {
         pChild.removeNode();

      } catch (BackingStoreException e) {
         Output.printColorln(Output.RED, "ERROR: Could not remove the function named: " + fname);
      }

      Output.printColorln(Output.CYAN, "User defined function deleted: '" + fname + "'");
   }

   /**
    * FunctionExists(): Returns true if the user defined function exists in the preferences system
    *
    * @param fname Function name to check for existence
    * @return Boolean value checking for existence
    */
   public static boolean FunctionExists(String fname) {
      Preferences pParent = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS);
      boolean childExists = false;

      // Verify that the user defined function exists
      try {
         for (String child : pParent.childrenNames()) {
            if (child.equals(fname)) {
               childExists = true;
               break;
            }
         }
      } catch (BackingStoreException e1) {
         Output.printColorln(Output.RED, "ERROR: Could not read from Java preferences");
      }

      return childExists;
   }

   /**
    * FunctionRun(): Execute the user defined function provided. Assumes functionName has been checked and is valid
    */
   public static void FunctionRun(StackObj calcStack, StackObj calcStack2, String functionName) {
      Preferences pChild = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + functionName);
      Output.printColorln(Output.CYAN, "Executing User Defined Function: '" + functionName + "'");

      // Loop through the steps in the function and send them to be executed
      for (int i = 0; i < Integer.parseInt(pChild.get("FunctionSteps", "Error")); i++) {
         String fullCommand = pChild.get("Step" + i, "Error");
         String command = "";
         String param = "";

         try {
            // If the number of spaces in the full command is zero, don't execute a split so we don't get an ArrayIndexOutOfBoundsException
            // Reference:  https://stackoverflow.com/questions/275944/how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
            if (fullCommand.codePoints().filter(ch -> ch == ' ').findAny().isEmpty()) {
               command = fullCommand.toLowerCase().trim();
            } else {
               String[] ci = fullCommand.toLowerCase().trim().split("\\s+", 2);
               command = ci[0];
               param = ci[1];
            }

         } catch (Exception e) {
            Output.printColorln(Output.RED, "ERROR: Problem parsing the command: '" + fullCommand + "' into command and arguments");
         }

         Output.debugPrintln("Step " + i + ":  " + pChild.get("Step" + i, "Error"));
         CommandParser.Parse(calcStack, calcStack2, fullCommand, command, param);
      }

   }

   /**
    * RecordingEnabled(): Return true if recording is turned on
    *
    * @return Boolean value if recording is currently enabled
    */
   public static boolean recordingIsEnabled() {
      return recordingEnabled;
   }

   /**
    * RecordCommand(): Save the user's input to the recording
    *
    * @param arg Save this input to the recording
    */
   public static void RecordCommand(String arg) {
      // Ignore the following commands from recording
      String[] commandsToIgnore = {"list", "debug", "ver", "version", "h", "help", "?", "record", "rec", "function", "func", "reset", "cx", "x", "exit", "quit"};

      // If the command starts with an ignored item, just return before adding it to the recording
      for (String s : commandsToIgnore) {
         if (arg.startsWith(s)) {
            Output.debugPrintln("Record ignoring the command '" + s + "'");
            return;
         }
      }

      Output.debugPrintln("Adding '" + arg.trim().toLowerCase() + "' to recording");
      recording.add(arg.trim().toLowerCase());
      Output.debugPrintln("Current Recording: " + recording);
   }

   /**
    * RemoveItemFromRecording(): Remove the value at index from the recording Most likely used if user inputs an invalid command
    *
    * @param index Index number of the recording to remove
    */
   public static void RemoveItemFromRecording(int index) {
      // Validate index
      try {
         if (index >= 0 && index > (recording.size() - 1)) {
            recording.remove(index);
         }
      } catch (Exception ex) {
         Output.printColorln(Output.RED, "ERROR: Can't remove recorded item from index at position: " + index);
      }
   }

   /**
    * RemoveItemFromRecording(): If no argument is given, remove the last item
    */
   public static void RemoveItemFromRecording() {
      RemoveItemFromRecording(recording.size() - 1);
   }

   /**
    * SaveRecordingToPrefs(): When you stop a recording, save it to the preferences system
    */
   public static void SaveRecordingToPrefs(String functionName) {
      Output.debugPrintln("Function's Name set to: '" + functionName + "'");

      // Save the recording and clear it
      Output.debugPrintln("Save Recordings: " + PREFS_PATH_FUNCTIONS + "/" + functionName);
      Preferences p = Preferences.userRoot().node(PREFS_PATH_FUNCTIONS + "/" + functionName);

      // Delete any existing function items with the same name
      try {
         p.clear();
      } catch (BackingStoreException e) {
         Output.printColorln(Output.RED, "ERROR: Could not clear '" + functionName + "' prior to savings");
      }

      // Save the recording into the preference
      p.putInt("FunctionSteps", recording.size());
      for (int i = 0; i < recording.size(); i++) {
         p.put("Step" + i, recording.get(i));
      }

      // Erase the recording as it's saved
      recording.clear();
   }

}
