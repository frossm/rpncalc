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

import java.io.*;
import java.net.URI;
import java.net.URL;

public class URLOperations {

   /**
    * ReadURL: Retrieve data from a website
    *
    * @param urlString
    * @return
    * @throws Exception
    */
   public static String ReadURL(String urlString) throws Exception {
      BufferedReader reader = null;
      StringBuilder buffer = null;

      try {
         URL url = new URI(urlString).toURL();
         reader = new BufferedReader(new InputStreamReader(url.openStream()));
         buffer = new StringBuilder();
         int read;
         char[] chars = new char[1024];
         while ((read = reader.read(chars)) != -1) {
            buffer.append(chars, 0, read);
         }

      } catch (Exception ex) {
         Output.printColorln(Output.RED, "ERROR: An IO Error Occurred\n" + ex.getMessage());
      } finally {
         if (reader != null) {
            reader.close();
         }
      }

      return buffer.toString();
   }

   /**
    * DownloadFileFromURL(): Download a file from a URL to the provided directory
    *
    * @param urlStr
    * @param file
    * @throws IOException
    */
   public static void DownloadURLToFile(String urlStr, String file) throws IOException {
      final int blockSize = 1024;
      URL url;
      BufferedInputStream bis = null;
      FileOutputStream fos = null;

      try {
         url = new URI(urlStr).toURL();
         bis = new BufferedInputStream(url.openStream());
         fos = new FileOutputStream(file);

      } catch (Exception ex) {
         Output.printColorln(Output.RED, "ERROR: An error occurred opening the URL\n" + ex.getMessage());
      }

      // Download chunks of the file and write them out
      int count;
      byte[] buffer = new byte[blockSize];
      try {
         while ((count = bis.read(buffer, 0, blockSize)) != -1) {
            fos.write(buffer, 0, count);
         }

      } catch (NullPointerException ex) {
         Output.printColorln(Output.RED, "ERROR: An error occurred reading from the URL\n" + ex.getMessage());
      }

      // Cleanup by closing the streams
      fos.close();
      bis.close();
   }

   /**
    * main(): Used for testing DownloadFileFromURL()
    *
    * @param args URL
    */
   public static void main(String[] args) {
      String url = args[0];
      String fullFilePathAndName = args[1].replace('\\', '/');

      // Download Test
      try {
         System.out.println("Testing URL Download to File:");
         System.out.println("   Downloading: " + url);
         System.out.println("   Destination: " + fullFilePathAndName);
         DownloadURLToFile(url, fullFilePathAndName);
      } catch (IOException ex) {
         System.out.println("An IO Error Occurred: Arguments are URL, DownloadedFilePath\n" + ex.getMessage());
      }
   }

}
