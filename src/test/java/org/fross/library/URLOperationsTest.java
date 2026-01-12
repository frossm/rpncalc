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

public class URLOperationsTest {
   final String URL_ADDRESS = "https://greenwoodsoftware.com/less";
   final String DOWNLOAD_FILE = "rpncalc_test";
   final String TEST_RESULT = "<title> Less </title>";

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
         fail();
      }

   }

   /**
    * Test DownloadURLToFile
    */
   @Test
   void DownloadURLToFileTest() {
      try {
         URLOperations.DownloadURLToFile(URL_ADDRESS, DOWNLOAD_FILE);
      } catch (Exception ex) {
         fail();
      }

      // Test if the file exists
      File testFile = new File(DOWNLOAD_FILE);
      Output.printColorln(Output.WHITE, "DownloadURLToFile File: " + testFile.getAbsolutePath());
      assertTrue(testFile.exists());

      // Test that it contain the string TEST_FUNCTION_NAME
      try (Stream<String> lines = Files.lines(Path.of(testFile.toURI()))) {
         assertTrue(lines.anyMatch(line -> line.contains(TEST_RESULT)));
      } catch (IOException ex) {
         fail();
      }

      // Remove the downloaded file
      if (testFile.delete()) {
         // Test that the file no longer exists
         assertFalse(testFile.exists());
      } else {
         Output.printColorln(Output.RED, "DownloadURLToFileTest TestFile Could Not Be Deleted: " + testFile.getAbsolutePath());
         fail();
      }

   }

}
