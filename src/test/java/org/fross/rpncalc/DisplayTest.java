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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisplayTest {
   @Test
   void testComma() {
      assertEquals("1", Display.Comma(new BigDecimal("1")));
      assertEquals("1.01", Display.Comma(new BigDecimal("1.01")));
      assertEquals("-2.000000001231", Display.Comma(new BigDecimal("-2.000000001231")));
      assertEquals("10,001", Display.Comma(new BigDecimal("10001")));
      assertEquals("-123,456,789.987654321", Display.Comma(new BigDecimal("-123456789.987654321")));
      assertEquals("-0.1234567890123456789", Display.Comma(new BigDecimal("-0.1234567890123456789")));
      assertEquals("-1,987,654,321,987,654,321", Display.Comma(new BigDecimal("-1987654321987654321")));
   }

   @Test
   void testQueryDecimalIndex() {
      assertEquals(6, Display.queryDecimalIndex("50,000"));
      assertEquals(1, Display.queryDecimalIndex("1"));
      assertEquals(1, Display.queryDecimalIndex("1.01"));
      assertEquals(5, Display.queryDecimalIndex("-1234.4321"));
      assertEquals(0, Display.queryDecimalIndex(".00001"));
      assertEquals(18, Display.queryDecimalIndex("-1,123,454,678,999.1234532643"));
      assertEquals(5, Display.queryDecimalIndex("-8e21"));
      assertEquals(1, Display.queryDecimalIndex("1.02e+22"));
      assertEquals(6, Display.queryDecimalIndex("-1e+22"));
   }

}
