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

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

import java.math.BigDecimal;

import static org.fross.rpncalc.Main.configProgramWidth;

public class Display {

   /**
    * Comma(BigDecimal): Add comma's to the integer portion and return a string
    *
    * @param bd Bid Decimal number to comma-ize
    */
   public static String Comma(BigDecimal bd) {
      String formatString = String.format("%%,.%df", bd.scale());
      return String.format(formatString, bd);
   }

   /**
    * queryDecimalIndex(): Return the index of the decimal in a string.  If none is given assume it's at the end.
    *
    * @param str - String with or without a decimal point in it
    * @return An integer with the location of the decimal (or the end if it doesn't exist)
    */
   public static int queryDecimalIndex(String str) {
      // Determine where the decimal point is located. If no decimal exists (-1) assume it's at the end
      if (!str.contains("."))
         return str.length();
      else
         return str.indexOf(".");
   }

   /**
    * DisplayStatusLine(): Display the last line of the header and the separator line. This is a separate function given it also
    * inserts the loaded stack and spaces everything correctly.
    */
   public static void ShowStatusLine(StackObj calcStack) {
      // Format the number of memory slots used
      String sfMem = String.format("Mem:%02d", StackMemory.QueryInUseMemorySlots());

      // Format the undo level to 2 digits. Can't image I'd need over 99 undo levels
      String sfUndo = String.format("Undo:%02d", calcStack.undoSize());

      // Determine how many dashes to use after remove space for the undo and stack name
      int numDashes = configProgramWidth - 2 - sfMem.length() - sfUndo.length() - calcStack.queryStackName().length() - 11;

      // [Recording] appears if it's turned on. Make room if it's enabled
      if (UserFunctions.recordingIsEnabled()) numDashes -= 12;

      // Print the StatusLine dashes
      Output.printColor(Ansi.Color.CYAN, "+");
      Output.printColor(Ansi.Color.CYAN, "-".repeat(numDashes));

      // Print the StatusLine Data in chunks to be able to better control color output
      if (UserFunctions.recordingIsEnabled()) {
         Output.printColor(Ansi.Color.CYAN, "[");
         Output.printColor(Ansi.Color.RED, "Recording");
         Output.printColor(Ansi.Color.CYAN, "]-");
      }
      Output.printColor(Ansi.Color.CYAN, "[");
      Output.printColor(Ansi.Color.WHITE, sfMem);
      Output.printColor(Ansi.Color.CYAN, "]-[");
      Output.printColor(Ansi.Color.WHITE, sfUndo);
      Output.printColor(Ansi.Color.CYAN, "]-[");
      Output.printColor(Ansi.Color.WHITE, calcStack.queryStackName() + ":" + StackManagement.QueryCurrentStackNum());
      Output.printColor(Ansi.Color.CYAN, "]-");
      Output.printColorln(Ansi.Color.CYAN, "+");
   }
}
