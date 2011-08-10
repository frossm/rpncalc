/*
 * Main class for the rpn application.
 *
 */
package org.fross.rpn;

import gnu.getopt.Getopt;
import java.io.Console;
import java.util.prefs.*;
import java.util.Stack;
import java.util.EmptyStackException;

public class Main {

   // Constants
   public static final String VERSION = "1.1";
   public static final String PREF_STACK = "Stack";
   public static final String PREF_MONEYMODE = "MoneyMode";
   // Class Variables
   public static boolean clDebug = false;

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
      boolean clClearPrefs = false, clPersistentStack = false;
      int OptionEntry;
      Stack CalcStack = new Stack();


      System.out.println("+--------------------------------------------------------------+");
      System.out.println("|                       RPN Calculator                    v" + VERSION + " |");
      System.out.println("|       Written by Michael Fross.  All rights reserved.        |");
      System.out.println("+--------------------------------------------------------------+");

      // Initialize the console used for command input
      Con = System.console();
      if (Con == null) {
         System.out.println("ERROR:  Could not initialize OS Console");
         System.exit(1);
      }

      // Process Command Line Options and set flags where needed
      Getopt OptG = new Getopt("DirSize", args, "Dcph?");
      while ((OptionEntry = OptG.getopt()) != -1) {
         switch (OptionEntry) {
            case 'D':														// Debug Mode
               clDebug = true;
               break;
            case 'c':														// Clear Preferences
               clClearPrefs = true;
               break;
            case 'p':														// User Persistent Stack
               clPersistentStack = true;
               break;
            case '?':														// Help
            case 'h':
            default:
               Usage();
               System.exit(0);
               break;
         }
      }

      Main.DebugPrint("DEBUG: Command Line Options");
      Main.DebugPrint("DEBUG:   -D:  " + clDebug);
      Main.DebugPrint("DEBUG:   -c:  " + clClearPrefs);
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
         }
      }

      // Clear Preferences if selected with '-c' command line parameter
      if (clClearPrefs == true) {
         System.out.println("Clearing Preferences...");
         try {
            prefs.clear();
         } catch (BackingStoreException ex) {
            System.out.println("ERROR:  Unable to clear preferences");
         }
      }

      // Display top help for comamnds
      UsageInLoop();


      // Start main command loop
      while (true) {
         System.out.println("+--------------------------------------------------------------+");
         // Display Stack
         for (int i = 0; i < CalcStack.size(); i++) {
            // Display current Stack.  Use Money Mode or Normal based on user preference
            if (prefMoneyMode == true) {
               System.out.println(" " + Number.MoneyFormat(CalcStack.get(i).toString()));
            } else {
               System.out.println(" " + Number.Comma(CalcStack.get(i).toString()));
            }
         }

         // Input Command String from User
         if (prefMoneyMode == false) {
            CommandInput = Con.readLine("\n>> ");
         } else {
            CommandInput = Con.readLine("\n$>> ");
         }


         // Process Help
         if (CommandInput.matches("^[Hh]")) {
            UsageInLoop();


            // Process Clear Stack
         } else if (CommandInput.matches("^[Cc]")) {
            Main.DebugPrint("DEBUG:  Clearing Stack");
            CalcStack = new Stack();


            // Process Last Item Delete
         } else if (CommandInput.matches("^[Dd]")) {
            Main.DebugPrint("DEBUG:  Deleting Last Item On Stack");
            if (!CalcStack.isEmpty()) {
               CalcStack.pop();
            }


            // Process Exit Program
         } else if (CommandInput.matches("^[XxQq]")) {
            Main.DebugPrint("DEBUG:  Exiting Command Loop");
            break;


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
            }


            // Process Sign
         } else if (CommandInput.matches("^[Ss]")) {
            Main.DebugPrint("DEBUG:  Sign Command Entered");
            if (!CalcStack.isEmpty()) {
               double Temp = Double.valueOf(CalcStack.pop().toString());
               CalcStack.push(Temp * -1);
            }


            // Process Money Mode Toggle
         } else if (CommandInput.matches("^[Mm]")) {
            if (prefMoneyMode == false) {
               Main.DebugPrint("DEBUG:  MoneyMode Now On");
               prefMoneyMode = true;
            } else {
               Main.DebugPrint("DEBUG:  MoneyMode Now Off");
               prefMoneyMode = false;
            }


            // Process Operand Entry
         } else if (CommandInput.matches("[\\*\\+\\-\\/\\^]")) {
            Main.DebugPrint("DEBUG:  Operand Entered");
            CalcStack = Number.SimpleMath(CommandInput.charAt(0), CalcStack);


            // Process Number Entered. Convert to double first
         } else if (CommandInput.matches("^-?\\d+(\\.)?\\d*")) {
            Main.DebugPrint("DEBUG:  Number Entered.  Adding to Stack");
            CalcStack.push(Double.valueOf(CommandInput));


            // Handle numbers with a single opperand at the end (a NumOp)
         } else if (CommandInput.matches("^-?\\d+(\\.)?\\d*[\\*\\+\\-\\/\\^]")) {
            char TempOp = CommandInput.charAt(CommandInput.length() - 1);
            String TempNum = CommandInput.substring(0, CommandInput.length() - 1);
            Main.DebugPrint("DEBUG:  NumOp Found: Op = '" + TempOp + "'");
            Main.DebugPrint("DEBUG:  NumOp Found: Num= '" + TempNum + "'");
            CalcStack.push(Double.valueOf(TempNum));
            CalcStack = Number.SimpleMath(TempOp, CalcStack);

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
         } catch (NumberFormatException ex) {
            // Don't update peferences if there are issues.
         } catch (EmptyStackException ex) {
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
      System.out.println("\n+--------------------------------------------------------------+");
      System.out.println("|Commands:                                                     |");
      System.out.println("|  c - Clear Stack   d - Del last item   f - Flip last 2 items |");
      System.out.println("|  s - Change Sign   x - Exit Program    h - Help              |");
      System.out.println("|  m - MoneyMode                                               |");
   }

   /**
    * Usage:  Display rpn.jar program usage.  This is basically the command line
    * parameters.
    */
   public static void Usage() {
      System.out.println("Usage:   java -jar rpn.jar [-D] -[c] -[p] -[h] -[?]\n");
      System.out.println("   -D:  Debug Mode.  Displays debug output.");
      System.out.println("   -c:  Clear Preferences");
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
} // End Class

