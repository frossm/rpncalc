/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2025 Michael Fross
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
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class StackOperations {
   /**
    * cmdDebug(): Toggle debug setting
    */
   public static void cmdDebug() {
      if (Debug.query()) {
         Debug.disable();
         Output.printColorln(Ansi.Color.RED, "Debug Disabled");
      } else {
         Debug.enable();
         Output.debugPrintln("Debug Enabled");
      }
   }

   /**
    * cmdList(): Sub commands to list are:
    * <p>
    * stacks: List the current list of saved stacks from the preferences system
    * mem: List the current values in the memory stacks
    * undo: List the contents of the undo stack which shows previous stack states
    */
   public static void cmdList(StackObj calcStack, String arg) {
      // Process the different list commands as sent by arg
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
            String headerText = "-Undo Stack for: " + calcStack.queryStackName() + ":" + StackManagement.QueryCurrentStackNum();
            int numDashes = Main.configProgramWidth - headerText.length();
            Output.printColorln(Ansi.Color.YELLOW, "\n" + headerText + "-".repeat(numDashes));

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

         case "cmd":
         case "cmds":
         case "command":
         case "commands":
            for (int i = 0; i < CommandHistory.size(); i++) {
               Output.printColorln(Ansi.Color.CYAN, String.format("  %d: %s", i, CommandHistory.get(i).split("##")[0]));
            }
            Output.println("");
            break;

         default:
            Output.printColorln(Ansi.Color.RED, "Error:  Unknown list command '" + arg + "'\n");
            Output.printColorln(Ansi.Color.CYAN, "Allowed list commands:");
            Help.DisplayListCommands(Ansi.Color.CYAN);
            Output.printColorln(Ansi.Color.CYAN, "\nSee help or user guide for additional information");
      }
   }

   /**
    * cmdLoad(stackToLoad): Load the named stack after saving current stack to prefs
    *
    * @param stackToLoad Name of the stack to load
    */
   public static void cmdLoad(StackObj calcStack, StackObj calcStack2, String stackToLoad) {
      // Save current Stack
      StackManagement.SaveStack(calcStack, "1");
      StackManagement.SaveStack(calcStack2, "2");

      // Set new stack
      Output.debugPrintln("Loading new stack: '" + stackToLoad + "'");
      calcStack.setStackNameAndRestore(stackToLoad, "1");
      calcStack2.setStackNameAndRestore(stackToLoad, "2");
   }

   /**
    * cmdReverse(): Reverse all the elements in the stack. Last becomes first and first becomes last
    */
   public static void cmdReverse(StackObj calcStack) {
      Output.debugPrintln("Reversing all items in the stack");

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
    */
   public static void cmdSwapStack(StackObj calcStack, StackObj calcStack2) {
      Output.debugPrintln("Swapping primary and secondary stack");

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
    * ExportStackToDisk(): Save the current stack contacts to the provided file
    *
    * @param arg Filename of the export
    */
   public static void exportStackToDisk(StackObj calcStack, String arg) {
      String fileName = arg.trim();

      // Return if no filename is given
      if (arg.isEmpty()) {
         Output.printColorln(Ansi.Color.RED, "Export requires a filename to be provided");
         return;
      }

      // Ensure we have at least one item on the stack
      if (calcStack.isEmpty()) {
         Output.printColorln(Ansi.Color.RED, "Export requires at least one item on the stack");
         return;
      }

      Output.debugPrintln("Export filename: '" + fileName + "'");

      // If the file exists, then delete it
      try {
         File file = new File(fileName);
         if (file.exists()) {
            Output.debugPrintln("'" + fileName + "' exists - deleting");

            // Delete the file.  If it returns false, then throw an error
            if (!file.delete()) {
               throw new Exception("File Delete Failed");
            }
         }
      } catch (Exception ex) {
         Output.printColorln(Ansi.Color.RED, "'" + fileName + "' exists but could not be deleted\n" + ex.getMessage());
         return;
      }

      //
      try {
         // Create the output filename and write the stack values to it
         if (new File(fileName).createNewFile()) {
            FileWriter fw = new FileWriter(fileName);

            for (int i = 0; i < calcStack.size(); i++) {
               fw.write(calcStack.get(i).toPlainString() + "\n");
            }

            // Close the FileWriter and flush the data to the file
            fw.close();
         }

      } catch (IOException ex) {
         Output.printColorln(Ansi.Color.RED, "Could not export stack values to '" + fileName + "'");
         return;
      }

      Output.printColorln(Ansi.Color.CYAN, "Export successful: '" + new File(fileName).getAbsoluteFile() + "'");
   }

   /**
    * cmdHomePage(): Open the configured browser to the RPNCalc Home Page
    */
   public static void cmdHomePage() {
      Browser.Launch(Help.HOMEPAGE);
   }

   /**
    * cmdUserGuide(): Open the configured browser to the RPNCalc User Guide
    */
   public static void cmdUserGuide() {
      Browser.Launch(Help.USERGUIDE);
   }

   /**
    * LoadStackFromDisk(): Load the contents of a file into the stack. The file should have one stack item per line
    *
    * @param arg filename
    */
   public static void importStackFromDisk(StackObj calcStack, String arg) {
      String fileName = arg.trim();

      Output.debugPrintln("Import filename as entered: '" + fileName + "'");

      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      try {
         // Verify the filename provided is a file and can be read
         if (new File(fileName).canRead() && new File(fileName).isFile()) {
            // Read lines from the file into the ArrayList
            ArrayList<String> linesRead = new ArrayList<>(Files.readAllLines(Paths.get(fileName)));

            // Clear the stack before importing the new values
            calcStack.clear();

            // Convert the strings to BigDecimal values. Skip empty lines
            for (String s : linesRead) {
               if (!s.isEmpty()) {
                  calcStack.push(new BigDecimal(s));
               }
            }

         } else {
            throw new IOException();
         }
      } catch (IOException ex) {
         Output.printColorln(Ansi.Color.RED, "ERROR: Could not read from the file '" + fileName + "'");
         Output.printColorln(Ansi.Color.RED, "ERROR: Please note the file must be in lower case");
         return;
      } catch (NumberFormatException ex) {
         Output.printColorln(Ansi.Color.RED, "The data in '" + fileName + "' can't be read as it is not in the correct format.\nThe import file format is simply one number per line");
         return;
      }

      Output.printColorln(Ansi.Color.CYAN, "Import successful from '" + fileName + "'");

   }

   /**
    * StackSwapItems(): Swap two elements in the stack
    * <p>
    * Approach: Empty the stack into an array. Replace the existing values with the swapped values. Then recreate the stack.
    *
    * @param stk   Primary Stack
    * @param item1 First item to swap
    * @param item2 Second item to swap
    */
   public static void StackSwapItems(StackObj stk, int item1, int item2) {
      int stkSize = stk.size();
      BigDecimal[] tempArray = new BigDecimal[stkSize];
      BigDecimal value1;
      BigDecimal value2;

      // Populate the array with the contents of the stack
      Output.debugPrintln("Size of Stack is: " + stkSize);
      for (int i = 0; i < stkSize; i++) {
         Output.debugPrintln("Backup: Array[" + i + "] = " + stk.peek());
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
         Output.debugPrintln("Restore: Array[" + i + "] = " + tempArray[i].toPlainString() + " -> Stack");
         stk.push(tempArray[i]);
      }

   }

}
