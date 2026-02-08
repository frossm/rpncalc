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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackConversionsTest {
   /**
    * Test method for fractional conversion
    */
   @Test
   void testCmdFraction() {
      StackObj stk = new StackObj();

      // Test #1
      stk.clear();
      stk.push(71.046875);
      assertEquals(71.046875, stk.peek().doubleValue());
      // 64
      String[] result = StackConversions.cmdFraction(stk, "64");
      assertEquals("71 3/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("71 1/16", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("71 1/16", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("71", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("71", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("71", result[3]);

      // Test #2
      stk.clear();
      stk.push(4.99);
      assertEquals(4.99, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("4 63/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("5", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("5", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("5", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("5", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("5", result[3]);

      // Test #3
      stk.clear();
      stk.push(23.212121);
      assertEquals(23.212121, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("23 7/32", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("23 7/32", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("23 3/16", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("23 1/4", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("23 1/4", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("23", result[3]);

      // Test #3
      stk.clear();
      stk.push(71.7272);
      assertEquals(71.7272, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("71 47/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("71 23/32", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("71 3/4", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("71 3/4", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("71 3/4", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("71 1/2", result[3]);

      // Test with negative numbers
      // Test #-1
      stk.clear();
      stk.push(-71.046875);
      assertEquals(-71.046875, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("-71 3/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("-71 1/16", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("-71 1/16", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("-71", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("-71", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-71", result[3]);

      // Test #-2
      stk.clear();
      stk.push(-4.99);
      assertEquals(-4.99, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("-4 63/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("-5", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("-5", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("-5", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("-5", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-5", result[3]);

      // Test #3
      stk.clear();
      stk.push(-23.212121);
      assertEquals(-23.212121, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("-23 7/32", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("-23 7/32", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("-23 3/16", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("-23 1/4", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("-23 1/4", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-23", result[3]);

      // Test #3
      stk.clear();
      stk.push(-71.7272);
      assertEquals(-71.7272, stk.peek().doubleValue());
      // 64
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("-71 47/64", result[3]);
      // 32
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("-71 23/32", result[3]);
      // 16
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("-71 3/4", result[3]);
      // 8
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("-71 3/4", result[3]);
      // 4
      result = StackConversions.cmdFraction(stk, "4");
      assertEquals("-71 3/4", result[3]);
      // 2
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-71 1/2", result[3]);

      // Misc
      stk.clear();
      stk.push(71.1);
      result = StackConversions.cmdFraction(stk, "16");
      assertEquals("71 1/8", result[3]);
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("71", result[3]);

      stk.clear();
      stk.push(14.77);
      result = StackConversions.cmdFraction(stk, "32");
      assertEquals("14 25/32", result[3]);

      // Test negative numbers with different bases
      stk.clear();
      stk.push(-123.456);
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-123 1/2", result[3]);

      stk.push(-123.456);
      result = StackConversions.cmdFraction(stk, "");
      assertEquals("-123 29/64", result[3]);

      // -41 33/34
      stk.push(-41.970588235);
      result = StackConversions.cmdFraction(stk, "2");
      assertEquals("-42", result[3]);

      // -12.123123123123123
      stk.push(-12.123123123123123);
      result = StackConversions.cmdFraction(stk, "64");
      assertEquals("-12 1/8", result[3]);
      result = StackConversions.cmdFraction(stk, "8");
      assertEquals("-12 1/8", result[3]);

      stk.clear();
      stk.push(1.2e12 / 2.2e14);
      assertEquals("0.005454545454545455", stk.peek().toString());
      result = StackConversions.cmdFraction(stk, "1024");
      assertEquals("0 3/512", result[3]);

   }

}
