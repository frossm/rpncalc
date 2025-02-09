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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackCommandsTest {

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdAddAll(org.fross.rpncalc.StackObj, java.lang.String)}.
    */
   @Test
   void testCmdAddAll() {
      StackObj stk = new StackObj();
      stk.push(1.23);
      stk.push(2.34);
      stk.push(3.45);
      stk.push(4.56);
      stk.push(5.67);

      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(6, stk.size());
      assertEquals(17.25, stk.peek().doubleValue());

      StackCommands.cmdAddAll(stk, "");
      StackCommands.cmdRound(stk, "6");
      assertEquals(34.5, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      // Scientific notation test #1
      stk.push(22e02);
      stk.push(11.123e2);
      assertEquals(3, stk.size());
      StackCommands.cmdAddAll(stk, "");
      StackCommands.cmdRound(stk, "6");
      assertEquals(3346.8, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      // Scientific notation test #2
      stk.push("-23.3223e4");
      assertEquals(2, stk.size());
      StackCommands.cmdAddAll(stk, "");
      StackCommands.cmdRound(stk, "6");
      assertEquals(-229876.2, stk.pop().doubleValue());
      assertTrue(stk.isEmpty());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdAbsoluteValue(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdAbsoluteValue() {
      StackObj stk = new StackObj();

      stk.push(123.456);
      StackCommands.cmdAbsoluteValue(stk);
      assertEquals(123.456, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(-789.123);
      StackCommands.cmdAbsoluteValue(stk);
      assertEquals(789.123, stk.peek().doubleValue());
      assertEquals(2, stk.size());

      // Scientific notation test
      stk.push("-23.3223e44");
      assertEquals(3, stk.size());
      StackCommands.cmdAbsoluteValue(stk);
      assertEquals(23.3223e44, stk.peek().doubleValue());
      assertEquals(3, stk.size());
   }

   /**
    * Testing Averages
    */
   @Test
   void testCmdAverage() {
      StackObj stk = new StackObj();

      // Test #1
      // Test with keep
      stk.push(1.1);
      stk.push(2.2);
      stk.push(3.3);
      stk.push(4.4);
      stk.push(-5.5);
      stk.push(-6.6);
      stk.push(-7.7);
      StackCommands.cmdAverage(stk, "keep");
      StackCommands.cmdRound(stk, "8");
      assertEquals(-1.25714286, stk.peek().doubleValue());
      assertEquals(8, stk.size());

      // Test #2
      // Test without keep
      stk.push(1.23);
      stk.push(2.34);
      stk.push(3.45);
      stk.push(4.56);
      stk.push(5.67);
      StackCommands.cmdAverage(stk, "");
      StackCommands.cmdRound(stk, "8");
      assertEquals(1, stk.size());
      assertEquals(0.55329670, stk.pop().doubleValue());

      // Test #3
      Double[] testValues1 = {-5.0, 1.2, 3.34, 3.44, 3.45, 7.3, 8.76, 33.2, 42.44, 1000.01};
      for (Double aDouble : testValues1) {
         stk.push(aDouble);
      }
      assertEquals(10, stk.size());
      StackCommands.cmdAverage(stk, "");
      assertEquals(109.814, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      // Test #4
      stk.clear();
      String[] testValues2 = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14"};
      for (String s : testValues2) {
         stk.push(s);
      }
      assertEquals(12, stk.size());
      StackCommands.cmdAverage(stk, "");
      StackCommands.cmdRound(stk, "5");
      assertEquals("10406510479258811833.33333", stk.peek().toEngineeringString());
      assertEquals(1, stk.size());

      // Scientific Notation Test
      stk.clear();
      Double[] testValues = {1.23456E11, 4.56789e12, 10.234e11, 12.1354e10, -1.23e13, -4.12321E17};
      for (Double testValue : testValues) {
         stk.push(testValue);
      }
      StackCommands.cmdAverage(stk, "");
      assertEquals(1, stk.size());
      StackCommands.cmdRound(stk, "10");
      assertEquals("-68721243983333333.3333333333", stk.pop().toString());
      assertTrue(stk.isEmpty());

   }

   /**
    * Testing the clear command
    */
   @Test
   void testCmdClear() {
      StackObj stk = new StackObj();

      // Add some items to the stack
      Double[] testValues = {-5.0, 1.2, 3.34, 3.44, 3.45, 7.3, 8.76, 33.2, 42.44, 1000.01};
      for (Double testValue : testValues) {
         stk.push(testValue);
      }

      // Ensure there are 10 items
      assertEquals(10, stk.size());

      // Clear the stack and check that it's empty. Don't use cmdClear as that will erase the maven test screen
      stk.clear();
      assertEquals(0, stk.size());
      assertTrue(stk.isEmpty());
   }

   /**
    * Test copy command
    */
   @Test
   void testCmdCopy() {
      StackObj stk = new StackObj();

      stk.push(1.23);
      stk.push(2.34);
      stk.push(3.45);
      stk.push(4.56);
      stk.push(5.67);
      stk.push(-1000.001);

      // Copy without an argument, so copy line1
      StackCommands.cmdCopy(stk, "");
      assertEquals(7, stk.size());
      assertEquals(-1000.001, stk.get(stk.size() - 1).doubleValue());
      assertEquals(-1000.001, stk.get(stk.size() - 2).doubleValue());
      assertEquals(5.67, stk.get(stk.size() - 3).doubleValue());
      assertEquals(1.23, stk.get(0).doubleValue());

      // Copy line4
      StackCommands.cmdCopy(stk, "4");
      assertEquals(8, stk.size());
      assertEquals(4.56, stk.pop().doubleValue());
      assertEquals(-1000.001, stk.pop().doubleValue());
      assertEquals(-1000.001, stk.pop().doubleValue());
      assertEquals(5.67, stk.pop().doubleValue());
      assertEquals(4.56, stk.pop().doubleValue());
      assertEquals(3.45, stk.pop().doubleValue());
      assertEquals(1.23, stk.get(0).doubleValue());

      // Test Scientific Notation Copy
      stk.push(1.23E44);
      StackCommands.cmdCopy(stk, "");
      assertEquals(4, stk.size());
      assertEquals("123E+42", stk.peek().toEngineeringString());
      assertEquals("123E+42", stk.get(stk.size() - 1).toEngineeringString());
      assertEquals(1.23, stk.get(0).doubleValue());

      // Test relative copy
      stk.clear();
      stk.push(1);
      stk.push(2);
      stk.push(3);
      stk.push(4);
      stk.push(5);
      StackCommands.cmdCopy(stk, "-3");
      assertEquals(6, stk.size());
      assertEquals("2", stk.pop().toString());

      StackCommands.cmdCopy(stk, "-1");
      assertEquals(6, stk.size());
      assertEquals("4", stk.pop().toString());

      StackCommands.cmdCopy(stk, "-4");
      assertEquals(6, stk.size());
      assertEquals("1", stk.pop().toString());

      StackCommands.cmdCopy(stk, "-5");
      assertEquals(5, stk.size());
      assertEquals("4", stk.get(stk.size() - 2).toString());

      StackCommands.cmdCopy(stk, "err");
      assertEquals(5, stk.size());
      assertEquals("4", stk.get(stk.size() - 2).toString());
   }

   /**
    * Test delete command no parameter. Should delete line1
    */
   @Test
   void testCmdDeleteNoParm() {
      StackObj stk = new StackObj();

      stk.push(5.0);
      stk.push(4.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(1.0);

      // Delete line1 by not providing a line number
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "");
      assertEquals(4, stk.size());
      assertEquals(2, stk.peek().doubleValue());
      assertEquals(5, stk.get(0).doubleValue());
      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(5, stk.size());
      assertEquals(14, stk.peek().doubleValue());

      // Delete all items one at a time
      StackCommands.cmdDelete(stk, "");
      assertEquals(4, stk.size());
      assertEquals(2, stk.peek().doubleValue());
      StackCommands.cmdDelete(stk, "");
      assertEquals(3, stk.size());
      assertEquals(3, stk.peek().doubleValue());
      StackCommands.cmdDelete(stk, "");
      assertEquals(2, stk.size());
      assertEquals(4, stk.peek().doubleValue());
      StackCommands.cmdDelete(stk, "");
      assertEquals(1, stk.size());
      assertEquals(5, stk.peek().doubleValue());
      StackCommands.cmdDelete(stk, "");
      assertEquals(0, stk.size());
      StackCommands.cmdDelete(stk, "");
      assertEquals(0, stk.size());
   }

   /**
    * Test delete with a single line number given
    */
   @Test
   void testCmdDelete1Line() {
      StackObj stk = new StackObj();

      stk.push(5.0);
      stk.push(4.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(1.0);

      // Delete line 3
      StackCommands.cmdDelete(stk, "3");
      assertEquals(4, stk.size());
      assertEquals(1, stk.peek().doubleValue());
      assertEquals(5, stk.get(0).doubleValue());
      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(5, stk.size());
      assertEquals(12, stk.peek().doubleValue());

      // Delete Line 5
      StackCommands.cmdDelete(stk, "5");
      assertEquals(4, stk.size());
      assertEquals(12, stk.peek().doubleValue());
      assertEquals(4, stk.get(0).doubleValue());
      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(5, stk.size());
      assertEquals(19, stk.peek().doubleValue());
   }

   /**
    * Test delete by providing a range of lines to delete
    */
   @Test
   void testCmdDeleteRange() {
      StackObj stk = new StackObj();

      stk.push(5.0);
      stk.push(4.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(1.0);

      StackCommands.cmdDelete(stk, "2-4");
      assertEquals(2, stk.size());
      assertEquals(1, stk.peek().doubleValue());
      assertEquals(5, stk.get(0).doubleValue());
      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(3, stk.size());
      assertEquals(6, stk.peek().doubleValue());

      // Undo the above changes and reset for next test
      StackCommands.cmdUndo(stk, "1");

      StackCommands.cmdDelete(stk, "5 - 2");
      assertEquals(1, stk.size());
      assertEquals(1, stk.peek().doubleValue());
      assertEquals(1, stk.get(0).doubleValue());
      StackCommands.cmdAddAll(stk, "keep");
      assertEquals(1, stk.size());
      assertEquals(1, stk.peek().doubleValue());

      // Delete last line
      StackCommands.cmdDelete(stk, "1-1");
      assertEquals(0, stk.size());
      StackCommands.cmdDelete(stk, "");
      assertEquals(0, stk.size());
   }

   /**
    * Test delete by sending illegal parameters. The size shouldn't change
    */
   @Test
   void testCmdDeleteParmError() {
      StackObj stk = new StackObj();

      stk.push(5.0);
      stk.push(4.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(1.0);

      // Send a bunch of incorrect parameters
      StackCommands.cmdDelete(stk, "x");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "5x");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "5-52a");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "-1");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "4-1000");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "200-1");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "keep");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "-1 * 5");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "1-6");
      assertEquals(5, stk.size());
      StackCommands.cmdDelete(stk, "5-0");
      assertEquals(5, stk.size());
   }

   /**
    * Test those bones! This is hard to get an exact test since the results are random, but we can check the ranges and
    * quantities
    */
   @Test
   void testCmdDice() {
      StackObj stk = new StackObj();

      // Roll some bones and the stack counts should equal the number of rolls
      StackCommands.cmdDice(stk, "10D4");
      assertEquals(10, stk.size());

      StackCommands.cmdDice(stk, "6d10");
      assertEquals(16, stk.size());

      StackCommands.cmdDice(stk, "500d100");
      assertEquals(516, stk.size());

      // Test illegal parameter syntax
      StackCommands.cmdDice(stk, "d1");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "11a12");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "10-2");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "ddd");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "10d");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "6");
      assertEquals(516, stk.size());
      StackCommands.cmdDice(stk, "10d0");
      assertEquals(516, stk.size());
   }

   /**
    * Test the 'dice' command by rolling a *lot* of them and ensuring all the rolls are within the range
    */
   @Test
   void testDieRollsAreWithinRange() {
      StackObj stk = new StackObj();

      // Roll 10000 d10 dice
      StackCommands.cmdDice(stk, "10000d100");
      for (int i = 0; i < stk.size(); i++) {
         if (stk.get(i).compareTo(BigDecimal.ONE) < 0 || stk.get(i).compareTo(new BigDecimal("100")) > 0) {
            fail();
         }
      }

      // This is a bit experimental, but with this large data set, the average should be around 50.
      // So test to see if it's < 40 or > 60 and throw an error if it does. This might not work well.
      StackCommands.cmdAverage(stk, "");
      if (stk.peek().doubleValue() < 40 || stk.peek().doubleValue() > 60) {
         fail();
      }
   }

   /**
    * Factorial test. MathTest takes care of most of this, just make sure it can be called with StackCommands.cmdFactorial()
    */
   @Test
   void testCmdFactorial() {
      StackObj stk = new StackObj();

      // Test #1
      stk.push(12);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("479001600", stk.pop().toEngineeringString());
      assertEquals(0, stk.size());

      // Test #2
      stk.push(12.123);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("479001600", stk.pop().toEngineeringString());
      assertEquals(0, stk.size());

      // Test #3
      stk.push(12.999);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("479001600", stk.pop().toEngineeringString());
      assertEquals(0, stk.size());

      // Test #4
      stk.push(13.0000001);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("6227020800", stk.pop().toEngineeringString());
      assertEquals(0, stk.size());

      // Test #5
      stk.push(-6.1);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("-6.1", stk.peek().toEngineeringString());
      assertEquals(1, stk.size());

      // Test #6
      stk.push(0);
      assertEquals(2, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("0", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());

      // Test #7
      stk.clear();
      stk.push(1e2);
      assertEquals(1, stk.size());
      StackCommands.cmdFactorial(stk);
      assertEquals("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000", stk.peek().toString());
      assertEquals(1, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdFlipSign(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdFlipSign() {
      StackObj stk = new StackObj();

      stk.push(123.321);

      StackCommands.cmdFlipSign(stk);
      assertEquals(-123.321, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      StackCommands.cmdFlipSign(stk);
      assertEquals(123.321, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(-4.56e+44);
      StackCommands.cmdFlipSign(stk);
      assertEquals("456E+42", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());

      StackCommands.cmdFlipSign(stk);
      assertEquals("-456E+42", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdInteger(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdInteger() {
      StackObj stk = new StackObj();

      stk.push(123.456);
      StackCommands.cmdInteger(stk);
      assertEquals(1, stk.size());
      assertEquals(123.0, stk.pop().doubleValue());

      stk.push(0.999);
      StackCommands.cmdInteger(stk);
      assertEquals(1, stk.size());
      assertEquals(0, stk.pop().doubleValue());

      stk.push(101.0);
      StackCommands.cmdInteger(stk);
      assertEquals(1, stk.size());
      assertEquals(101, stk.pop().doubleValue());

      stk.push(-1040.341234);
      StackCommands.cmdInteger(stk);
      assertEquals(1, stk.size());
      assertEquals(-1040, stk.pop().doubleValue());

      stk.push(-1.23e4);
      StackCommands.cmdInteger(stk);
      assertEquals(1, stk.size());
      assertEquals(-12300, stk.pop().doubleValue());
   }

   /**
    * Testing the linear regression results
    * Reference for testing:  <a href="https://www.socscistatistics.com/tests/regression/default.aspx">...</a>
    */
   @Test
   void testCmdLinearRegression() {
      StackObj stk = new StackObj();

      // Test #1
      stk.push(11.00);
      stk.push(9.00);
      stk.push(22.00);
      stk.push(15.00);
      stk.push(20.00);
      stk.push(10.00);
      stk.push(35.00);
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "4");
      assertEquals(8, stk.size());
      assertEquals(27.7143, stk.pop().doubleValue());

      // Test #2
      stk.clear();
      stk.push(-5.67);
      stk.push(0.0);
      stk.push(3.45);
      stk.push(9.01);
      stk.push(12.0);
      stk.push(15.0);
      stk.push(6.99);
      stk.push(11.11);
      stk.push(22.22);
      stk.push(3.0);
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "6");
      assertEquals(11, stk.size());
      assertEquals(16.671333, stk.pop().doubleValue());

      // Test #3
      stk.clear();
      stk.push(29.11);
      stk.push(11.22);
      stk.push(-18.33);
      stk.push(50.44);
      stk.push(-44.55);
      stk.push(49.66);
      stk.push(5.77);
      stk.push(-34.88);
      stk.push(40.99);
      stk.push(3.00);
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "6");
      assertEquals(11, stk.size());
      assertEquals(4.271333, stk.pop().doubleValue());

      // Test #4
      stk.clear();
      Double[] testValues = {29.0, 41.0, 8.0, 18.0, 22.0, 99.0, 32.0, 15.0, 31.0, 3.0, 72.0, 12.0, 60.0, 32.0, 54.0, 45.0, 34.0, 76.0, 5.0, 67.0};
      for (Double testValue : testValues) {
         stk.push(testValue);
      }
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "7");
      assertEquals(48.9842105, stk.pop().doubleValue());

      // Test #5
      stk.clear();
      Double[] testValues1 = {16.0, 1.0, 14.234, -2.112, -5.1234, 2.345, 8.1, -2.334, 0.0, 4.567, -2.552123, -12.3452, 0.123, -0.9582234, -8.321, -9.9899};
      for (Double aDouble : testValues1) {
         stk.push(aDouble);
      }
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "9");
      assertEquals(17, stk.size());
      assertEquals(-9.101980055, stk.pop().doubleValue());

      // Test #6 - Scientific Notation
      stk.clear();
      String[] testValues2 = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14"};
      for (String s : testValues2) {
         stk.push(s);
      }
      assertEquals(12, stk.size());
      StackCommands.cmdLinearRegression(stk, "a");
      StackCommands.cmdRound(stk, "9");
      assertEquals("47498562223075895424.242424242", stk.peek().toEngineeringString());
      assertEquals(13, stk.size());

      // Test #7 - Custom X values
      stk.clear();
      Double[] testValues3 = {29.0, 41.0, 8.0, 18.0, 22.0, 99.0, 32.0, 15.0, 31.0, 3.0, 72.0, 12.0, 60.0, 32.0, 54.0, 45.0, 34.0, 76.0, 5.0, 67.0};
      for (Double aDouble : testValues3) {
         stk.push(aDouble);
      }
      StackCommands.cmdLinearRegression(stk, "30 a");
      StackCommands.cmdRound(stk, "4");
      assertEquals(58.6135, stk.pop().doubleValue());
      assertEquals(20, stk.size());

      // Test #8 - Custom X values
      stk.clear();
      Double[] testValues4 = {19.0, 41.0, 8.0, 18.0, 12.0, -99.0, 32.0, 15.0, 31.0, 3.0, 72.0, 12.0, 60.0, 32.0, 54.0, 45.0, 34.0, 76.0, 5.0, 67.0};
      for (Double aDouble : testValues4) {
         stk.push(aDouble);
      }
      StackCommands.cmdLinearRegression(stk, "22 a");
      StackCommands.cmdRound(stk, "4");
      assertEquals(57.1564, stk.pop().doubleValue());
      assertEquals(20, stk.size());

      // Test #9 - Custom X values
      stk.clear();
      Double[] testValues5 = {29.0, 41.0, 8.0, 18.0, 22.0, -99.0, 32.0, 15.0, 31.0, 3.0, 72.0, 12.0, -60.0, 32.0, 54.0, 45.0, 14.0, 76.0, 5.0, 44.0};
      for (Double aDouble : testValues5) {
         stk.push(aDouble);
      }
      StackCommands.cmdLinearRegression(stk, "45 a");
      StackCommands.cmdRound(stk, "4");
      assertEquals(69.1932, stk.pop().doubleValue());
      assertEquals(20, stk.size());


   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdLog(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdLog() {
      StackObj stk = new StackObj();

      // Test #1
      stk.push(123.456);
      StackCommands.cmdLog(stk);
      StackCommands.cmdRound(stk, "7");
      assertEquals(4.8158848, stk.pop().doubleValue());

      // Test #2
      stk.push(123.456);
      StackCommands.cmdLog(stk);
      StackCommands.cmdRound(stk, "7");
      assertEquals(4.8158848, stk.pop().doubleValue());

      // Test #3
      stk.push(12332.12333);
      StackCommands.cmdLog(stk);
      StackCommands.cmdRound(stk, "7");
      assertEquals(9.4199628, stk.pop().doubleValue());

      // Test #4 - Scientific Notation
      stk.push(4.567e6);
      StackCommands.cmdLog(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("15.3343670922", stk.pop().toString());
      stk.push(9.87654321e22);
      StackCommands.cmdLog(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("52.9470346189", stk.pop().toString());

   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdLog10(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdLog10() {
      StackObj stk = new StackObj();

      // Test #1
      stk.push(9.41996);
      StackCommands.cmdLog10(stk);
      StackCommands.cmdRound(stk, "15");
      assertEquals(0.974049058651035, stk.pop().doubleValue());

      // Test #2
      stk.push(1588.963);
      StackCommands.cmdLog10(stk);
      StackCommands.cmdRound(stk, "15");
      assertEquals(3.201113784505733, stk.pop().doubleValue());

      // Test #3
      stk.push(0.00123);
      StackCommands.cmdLog10(stk);
      StackCommands.cmdRound(stk, "15");
      assertEquals(-2.910094888560602, stk.pop().doubleValue());

      // Test #4 - Scientific Notation
      stk.push(4.567e6);
      StackCommands.cmdLog10(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("6.6596310116", stk.pop().toString());

      stk.push(9.87654321e22);
      StackCommands.cmdLog10(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("22.9946049681", stk.pop().toString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdMaximum(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdMaximum() {
      StackObj stk = new StackObj();

      stk.push(4.56);
      stk.push(1.23);
      stk.push(5.67);
      stk.push(-11.12);
      stk.push(2.34);
      stk.push(4.56);
      stk.push(3.45);
      stk.push(-123.2245);

      // Test #1
      StackCommands.cmdMaximum(stk);
      assertEquals(9, stk.size());
      assertEquals(5.67, stk.peek().doubleValue());

      // Test #2
      stk.push(5.68);
      StackCommands.cmdMaximum(stk);
      assertEquals(11, stk.size());
      assertEquals(5.68, stk.peek().doubleValue());
      assertEquals(5.68, stk.get(stk.size() - 1).doubleValue());

      // Test #3 - Scientific Notation
      stk.push(-1.2e22);
      stk.push(5.43E11);
      stk.push(2.3343e3);
      StackCommands.cmdMaximum(stk);
      assertEquals(15, stk.size());
      assertEquals("5.43E+11", stk.peek().toString());
      assertEquals("2334.3", stk.get(stk.size() - 2).toPlainString());
   }

   /**
    * Test median command with an odd number of stack items
    */
   @Test
   void testMedianOddNumberOfValues() {
      StackObj stk = new StackObj();

      // Test #1
      // Test odd numbers on the stack with keep flag
      stk.push(3.1);
      stk.push(2.2);
      stk.push(5.3);
      stk.push(-4.4);
      stk.push(-1.5);

      StackCommands.cmdMedian(stk, "keep");
      assertEquals(2.2, stk.pop().doubleValue());
      assertEquals(5, stk.size());

      // Ensure the order is back to normal after the median sort
      assertEquals(3.1, stk.get(0).doubleValue());
      assertEquals(2.2, stk.get(1).doubleValue());
      assertEquals(5.3, stk.get(2).doubleValue());
      assertEquals(-4.4, stk.get(3).doubleValue());
      assertEquals(-1.5, stk.get(4).doubleValue());

      // Test #2
      Double[] testValues = {43.39, 26.20739, 87.59777, 55.98073, 36.38447, 39.96893, 93.32821, 74.68383, 14.7644, 79.13016, 94.21511, 38.45116, 89.67177, 25.71, 70.48159, 57.75962, 80.24972, 82.27109, 8.14497, 75.00809, 22.74851, 85.22599, 29.16305, 85.22427, 56.10867};

      // Build the stack
      stk.clear();
      for (Double testValue : testValues) {
         stk.push(testValue);
      }

      assertEquals(25, stk.size());
      StackCommands.cmdMedian(stk, "keep");
      assertEquals(57.75962, stk.peek().doubleValue());
      assertEquals(26, stk.size());

      // Test #3 - Scientific Notation
      stk.clear();
      String[] testValues1 = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14", "-453.22e9"};
      for (String s : testValues1) {
         stk.push(s);
      }

      assertEquals(13, stk.size());
      StackCommands.cmdMedian(stk, "keep");
      assertEquals(14, stk.size());
      assertEquals("3.8128E+12", stk.pop().toString());

   }

   /**
    * Test median command with an even number of stack items
    */
   @Test
   void testMedianEvenNumberOfValues() {
      StackObj stk = new StackObj();

      // Test even numbers on the stack
      stk.push(4.56);
      stk.push(-1.23);
      stk.push(5.67);
      stk.push(2.34);
      stk.push(4.56);
      stk.push(-3.45);

      StackCommands.cmdMedian(stk, "keep");
      StackCommands.cmdRound(stk, "2");

      assertEquals(3.45, stk.pop().doubleValue());
      assertEquals(6, stk.size());

      // Ensure the order is back to normal after the median sort
      assertEquals(4.56, stk.get(0).doubleValue());
      assertEquals(-1.23, stk.get(1).doubleValue());
      assertEquals(5.67, stk.get(2).doubleValue());
      assertEquals(2.34, stk.get(3).doubleValue());
      assertEquals(4.56, stk.get(4).doubleValue());
      assertEquals(-3.45, stk.get(5).doubleValue());

      // Test #2
      stk.clear();
      Double[] testValues1 = {43.39, 26.20739, 87.59777, 55.98073, 36.38447, 39.96893, 93.32821, 74.68383, 14.7644, 79.13016, 94.21511, 38.45116, 89.67177, 25.71, 70.48159, 57.75962, 80.24972, 82.27109, 8.14497, 75.00809, 22.74851, 85.22599, 29.16305, 85.22427, 56.10867, -88.7123};
      for (Double aDouble : testValues1) {
         stk.push(aDouble);
      }

      assertEquals(26, stk.size());
      StackCommands.cmdMedian(stk, "keep");
      assertEquals(56.934145, stk.peek().doubleValue());
      assertEquals(27, stk.size());

      // Test #3
      stk.clear();
      Double[] testValues2 = {-5.0, 1.2, 3.34, 3.44, 3.45, 7.3, 8.76, 33.2, 42.44, 1000.01};
      for (Double aDouble : testValues2) {
         stk.push(aDouble);
      }
      assertEquals(10, stk.size());
      StackCommands.cmdMedian(stk, "");
      assertEquals(5.375, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      // Test #4 - Scientific Notation
      stk.clear();
      Double[] testValues3 = {-5.0E9, 1.2E10, 3.34E11, 3.44E12, 3.45E13, 7.3E12, 8.76E11, 33.2E14, 42.44E8, 1000.01E10};
      for (Double aDouble : testValues3) {
         stk.push(aDouble);
      }
      assertEquals(10, stk.size());
      StackCommands.cmdMedian(stk, "");
      assertEquals("2.158E+12", stk.peek().toEngineeringString());
      assertEquals(1, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdMinimum(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdMinimum() {
      StackObj stk = new StackObj();

      stk.push(4.56);
      stk.push(1.23);
      stk.push(5.67);
      stk.push(-11.12);
      stk.push(2.34);
      stk.push(4.56);
      stk.push(3.45);
      stk.push(-123.2245);

      // Test #1
      StackCommands.cmdMinimum(stk);
      assertEquals(9, stk.size());
      assertEquals(-123.2245, stk.peek().doubleValue());

      // Test #2
      stk.push(-123.2246);
      StackCommands.cmdMinimum(stk);
      assertEquals(11, stk.size());
      assertEquals(-123.2246, stk.peek().doubleValue());
      assertEquals(-123.2246, stk.get(stk.size() - 1).doubleValue());

      // Test #3 - Scientific Notation
      String[] testValues = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14", "-453.22e9"};
      for (String testValue : testValues) {
         stk.push(testValue);
      }
      StackCommands.cmdMinimum(stk);
      assertEquals(25, stk.size());
      assertEquals("-9.1404E+19", stk.peek().toString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdModulus(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdModulus() {
      StackObj stk = new StackObj();

      stk.push(5.67);
      stk.push(2.34);

      StackCommands.cmdModulus(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(0.99, stk.pop().doubleValue());

      stk.push(88.0);
      stk.push(5.0);
      StackCommands.cmdModulus(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(3.0, stk.pop().doubleValue());

      stk.push(-1234.98765);
      stk.push(44.65432);
      StackCommands.cmdModulus(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-29.32101, stk.pop().doubleValue());

      stk.push(-0.2356);
      stk.push(-8.123);
      StackCommands.cmdModulus(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-0.2356, stk.pop().doubleValue());

      stk.push(-144.144);
      stk.push(16.12);
      StackCommands.cmdModulus(stk);
      StackCommands.cmdRound(stk, "3");
      assertEquals(-15.184, stk.pop().doubleValue());

      stk.push(154.321E10);
      stk.push(1.1E6);
      StackCommands.cmdModulus(stk);
      assertEquals(200000, stk.pop().doubleValue());

      stk.push(899.123E19);
      stk.push(1.12345E9);
      StackCommands.cmdModulus(stk);
      assertEquals(939950000, stk.pop().doubleValue());

   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdRandom(org.fross.rpncalc.StackObj, java.lang.String)}.
    * Need to work Random to support numbers > a long
    */
   @Test
   void testCmdRandom() {
      StackObj stk = new StackObj();
      int numberOfValues = 5000;

      // Generate a large number of values between 1 and 100 inclusive
      for (int i = 0; i < numberOfValues; i++) {
         StackCommands.cmdRandom(stk, "1 100");
      }

      // Ensure there are the correct count of numbers and they are all in the correct range
      assertEquals(numberOfValues, stk.size());
      for (int i = 0; i < numberOfValues; i++) {
         if (stk.get(i).compareTo(BigDecimal.ZERO) < 0 || stk.get(i).compareTo(new BigDecimal("100")) > 0) {
            fail();
         }
      }

   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdRound(org.fross.rpncalc.StackObj, java.lang.String)}.
    */
   @Test
   void testCmdRound() {
      StackObj stk = new StackObj();

      stk.push(2.34567);

      StackCommands.cmdRound(stk, "");
      assertEquals(2, stk.pop().doubleValue());

      stk.push(2.34567);
      StackCommands.cmdRound(stk, "2");

      assertEquals(2.35, stk.pop().doubleValue());

      stk.push(2.34567);
      StackCommands.cmdRound(stk, "4");
      assertEquals(2.3457, stk.pop().doubleValue());

      stk.push(-65.4321);
      StackCommands.cmdRound(stk, "1");
      assertEquals(-65.4, stk.pop().doubleValue());

      stk.push(-65.4329);
      StackCommands.cmdRound(stk, "1");
      assertEquals(-65.4, stk.pop().doubleValue());

      stk.push(-65.4329);
      StackCommands.cmdRound(stk, "3");
      assertEquals(-65.433, stk.pop().doubleValue());

      stk.push(-65.4329);
      StackCommands.cmdRound(stk, "12");
      assertEquals(-65.4329, stk.pop().doubleValue());

      stk.push(0.1);
      StackCommands.cmdRound(stk, "0");
      assertEquals(0, stk.pop().doubleValue());

      // Scientific Notation
      stk.push(1.23456789e19);
      StackCommands.cmdRound(stk, "4");
      assertEquals("12345678900000000000.0000", stk.peek().toString());
      StackCommands.cmdRound(stk, "8");
      assertEquals("12345678900000000000.00000000", stk.peek().toString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdSwapElements(org.fross.rpncalc.StackObj, java.lang.String)}.
    */
   @Test
   void testCmdSwapElements() {
      StackObj stk = new StackObj();

      stk.push(1.0);
      stk.push(2.0);
      stk.push(3.0);
      stk.push(4.0);
      stk.push(5.0);

      // With no param given, swap line1 and line2
      StackCommands.cmdSwapElements(stk, "");
      assertEquals(4, stk.pop().doubleValue());
      assertEquals(5, stk.pop().doubleValue());
      assertEquals(3, stk.pop().doubleValue());
      assertEquals(2, stk.size());

      // Swap line3 and line2
      stk.push(4.0);
      stk.push(5.0);
      StackCommands.cmdSwapElements(stk, "3 2");
      assertEquals(5, stk.pop().doubleValue());
      assertEquals(2, stk.pop().doubleValue());
      assertEquals(4, stk.pop().doubleValue());
      assertEquals(1, stk.pop().doubleValue());
      assertEquals(0, stk.size());

      // Scientific Notation
      stk.push(1.234e44);
      stk.push(5.4321e33);
      assertEquals(2, stk.size());
      assertEquals("5.4321E+33", stk.peek().toEngineeringString());
      StackCommands.cmdSwapElements(stk, "");
      assertEquals("123.4E+42", stk.peek().toEngineeringString());
      assertEquals("5.4321E+33", stk.get(0).toEngineeringString());
   }

   /**
    * Test stack sorting descending
    */
   @Test
   void testSortingDescending() {
      StackObj stk = new StackObj();

      stk.push(1.0);
      stk.push(-2.0);
      stk.push(6.882);
      stk.push(9.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(7.0);
      stk.push(6.881);
      stk.push(-1.0);
      stk.push(4.0);
      stk.push(3.9);
      stk.push(-1.01);
      StackCommands.cmdSort(stk, "descending");

      assertEquals(12, stk.size());

      assertEquals(-2, stk.get(0).doubleValue());
      assertEquals(-1.01, stk.get(1).doubleValue());
      assertEquals(-1, stk.get(2).doubleValue());
      assertEquals(1, stk.get(3).doubleValue());
      assertEquals(2, stk.get(4).doubleValue());
      assertEquals(3, stk.get(5).doubleValue());
      assertEquals(3.9, stk.get(6).doubleValue());
      assertEquals(4, stk.get(7).doubleValue());
      assertEquals(6.881, stk.get(8).doubleValue());
      assertEquals(6.882, stk.get(9).doubleValue());
      assertEquals(7, stk.get(10).doubleValue());
      assertEquals(9, stk.get(11).doubleValue());

      // Scientific Notation
      stk.clear();
      String[] testValues = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14", "-453.22e9"};
      for (String testValue : testValues) {
         stk.push(testValue);
      }

      assertEquals(13, stk.size());
      StackCommands.cmdSort(stk, "descending");
      assertEquals("217.96E+18", stk.peek().toEngineeringString());
      assertEquals("-91.404E+18", stk.get(0).toEngineeringString());
   }

   /**
    * Test stack sorting ascending
    */
   @Test
   void testSortingAscending() {
      StackObj stk = new StackObj();

      stk.push(1.0);
      stk.push(-2.0);
      stk.push(6.882);
      stk.push(9.0);
      stk.push(3.0);
      stk.push(2.0);
      stk.push(7.0);
      stk.push(6.881);
      stk.push(-1.0);
      stk.push(4.0);
      stk.push(3.9);
      stk.push(-1.01);
      StackCommands.cmdSort(stk, "ascending");

      assertEquals(12, stk.size());

      assertEquals(-2, stk.get(11).doubleValue());
      assertEquals(-1.01, stk.get(10).doubleValue());
      assertEquals(-1, stk.get(9).doubleValue());
      assertEquals(1, stk.get(8).doubleValue());
      assertEquals(2, stk.get(7).doubleValue());
      assertEquals(3, stk.get(6).doubleValue());
      assertEquals(3.9, stk.get(5).doubleValue());
      assertEquals(4, stk.get(4).doubleValue());
      assertEquals(6.881, stk.get(3).doubleValue());
      assertEquals(6.882, stk.get(2).doubleValue());
      assertEquals(7, stk.get(1).doubleValue());
      assertEquals(9, stk.get(0).doubleValue());

      // Scientific Notation
      stk.clear();
      String[] testValues = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14", "-453.22e9"};
      for (String testValue : testValues) {
         stk.push(testValue);
      }

      assertEquals(13, stk.size());
      StackCommands.cmdSort(stk, "ascending");
      assertEquals("-91.404E+18", stk.peek().toEngineeringString());
      assertEquals("217.96E+18", stk.get(0).toEngineeringString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdSqrt(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdSqrt() {
      StackObj stk = new StackObj();

      stk.push(25.0);
      stk.push(12.5);

      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(3.53553, stk.pop().doubleValue());

      StackCommands.cmdSqrt(stk);
      assertEquals(5, stk.pop().doubleValue());

      stk.push(-100.0);
      StackCommands.cmdSqrt(stk);
      assertEquals(1, stk.size());
      assertEquals(-100.0, stk.pop().doubleValue());

      // Scientific Notation
      stk.clear();
      String[] testValues = {"9.796E15", "52.266E12", "4.812E11", "21.857E14", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7"};
      for (String testValue : testValues) {
         stk.push(testValue);
      }
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("10283.0929199342", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("14763468427.1684612832", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("1952639.2395934278", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("29778683.6512294479", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("9639553.9315883284", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("46751470.5651062916", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("693685.8078409850", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("7229522.8058288882", stk.pop().toEngineeringString());
      StackCommands.cmdSqrt(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("98974744.2532689750", stk.pop().toEngineeringString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackCommands#cmdStdDeviation(org.fross.rpncalc.StackObj, java.lang.String)}.
    */
   @Test
   void testCmdStdDeviation() {
      // Test #1
      StackObj stk = new StackObj();
      Double[] testValues = {10.0, 5.0, 1.0};
      for (Double testValue : testValues) {
         stk.push(testValue);
      }

      // Take the std deviation of the stack and keep values
      StackCommands.cmdStdDeviation(stk, "keep");
      StackCommands.cmdRound(stk, "10");
      assertEquals(4, stk.size());
      assertEquals(3.6817870057, stk.pop().doubleValue());

      // Test #2
      StackObj stk1 = new StackObj();
      Double[] testValues1 = {10.01, -12.55, 23.99, 16.102, -23.56, 21.0, 16.123};
      for (Double aDouble : testValues1) {
         stk1.push(aDouble);
      }

      // Take the std deviation of the stack and keep values
      StackCommands.cmdStdDeviation(stk1, "keep");
      StackCommands.cmdRound(stk1, "10");
      assertEquals(8, stk1.size());
      assertEquals(16.7982686219, stk1.pop().doubleValue());

      // Sort the stack - sd should be the same
      StackCommands.cmdSort(stk1, "d");
      StackCommands.cmdStdDeviation(stk1, "");
      StackCommands.cmdRound(stk1, "7");
      assertEquals(1, stk1.size());
      assertEquals(16.7982686, stk1.pop().doubleValue());

      // Scientific Notation
      stk.clear();
      String[] testValues2 = {"9.796E15", "-16.819E17", "52.266E12", "4.812E11", "21.857E14", "-91.404E18", "92.921E12", "88.677E13", "38.128E11", "21.796E19", "10.5742E7", "-89.922E14", "-453.22e9"};
      for (String s : testValues2) {
         stk.push(s);
      }
      assertEquals(13, stk.size());
      StackCommands.cmdStdDeviation(stk, "");
      StackCommands.cmdRound(stk, "10");
      assertEquals(1, stk.size());
      assertEquals("64845675563532622599.8090660774", stk.pop().toEngineeringString());
   }

   /**
    * Test Undo capabilities
    */
   @Test
   void testCmdUndo() {
      StackObj stk = new StackObj();
      Double[] testData = {10.0, 12.0, 23.0, 23.0, 16.0, 23.0, 21.0, 16.0};

      // Load the test data into the stack
      for (Double i : testData) {
         stk.saveUndo();
         stk.push(i);
      }

      StackCommands.cmdUndo(stk, "");
      assertEquals(7, stk.size());
      assertEquals(21, stk.peek().doubleValue());
      assertEquals(10, stk.get(0).doubleValue());

      StackCommands.cmdUndo(stk, "3");
      assertEquals(2, stk.size());
      assertEquals(12, stk.peek().doubleValue());
      assertEquals(10, stk.get(0).doubleValue());

      StackCommands.cmdAddAll(stk, "");
      assertEquals(1, stk.size());
      assertEquals(22, stk.pop().doubleValue());

      StackCommands.cmdUndo(stk, "");
      assertEquals(2, stk.size());
      assertEquals(12, stk.peek().doubleValue());
      assertEquals(10, stk.get(0).doubleValue());
   }

}
