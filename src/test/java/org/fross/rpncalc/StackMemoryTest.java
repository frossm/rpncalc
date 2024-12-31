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

import org.junit.jupiter.api.Test;

import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackMemoryTest {
   /**
    * Test method for {@link org.fross.rpncalc.StackMemory#SetMaxMemorySlots(java.lang.String)}.
    */
   @Test
   void testSetMaxMemorySlots() {
      // Save current number of memory slots given changes are persistent
      Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");
      int currentSlots = prefConfig.getInt("memoryslots", Integer.MAX_VALUE);

      // If the value doesn't exist, it will error out - so add one if it's not there
      if (currentSlots == Integer.MAX_VALUE) {
         prefConfig.putInt("memoryslots", Main.CONFIG_DEFAULT_MEMORY_SLOTS);
         currentSlots = Main.CONFIG_DEFAULT_MEMORY_SLOTS;
      }

      boolean result = StackMemory.SetMaxMemorySlots("2099");
      assertTrue(result);
      assertEquals(2099, StackMemory.memorySlots.length);

      // Set slots back to what they were originally
      StackMemory.SetMaxMemorySlots(currentSlots + "");
      assertEquals(currentSlots, StackMemory.memorySlots.length);
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackMemory#QueryInUseMemorySlots()}.
    */
   @Test
   void testQueryInUseMemorySlots() {
      StackObj stk = new StackObj();

      stk.push(1.0);

      // Clear the memory slots
      StackMemory.cmdMem(stk, "clearall");

      // 2 items to slots
      StackMemory.cmdMem(stk, "1 add");
      StackMemory.cmdMem(stk, "2 add");

      // Test there are 2 slots in use
      assertEquals(2, StackMemory.QueryInUseMemorySlots());

      // Remove one and test again
      StackMemory.cmdMem(stk, "1 clear");
      assertEquals(1, StackMemory.QueryInUseMemorySlots());

      // Clear the memory slots
      StackMemory.cmdMem(stk, "clearall");
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackMemory#cmdMem(org.fross.rpncalc.StackObj, java.lang.String)}.
    * add, clear, and clearall tested above
    */
   @Test
   void testCmdMem() {
      StackObj stk = new StackObj();

      // Copy
      stk.push(71.0);
      StackMemory.cmdMem(stk, "clearall");
      StackMemory.cmdMem(stk, "2 add");
      assertEquals(1, StackMemory.QueryInUseMemorySlots());

      StackMemory.cmdMem(stk, "2 copy");
      assertEquals(2, stk.size());
      assertEquals(142, stk.pop().add(stk.pop()).doubleValue());

      stk.push(1.234e12);
      StackMemory.cmdMem(stk, "4 add");
      assertEquals(2, StackMemory.QueryInUseMemorySlots());
      StackMemory.cmdMem(stk, "4 copy");
      Math.Divide(stk);
      assertEquals(1, stk.peek().doubleValue());

      // addall
      stk.clear();
      stk.push(11.0);
      stk.push(12.0);
      stk.push(13.0);
      StackMemory.cmdMem(stk, "addall");
      assertEquals(4, StackMemory.QueryInUseMemorySlots());

      // copyall
      stk.clear();
      StackMemory.cmdMem(stk, "copyall");
      assertEquals(4, stk.size());
      StackCommands.cmdAddAll(stk, "");
      StackCommands.cmdRound(stk, "2");
      assertEquals("1234000000036.00", stk.pop().toEngineeringString());
   }

}
