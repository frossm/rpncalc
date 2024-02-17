/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2024 Michael Fross
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
import org.fusesource.jansi.Ansi.Color;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class Browser {
   static Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");

   /**
    * Launch(): Launch the defined browser to the provided URL
    *
    * @param url URL to the User Guide
    */
   protected static void Launch(String url) {
      String command = "";

      try {
         // Get the configured browser from prefs
         File browser = new File(prefConfig.get("browser", "none"));

         // Ensure the browser in prefs is valid
         if (!browser.canExecute()) {
            ConfigureBrowser("");
            browser = new File(prefConfig.get("browser", "none"));
         }

         // Set the command to execute
         command = browser.getAbsolutePath() + " " + url;

         Output.debugPrintln("Browser Command: '" + command + "'");

         // Open the browser
         Runtime.getRuntime().exec(command);

      } catch (IOException ex) {
         Output.printColorln(Color.RED, "ERROR: Could not launch browser: '" + command + "'");
      }
   }

   /**
    * ConfigureBrowser(): Sets the provided string as the configured browser assuming it's valid
    *
    * @param bName String of the full path to the local web browser
    */
   protected static void ConfigureBrowser(String bName) {
      // Ensure the provided path is a valid file
      if (new File(bName).canExecute()) {
         // Save this file as the browser in the config and return
         prefConfig.put("browser", bName);
         Output.printColorln(Color.CYAN, "Browser set to: '" + prefConfig.get("browser", "<not configured>") + "'");

      } else {
         Output.printColorln(Color.RED, "ERROR: Provided browser is not valid: '" + bName + "'");
         // Since the provided bName is not valid, ask for one explicitly
         Output.printColorln(Color.YELLOW, "Enter the full path to your preferred browser");
         Output.printColorln(Color.YELLOW, "NOTE: Always use slashes '/' instead of backslashes '\\')");

         try {
            String browserInput = Main.scanner.readLine(":: ");
            Output.println("");

            // If the user entered nothing, remove preference
            if (browserInput.isEmpty()) {
               prefConfig.remove("browser");

            } else {
               // Confirm entered browser can be executed
               if (!new File(browserInput).canExecute()) {
                  Output.printColorln(Color.RED, "ERROR: Provided file is not executable: '" + browserInput + "'");
               } else {
                  prefConfig.put("browser", browserInput);
                  Output.printColorln(Color.CYAN, "Browser set to: '" + prefConfig.get("browser", "<not configured>") + "'");
               }

            }

         } catch (Exception e) {
            Output.printColorln(Color.RED, "ERROR: Could not read user input");
         }

      }

   }

}