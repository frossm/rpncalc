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

import org.fross.library.Debug;
import org.fross.library.Output;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackOperationsTest {
   // Set Temp Directory for import / export testing
   @TempDir
   static Path tempDir;

   /**
    * Testing turning debug on and off
    */
   @Test
   void testCmdDebug() {
      assertFalse(Debug.query());
      StackOperations.cmdDebug();
      assertTrue(Debug.query());
      StackOperations.cmdDebug();
      assertFalse(Debug.query());
   }

   /**
    * Test reversing the stack with the 'rev' command
    */
   @Test
   void testReverseCommand() {
      StackObj stk = new StackObj();

      // Load the stack
      stk.push(1.0);
      stk.push(2.0);
      stk.push(3.0);
      stk.push(4.0);
      stk.push(5.0);

      // Check the current stack
      assertEquals(5, stk.size());
      assertEquals(1.0, stk.get(0).doubleValue());
      assertEquals(2.0, stk.get(1).doubleValue());
      assertEquals(3.0, stk.get(2).doubleValue());
      assertEquals(4.0, stk.get(3).doubleValue());
      assertEquals(5.0, stk.get(4).doubleValue());

      // Reverse the stack items
      StackOperations.cmdReverse(stk);

      // Validate they are reversed
      assertEquals(5, stk.size());
      assertEquals(5.0, stk.get(0).doubleValue());
      assertEquals(4.0, stk.get(1).doubleValue());
      assertEquals(3.0, stk.get(2).doubleValue());
      assertEquals(2.0, stk.get(3).doubleValue());
      assertEquals(1.0, stk.get(4).doubleValue());

      // Reverse the stack items
      StackOperations.cmdReverse(stk);

      assertEquals(5, stk.size());
      assertEquals(1.0, stk.get(0).doubleValue());
      assertEquals(2.0, stk.get(1).doubleValue());
      assertEquals(3.0, stk.get(2).doubleValue());
      assertEquals(4.0, stk.get(3).doubleValue());
      assertEquals(5.0, stk.get(4).doubleValue());
   }

   /**
    * Test method to create a new test stack, add values to it, and check that you can load it
    */
   @Test
   void testLoadCommand() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      // Clear out any saves items in the junittest saved stack if it exists
      try {
         Preferences.userRoot().node(StackManagement.PREFS_PATH + "/junittest").removeNode();
      } catch (BackingStoreException e) {
         Output.printColorln(Output.RED, e.getMessage());
      }

      // Set the stack names
      stk1.setStackNameAndRestore("junittest", "1");
      stk2.setStackNameAndRestore("junittest", "2");

      // Fill up the stacks
      for (double i = 1.0; i <= 10.00; i++) {
         stk1.push(i);
         stk2.push(i + 100.00);
      }

      // Save the stacks to the registry
      StackManagement.SaveStack(stk1, "1");
      StackManagement.SaveStack(stk2, "2");

      // Load the junit5 test stack and make sure the name is queried successfully
      CommandParser.Parse(stk1, stk2, "load junittest", "load", "junittest");
      assertEquals("junittest", stk1.queryStackName());
      assertEquals("junittest", stk2.queryStackName());

      // stk1: Check to make sure the numbers are accurate by adding them up
      StackCommands.cmdAddAll(stk1, "keep");
      assertEquals(11, stk1.size());
      assertEquals(55, stk1.pop().doubleValue());

      // stk2: Check to make sure the numbers are accurate by adding them up
      StackCommands.cmdAddAll(stk2, "keep");
      assertEquals(11, stk2.size());
      assertEquals(1055, stk2.pop().doubleValue());

      // Load default stack
      StackOperations.cmdLoad(stk1, stk2, "default");
      assertEquals("default", stk1.queryStackName());

      // Load the junittest stack again
      StackOperations.cmdLoad(stk1, stk2, "junittest");
      assertEquals("junittest", stk1.queryStackName());

      // stk1: Check to make sure the numbers are accurate by adding them up
      StackCommands.cmdAddAll(stk1, "keep");
      assertEquals(11, stk1.size());
      assertEquals(55, stk1.pop().doubleValue());

      // stk2: Check to make sure the numbers are accurate by adding them up
      StackCommands.cmdAddAll(stk2, "keep");
      assertEquals(11, stk2.size());
      assertEquals(1055, stk2.pop().doubleValue());

      // Cleanup by removing the junittest stack from the preferences system
      try {
         Preferences.userRoot().node(StackManagement.PREFS_PATH + "/junittest").removeNode();
      } catch (BackingStoreException e) {
         Output.printColorln(Output.RED, e.getMessage());
      }
   }

   /**
    * Test stack output to file
    */
   @Test
   void testExport() {
      File testFile = tempDir.resolve("rpncalc.import").toFile();
      String testFileName = testFile.getAbsolutePath();
      String[] testValues = {"-1.0123", "2.0234", "3.0345", "-15.0456", "-3.123e17", "2.123E8", "2.0567", "17.0678", "38.0789", "53.0891", "14.0123", "73.0234", "72.0345", "72.0456", "10.0567", "83.0678", "-60.0789", "76.0890", "59.090", "30.0234", "-42.0345", "89.0456", "4.56e19", "30.0567", "44.0678", "-31.0789"};

      // Build the StackObject
      StackObj stk = new StackObj();
      for (String testValue : testValues) {
         stk.push(testValue);
      }
      assertEquals(26, stk.size());

      // Delete the testFile if it exists
      try {
         if (testFile.exists() && !testFile.delete()) {
            throw new IOException("Delete Failed");
         }
      } catch (IOException ex) {
         Output.println("Testing Export: Issue deleting test file: ' " + testFileName + "'");
      }

      // Export the file and see if it looks OK
      StackOperations.exportStackToDisk(stk, testFileName);
      assertTrue(testFile.exists());
      assertTrue(testFile.isFile());
      assertTrue(testFile.length() > 0);

      // Import the stack from the file and compare
      try {
         // Verify the filename provided is a file and can be read
         if (new File(testFileName).canRead() && new File(testFileName).isFile()) {
            // Read lines from the file into the ArrayList
            ArrayList<String> linesRead = new ArrayList<>(Files.readAllLines(Paths.get(testFileName)));

            // Test that read-from-file value = test value
            for (int i = 0; i < testValues.length; i++) {
               assertEquals(0, new BigDecimal(testValues[i]).compareTo(new BigDecimal(linesRead.get(i))));
            }
         } else {
            throw new IOException();
         }
      } catch (IOException ex) {
         Output.printColorln(Output.RED, "ERROR: Could not read from the file '" + testFileName + "'");
         Output.printColorln(Output.RED, "ERROR: Please note the file must be in lower case");

      } catch (NumberFormatException ex) {
         Output.printColorln(Output.RED, "The data in '" + testFileName + "' can't be read as it is not in the correct format.\nThe import file format is simply one number per line");
      }

   }

   /**
    * Test import capabilities
    */
   @Test
   void testImport() {
      File testFile = tempDir.resolve("rpncalc.import").toFile();
      String testFileName = testFile.getAbsolutePath();
      String[] testValues = {"-1.0123", "2.0234", "3.0345", "-15.0456", "-3.123e17", "2.123E8", "2.0567", "17.0678", "38.0789", "53.0891", "14.0123", "73.0234", "72.0345", "72.0456", "10.0567", "83.0678", "-60.0789", "76.0890", "59.090", "30.0234", "-42.0345", "89.0456", "4.56e19", "30.0567", "44.0678", "-31.0789"};

      // Create a test file
      try {
         FileWriter fw = new FileWriter(testFileName.toLowerCase());

         // Loop through the test values write them to disk
         for (String testValue : testValues) {
            fw.write(testValue + "\n");
         }

         // Close the FileWriter
         fw.close();

      } catch (Exception ex) {
         Output.printColorln(Output.RED, "ERROR:  Could not create testfile used for import testing");
         fail();
      }

      StackObj stk = new StackObj();
      StackOperations.importStackFromDisk(stk, testFileName);

      // Verify the import values match the file data
      for (int i = 0; i < testValues.length; i++) {
         assertEquals(0, new BigDecimal(testValues[i]).compareTo(stk.get(i)));
      }

   }

   /**
    * Testing stack swapping
    */
   @Test
   void testStackSwap() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      // Standard numbers
      CommandParser.Parse(stk1, stk2, "123", "123", "");
      CommandParser.Parse(stk1, stk2, "456.789", "456.789", "");
      assertEquals(2, stk1.size());

      // Swap the stack and the size should be 0
      CommandParser.Parse(stk1, stk2, "ss", "ss", "");
      assertEquals(0, stk1.size());

      // Swap it back and we should be at 2 again
      CommandParser.Parse(stk1, stk2, "ss", "ss", "");
      assertEquals(2, stk1.size());
   }

}
