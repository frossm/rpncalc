/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2026 Michael Fross
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StackConstantsTest {
   @Test
   void testCmdPI() {
      StackObj calcStack = new StackObj();
      StackConstants.cmdPI(calcStack);

      // Ensure the stack is not null
      assertNotNull(calcStack);

      // Ensure there is only one item in the stack
      assertEquals(1, calcStack.size());

      // Ensure that the value is correct
      assertEquals("3.14159265358979323846264338327950288419716939937510", calcStack.getAsString(0));
   }

   @Test
   void testCmdPHI() {
      StackObj calcStack = new StackObj();
      StackConstants.cmdPHI(calcStack);

      // Ensure the stack is not null
      assertNotNull(calcStack);

      // Ensure there is only one item in the stack
      assertEquals(1, calcStack.size());

      // Ensure that the value is correct
      assertEquals("1.61803398874989", calcStack.getAsString(0));
   }

   @Test
   void testCmdEulersNumber() {
      StackObj calcStack = new StackObj();
      StackConstants.cmdEulersNumber(calcStack);

      // Ensure the stack is not null
      assertNotNull(calcStack);

      // Ensure there is only one item in the stack
      assertEquals(1, calcStack.size());

      // Ensure that the value is correct
      assertEquals("2.71828182845904523536028747135266249775724709369995", calcStack.getAsString(0));
   }

   @Test
   void testCmdEulersConstant() {
      StackObj calcStack = new StackObj();
      StackConstants.cmdEulersConstant(calcStack);

      // Ensure the stack is not null
      assertNotNull(calcStack);

      // Ensure there is only one item in the stack
      assertEquals(1, calcStack.size());

      // Ensure that the value is correct
      assertEquals("0.577215664901532860606512090082402431", calcStack.getAsString(0));
   }

   @Test
   void testCmdSpeedOfLight() {
      StackObj calcStack = new StackObj();
      StackConstants.cmdSpeedOfLight(calcStack);

      // Ensure the stack is not null
      assertNotNull(calcStack);

      // Ensure there is only one item in the stack
      assertEquals(1, calcStack.size());

      // Ensure that the value is correct
      assertEquals("299792458", calcStack.getAsString(0));
   }

}
