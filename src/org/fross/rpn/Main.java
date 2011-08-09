/*
 * Main class for the rpn application.
 */
package org.fross.rpn;

import gnu.getopt.Getopt;
import java.io.Console;
import java.util.prefs.*;
import java.util.Stack;
import java.util.EmptyStackException;

public class Main {

   // Constants
   public static final String VERSION = "1.0";
   public static final String PREF_STACK = "Stack";
   public static final String PREF_MONEYMODE = "MoneyMode";
   // Class Variables
   public static boolean clDebug = false;

   /**
    * Main application program.  Starts the command input
    * loop and handles input.
    *
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      Console Con = null;
      String CommandInput = null;
      boolean prefMoneyMode = false;
      String prefStack = null;
      Stack CalcStack = new Stack();
      int OptionEntry;
      boolean clClearPrefs = false, clPersistentStack = false;


      System.out.println("+--------------------------------------------------------------+");
      System.out.println("|                       RPN Calculator                    v" + QueryVersion() + " |");
      System.out.println("|       Written by Michael Fross.  All rights reserved.        |");
      System.out.println("+--------------------------------------------------------------+");

      // Initialize the console used for command input
      Con = System.console();
      if (Con == null) {
         System.out.println("ERROR:  Could not initialize OS Console");
         System.exit(1);
      }

      // Process Command Line Options
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


      // Start main comman loop
      while (true) {
         System.out.println("+--------------------------------------------------------------+");
         // Display Stack
         for (int i = 0; i < CalcStack.size(); i++) {
            // Display current Stack
            if (prefMoneyMode == true) {
               System.out.println(" " + Number.MoneyFormat(CalcStack.get(i).toString()));
            } else {
               System.out.println(" " + Number.Comma(CalcStack.get(i).toString()));
            }
         }

         // Input Command String from User
         CommandInput = Con.readLine("\n>> ");

         if (CommandInput.matches("^[Hh]")) {
            // Process Help
            UsageInLoop();

         } else if (CommandInput.matches("^[Cc]")) {
            // Process Clear
            CalcStack = new Stack();

         } else if (CommandInput.matches("^[Dd]")) {
            // Process Delete
            if (!CalcStack.isEmpty()) {
               CalcStack.pop();
            }

         } else if (CommandInput.matches("^[XxQq]")) {
            // Process Exit
            break;

         } else if (CommandInput.matches("^[Ff]")) {
            // Process Flip
            if (CalcStack.size() < 2) {
               System.out.println("ERROR:  Two elements are needed for flip");
            } else {
               Object Temp1 = CalcStack.pop();
               Object Temp2 = CalcStack.pop();
               CalcStack.push(Temp1);
               CalcStack.push(Temp2);
            }

         } else if (CommandInput.matches("^[Ss]")) {
            // Process Sign
            if (!CalcStack.isEmpty()) {
               double Temp = Double.valueOf(CalcStack.pop().toString());
               CalcStack.push(Temp * -1);
            }

         } else if (CommandInput.matches("^[Mm]")) {
            // Process Money Mode Toggle
            if (prefMoneyMode == false) {
               Main.DebugPrint("DEBUG:  MoneyMode Now On");
               prefMoneyMode = true;
            } else {
               Main.DebugPrint("DEBUG:  MoneyMode Now Off");
               prefMoneyMode = false;
            }

         } else if (CommandInput.matches("[\\*\\+\\-\\/\\^]")) {
            // Process Operand Entry
            CalcStack = Number.SimpleMath(CommandInput.charAt(0), CalcStack);

         } else if (CommandInput.matches("^-?\\d+(\\.)?\\d*")) {
            // Process Number Entered. Convert to double first
            //TODO: Handle ending a number in an operand
            CalcStack.push(Double.valueOf(CommandInput));

         } else if (!CommandInput.isEmpty()) {
            // Display an error if I didnt' understand the input
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
    * QueryVersion
    * Returns the version number of the application
    */
   public static String QueryVersion() {
      return VERSION;
   }

   /**
    * Display usage information inside of the command input loop
    */
   public static void UsageInLoop() {
      System.out.println("\n+--------------------------------------------------------------+");
      System.out.println("|Commands:                                                     |");
      System.out.println("|  c - Clear Stack   d - Del last item   f - Flip last 2 items |");
      System.out.println("|  s - Change Sign   x - Exit Program    h - Help              |");
      System.out.println("|  m - MoneyMode                                               |");
   }

   /**
    * Display command line usage information
    */
   public static void Usage() {
      System.out.println("Usage:   java -jar rpn.jar [-D] -[c] -[p] -[h] -[?]\n");
      System.out.println("   -D:  Debug Mode.  Displays debug output.");
      System.out.println("   -c:  Clear Preferences");
      System.out.println("   -p:  Use Persistent Stack");
      System.out.println("   -h:  Display this help information '-?' can also be used.");
   }

   /**
    * Simple method that prints debug information if in debug mode
    */
   public static void DebugPrint(String Msg) {
      if (Main.clDebug == true) {
         System.out.println(Msg);
      }
   }
} // End Class

