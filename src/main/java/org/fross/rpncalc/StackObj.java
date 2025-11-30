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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;
import java.util.prefs.Preferences;

public class StackObj implements Cloneable {
   // Class Variables
   protected String stackName;
   protected Stack<BigDecimal> calcStack = new Stack<>();
   protected Stack<Stack<BigDecimal>> undoStack = new Stack<>();

   // Default global match context with unlimited precision
   public final MathContext mc = MathContext.UNLIMITED;

   // Constructor
   StackObj() {
      // Initialize class variables
      this.stackName = "default";
   }

   /**
    * clear(): Remove all items from the calculator stack
    */
   public void clear() {
      calcStack.clear();
   }

   /**
    * clone(): Return a clone of the calcStack object
    */
   @Override
   public StackObj clone() throws CloneNotSupportedException {
      return (StackObj) super.clone();
   }

   /**
    * Return the stack value provided as a string
    *
    * @param index Index of stack value
    * @return value of the stack at the provided index
    */
   public String getAsString(int index) {
      return this.get(index).toString();
   }

   /**
    * get(): Return the calculator stack item at the index provided
    *
    * @param index Index of stack value
    * @return value of the stack at the provided index
    */
   public BigDecimal get(int index) {
      return calcStack.get(index);
   }

   /**
    * getStack(): Return the entire calculator stack as a stack<BigDecimal>
    *
    * @return entire stack object
    */
   public Stack<BigDecimal> getStack() {
      return calcStack;
   }

   /**
    * isEmpty(): Return true if the calculator stack is empty - false if not
    *
    * @return true if stack is empty - false if not
    */
   public boolean isEmpty() {
      return calcStack.isEmpty();
   }

   /**
    * replaceStack(): Replace the existing calc stack with the provided one
    *
    * @param stk Primary stack object
    */
   public void replaceStack(Stack<BigDecimal> stk) {
      calcStack.clear();
      for (int i = 0; i < stk.size(); i++) {
         calcStack.add(i, stk.get(i));
      }
   }

   /**
    * peek(): Return the value at the top of the stack without removing it
    *
    * @return value at the top of the stack
    */
   public BigDecimal peek() {
      return calcStack.peek();
   }

   /**
    * pop(): Remove and return the item on the top of the calculator stack
    *
    * @return value at the top of the stack and remove it from the stack
    */
   public BigDecimal pop() {
      return calcStack.pop();
   }

   /**
    * push(): Add an item onto the top of the stack
    *
    * @param item BigDecimal item to add to the stack
    */
   public void push(BigDecimal item) {
      calcStack.push(item);
   }

   /**
    * push(): Add an item onto the top of the stack
    *
    * @param item String item to add to the sack
    */
   public void push(String item) {
      try {
         this.calcStack.push(new BigDecimal(item, this.mc));
      } catch (Exception ex) {
         Output.printColorln(Ansi.Color.RED, "Error: " + ex.getMessage());
      }
   }

   /**
    * push(): Add a value to the stack at a specific position
    *
    * @param item String to insert into the stack
    * @param location Insert at this point.  The bottom of that stack is 0.
    */
   public void push(String item, int location) {
      BigDecimal[] array = new BigDecimal[calcStack.size()];

      // Loop through stack and build an array of the current stack
      for (int i = 0; i < calcStack.size(); i++) {
         array[i] = calcStack.get(i);
      }

      // Clear the stack and get ready to reload it
      calcStack.clear();

      // Reload the stack pushing the new value at the right spot
      for (int i = 0; i < array.length; i++) {
         if (i == location) {
            calcStack.push(new BigDecimal(item, this.mc));
            calcStack.push(array[i]);
         } else {
            calcStack.push(array[i]);
         }
      }
   }

   /**
    * push(): Add an item onto the top of the stack
    *
    * @param item Double item to add to the stack
    */
   public void push(Double item) {
      calcStack.push(new BigDecimal(String.valueOf(item), this.mc));
   }

   /**
    * push(): Add an item onto the top of the stack
    *
    * @param item Integer item to add to the stack
    */
   public void push(int item) {
      calcStack.push(new BigDecimal(String.valueOf(item), this.mc));
   }

   /**
    * queryStackName(): Return the name of this stack
    *
    * @return String value of the stack name
    */
   public String queryStackName() {
      return this.stackName;
   }

   /**
    * remove(): Remove an item from the calculator stack at the index provided
    *
    * @param index index value of item to remove from the stack
    */
   public void remove(int index) {
      calcStack.remove(index);
   }

   /**
    * saveUndo(): Add the current calcStack to the undo stack. Usually done before a calculator operation is performed
    */
   @SuppressWarnings("unchecked")
   public void saveUndo() {
      try {
         undoStack.push((Stack<BigDecimal>) calcStack.clone());
      } catch (ClassCastException ex) {
         Output.printColor(Ansi.Color.RED, "Error saving to the undo state");
      }

   }

   /**
    * setStackNameAndRestore(): Set the name of this stack and load it from the saved stacks in preferences
    *
    * @param name Name of the stack
    * @param slot number of the stack
    */
   public void setStackNameAndRestore(String name, String slot) {
      final String PREFS_PATH = "/org/fross/rpn/stacks";
      this.stackName = name;

      // Clear this stack before restoring
      this.clear();

      Output.debugPrintln("RestoreStack: " + PREFS_PATH + "/" + this.queryStackName() + "/" + slot);

      // Override the default stack location with the provided one
      Preferences prefs = Preferences.userRoot().node(PREFS_PATH + "/" + this.queryStackName() + "/" + slot);
      int numElements = prefs.getInt("StackElements", 0);

      // Restore the stack items to the stack.  If there is an error, abort the stack restore & continue on
      Output.debugPrintln("Restoring Stack:");

      for (int i = 0; i < numElements; i++) {
         try {
            this.push(prefs.getDouble("Stack" + i, 0.0));
            Output.debugPrintln("  - Restoring #" + (numElements - i) + ":  " + this.get(i));
         } catch (Exception ex) {
            Output.printColor(Ansi.Color.RED, "ERROR: Error restoring stack.  Skipping...");
            break;
         }
      }

      // Set the stack number to be 1 on a newly restored stack
      if (StackManagement.QueryCurrentStackNum() == 2) {
         StackManagement.ToggleCurrentStackNum();
      }

   }

   /**
    * size(): Return the size of the calculator stack
    *
    * @return an integer of the number of items on the stack
    */
   public int size() {
      return calcStack.size();
   }

   /**
    * sort(): Sort the stack. Requires the parameter "ascending" or "descending"
    */
   public void sort(String mode) {
      if (!mode.equalsIgnoreCase("ascending") && !mode.equalsIgnoreCase("descending")) {
         Output.printColorln(Ansi.Color.RED, "ERROR: sort requires a 'ascending' or 'descending' parameter");
         return;
      }

      StackObj sortedStack = new StackObj();

      while (!calcStack.isEmpty()) {
         // Pull out a value from the stack
         BigDecimal tmpValue = calcStack.pop();

         // While temporary stack is not empty and top of stack is greater than the tempValue
         // while (!sortedStack.isEmpty() && sortedStack.peek() > tmpValue) {
         while (!sortedStack.isEmpty() && sortedStack.peek().compareTo(tmpValue) > 0) {
            calcStack.push(sortedStack.pop());
         }

         sortedStack.push(tmpValue);
      }

      // Fill the calcStack back with the sorted values from sortedStack
      if (mode.equalsIgnoreCase("ascending")) {
         // Ascending
         for (int i = sortedStack.size() - 1; i >= 0; i--) {
            calcStack.push(sortedStack.get(i));
         }
      } else {
         // Descending
         for (int i = 0; i < sortedStack.size(); i++) {
            calcStack.push(sortedStack.get(i));
         }
      }

   }

   /**
    * undoGet(): Return the element of the undo stack at the index provided
    *
    * @param index Index of the undo item to return
    * @return stack stack object
    */
   public Stack<BigDecimal> undoGet(int index) {
      return undoStack.get(index);
   }

   /**
    * undoGet(): If no index is provided, return the entire undo stack
    *
    * @return entire undo stack
    */
   public Stack<Stack<BigDecimal>> undoGet() {
      return undoStack;
   }

   /**
    * undoRemove(): Remove the undo stack item at the index provided
    *
    * @param index index of item to remove from the undo stack
    */
   public void undoRemove(int index) {
      undoStack.remove(index);
   }

   /**
    * undoSize(): Return the size of the undo stack
    *
    * @return size Integer of the undo stack size
    */
   public int undoSize() {
      return undoStack.size();
   }

}
