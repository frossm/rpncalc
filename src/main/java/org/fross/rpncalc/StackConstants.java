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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.fross.rpncalc.StackConversions.cmdFraction;

public class StackConstants {
   /**
    * cmdEulersConstant(): Add the Euler constant to the stack
    */
   public static void cmdEulersConstant(StackObj calcStack) {
      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      calcStack.push("0.577215664901532860606512090082402431");

      Output.printColorln(Output.CYAN, "Euler's constant (y) has been added to the stack");
   }

   /**
    * cmdEulersNumber(): Add the Euler number to the stack
    */
   public static void cmdEulersNumber(StackObj calcStack) {
      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      calcStack.push("2.71828182845904523536028747135266249775724709369995");

      Output.printColorln(Output.CYAN, "Euler's number (e) has been added to the stack");
   }

   /**
    * cmdPI(): Add the value of PI to the stack
    */
   public static void cmdPI(StackObj calcStack) {
      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      calcStack.push("3.14159265358979323846264338327950288419716939937510");

      Output.printColorln(Output.CYAN, "The value PI added to the stack");
   }

   /**
    * cmdPHI(): Add the value PHI (Golden Ratio) to the stack
    */
   public static void cmdPHI(StackObj calcStack) {
      BigDecimal phi = new BigDecimal("1.61803398874989");
      BigDecimal phiInverse = BigDecimal.ONE.divide(phi, MathContext.DECIMAL128);

      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      // If there is something in the stack, display the long and short sections
      if (!calcStack.isEmpty()) {
         BigDecimal value = calcStack.peek();
         Output.printColorln(Output.YELLOW, "If Long Section  = " + value + "    Short Section = " + value.multiply(phiInverse).setScale(5, RoundingMode.HALF_UP));
         Output.printColorln(Output.YELLOW, "If Short Section = " + value + "    Long Section  = " + value.multiply(phi).setScale(5, RoundingMode.HALF_UP));
      }

      // Add the value of Phi to the top of the stack
      calcStack.push(phi);

      Output.printColorln(Output.CYAN, "Phi, the golden ratio, has been added to the stack");
   }

   /**
    * cmdPHIBox(): Given the length, display a width and height for a box. Display and add width/height to stack
    */
   public static void cmdPHIBox(StackObj calcStack) {
      BigDecimal phi = new BigDecimal("1.61803398874989");
      BigDecimal phiInverse = BigDecimal.ONE.divide(phi, MathContext.DECIMAL128);
      String base = "16";

      // Verify there is at least one number on the stack
      if (!calcStack.isEmpty()) {
         // Calculate the box side dimensions
         BigDecimal length = calcStack.peek();
         BigDecimal width = length.multiply(phiInverse).setScale(5, RoundingMode.HALF_UP);
         BigDecimal height = width.multiply(phiInverse).setScale(5, RoundingMode.HALF_UP);

         // Calculate the fractional equivalent
         String lengthFraction = cmdFraction(length, base);
         String widthFraction = cmdFraction(width, base);
         String heightFraction = cmdFraction(height, base);

         // Determine the length of the longest strings to use in the fractional output display
         int maxNumberLen = Math.Max(length.toPlainString().length(), width.toPlainString().length(), height.toPlainString().length());
         int maxFractionLen = Math.Max(lengthFraction.length(), widthFraction.length(), heightFraction.length());

         // Output a formated message
         String rowFormat = "%-16s %" + maxNumberLen + "s [%" + maxFractionLen + "s]";
         Output.printColorln(Output.CYAN, String.format(rowFormat, "Box Length [X]:", length.toPlainString(), lengthFraction));
         Output.printColorln(Output.CYAN, String.format(rowFormat, "Box Width  [Y]:", width.toPlainString(), widthFraction));
         Output.printColorln(Output.CYAN, String.format(rowFormat, "Box Height [Z]:", height.toPlainString(), heightFraction));

         // Save current calcStack to the undoStack
         calcStack.saveUndo();

         // Add the width and height below the user provided length
         calcStack.push(width);
         calcStack.push(height);

      } else {
         Output.printColorln(Output.RED, "ERROR:  There must be at least one number on the stack");
      }
   }

   /**
    * cmdSpeedOfLight(): Add the speed of light in meters/second to the stack
    */
   public static void cmdSpeedOfLight(StackObj calcStack) {
      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      calcStack.push("299792458");

      Output.printColorln(Output.CYAN, "Speed of Light (c) added to the stack");
   }
}
