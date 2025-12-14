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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Fross (michael@fross.org)
 */
class StackConversionsTest {
   /**
    * Test converting from a percent to a number
    * Example: 34.5% -> .345
    */
   @Test
   void testCmdFromPercent() {
      StackObj stk = new StackObj();

      stk.push("2.34");
      StackConversions.cmdFromPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("0.02340", stk.pop().toString());

      stk.push(-44.9873);
      StackConversions.cmdFromPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("-0.44987", stk.pop().toString());

      stk.push(-154.321E10);
      StackConversions.cmdFromPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("-15432100000.00000", stk.pop().toString());

      stk.push(1.1E6);
      StackConversions.cmdFromPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("11000.00000", stk.pop().toString());
   }

   /**
    * Test converting from a number to a percent
    * Example: .345 -> 34.5%
    */
   @Test
   void testCmdToPercent() {
      StackObj stk = new StackObj();

      stk.push(71.2345);
      StackConversions.cmdToPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(7123.45, stk.pop().doubleValue());

      stk.push(-44.987);
      StackConversions.cmdToPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-4498.7, stk.pop().doubleValue());

      stk.push(-154.321E10);
      StackConversions.cmdToPercent(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("-154321000000000.00000", stk.pop().toString());

      stk.push(1.1E6);
      StackConversions.cmdToPercent(stk);
      assertEquals("110000000.0", stk.pop().toString());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackConversions#cmdIn2Mm(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdConvertIN2MM() {
      StackObj stk = new StackObj();

      stk.push(31.6);
      StackConversions.cmdIn2Mm(stk);
      assertEquals(802.64, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(1.234e12);
      StackConversions.cmdIn2Mm(stk);
      assertEquals("3.13436E+13", stk.peek().toString());
      assertEquals(2, stk.size());

   }

   /**
    * Test method for {@link org.fross.rpncalc.StackConversions#cmdMm2In(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdConvertMM2IN() {
      StackObj stk = new StackObj();

      stk.push(666.0);
      StackConversions.cmdMm2In(stk);
      StackCommands.cmdRound(stk, "4");
      assertEquals(26.2205, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(1.234e12);
      StackConversions.cmdMm2In(stk);
      StackCommands.cmdRound(stk, "10");
      assertEquals("48582677165.3543307087", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackConversions#cmdRad2Deg(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdRad2Deg() {
      StackObj stk = new StackObj();

      stk.push(4.321);
      StackConversions.cmdRad2Deg(stk);
      StackCommands.cmdRound(stk, "4");
      assertEquals(247.5751, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(1.234e12);
      StackConversions.cmdRad2Deg(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("70702991919143.58288", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackConversions#cmdDeg2Rad(org.fross.rpncalc.StackObj)}.
    */
   @Test
   void testCmdDeg2Rad() {
      StackObj stk = new StackObj();

      stk.push(189.6);
      StackConversions.cmdDeg2Rad(stk);
      StackCommands.cmdRound(stk, "4");
      assertEquals(3.3091, stk.peek().doubleValue());
      assertEquals(1, stk.size());

      stk.push(1.234e12);
      StackConversions.cmdDeg2Rad(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals("21537362969.61003", stk.peek().toEngineeringString());
      assertEquals(2, stk.size());
   }

   /**
    * Test method for {@link org.fross.rpncalc.StackConversions#cmdFraction(org.fross.rpncalc.StackObj, java.lang.String)}.
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

   /**
    * Test Grams to Ounces conversion
    */
   @Test
   void testGram2Oz() {
      StackObj stk = new StackObj();

      stk.push(1234.567);
      StackConversions.cmdGram2Oz(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(43.54807, stk.pop().doubleValue());

      stk.push(-22.2);
      StackConversions.cmdGram2Oz(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-0.78308, stk.pop().doubleValue());

      stk.push(44.55e12);
      StackConversions.cmdGram2Oz(stk);
      StackCommands.cmdRound(stk, "2");
      assertEquals("1571455004853.81", stk.pop().toEngineeringString());
   }

   /**
    * Test Ounces to grams conversion
    */
   @Test
   void testOz2Gram() {
      StackObj stk = new StackObj();

      stk.push(12.345);
      StackConversions.cmdOz2Gram(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(349.97486, stk.pop().doubleValue());

      stk.push(-0.0221);
      StackConversions.cmdOz2Gram(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-0.62652, stk.pop().doubleValue());

      stk.push(44.55e12);
      StackConversions.cmdOz2Gram(stk);
      StackCommands.cmdRound(stk, "2");
      assertEquals("1262971255218750.00", stk.pop().toEngineeringString());
   }

   /**
    * Test Kilograms to US pounds conversion
    */
   @Test
   void testKg2Lb() {
      StackObj stk = new StackObj();

      stk.push(123.321);
      StackConversions.cmdKg2Lb(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(271.87627, stk.pop().doubleValue());

      stk.push(-0.369);
      StackConversions.cmdKg2Lb(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-0.81351, stk.pop().doubleValue());

      stk.push(4.321e18);
      StackConversions.cmdKg2Lb(stk);
      StackCommands.cmdRound(stk, "2");
      assertEquals("9526174348797800000.00", stk.pop().toEngineeringString());

   }

   /**
    * Test US Pounds to Kilograms conversion
    */
   @Test
   void testLb2Kg() {
      StackObj stk = new StackObj();

      stk.push(456.654);
      StackConversions.cmdLb2Kg(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(207.13477, stk.pop().doubleValue());

      stk.push(-0.987654);
      StackConversions.cmdLb2Kg(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-0.44799, stk.pop().doubleValue());

      stk.push(4.321e18);
      StackConversions.cmdLb2Kg(stk);
      StackCommands.cmdRound(stk, "2");
      assertEquals("1959972630770000000.00", stk.pop().toEngineeringString());
   }

   /**
    * Test Inches to Feet conversion
    */
   @Test
   void testIn2Ft() {
      StackObj stk = new StackObj();

      stk.push(123.321);
      StackConversions.cmdIn2Ft(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(10.27675, stk.pop().doubleValue());

      stk.push(-50.987654);
      StackConversions.cmdIn2Ft(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-4.24897, stk.pop().doubleValue());
   }

   /**
    * Test Feet to Inches conversion
    */
   @Test
   void testFt2In() {
      StackObj stk = new StackObj();

      stk.push(7117.44);
      StackConversions.cmdFt2In(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(85409.28, stk.pop().doubleValue());

      stk.push(-32.0011);
      StackConversions.cmdFt2In(stk);
      StackCommands.cmdRound(stk, "5");
      assertEquals(-384.0132, stk.pop().doubleValue());
   }

   /**
    * Test Celsius to Fahrenheit Conversion and the Reverse
    */
   @Test
   void testTempConversions() {
      StackObj stk = new StackObj();
      Double[] testDataF = {
            -99.99999999000, -98.90000000000, -97.80000000000, -96.70000000000, -95.60000000000, -94.50000000000, -93.40000001000, -92.30000001000,
            -91.19999999000, -90.09999999000, -89.00000000000, -87.90000000000, -86.80000000000, -85.70000000000, -84.60000000000, -83.50000001000,
            -82.40000001000, -81.29999999000, -80.19999999000, -79.10000000000, -78.00000000000, -76.90000000000, -75.80000000000, -74.70000000000,
            -73.60000001000, -72.50000001000, -71.39999999000, -70.29999999000, -69.20000000000, -68.10000000000, -67.00000000000, -65.90000000000,
            -64.80000000000, -63.70000001000, -62.60000001000, -61.49999999000, -60.39999999000, -59.30000000000, -58.20000000000, -57.10000000000,
            -56.00000000000, -54.90000000000, -53.80000001000, -52.70000001000, -51.59999999000, -50.49999999000, -49.40000000000, -48.30000000000,
            -47.20000000000, -46.10000000000, -45.00000000000, -43.90000001000, -42.80000001000, -41.69999999000, -40.59999999000, -39.50000000000,
            -38.40000000000, -37.30000000000, -36.20000000000, -35.10000000000, -34.00000001000, -32.90000001000, -31.79999999000, -30.69999999000,
            -29.60000000000, -28.50000000000, -27.40000000000, -26.30000000000, -25.20000000000, -24.10000001000, -23.00000001000, -21.89999999000,
            -20.79999999000, -19.70000000000, -18.60000000000, -17.50000000000, -16.40000000000, -15.30000000000, -14.20000001000, -13.10000001000,
            -11.99999999000, -10.89999999000, -9.80000000000, -8.70000000000, -7.60000000000, -6.50000000000, -5.40000000000, -4.30000001000, -3.20000001000
            , -2.09999999000, -0.99999999000, 0.10000000000, 1.20000000000, 2.30000000000, 3.40000000000, 4.50000000000, 5.59999999000, 6.69999999000,
            7.80000001000, 8.90000001000, 10.00000000000, 11.10000000000, 12.20000000000, 13.30000000000, 14.40000000000, 15.49999999000, 16.59999999000,
            17.70000001000, 18.80000001000, 19.90000000000, 21.00000000000, 22.10000000000, 23.20000000000, 24.30000000000, 25.39999999000, 26.49999999000,
            27.60000001000, 28.70000001000, 29.80000000000, 30.90000000000, 32.00000000000, 33.10000000000, 34.20000000000, 35.29999999000, 36.39999999000,
            37.50000001000, 38.60000001000, 39.70000000000, 40.80000000000, 41.90000000000, 43.00000000000, 44.10000000000, 45.19999999000, 46.29999999000,
            47.40000001000, 48.50000001000, 49.60000000000, 50.70000000000, 51.80000000000, 52.90000000000, 54.00000000000, 55.09999999000, 56.19999999000,
            57.30000001000, 58.40000001000, 59.50000000000, 60.60000000000, 61.70000000000, 62.80000000000, 63.90000000000, 64.99999999000, 66.09999999000,
            67.20000001000, 68.30000001000, 69.40000000000, 70.50000000000, 71.60000000000, 72.70000000000, 73.80000000000, 74.89999999000, 75.99999999000,
            77.10000001000, 78.20000001000, 79.30000000000, 80.40000000000, 81.50000000000, 82.60000000000, 83.70000000000, 84.79999999000, 85.89999999000,
            87.00000001000, 88.10000001000, 89.20000000000, 90.30000000000, 91.40000000000, 92.50000000000, 93.60000000000, 94.69999999000, 95.79999999000,
            96.90000001000, 98.00000001000, 99.10000000000, 100.20000000000, 101.30000000000, 102.40000000000, 103.50000000000, 104.59999999000, 105.69999999000,
            106.80000001000, 107.90000001000, 109.00000000000, 110.10000000000, 111.20000000000, 112.30000000000, 113.40000000000, 114.49999999000, 115.59999999000,
            116.70000001000, 117.80000001000, 118.90000000000, 120.00000000000, 121.10000000000, 122.20000000000, 123.30000000000, 124.39999999000, 125.49999999000,
            126.60000001000, 127.70000001000, 128.80000000000, 129.90000000000, 131.00000000000, 132.10000000000, 133.20000000000, 134.29999999000, 135.39999999000,
            136.50000001000, 137.60000001000, 138.70000000000, 139.80000000000, 140.90000000000, 142.00000000000, 143.10000000000, 144.19999999000, 145.29999999000,
            146.40000001000, 147.50000001000, 148.60000000000, 149.70000000000, 150.80000000000, 151.90000000000, 153.00000000000, 154.09999999000, 155.19999999000,
            156.30000001000, 157.40000001000, 158.50000000000, 159.60000000000, 160.70000000000, 161.80000000000, 162.90000000000, 163.99999999000, 165.09999999000,
            166.20000001000, 167.30000001000, 168.40000000000, 169.50000000000, 170.60000000000, 171.70000000000, 172.80000000000, 173.89999999000, 174.99999999000,
            176.10000001000, 177.20000001000, 178.30000000000, 179.40000000000, 180.50000000000, 181.60000000000, 182.70000000000, 183.79999999000, 184.89999999000,
            186.00000001000, 187.10000001000, 188.20000000000, 189.30000000000, 190.40000000000, 191.50000000000, 192.60000000000, 193.69999999000, 194.79999999000,
            195.90000001000, 197.00000001000, 198.10000000000, 199.20000000000, 200.30000000000
      };
      Double[] testDataC = {
            -73.33333333, -72.72222222, -72.11111111, -71.5, -70.88888889, -70.27777778, -69.66666667, -69.05555556, -68.44444444, -67.83333333, -67.22222222,
            -66.61111111, -66.0, -65.38888889, -64.77777778, -64.16666667, -63.55555556, -62.94444444, -62.33333333, -61.72222222, -61.11111111, -60.5, -59.88888889,
            -59.27777778, -58.66666667, -58.05555556, -57.44444444, -56.83333333, -56.22222222, -55.61111111, -55.0, -54.38888889, -53.77777778, -53.16666667,
            -52.55555556, -51.94444444, -51.33333333, -50.72222222, -50.11111111, -49.5, -48.88888889, -48.27777778, -47.66666667, -47.05555556, -46.44444444,
            -45.83333333, -45.22222222, -44.61111111, -44.0, -43.38888889, -42.77777778, -42.16666667, -41.55555556, -40.94444444, -40.33333333, -39.72222222,
            -39.11111111, -38.5, -37.88888889, -37.27777778, -36.66666667, -36.05555556, -35.44444444, -34.83333333, -34.22222222, -33.61111111, -33.0, -32.38888889,
            -31.77777778, -31.16666667, -30.55555556, -29.94444444, -29.33333333, -28.72222222, -28.11111111, -27.5, -26.88888889, -26.27777778, -25.66666667,
            -25.05555556, -24.44444444, -23.83333333, -23.22222222, -22.61111111, -22.0, -21.38888889, -20.77777778, -20.16666667, -19.55555556, -18.94444444,
            -18.33333333, -17.72222222, -17.11111111, -16.5, -15.88888889, -15.27777778, -14.66666667, -14.05555556, -13.44444444, -12.83333333, -12.22222222,
            -11.61111111, -11.0, -10.38888889, -9.77777778, -9.16666667, -8.55555556, -7.94444444, -7.33333333, -6.72222222, -6.11111111, -5.5, -4.88888889, -4.27777778,
            -3.66666667, -3.05555556, -2.44444444, -1.83333333, -1.22222222, -0.61111111, 0.0, 0.61111111, 1.22222222, 1.83333333, 2.44444444, 3.05555556, 3.66666667,
            4.27777778, 4.88888889, 5.5, 6.11111111, 6.72222222, 7.33333333, 7.94444444, 8.55555556, 9.16666667, 9.77777778, 10.38888889, 11.0, 11.61111111, 12.22222222,
            12.83333333, 13.44444444, 14.05555556, 14.66666667, 15.27777778, 15.88888889, 16.5, 17.11111111, 17.72222222, 18.33333333, 18.94444444, 19.55555556,
            20.16666667, 20.77777778, 21.38888889, 22.0, 22.61111111, 23.22222222, 23.83333333, 24.44444444, 25.05555556, 25.66666667, 26.27777778, 26.88888889, 27.5,
            28.11111111, 28.72222222, 29.33333333, 29.94444444, 30.55555556, 31.16666667, 31.77777778, 32.38888889, 33.0, 33.61111111, 34.22222222, 34.83333333,
            35.44444444, 36.05555556, 36.66666667, 37.27777778, 37.88888889, 38.5, 39.11111111, 39.72222222, 40.33333333, 40.94444444, 41.55555556, 42.16666667,
            42.77777778, 43.38888889, 44.0, 44.61111111, 45.22222222, 45.83333333, 46.44444444, 47.05555556, 47.66666667, 48.27777778, 48.88888889, 49.5, 50.11111111,
            50.72222222, 51.33333333, 51.94444444, 52.55555556, 53.16666667, 53.77777778, 54.38888889, 55.0, 55.61111111, 56.22222222, 56.83333333, 57.44444444,
            58.05555556, 58.66666667, 59.27777778, 59.88888889, 60.5, 61.11111111, 61.72222222, 62.33333333, 62.94444444, 63.55555556, 64.16666667, 64.77777778,
            65.38888889, 66.0, 66.61111111, 67.22222222, 67.83333333, 68.44444444, 69.05555556, 69.66666667, 70.27777778, 70.88888889, 71.5, 72.11111111, 72.72222222,
            73.33333333, 73.94444444, 74.55555556, 75.16666667, 75.77777778, 76.38888889, 77.0, 77.61111111, 78.22222222, 78.83333333, 79.44444444, 80.05555556,
            80.66666667, 81.27777778, 81.88888889, 82.5, 83.11111111, 83.72222222, 84.33333333, 84.94444444, 85.55555556, 86.16666667, 86.77777778, 87.38888889, 88.0,
            88.61111111, 89.22222222, 89.83333333, 90.44444444, 91.05555556, 91.66666667, 92.27777778, 92.88888889, 93.5
      };

      //==================================================
      // C2F
      //==================================================
      // Add the test values to the stack & test
      for (int i = 0; i < 274; i++) {
         // Push C to the stack and round
         stk.push(testDataC[i]);
         StackCommands.cmdRound(stk, "8");

         // Convert & round
         StackConversions.cmdC2F(stk);
         StackCommands.cmdRound(stk, "8");

         // Test
         assertEquals(stk.pop().doubleValue(), testDataF[i]);
      }

      //==================================================
      // F2C
      //==================================================
      // Add the test values to the stack & test
      stk.clear();
      for (int i = 0; i < 274; i++) {
         // Push F to the stack and round
         stk.push(testDataF[i]);
         StackCommands.cmdRound(stk, "8");

         // Convert & round
         StackConversions.cmdF2C(stk);
         StackCommands.cmdRound(stk, "8");

         // Test
         assertEquals(stk.pop().doubleValue(), testDataC[i]);
      }
   }
}
