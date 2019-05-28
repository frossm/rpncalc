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

		// Copy the elements in the stack to a temporary stack except for the one to delete
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

}
