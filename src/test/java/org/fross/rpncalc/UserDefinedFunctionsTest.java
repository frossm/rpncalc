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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael Fross (michael@fross.org)
 */
class UserDefinedFunctionsTest {
   /**
    * Test recording, testing, and deleting a UDF
    */
   @Test
   void CreateFunction() {
      String testFunctionName = "automated-testing";

      // If the test function already exists for some reason delete it
      if (UserFunctions.FunctionExists(testFunctionName)) {
         UserFunctions.FunctionDelete(testFunctionName);
      }

      // Create the test stack
      StackObj stk = new StackObj();

      // Add several values to the stack
      stk.push("150123123");
      stk.push(3.25e4);
      assertEquals(2, stk.size());

      // Enable recording
      UserFunctions.cmdRecord("on");
      assertTrue(UserFunctions.recordingIsEnabled());

      // Add the numbers on the stack together
      CommandParser.Parse(stk, stk, "+", "+", "");
      UserFunctions.RecordCommand("+");
      assertEquals(1, stk.size());
      assertEquals("150155623.0", stk.getAsString(0));

      // Stop the recording
      UserFunctions.cmdRecord("off " + testFunctionName);
      assertFalse(UserFunctions.recordingIsEnabled());

      // Ensure the test is in the preferences system
      assertTrue(UserFunctions.FunctionExists(testFunctionName));

      // Use the new function to add two numbers
      CommandParser.Parse(stk, stk, "4", "4", "");
      assertEquals(2, stk.size());
      CommandParser.Parse(stk, stk, testFunctionName, testFunctionName, "");
      assertEquals(1, stk.size());
      assertEquals("150155627.0", stk.getAsString(0));

      // Remove the test function
      UserFunctions.FunctionDelete(testFunctionName);
      assertFalse(UserFunctions.FunctionExists(testFunctionName));

   }

}
