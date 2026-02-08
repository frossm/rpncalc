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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackManagementTest {
   /**
    * Test method for {@link org.fross.rpncalc.StackManagement#QueryCurrentStackNum()}.
    */
   @Test
   void testQueryCurrentStackNum() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      assertEquals(1, StackManagement.QueryCurrentStackNum());

      StackOperations.cmdSwapStack(stk1, stk2);
      assertEquals(2, StackManagement.QueryCurrentStackNum());

      StackOperations.cmdSwapStack(stk1, stk2);
      assertEquals(1, StackManagement.QueryCurrentStackNum());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackManagement#ToggleCurrentStackNum()}.
    */
   @Test
   void testToggleCurrentStackNum() {
      StackManagement.ToggleCurrentStackNum();
      assertEquals(2, StackManagement.QueryCurrentStackNum());
      StackManagement.ToggleCurrentStackNum();
      assertEquals(1, StackManagement.QueryCurrentStackNum());
   }

   /**
    * Testing querying the name of the loaded stack
    */
   @Test
   void testQueryLoadedStack() {
      StackObj stk = new StackObj();

      stk.setStackNameAndRestore("junittest", "1");
      assertEquals("junittest", stk.queryStackName());
   }

}