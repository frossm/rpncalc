package org.fross.rpn;

import java.util.Stack;

public class StackOps {

	/**
	 * StackDeleteItem(): Delete a stack element
	 * 
	 * @param stk
	 * @param lineToDelete
	 * @return
	 */
	public static Stack<Double> StackDeleteItem(Stack<Double> stk, int elementToDelete) {
		Stack<Double> tempStack = new Stack<Double>();

		// Copy the elements in the stack to a temporary stack except for the one to
		// delete
		try {
			for (int i = 0; i <= elementToDelete; i++) {
				if (i != elementToDelete) {
					Debug.Print("Moving line:    #" + (i + 1) + " [" + stk.peek() + "] to a temp stack");
					tempStack.push(stk.pop());
				} else {
					Debug.Print("Skipping Line:  #" + (i + 1) + " [" + stk.peek() + "] as it's being deleted");
					stk.pop();
				}
			}
		} catch (Exception ex) {
			Debug.Print(ex.getMessage());
		}

		// Copy the elements in the temp stack back to the main one
		try {
			while (tempStack.size() > 0) {
				Debug.Print("Restore Value:  " + tempStack.peek());
				stk.push(tempStack.pop());
			}
		} catch (Exception ex) {
			Debug.Print(ex.getMessage());
		}

		return (stk);
	}

	/**
	 * StackSwapItems(): Swap two elements in the stack
	 * 
	 * Approach: Empty the stack into an array.  
	 * Replace the existing values with the swapped values.
	 * Then recreate the stack.
	 * 
	 * @param stk
	 * @param item1
	 * @param item2
	 * @return
	 */
	public static Stack<Double> StackSwapItems(Stack<Double> stk, int item1, int item2) {
		int stkSize = stk.size();
		Double tempArray[] = new Double[stkSize];
		Double value1;
		Double value2;

		// Populate the array with the contents of the stack
		Debug.Print("Size of Stack is: " + stkSize);
		for (int i = 0; i < stkSize; i++) {
			// System.out.println("i = " + i);
			System.out.println("Array [" + i + "] = " + stk.peek());
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
			System.out.println("Array [" + i + "] -> Stack");
			stk.push(tempArray[i]);
		}

		return (stk);
	}

}
