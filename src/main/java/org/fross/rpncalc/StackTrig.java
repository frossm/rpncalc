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

public class StackTrig {
   /**
    * cmdTrig(): Calculate the trig functions. There was so much overlap in the functions I consolidated the operations together
    *
    * @param calcStack Primary Stack
    * @param cmd       Command entered
    * @param arg       Parameters after the command
    */
   public static void cmdTrig(StackObj calcStack, String cmd, String arg) {
      // Ensure we have at least one item on the stack
      if (calcStack.isEmpty()) {
         Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
         return;
      }

      double angle = 0.0;

      try {
         angle = calcStack.pop().doubleValue();

         // Calculations are done in radians. Convert if 'rad' is NOT provided as a parameter
         if (arg.toLowerCase().charAt(0) != 'r') {
            Output.printColorln(Ansi.Color.RED, "ERROR: unknown " + cmd + " parameter: '" + arg + "'");
            calcStack.push(angle);
            return;
         }
      } catch (StringIndexOutOfBoundsException ex) {
         angle = java.lang.Math.toRadians(angle);
      }

      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      // Push the result back onto the stack
      switch (cmd) {
         case "tan":
            calcStack.push(java.lang.Math.tan(angle));
            break;

         case "sin":
            calcStack.push(java.lang.Math.sin(angle));
            break;

         case "cos":
            calcStack.push(java.lang.Math.cos(angle));
            break;

         default:
            Output.printColorln(Ansi.Color.RED, "ERROR: Could not understand trig command: '" + cmd + "'");
      }
   }

   /**
    * cmdArcTrig(): Calculate the arc Trig functions. There was so much overlap in the functions I consolidated
    *
    * @param calcStack Primary Stack
    * @param cmd       Command entered
    * @param arg       Parameters after the command
    */
   public static void cmdArcTrig(StackObj calcStack, String cmd, String arg) {
      // Ensure we have at least one item on the stack
      if (calcStack.isEmpty()) {
         Output.printColorln(Ansi.Color.RED, "ERROR: Must be at least one item on the stack");
         return;
      }

      double result;
      double originalValue;

      originalValue = calcStack.peek().doubleValue();

      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      // Calculate the arc trig function
      switch (cmd) {
         case "asin":
            result = java.lang.Math.asin(calcStack.pop().doubleValue());
            break;

         case "acos":
            result = java.lang.Math.acos(calcStack.pop().doubleValue());
            break;

         case "atan":
            result = java.lang.Math.atan(calcStack.pop().doubleValue());
            break;

         default:
            Output.printColorln(Ansi.Color.RED, "ERROR: Unknown command: '" + cmd + "'");
            calcStack.push(originalValue);
            return;
      }

      try {
         // Display value in degrees or if 'rad' is a parameter, as radians
         if (arg.toLowerCase().charAt(0) == 'r') {
            calcStack.push(result);
         } else {
            calcStack.push(originalValue);
            Output.printColorln(Ansi.Color.RED, "ERROR: unknown " + cmd + " parameter: '" + arg + "'");
         }

      } catch (StringIndexOutOfBoundsException ex) {
         calcStack.push(java.lang.Math.toDegrees(result));
      }
   }

   /**
    * cmdHypotenuse(): Calculates the hypotenuse by pulling the top two stack items and using them as the triangle legs
    *
    * @param calcStack Primary Stack
    */
   public static void cmdHypotenuse(StackObj calcStack) {
      // Ensure we have two items on the stack
      if (calcStack.size() < 2) {
         Output.printColorln(Ansi.Color.RED, "ERROR:  There must be two items on the stack to calculate the hypotenuse");
         return;
      }

      // Save current calcStack to the undoStack
      calcStack.saveUndo();        // Save current calcStack to the undoStack

      // Pop the two values and push the hypotenuse back onto the stack
      calcStack.push(java.lang.Math.hypot(calcStack.pop().doubleValue(), calcStack.pop().doubleValue()));
   }
}
