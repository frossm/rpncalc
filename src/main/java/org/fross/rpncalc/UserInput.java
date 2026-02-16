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

import org.fross.library.Output;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;

import java.nio.file.Paths;

public class UserInput {
   private static LineReader lineReader = null;
   private static Terminal terminal = null;

   /**
    * setTerminal(): Main should call this once at startup
    */
   public static void setTerminal(Terminal t) {
      terminal = t;
   }

   /**
    * GetInput(): Read user input from the terminal
    */
   public static String GetInput(String prompt) {
      String inputString = "";

      // Ensure Main has provided a terminal
      if (terminal == null) {
         Output.fatalError("Terminal is not initialized. Call UserInput.setTerminal() first", 11);
      }

      // Lazy initialization of the LineReader (only once)
      if (lineReader == null) {
         try {
            lineReader = LineReaderBuilder.builder()
                  .terminal(terminal)
                  .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".rpncalc_history"))
                  .history(new DefaultHistory())
                  .build();

         } catch (Exception ex) {
            Output.printColorln(Output.RED, "Error creating the persistent LineReader:\n" + ex.getMessage());
         }
      }

      // Read the user input
      try {
         inputString = lineReader.readLine(prompt);

      } catch (UserInterruptException | EndOfFileException ex) {
         // User entered Ctrl-C or Ctrl-D. Return "exit" so Main can close gracefully
         Output.printColorln(Output.CYAN, "Exiting...");
         inputString = "exit";
      } catch (Exception ex) {
         Output.printColorln(Output.RED, "Error reading input:\n" + ex.getMessage());
      }

      return inputString;
   }
}