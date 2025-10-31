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

import java.util.Stack;

public class CommandHistory {
   // Class Variables
   static final Stack<String> cmdHistory = new Stack<>();

   /**
    * addCommand(): Add a command to the command history
    *
    * @param cmdInput      Full entered command
    * @param cmdInputCmd   First part of the input - the command
    * @param cmdInputParam Parameters to that input
    */
   public static void addCommand(String cmdInput, String cmdInputCmd, String cmdInputParam) {
      String[] commandsToIgnore = {"list", "debug", "ver", "h", "help", "?", "rec", "rep", "func", "reset"};
      boolean saveHistory = true;
      for (String s : commandsToIgnore) {
         if (cmdInput.startsWith(s)) {
            saveHistory = false;
            break;
         }
      }
      if (saveHistory) {
         cmdHistory.push(cmdInput + "##" + cmdInputCmd + "##" + cmdInputParam);
      }

   }

   /**
    * remove(): Remove the last command history item
    */
   public static void remove() {
      cmdHistory.remove(cmdHistory.size() - 1);
   }

   /**
    * clear(): Clear the command history
    */
   public static void clear() {
      cmdHistory.clear();
   }

   /**
    * get(i): Return the history item at the provided point
    *
    * @param i Index
    * @return String at index i
    */
   public static String get(int i) {
      try {
         return cmdHistory.get(i);
      } catch (ArrayIndexOutOfBoundsException e) {
         return "";
      }
   }

   /**
    * get(): Return the last command entered
    *
    * @return last command entered
    */
   public static String get() {
      return cmdHistory.get(cmdHistory.size() - 1);
   }

   /**
    * remove(i): Remove the specified command history item
    *
    * @param i index of item to remove
    */
   public static void remove(int i) {
      try {
         cmdHistory.remove(i);
      } catch (ArrayIndexOutOfBoundsException e) {
         // Skip the removal
      }
   }


   /**
    * size():  Return the size of the command history
    *
    * @return the size of the command history stack
    */
   public static int size() {
      return cmdHistory.size();
   }
}
