/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 * Copyright (c) 2011-2026 Michael Fross
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class StackManagement {
   // Class Constants
   protected static final String PREFS_PATH = "/org/fross/rpn/stacks";

   // Class Variables
   private static int currentStackNum = 1;

   /**
    * QueryCurrentStackNum(): Return the current active stack number
    *
    * @return Current active stack number
    */
   public static int QueryCurrentStackNum() {
      return currentStackNum;
   }

   /**
    * QueryStacks(): Return a string array of all current stacks
    *
    * @return String array of all of the current stack
    */
   public static String[] QueryStacks() {
      String[] stacks = {};
      Preferences prefsQuery = Preferences.userRoot().node(PREFS_PATH);

      try {
         stacks = prefsQuery.childrenNames();
      } catch (BackingStoreException ex) {
         Output.printColor(Output.RED, "Error Reading Stacks from Java Preferences");
      }

      return (stacks);
   }

   /**
    * SaveStack(): Save the provided stack into the preferences system
    *
    * @param stk       - Stack to Save
    * @param stackSlot - Stack Save slot number. Should be default, 1, or 2.
    */
   public static void SaveStack(StackObj stk, String stackSlot) {
      Output.debugPrintln("SaveStack: " + PREFS_PATH + "/" + stk.queryStackName() + "/" + stackSlot);

      // Override the default stack location with the provided one
      Preferences p = Preferences.userRoot().node(PREFS_PATH + "/" + stk.queryStackName() + "/" + stackSlot);

      // Let's clear out any stack values that may exist
      try {
         p.clear();
      } catch (BackingStoreException e) {
         Output.printColorln(Output.RED, "ERROR: Could not clear current preferences in Stack #" + stackSlot);
         Output.printColorln(Output.RED, e.getMessage());
      }

      // Save number of elements to key StackElements
      p.putInt("StackElements", stk.size());

      // Loop through each member of the stack and save it to the preferences
      for (int i = 0; i <= stk.size() - 1; i++) {
         Output.debugPrintln("  - Saving #" + (stk.size() - i) + ":  " + stk.get(i).toPlainString());
         p.put("Stack" + i, stk.get(i).toPlainString());
      }

   }

   /**
    * Toggle CurrentStackNum(): Toggle to the next stack
    */
   public static void ToggleCurrentStackNum() {
      if (currentStackNum == 1) currentStackNum = 2;
      else currentStackNum = 1;
   }

}
