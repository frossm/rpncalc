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

import org.fross.library.Output;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael Fross (michael@fross.org)
 */
class UserDefinedFunctionsTest {
   final String TEST_FUNCTION_NAME = "automated-test-function";

   // Set Temp Directory for import / export testing
   @TempDir
   static Path tempDir;

   /**
    * Remove the automated-testing UDF
    * Note: This is not an actual test, but rather a support method
    */
   void RemoveUDFFunction() {
      UserFunctions.FunctionDelete(TEST_FUNCTION_NAME);
   }

   /**
    * Test creating, testing, and deleting a UDF
    */
   @Test
   void UDFFunctionTest() {
      // If the test function already exists for some reason delete it
      if (UserFunctions.FunctionExists(TEST_FUNCTION_NAME)) {
         RemoveUDFFunction();
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
      UserFunctions.cmdRecord("off " + TEST_FUNCTION_NAME);
      assertFalse(UserFunctions.recordingIsEnabled());

      // Ensure the test exists
      assertTrue(UserFunctions.FunctionExists(TEST_FUNCTION_NAME));

      // Use the new function to add two numbers
      CommandParser.Parse(stk, stk, "4", "4", "");
      assertEquals(2, stk.size());
      CommandParser.Parse(stk, stk, TEST_FUNCTION_NAME, TEST_FUNCTION_NAME, "");
      assertEquals(1, stk.size());
      assertEquals("150155627.0", stk.getAsString(0));

      // --------------- Function Export Test ---------------
      File tempFile = tempDir.resolve("RPNCalc_UDFTest").toFile();

      // Export the UDFs
      CommandParser.Parse(stk, stk, "func export " + tempFile.getAbsolutePath(), "func", "export " + tempFile.getAbsolutePath());

      // Test if the file exists
      assertTrue(tempFile.exists());

      // Test that it contain the string TEST_FUNCTION_NAME
      try (Stream<String> lines = Files.lines(Path.of(tempFile.toURI()))) {
         assertTrue(lines.anyMatch(line -> line.contains(TEST_FUNCTION_NAME)));
      } catch (IOException ex) {
         Output.printColorln(Output.RED, "Func Export test failed: \n " + ex.getMessage());
         fail(ex.getMessage());
      }

      // --------------- Function Import Test ---------------
      // Remove all functions and import the previously exported file
      CommandParser.Parse(stk, stk, "func delall", "func", "delall");

      // Restore functions back to what they were before we tested
      CommandParser.Parse(stk, stk, "func import " + tempFile.getAbsolutePath(), "func", "import " + tempFile.getAbsolutePath());

      // Test that the function we created earlier exists
      assertTrue(UserFunctions.FunctionExists(TEST_FUNCTION_NAME));

      // --------------- Clean Up ---------------
      // Remove the exported file
      if (tempFile.delete()) {
         Output.println("Text Function Export File Successfully Deleted: " + tempFile.getAbsolutePath());
         assertFalse(tempFile.exists());
      } else {
         Output.printColorln(Output.RED, "Text Function Export File Could Not Be Deleted: " + tempFile.getAbsolutePath());
      }

      // Remove the created function
      RemoveUDFFunction();
      assertFalse(UserFunctions.FunctionExists(TEST_FUNCTION_NAME));

   }

}
