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

/**
 * @author Michael Fross (michael@fross.org)
 */
public class CommandHistoryTest {

   /**
    * Test History Commands
    */
   @Test
   void testHistoryCommands() {
      // Add 3 commands
      CommandHistory.addCommand("44", "44", "");
      CommandHistory.addCommand("4/", "4/", "");
      CommandHistory.addCommand("copy", "copy", "");
      assertEquals(3, CommandHistory.size());

      // Get without param
      assertEquals("copy##copy##", CommandHistory.get());
      assertEquals("44##44##", CommandHistory.get(0));

      // Get with param
      CommandHistory.addCommand("LongCommand", "LongCommand", "ParamHere");
      assertEquals(4, CommandHistory.size());
      assertEquals("LongCommand##LongCommand##ParamHere", CommandHistory.get());

      // Remove that 44 from the beginning
      CommandHistory.remove(0);
      assertEquals(3, CommandHistory.size());
      assertEquals("4/##4/##", CommandHistory.get(0));

      // Remove illegal index value
      CommandHistory.remove(-1);
      assertEquals(3, CommandHistory.size());

      // Remove the last command
      CommandHistory.remove();
      assertEquals(2, CommandHistory.size());
      assertEquals("4/##4/##", CommandHistory.get(0));

      // Test Clear
      CommandHistory.clear();
      assertEquals(0, CommandHistory.size());
      assertEquals("", CommandHistory.get(0));
      assertEquals("", CommandHistory.get(CommandHistory.size() - 1));
   }

}
