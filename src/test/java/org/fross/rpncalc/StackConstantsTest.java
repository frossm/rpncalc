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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class StackConstantsTest {
	@Test
	void testCmdPI() {
		StackObj calcStack = new StackObj();
		StackConstants.cmdPI(calcStack);

		// Ensure the stack is not null
		assertNotNull(calcStack);

		// Ensure there is only one items in the stack
		assertEquals(1, calcStack.size());

		// Ensure that the value is correct
		assertEquals(java.lang.Math.PI, calcStack.get(0));
	}

	@Test
	void testCmdPHI() {
		StackObj calcStack = new StackObj();
		StackConstants.cmdPHI(calcStack);

		// Ensure the stack is not null
		assertNotNull(calcStack);

		// Ensure there is only one items in the stack
		assertEquals(1, calcStack.size());

		// Ensure that the value is correct
		assertEquals(1.61803398874989, calcStack.get(0));
	}

	@Test
	void testCmdEuler() {
		StackObj calcStack = new StackObj();
		StackConstants.cmdEuler(calcStack);

		// Ensure the stack is not null
		assertNotNull(calcStack);

		// Ensure there is only one items in the stack
		assertEquals(1, calcStack.size());

		// Ensure that the value is correct
		assertEquals(2.7182818284590452353602874713527, calcStack.get(0));
	}

	@Test
	void testCmdSpeedOfLight() {
		StackObj calcStack = new StackObj();
		StackConstants.cmdSpeedOfLight(calcStack);

		// Ensure the stack is not null
		assertNotNull(calcStack);

		// Ensure there is only one items in the stack
		assertEquals(1, calcStack.size());

		// Ensure that the value is correct
		assertEquals((double) 299792458, calcStack.get(0));
	}

}
