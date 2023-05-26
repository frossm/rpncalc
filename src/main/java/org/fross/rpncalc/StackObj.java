/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2023 Michael Fross
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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;
import java.util.prefs.Preferences;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

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
	 * 
	 */
	public void clear() {
		calcStack.clear();
	}

	/**
	 * clone(): Return a clone of the calcStack object
	 * 
	 */
	@Override
	public StackObj clone() throws CloneNotSupportedException {
		return (StackObj) super.clone();
	}

	/**
	 * Return the stack value provided as a string
	 * 
	 * @param index
	 * @return
	 */
	public String getAsString(int index) {
		return this.get(index).toEngineeringString();
	}

	/**
	 * get(): Return the calculator stack item at the index provided
	 * 
	 * @param index
	 * @return
	 */
	public BigDecimal get(int index) {
		return calcStack.get(index);
	}

	/**
	 * getStack(): Return the entire calculator stack as a stack<BigDecimal>
	 * 
	 * @return
	 */
	public Stack<BigDecimal> getStack() {
		return calcStack;
	}

	/**
	 * isEmpty(): Return true if the calculator stack is empty - false if not
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (calcStack.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * replaceStack(): Replace the existing calc stack with the provided one
	 * 
	 * @param stk
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
	 * @return
	 */
	public BigDecimal peek() {
		return calcStack.peek();
	}

	/**
	 * pop(): Remove and return the item on the top of the calculator stack
	 * 
	 * @return
	 */
	public BigDecimal pop() {
		return calcStack.pop();
	}

	/**
	 * push(): Add an item onto the top of the stack
	 * 
	 * @param item
	 */
	public void push(BigDecimal item) {
		calcStack.push(item);
	}

	/**
	 * push(): Add an item onto the top of the stack
	 * 
	 * @param item
	 */
	public void push(String item) {
		calcStack.push(new BigDecimal(item, this.mc));
	}

	/**
	 * push(): Add an item onto the top of the stack
	 * 
	 * @param item
	 */
	public void push(Double item) {
		calcStack.push(new BigDecimal(String.valueOf(item), this.mc));
	}

	/**
	 * push(): Add an item onto the top of the stack
	 * 
	 * @param item
	 */
	public void push(int item) {
		calcStack.push(new BigDecimal(String.valueOf(item), this.mc));
	}

	/**
	 * queryStackName(): Return the name of this stack
	 * 
	 * @return
	 */
	public String queryStackName() {
		return this.stackName;
	}

	/**
	 * remove(): Remove an item from the calculator stack at the index provided
	 * 
	 * @param index
	 */
	public void remove(int index) {
		calcStack.remove(index);
	}

	/**
	 * saveUndo(): Add the current calcStack to the undo stack. Usually done before a calculator operation is performed
	 * 
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
	 * @param name
	 */
	public void setStackNameAndRestore(String name, String slot) {
		final String PREFS_PATH = "/org/fross/rpn/stacks";
		this.stackName = name;

		// Clear this stack before restoring
		this.clear();

		Output.debugPrint("RestoreStack: " + PREFS_PATH + "/" + this.queryStackName() + "/" + slot);

		// Override the default stack location with the provided one
		Preferences prefs = Preferences.userRoot().node(PREFS_PATH + "/" + this.queryStackName() + "/" + slot);
		int numElements = prefs.getInt("StackElements", 0);

		Output.debugPrint("Restoring Stack:");
		for (int i = 0; i < numElements; i++) {
			this.push(prefs.getDouble("Stack" + i, 0.0));
			Output.debugPrint("  - Restoring #" + (numElements - i) + ":  " + this.get(i));
		}

		// Set the stack number to be 1 on a newly restored stack
		if (StackManagement.QueryCurrentStackNum() == 2) {
			StackManagement.ToggleCurrentStackNum();
		}

	}

	/**
	 * size(): Return the size of the calculator stack
	 * 
	 * @return
	 */
	public int size() {
		return calcStack.size();
	}

	/**
	 * sort(): Sort the stack. Requires the parameter "ascending" or "descending"
	 * 
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

		// Clear the calcStack and replace the values with the sorted stack
		calcStack.clear();

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
	 * @param index
	 * @return
	 */
	public Stack<BigDecimal> undoGet(int index) {
		return undoStack.get(index);
	}

	/**
	 * undoGet(): If no index is provided, return the entire undo stack
	 * 
	 * @return
	 */
	public Stack<Stack<BigDecimal>> undoGet() {
		return undoStack;
	}

	/**
	 * undoRemove(): Remove the undo stack item at the index provided
	 * 
	 * @param index
	 */
	public void undoRemove(int index) {
		undoStack.remove(index);
	}

	/**
	 * undoSize(): Return the size of the undo stack
	 * 
	 * @return
	 */
	public int undoSize() {
		return undoStack.size();
	}

}
