/* ------------------------------------------------------------------------------
 * Library Project
 *
 *  Library holds methods and classes frequently used by my programs.
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
package org.fross.library;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

public class URLOperationsTest {
   final String URL_ADDRESS = "https://greenwoodsoftware.com/less";
   final String TEST_RESULT = "<title> Less </title>";

   // Set Temp Directory for import / export testing
   @TempDir
   static Path tempDir;

   /**
    * Test ReadURL
    */
   @Test
   void ReadURLTest() {
      String result;

      try {
         result = URLOperations.ReadURL(URL_ADDRESS);
         assertTrue(result.contains(TEST_RESULT));
      } catch (Exception ex) {
         fail(ex.getMessage());
      }

   }

   /**
    * Test DownloadURLToFile
    */
   @Test
   void DownloadURLToFileTest() {
      File tempFile = tempDir.resolve("RPNCalc_URLOperationsTest").toFile();

      try {
         URLOperations.DownloadURLToFile(URL_ADDRESS, tempFile.getAbsolutePath());
      } catch (Exception ex) {
         fail();
      }

      // Test if the file exists
      Output.printColorln(Output.WHITE, "DownloadURLToFile File: " + tempFile.getAbsolutePath());
      assertTrue(tempFile.exists());

      // Test that it contain the string TEST_FUNCTION_NAME
      try (Stream<String> lines = Files.lines(Path.of(tempFile.toURI()))) {
         assertTrue(lines.anyMatch(line -> line.contains(TEST_RESULT)));
      } catch (IOException ex) {
         fail(ex.getMessage());
      }

      // Remove the downloaded file
      if (tempFile.delete()) {
         // Test that the file no longer exists
         assertFalse(tempFile.exists());
      } else {
         fail("Unable to delete test file: " + tempFile.getAbsolutePath());
      }

   }

}
