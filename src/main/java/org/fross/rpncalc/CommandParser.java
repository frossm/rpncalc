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
import java.math.MathContext;

public class CommandParser {

   /**
    * Parse(): Take the user input and send it to the executing function
    *
    * @param calcStack     Primary stack
    * @param calcStack2    Secondary stack
    * @param cmdInput      Full command input
    * @param cmdInputCmd   Entered command
    * @param cmdInputParam Entered command parameter
    */
   public static void Parse(StackObj calcStack, StackObj calcStack2, String cmdInput, String cmdInputCmd, String cmdInputParam) {
      // Remove any commas in the command to allow for "1,234.00"
      cmdInputCmd = cmdInputCmd.replaceAll(",", "");

      // Massive switch statement to process user input and call the correct functions
      switch (cmdInputCmd) {

         /*------------------------------------------------------------------------------
          * Calculator Commands
          *-----------------------------------------------------------------------------*/
         // Undo
         case "undo":
         case "u":
            StackCommands.cmdUndo(calcStack, cmdInputParam);
            break;

         // Flip Sign
         case "flip":
         case "f":
            StackCommands.cmdFlipSign(calcStack);
            break;

         // Clear Screen and Stack
         case "clear":
         case "c":
            StackCommands.cmdClear(calcStack);
            break;

         // Clean the screen and redisplay the stack
         case "clean":
         case "cl":
            StackCommands.cmdClean();
            break;

         // Delete
         case "delete":
         case "del":
         case "d":
            StackCommands.cmdDelete(calcStack, cmdInputParam);
            break;

         // Down: Shift the stack down so Line2 becomes Line1 and the original Line1 goes to the bottom
         case "down":
            StackCommands.cmdDown(calcStack);
            break;

         // Up: Shift the stack up so Line1 becomes Line2 and the last stack items moves into Line1
         case "up":
            StackCommands.cmdUp(calcStack);
            break;

         // Factorial
         case "factorial":
         case "fact":
            StackCommands.cmdFactorial(calcStack);
            break;

         // Swap Elements in a stack
         case "swap":
         case "s":
            StackCommands.cmdSwapElements(calcStack, cmdInputParam);
            break;

         // Square Root
         case "sqrt":
            StackCommands.cmdSqrt(calcStack);
            break;

         // Round
         case "round":
            StackCommands.cmdRound(calcStack, cmdInputParam);
            break;

         // AddAll
         case "addall":
         case "aa":
            StackCommands.cmdAddAll(calcStack, cmdInputParam);
            break;

         // Modulus
         case "modulus":
         case "mod":
            StackCommands.cmdModulus(calcStack);
            break;

         // Average
         case "mean":
         case "average":
         case "avg":
            StackCommands.cmdAverage(calcStack, cmdInputParam);
            break;

         // Sort
         case "sort":
            StackCommands.cmdSort(calcStack, cmdInputParam);
            break;

         // Median
         case "median":
            StackCommands.cmdMedian(calcStack, cmdInputParam);
            break;

         // Standard Deviation
         case "sd":
            StackCommands.cmdStdDeviation(calcStack, cmdInputParam);
            break;

         // Copy Item
         case "copy":
         case "dup":
            StackCommands.cmdCopy(calcStack, cmdInputParam);
            break;

         // Natural (base e) Logarithm
         case "log":
            StackCommands.cmdLog(calcStack);
            break;

         // Base10 Logarithm
         case "log10":
            StackCommands.cmdLog10(calcStack);
            break;

         // Integer
         case "int":
            StackCommands.cmdInteger(calcStack);
            break;

         // Linear Regression
         case "lr":
            StackCommands.cmdLinearRegression(calcStack, cmdInputParam);
            break;

         // Absolute Value
         case "abs":
            StackCommands.cmdAbsoluteValue(calcStack);
            break;

         // Minimum Value
         case "min":
            StackCommands.cmdMinimum(calcStack);
            break;

         // Maximum Value
         case "max":
            StackCommands.cmdMaximum(calcStack);
            break;

         // Random Number Generation
         case "rand":
         case "random":
            StackCommands.cmdRandom(calcStack, cmdInputParam);
            break;

         // Dice
         case "dice":
         case "roll":
            StackCommands.cmdDice(calcStack, cmdInputParam);
            break;

         // Echo
         case "echo":
            StackCommands.cmdEcho(calcStack, cmdInputParam);
            break;

         // Repeat
         case "repeat":
         case "rep":
            StackCommands.cmdRepeat(calcStack, cmdInputParam);
            break;

         /*------------------------------------------------------------------------------
          * Conversions
          *-----------------------------------------------------------------------------*/
         // Percent
         case "to%":
            StackConversions.cmdToPercent(calcStack);
            break;
         case "from%":
            StackConversions.cmdFromPercent(calcStack);
            break;

         // Fraction
         case "frac":
         case "fraction":
            String[] outString = StackConversions.cmdFraction(calcStack, cmdInputParam);
            // If there wasn't an error (which would return an empty string), display the results
            if (!outString[0].isEmpty()) {
               Output.printColorln(Ansi.Color.YELLOW, outString[0]);
               Output.printColorln(Ansi.Color.WHITE, outString[1]);
               Output.printColorln(Ansi.Color.YELLOW, outString[2]);
            }
            break;

         // Convert inches to millimeters
         case "in2mm":
            StackConversions.cmdIn2Mm(calcStack);
            break;

         // Convert millimeters to inches
         case "mm2in":
            StackConversions.cmdMm2In(calcStack);
            break;

         // Convert inches to feet
         case "in2ft":
            StackConversions.cmdIn2Ft(calcStack);
            break;

         // Convert feet to inches
         case "ft2in":
            StackConversions.cmdFt2In(calcStack);
            break;

         // Convert to Radians
         case "deg2rad":
            StackConversions.cmdDeg2Rad(calcStack);
            break;

         // Convert to Degrees
         case "rad2deg":
            StackConversions.cmdRad2Deg(calcStack);
            break;

         // Convert grams to ounces
         case "gram2oz":
            StackConversions.cmdGram2Oz(calcStack);
            break;

         // Convert ounces to grams
         case "oz2gram":
            StackConversions.cmdOz2Gram(calcStack);
            break;

         // Convert Kilograms to US Pounds
         case "kg2lb":
            StackConversions.cmdKg2Lb(calcStack);
            break;

         // Convert US Pounds to Kilograms
         case "lb2kg":
            StackConversions.cmdLb2Kg(calcStack);
            break;

         /*------------------------------------------------------------------------------
          * Stack Trigonometry Functions
          *-----------------------------------------------------------------------------*/
         // Trigonometry Functions
         case "tan":
         case "sin":
         case "cos":
            StackTrig.cmdTrig(calcStack, cmdInputCmd, cmdInputParam);
            break;

         // Arc-Trigonometry Functions
         case "atan":
         case "asin":
         case "acos":
            StackTrig.cmdArcTrig(calcStack, cmdInputCmd, cmdInputParam);
            break;

         // Hypotenuse
         case "hypot":
         case "hypotenuse":
            StackTrig.cmdHypotenuse(calcStack);
            break;

         /*------------------------------------------------------------------------------
          * Stack Memory Functions
          *-----------------------------------------------------------------------------*/
         case "memory":
         case "mem":
            // I often mistype 'mem list' instead of 'list mem' I'm going to allow that to work
            if (cmdInput.toLowerCase().startsWith("mem list")) {
               Output.printColorln(Ansi.Color.CYAN, "Rewriting command to 'list mem'");
               CommandParser.Parse(calcStack, calcStack2, "list mem", "list", "mem");
            } else {
               StackMemory.cmdMem(calcStack, cmdInputParam);
            }
            break;

         /*------------------------------------------------------------------------------
          * Constants
          *-----------------------------------------------------------------------------*/
         // Add PI
         case "pi":
            StackConstants.cmdPI(calcStack);
            break;

         // Add PHI also known as The Golden Ratio
         case "phi":
            StackConstants.cmdPHI(calcStack);
            break;

         // Euler's number
         case "eulersnum":
         case "eulersnumber":
            StackConstants.cmdEulersNumber(calcStack);
            break;

         // Euler's constant
         case "eulersconst":
         case "eulersconstant":
            StackConstants.cmdEulersConstant(calcStack);
            break;

         // Speed of light
         case "sol":
         case "speedoflight":
            StackConstants.cmdSpeedOfLight(calcStack);
            break;

         /*------------------------------------------------------------------------------
          * User Defined Functions
          *-----------------------------------------------------------------------------*/
         // Turn recording on or off
         case "rec":
         case "record":
            UserFunctions.cmdRecord(cmdInputParam);
            break;

         // Allows for running or deleting functions
         case "func":
         case "function":
            UserFunctions.cmdFunction(cmdInputParam);
            break;

         /*------------------------------------------------------------------------------
          * Stack Operational Commands
          *-----------------------------------------------------------------------------*/
         // List
         case "list":
            StackOperations.cmdList(calcStack, cmdInputParam);
            break;

         // Load new or existing stack
         case "load":
            StackOperations.cmdLoad(calcStack, calcStack2, cmdInputParam);
            break;

         // Export the current stack to a provided file
         case "export":
            StackOperations.exportStackToDisk(calcStack, cmdInputParam);
            break;

         // Open browser to homepage
         case "hp":
         case "homepage":
            StackOperations.cmdHomePage();
            break;

         // Open browser to User Guide
         case "ug":
         case "userguide":
            StackOperations.cmdUserGuide();
            break;

         // Import stack from disk
         case "import":
            StackOperations.importStackFromDisk(calcStack, cmdInputParam);
            break;

         // Swap Stack
         case "ss":
            StackOperations.cmdSwapStack(calcStack, calcStack2);
            break;

         // Debug Toggle
         case "debug":
            StackOperations.cmdDebug();
            break;

         // Set configuration options
         case "set":
            Configuration.cmdSet(cmdInputParam);
            break;

         // Reset configuration to defaults
         case "reset":
            Configuration.cmdReset();
            break;

         // Reverse Stack
         case "rev":
         case "reverse":
            StackOperations.cmdReverse(calcStack);
            break;

         // Show License
         case "license":
            Help.DisplayLicense();
            break;

         // Version
         case "ver":
         case "version":
            Help.DisplayVersion();
            break;

         // Help
         case "h":
         case "?":
         case "help":
            Help.Display();
            break;

         // Exit
         case "cx":
         case "clearexit":
            calcStack.clear();
         case "x":
         case "exit":
         case "quit":
            Output.debugPrintln("Exiting Command Loop");
            Main.ProcessCommandLoop = false;
            break;

         /*------------------------------------------------------------------------------
          * Operands
          *-----------------------------------------------------------------------------*/
         case "+":
         case "-":
         case "*":
         case "/":
         case "^":
            StackCommands.cmdOperand(calcStack, cmdInputCmd);
            break;

         default:
            // Determine if the command is a user defined function and execute if it is
            if (UserFunctions.FunctionExists(cmdInput)) {
               Output.debugPrintln("Executing User Defined Function: '" + cmdInput + "'");
               UserFunctions.FunctionRun(calcStack, calcStack2, cmdInput);

               // Check for a fraction. If number entered contains a '/' but it's not at the end, then it must be a fraction
            } else if (cmdInput.contains("/") && !cmdInput.substring(cmdInput.length() - 1).matches("/")) {
               Output.debugPrintln("Fraction has been entered");
               try {
                  BigDecimal fracInteger = BigDecimal.ZERO;
                  BigDecimal fracDecimalEquiv;

                  // If there wasn't an integer entered, move the fraction to the parameter variable
                  if (cmdInputCmd.contains("/")) {
                     cmdInputParam = cmdInputCmd;
                  } else {
                     fracInteger = new BigDecimal(cmdInputCmd);
                  }

                  BigDecimal fracTop = new BigDecimal(cmdInputParam.substring(0, cmdInputParam.indexOf('/')));
                  BigDecimal fracBottom = new BigDecimal(cmdInputParam.substring(cmdInputParam.indexOf('/') + 1));

                  Output.debugPrintln("Fraction Top:\t" + fracTop);
                  Output.debugPrintln("Fraction Bot:\t" + fracBottom);

                  // Divide the fraction and get a decimal equivalent
                  fracDecimalEquiv = fracTop.divide(fracBottom, MathContext.DECIMAL128);

                  // Overall decimal equivalent (integer + decimal)
                  // If integer is negative, make the decimal negative, so we can add them
                  if (fracInteger.signum() < 0) {
                     fracDecimalEquiv = fracDecimalEquiv.multiply(new BigDecimal("-1"));
                  }
                  BigDecimal endResult = fracInteger.add(fracDecimalEquiv);

                  // Simply convert the fraction to a decimal and add it to the stack
                  Output.debugPrintln("Fraction Entered: '" + cmdInput + "' Decimal: " + endResult);

                  // Save current calcStack to the undoStack
                  calcStack.saveUndo();

                  // Add the decimal number to the stack and continue with next command
                  calcStack.push(endResult);

               } catch (NumberFormatException ex) {
                  Output.printColorln(Ansi.Color.RED, "Illegal Fraction Entered: '" + cmdInput + "'");
                  break;
               }

               // Number entered, add to stack
            } else if (cmdInputCmd.matches("^-?\\d*\\.?\\d*")) {
               // Save current calcStack to the undoStack
               calcStack.saveUndo();

               Output.debugPrintln("Placing the number '" + cmdInputCmd + "' onto the stack");
               calcStack.push(new BigDecimal(cmdInputCmd));

               // If the number entered ends with a "%" then multiply by 0.01 and add that result to the stack
            } else if (cmdInputCmd.matches("^\\S*\\d%$")) {
               // Save current calcStack to the undoStack
               calcStack.saveUndo();

               String num = "";
               try {
                  num = cmdInputCmd.substring(0, cmdInputCmd.indexOf('%'));
                  calcStack.push(new BigDecimal(num));
                  calcStack.push("0.01");
                  Math.Multiply(calcStack);

                  Output.debugPrintln("Percent entered:  " + num + "% * 0.01 = " + calcStack.peek());

               } catch (IndexOutOfBoundsException ex) {
                  Output.printColorln(Ansi.Color.RED, "Unable to parse '" + cmdInputCmd + "'");
               } catch (ArithmeticException | NullPointerException ex) {
                  Output.printColorln(Ansi.Color.RED, "Error multiplying " + num + " by 0.01");
               }

               // Handle NumOps - numbers with a single operand at the end (*, /, +, -, ^)
            } else if (cmdInputCmd.matches("^-?\\d*\\.?\\d*[Ee]?\\d*[*+\\-/^]")) {
               // Save current calcStack to the undoStack
               calcStack.saveUndo();

               Output.debugPrintln("CalcStack has " + calcStack.size() + " elements");

               // Verify stack contains at least one element
               if (!calcStack.isEmpty()) {
                  try {
                     String tempOp = cmdInputCmd.substring(cmdInputCmd.length() - 1);
                     String tempNum = cmdInput.substring(0, cmdInput.length() - 1);
                     Output.debugPrintln("NumOp Found: Num= '" + tempNum + "'");
                     Output.debugPrintln("NumOp Found: Op = '" + tempOp + "'");
                     calcStack.push(new BigDecimal(tempNum));
                     Math.Parse(tempOp, calcStack);

                  } catch (NumberFormatException ex) {
                     // Prevents a crash if user enters "-+" (which they shouldn't do)
                     Output.printColorln(Ansi.Color.RED, "Unknown Command: '" + cmdInput + "'");
                     break;
                  }

               } else {
                  Output.printColorln(Ansi.Color.RED, "One number is required to be on the stack to use a NumOp");
               }

               // Scientific notation number entered
            } else if (cmdInputCmd.toLowerCase().contains("e")) {
               Output.debugPrintln("Scientific Number Entered");
               // Make sure the digits before and after the 'e' are numbers
               try {
                  String[] numberSplit = cmdInputCmd.toLowerCase().trim().replace(" ", "").split("e");
                  if (Math.isNumeric(numberSplit[0]) && Math.isNumeric(numberSplit[1])) {
                     // Ensure there is no decimal in the exponent portion
                     if (numberSplit[1].contains(".")) {
                        throw new IllegalArgumentException();
                     }

                     // Save current calcStack to the undoStack
                     calcStack.saveUndo();

                     Output.debugPrintln("Adding the scientific notation number '" + cmdInputCmd + "' onto the stack");
                     calcStack.push(new BigDecimal(cmdInputCmd.toLowerCase()));

                  } else {
                     throw new IllegalArgumentException();
                  }

               } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
                  Output.printColorln(Ansi.Color.RED, "Illegal Scientific Notation Number Entered: '" + cmdInputCmd + "'");
                  break;
               }

            } else {
               // If a user enters an invalid command, remove it from the recording if enabled
               if (UserFunctions.recordingEnabled) {
                  Output.debugPrintln("Removing '" + UserFunctions.recording.get(UserFunctions.recording.size() - 1) + "' from the recording");
                  UserFunctions.RemoveItemFromRecording();
               }

               // Let user know a bad command was provided
               Output.printColorln(Ansi.Color.RED, "Unknown Command: '" + cmdInput + "'");

               // Remove this invalid command from the command history
               if (CommandHistory.size() > 1) CommandHistory.remove();
            }
            break;

      } // End of giant switch statement

   }

}