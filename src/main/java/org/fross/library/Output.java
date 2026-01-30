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

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp.Capability;

public class Output {
   // Shorthand color constants
   public static final int BLACK = AttributedStyle.BLACK;     // 0
   public static final int RED = AttributedStyle.RED;         // 1
   public static final int GREEN = AttributedStyle.GREEN;     // 2
   public static final int YELLOW = AttributedStyle.YELLOW;   // 3
   public static final int BLUE = AttributedStyle.BLUE;       // 4
   public static final int MAGENTA = AttributedStyle.MAGENTA; // 5
   public static final int CYAN = AttributedStyle.CYAN;       // 6
   public static final int WHITE = AttributedStyle.WHITE;     // 7

   static boolean colorizedOutput = true;      // By default, color is enabled
   private static Terminal terminal;

   public static void setTerminal(Terminal t) {
      terminal = t;
   }

   /**
    * enableColor(): Enable or disable colorized output
    *
    * @param value TRUE or FALSE to enable colorized output
    */
   public static void enableColor(boolean value) {
      colorizedOutput = value;
   }

   /**
    * queryColorEnabled(): Return true if colorized output is configured. False if not.
    *
    * @return Return TRUE or FALSE if colorized output is enabled
    */
   public static boolean queryColorEnabled() {
      return colorizedOutput;
   }


   /**
    * returnColorString(): Return the colorized string
    * <p>
    *
    * @param fgColor Foreground Color
    * @param bgColor Background Color
    * @param msg     Message to display
    */
   public static String returnColorString(int fgColor, int bgColor, String msg) {
      if (colorizedOutput) {
         // Initialize style with Foreground and Bold
         AttributedStyle style = AttributedStyle.DEFAULT.foreground(fgColor).bold();

         // Apply background if it's not -1 (transparent/default)
         if (bgColor != -1) {
            style = style.background(bgColor);
         }

         // Build the string
         String styledMsg = new AttributedStringBuilder().style(style).append(msg).toAnsi();

         // Return the result
         return styledMsg;

      } else {
         return msg;
      }
   }

   /**
    * printColor(): Print to the console with the provided foreground & background color
    * <p>
    *
    * @param fgColor Foreground Color
    * @param bgColor Background Color
    * @param msg     Message to display
    */
   public static void printColor(int fgColor, int bgColor, String msg) {
      if (terminal != null && colorizedOutput) {
         String styledMsg = returnColorString(fgColor, bgColor, msg);
         terminal.writer().print(styledMsg);
         terminal.flush();

      } else {
         print(msg);
      }
   }

   /**
    * printColor(): Overloaded. Just provide a foreground color
    *
    * @param fgColor Foreground Color
    * @param msg     Message to display
    */
   public static void printColor(int fgColor, String msg) {
      printColor(fgColor, -1, msg);
   }

   /**
    * printColorln(): Print to the console with the provided foreground color
    * <p>
    *
    * @param fgColor Foreground Color
    * @param bgColor Background Color
    * @param msg     Message to display
    */
   public static void printColorln(int fgColor, int bgColor, String msg) {
      printColor(fgColor, bgColor, msg + "\n");
   }

   /**
    * printColorln(): Overloaded. Added background parameter
    *
    * @param fgColor Foreground Color
    * @param msg     Message to display
    */
   public static void printColorln(int fgColor, String msg) {
      printColor(fgColor, -1, msg + "\n");
   }

   /**
    * println(): Basic System.out.println call. It's here so all text output can go through this function.
    *
    * @param msg Message to display
    */
   public static void println(String msg) {
      if (terminal != null) {
         terminal.writer().println(msg);
         terminal.flush();
      } else {
         System.out.println(msg);
      }
   }

   /**
    * print(): Basic System.out.print call. It's here so out text output can go through this function.
    *
    * @param msg Message to display
    */
   public static void print(String msg) {
      if (terminal != null) {
         terminal.writer().print(msg);
         terminal.flush();
      } else {
         System.out.print(msg);
      }
   }

   /**
    * fatalError(): Print the provided string in RED and exit the program with the error code given
    *
    * @param msg       Message to display
    * @param errorCode Error code to return
    */
   public static void fatalError(String msg, int errorCode) {
      Output.printColorln(RED, "\nFATAL ERROR: " + msg);
      System.exit(errorCode);
   }

   /**
    * debugPrintln(): Print the provided text in RED with the preface of DEBUG: with a newline
    *
    * @param msg Message to display
    */
   public static void debugPrintln(String msg) {
      debugPrint(msg + "\n");
   }

   /**
    * debugPrint(): Print the provided text in RED with the preface of DEBUG: and no new line at the end
    *
    * @param msg Message to display
    */
   public static void debugPrint(String msg) {
      if (Debug.query()) {
         Output.printColor(RED, "DEBUG:  " + msg);
      }
   }

   /**
    * clearScreen(): Clears the screen
    */
   public static void clearScreen() {
      if (terminal == null) return;

      // This looks up the OS-specific "clear" command (like 'cls' or 'clear')
      terminal.puts(Capability.clear_screen);
      terminal.puts(Capability.cursor_home);

      // I had issues with clearing the screen in Linux. This is a failsafe.
      // \033[2J = Clear entire screen
      // \033[H  = Move cursor to home (0,0)
      terminal.writer().print("\033[H\033[2J");

      // Always flush to ensure the command is sent to the screen immediately
      terminal.flush();
   }

   /**
    * Ansi256Test(): Simple printout of colors to test jAnsi 256 on terminals
    *
    */
   public static void Ansi256Test() {
      // Test Foregrounds
      for (int index = 0; index < 256; index++) {
         Output.printColor(index, String.format("%d", index));
      }

      System.out.println("\n");

      // Test Backgrounds
      for (int index = 0; index < 256; index++) {
         Output.printColor(-1, index, String.format("%d", index));
      }
   }

}