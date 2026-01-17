/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
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
package org.fross.rpncalc;

import org.fross.library.Output;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class StackConversions {
   // Class Constants
   public static final BigInteger DEFAULT_FRACTION_DENOMINATOR = new BigInteger("64");  // Default Smallest Fraction Denominator

   public static void cmdConvert(StackObj calcStack, String args) {
      String fromUnit = "";
      String toUnit = "";
      BigDecimal result;

      // Verify at least one item exists on the stack
      if (calcStack.isEmpty()) {
         Output.printColorln(Output.RED, "ERROR:  This operation requires at least one item on the stack");
         return;
      }

      // Determine the from and to values
      try {
         fromUnit = args.split(" ")[0];
         toUnit = args.split(" ")[1];
      } catch (Exception ex) {
         Output.printColorln(Output.RED, "ERROR:  Two units are required.  'convert FROM TO'\n");
         StackConversions.DisplayConversionUnits(Output.CYAN);
         return;
      }

      Output.debugPrintln("Converting " + calcStack.peek() + " from " + fromUnit + " to " + toUnit);

      // Save current calcStack to the undoStack
      calcStack.saveUndo();

      // Perform the conversion
      try {
         result = UnitConverter.convert(calcStack.peek(), fromUnit, toUnit).value();
         calcStack.pop();
         calcStack.push(result);
      } catch (Exception ex) {
         Output.printColorln(Output.RED, ex.getMessage());
      }

   }

   /**
    * DisplayConversionUnits():  Display the Convert command's supported units
    */
   public static void DisplayConversionUnits(int clr) {
      String units = """
            Length:
              mm      Millimeter
              cm      Centimeter
              m       Meter
              km      Kilometer
              in      Inch
              ft      Foot
              yd      Yard
              mi      Mile
              
            Mass:
              mg      Milligram
              g       Gram
              kg      Kilogram
              tonne   Metric Ton
              oz      Ounce
              lb      Pound
              ton     US Short Ton
              
            Temperature:
              c       Celsius
              f       Fahrenheit
              k       Kelvin
              
            Time:
              ms      Millisecond
              s       Second
              min     Minute
              hr      Hour
              day     Day
              week    Week
              
            Volume:
              ml      Milliliter
              l       Liter
              floz    Fluid Ounce
              cup     Cup
              pt      Pint
              qt      Quart
              gal     Gallon
              
            Angle:
              rad     Radian
              deg     Degree
              
            Percentage:
              decimal Decimal
              %       Percent
            """;
      Output.printColorln(clr, units);
   }

   /**
    * cmdFraction(): Display the last stack item as a fraction with a minimum base of the provided number. For example, sending
    * 64 would produce a fraction of 1/64th but will be reduced if possible.
    *
    * @param calcStack Primary stack
    * @param param     Fraction base
    * @return Conversion output
    */
   public static String[] cmdFraction(StackObj calcStack, String param) {
      String[] outputString = {"", "", "", ""};
      boolean negativeNumber = false;

      // Verify we have an item on the stack
      if (calcStack.isEmpty()) {
         Output.printColorln(Output.RED, "ERROR:  There must be at least one item on the stack");
         return outputString;
      }

      // Set the last stack item as the startingNumber
      BigDecimal startingNumber = calcStack.peek();

      // If starting number is negative, set a variable then remove the negative sign
      if (startingNumber.signum() < 0) {
         negativeNumber = true;
         startingNumber = startingNumber.abs();
      }

      // The base to convert the fraction to. For example, 64 = 1/64th
      BigInteger denominator = DEFAULT_FRACTION_DENOMINATOR;

      // If a denominator is provided, use it instead of the default
      try {
         if (!param.isEmpty()) denominator = new BigInteger(param);
      } catch (NumberFormatException ex) {
         Output.printColorln(Output.RED, "ERROR: '" + param + "' is not a valid denominator");
         return outputString;
      }

      // Round the decimal number to the right denominator
      // If My_Dim != ((Floor((My_Dim * 16) + 0.5)) / 16) (For 1/16 as the denominator)
      // Reference: https://community.ptc.com/t5/3D-Part-Assembly-Design/Rounding-decimals-to-the-nearest-fraction/td-p/657420
      BigDecimal roundedNumber = BigDecimal.ZERO;
      try {
         roundedNumber = startingNumber.multiply(new BigDecimal(denominator)).add(new BigDecimal("0.5")).setScale(0, RoundingMode.FLOOR);
         roundedNumber = roundedNumber.divide(new BigDecimal(denominator), MathContext.DECIMAL128);
      } catch (ArithmeticException ex) {
         Output.printColorln(Output.RED, "Error calculating the rounded fraction\n" + ex.getMessage());
      }

      // Determine the integer portion of the number
      BigInteger integerPart = roundedNumber.toBigInteger();

      // Need to make the decimal a fraction my multiplying by the 10ths.
      // Determine the number of decimals places and tale 10^scale
      BigDecimal scaleMultiplier = BigDecimal.TEN.pow(roundedNumber.scale());

      // Numerator = decimal portion * scale, so we have an integer
      // Decimal = the scale
      // Example: 0.25 becomes 25/100 | 0.123 becomes 123/1000
      BigDecimal numeratorTemp = roundedNumber.subtract(new BigDecimal(integerPart));
      numeratorTemp = numeratorTemp.multiply(scaleMultiplier);
      BigInteger numerator = numeratorTemp.toBigInteger();
      denominator = scaleMultiplier.toBigInteger();

      // Get the Greatest Common Divisor, so we can simply the fraction
      BigInteger gcd = Math.GreatestCommonDivisor(numerator, denominator);
      Output.debugPrintln("Greatest Common Divisor for " + numerator + " and " + denominator + " is " + gcd);

      // Simply the fraction
      numerator = numerator.divide(gcd);
      denominator = denominator.divide(gcd);

      // If starting number was negative, set it as negative again
      if (negativeNumber) {
         integerPart = integerPart.multiply(new BigInteger("-1"));
      }

      // Output the fractional display
      // If there is no fractional result, remove it, so we don't see '0/1'
      String stackHeader = "-Fraction (Granularity: 1/" + denominator + ")";
      String result = integerPart + " " + ((numerator.compareTo(BigInteger.ZERO) == 0) ? "" : numerator + "/" + denominator);

      // Header Top
      outputString[0] = "\n" + stackHeader + "-".repeat(Main.configProgramWidth - stackHeader.length());
      // Results
      outputString[1] = " " + calcStack.peek().setScale(5, RoundingMode.HALF_UP) + " is approximately '" + result.trim() + "'";
      // Header Bottom
      outputString[2] = "-".repeat(Main.configProgramWidth) + "\n";
      // Results for testing
      outputString[3] = result.trim();

      return outputString;
   }

}