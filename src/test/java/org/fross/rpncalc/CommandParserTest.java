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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Fross (michael@fross.org)
 */
class CommandParserTest {

   /**
    * Parser is just a big switch statement and takes commands entered and runs the specific method. The key part to test is the
    * 'default:' portion where numbers, fractions, and NumOps are entered
    **/

   // Test standard number inputs
   @Test
   void testNormalNumberEntry() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      // Fraction Input
      stk1.clear();
      CommandParser.Parse(stk1, stk2, "123", "123", "");
      assertEquals(123, stk1.peek().doubleValue());
      assertEquals(1, stk1.size());

      CommandParser.Parse(stk1, stk2, "-0.0000123", "-0.0000123", "");
      assertEquals("-0.0000123", stk1.peek().toPlainString());
      assertEquals(2, stk1.size());

      CommandParser.Parse(stk1, stk2, "123.321", "123.321", "");
      assertEquals(123.321, stk1.peek().doubleValue());
      assertEquals(3, stk1.size());

      CommandParser.Parse(stk1, stk2, ".234", ".234", "");
      assertEquals(.234, stk1.peek().doubleValue());
      assertEquals(4, stk1.size());

      CommandParser.Parse(stk1, stk2, "123.321e12", "123.321e12", "");
      assertEquals("123.321E+12", stk1.peek().toEngineeringString());
      assertEquals(5, stk1.size());

      CommandParser.Parse(stk1, stk2, "-.1E44", "-.1E44", "");
      assertEquals("-10E+42", stk1.peek().toEngineeringString());
      assertEquals(6, stk1.size());

      CommandParser.Parse(stk1, stk2, "123,456,321.321", "123,456,321.321", "");
      assertEquals("123456321.321", stk1.peek().toString());
      assertEquals(7, stk1.size());
   }

   // Test fractional inputs
   @Test
   void testFractionInput() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      // Fraction Input
      stk1.clear();
      CommandParser.Parse(stk1, stk2, "1/8", "1/8", "");
      assertEquals(.125, stk1.peek().doubleValue());
      assertEquals(1, stk1.size());

      CommandParser.Parse(stk1, stk2, "47/88", "47/88", "");
      StackCommands.cmdRound(stk1, "4");
      assertEquals(0.5341, stk1.peek().doubleValue());
      assertEquals(2, stk1.size());

      CommandParser.Parse(stk1, stk2, "1 3/16", "1", "3/16");
      assertEquals(1.1875, stk1.peek().doubleValue());
      assertEquals(3, stk1.size());

      CommandParser.Parse(stk1, stk2, "-4 1/64", "-4", "1/64");
      StackCommands.cmdRound(stk1, "4");
      assertEquals(-4.0156, stk1.peek().doubleValue());
      assertEquals(4, stk1.size());

      // Test scientific notation
      CommandParser.Parse(stk1, stk2, "-4.56e12/6.78e3", "-4.56e12/6.78e3", "");
      StackCommands.cmdRound(stk1, "16");
      assertEquals("-672566371.6814159292035398", stk1.peek().toEngineeringString());
      assertEquals(5, stk1.size());

      CommandParser.Parse(stk1, stk2, "9.99E12/3.33E12", "9.99E12/3.33E12", "");
      StackCommands.cmdRound(stk1, "7");
      assertEquals("3.0000000", stk1.peek().toEngineeringString());
      assertEquals(6, stk1.size());

      CommandParser.Parse(stk1, stk2, "1/33.333E10", "1/33.333E10", "");
      StackCommands.cmdRound(stk1, "7");
      assertEquals("0.0000000", stk1.peek().toPlainString());
      assertEquals(7, stk1.size());
   }

   // Test a "NumOp" - a number with an operand at the end
   @Test
   void testNumOp() {
      StackObj stk1 = new StackObj();
      StackObj stk2 = new StackObj();

      stk1.clear();
      CommandParser.Parse(stk1, stk2, "-123.456", "-123.456", "");
      assertEquals(1, stk1.size());
      assertEquals(-123.456, stk1.peek().doubleValue());

      CommandParser.Parse(stk1, stk2, "2+", "2+", "");
      assertEquals(1, stk1.size());
      assertEquals(-121.456, stk1.peek().doubleValue());

      CommandParser.Parse(stk1, stk2, "16.61-", "16.61-", "");
      assertEquals(1, stk1.size());
      assertEquals(-138.066, stk1.peek().doubleValue());

      CommandParser.Parse(stk1, stk2, "-2.2*", "-2.2*", "");
      assertEquals(1, stk1.size());
      assertEquals(303.7452, stk1.peek().doubleValue());

      CommandParser.Parse(stk1, stk2, "10/", "10/", "");
      assertEquals(1, stk1.size());
      assertEquals(30.37452, stk1.peek().doubleValue());

      CommandParser.Parse(stk1, stk2, "2^", "2^", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "4");
      assertEquals(922.6115, stk1.peek().doubleValue());

      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "4");
      assertEquals(922.6115, stk1.peek().doubleValue());

      // Test scientific notation with NumOps
      stk1.clear();
      stk1.push(-123.456e12);
      CommandParser.Parse(stk1, stk2, "123.456e12/", "123.456e12/", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "4");
      assertEquals("-1.0000", stk1.peek().toString());

      CommandParser.Parse(stk1, stk2, "3.3E14*", "3.3E14*", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "4");
      assertEquals("-330000000000000.0000", stk1.peek().toString());

      CommandParser.Parse(stk1, stk2, "2/", "2/", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "4");
      assertEquals("-165000000000000.0000", stk1.peek().toString());

      CommandParser.Parse(stk1, stk2, "2.34e12/", "2.234e12/", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "9");
      assertEquals("-70.512820513", stk1.peek().toString());

      CommandParser.Parse(stk1, stk2, ".02e2^", ".02e2^", "");
      assertEquals(1, stk1.size());
      StackCommands.cmdRound(stk1, "8");
      assertEquals("4972.05785670", stk1.peek().toString());

   }

   // Test the entry of a scientific notation number
   @Test
   void testScientificNotationNumberEntry() {
      StackObj stk = new StackObj();

      CommandParser.Parse(stk, stk, "1.234e8", "1.234e8", "");
      assertEquals(1, stk.size());
      assertEquals("1.234E+8", stk.peek().toString());

      CommandParser.Parse(stk, stk, "-7.556677E12", "-7.556677E12", "");
      assertEquals(2, stk.size());
      assertEquals("-7.556677E+12", stk.peek().toEngineeringString());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "-3.556677x3", "-3.556677x3", "");
      assertEquals(2, stk.size());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "45.123~3", "45.123~3", "");
      assertEquals(2, stk.size());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "-7.556677ee3", "-7.556677ee3", "");
      assertEquals(2, stk.size());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "7.5566773E", "7.5566773E", "");
      assertEquals(2, stk.size());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "E7.5566773", "E7.5566773", "");
      assertEquals(2, stk.size());

      // Bad Syntax
      CommandParser.Parse(stk, stk, "1.234e8.3", "1.234e8.3", "");
      assertEquals(2, stk.size());

   }

}
