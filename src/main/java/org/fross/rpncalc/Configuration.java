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
import org.fusesource.jansi.Ansi;

import java.util.prefs.Preferences;

public class Configuration {
   /**
    * set(): Set configuration Options
    */
   public static void cmdSet(String arg) {
      Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");
      String[] argParse;
      String command;
      String value;

      // If there is not a parameter provided, display the current values and return
      if (arg.isBlank()) {
         Output.printColorln(Ansi.Color.YELLOW, "\n-Configuration Values" + "-".repeat(Main.configProgramWidth - 21));
         Output.printColorln(Ansi.Color.CYAN, String.format("Width:     %02d\t|  Sets the program width in characters", Main.configProgramWidth));
         Output.printColorln(Ansi.Color.CYAN, "Align:      " + Main.configAlignment + "\t|  Set display alignment. Values: (l)eft, (d)ecimal, (r)ight");
         Output.printColorln(Ansi.Color.CYAN, String.format("MemSlots:  %02d\t|  Sets number of available memory slots", Main.configMemorySlots));
         Output.printColorln(Ansi.Color.YELLOW, "-".repeat(Main.configProgramWidth) + "\n");
         return;
      }

      // Parse the provided argument into a command and value
      try {
         argParse = arg.split(" ");
         command = argParse[0];
         value = argParse[1].toLowerCase();

         Output.debugPrintln("Set Command: '" + command + "'");
         Output.debugPrintln("Set Value:   '" + value + "'");

         switch (command.toLowerCase()) {
            case "align":
            case "alignment":
               if (value.compareTo("l") != 0 && value.compareTo("d") != 0 && value.compareTo("r") != 0) {
                  Output.printColorln(Ansi.Color.RED, "Alignment can only be 'l'eft, 'd'ecimal, or 'r'ight. See help for usage");
                  return;
               }
               Main.configAlignment = value;
               Output.debugPrintln("Saving Alignment value to preferences");
               prefConfig.put("alignment", value);
               Output.printColorln(Ansi.Color.CYAN, "Alignment set to '" + value + "'");
               break;

            case "width":
               if (Integer.parseInt(value) < Main.PROGRAM_MINIMUM_WIDTH) {
                  Output.printColorln(Ansi.Color.RED, "Error.  Minimum width is " + Main.PROGRAM_MINIMUM_WIDTH + ". Setting width to that value.");
                  value = "" + Main.PROGRAM_MINIMUM_WIDTH;
               }
               Main.configProgramWidth = Integer.parseInt(value);
               Output.debugPrintln("Saving Program Width value to preferences");
               prefConfig.putInt("programwidth", Integer.parseInt(value));
               Output.printColorln(Ansi.Color.CYAN, "Program Width set to '" + value + "'");
               break;

            case "mem":
            case "memslots":
            case "memoryslots":
               if (StackMemory.SetMaxMemorySlots(value)) {
                  Output.printColorln(Ansi.Color.CYAN, "Memory Slots set to '" + value + "'");
               }
               break;

            default:
               Output.printColorln(Ansi.Color.RED, "ERROR: Unknown set command: '" + command + "'");
         }

      } catch (Exception ex) {
         Output.printColorln(Ansi.Color.RED, "Error parsing set command: 'set " + arg + "'  See help for set command usage");
      }
   }

   /**
    * reset(): Resets the configuration variables back to default
    */
   public static void cmdReset() {
      Output.printColorln(Ansi.Color.CYAN, "Alignment, Width, and Memory slots reset to default values");
      Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");

      // Reset Alignment
      prefConfig.put("alignment", Main.CONFIG_DEFAULT_ALIGNMENT);
      Main.configAlignment = Main.CONFIG_DEFAULT_ALIGNMENT;

      // Reset Width
      prefConfig.putInt("programwidth", Main.CONFIG_DEFAULT_PROGRAM_WIDTH);
      Main.configProgramWidth = Main.CONFIG_DEFAULT_PROGRAM_WIDTH;

      // Reset the number of Memory Slots
      StackMemory.SetMaxMemorySlots(Main.CONFIG_DEFAULT_MEMORY_SLOTS + "");
      prefConfig.putInt("memoryslots", Main.CONFIG_DEFAULT_MEMORY_SLOTS);
      Main.configMemorySlots = Main.CONFIG_DEFAULT_MEMORY_SLOTS;
   }

}
