/**************************************************************************************************************
 * Library Project
 *
 *  Library holds methods and classes frequently used by my programs.
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
 ***************************************************************************************************************/
package org.fross.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class FormatTest {

   /**
    * Test method for {@link org.fross.library.Format#Comma(java.lang.Double)}.
    */
   @Test
   void testCommaDouble() {
      assertEquals("1,234,567.01", Format.Comma(1234567.01));
      assertEquals("999.9999", Format.Comma(999.9999));
      assertEquals(".003", Format.Comma(.003));
      assertEquals("1,000.00", Format.Comma(Double.valueOf(1000)));
      assertEquals("-.123456", Format.Comma(-0.123456));
   }

   /**
    * Test method for {@link org.fross.library.Format#Comma(java.lang.Long)}.
    */
   @Test
   void testCommaLong() {
      assertEquals("1,234,567", Format.Comma(1234567L));
      assertEquals("999", Format.Comma(999L));
      assertEquals("123,456,789", Format.Comma(123456789L));
      assertEquals("1,000", Format.Comma(1000L));
   }

   /**
    * Test Comma when sending a string
    */
   @Test
   void testCommaString() {
      assertEquals("1,234,567.00", Format.Comma("1234567"));
      assertEquals("999.00", Format.Comma("999"));
      assertEquals("963.11", Format.Comma("963.11"));
      assertEquals("5,123.11", Format.Comma("5123.11"));
      assertEquals(".123456", Format.Comma(".123456"));
      assertEquals("-.123456", Format.Comma("-.123456"));
      assertEquals("-12,345,678.123456", Format.Comma("-12345678.123456"));
   }

   /**
    * Test method for {@link org.fross.library.Format#humanReadableBytes(long)}.
    */
   @Test
   void testHumanReadableBytes() {
      assertEquals("1.235 GB", Format.humanReadableBytes(1234567890));
      assertEquals("1.000 KB", Format.humanReadableBytes(1000));
      assertEquals("4.096 MB", Format.humanReadableBytes(4096000));
   }

   /**
    * Test method for {@link org.fross.library.Format#CenterText(int, java.lang.String, java.lang.String, java.lang.String)}.
    */
   @Test
   void testCenterTextIntStringStringString() {
      String in = "Even Length String";
      String out = "-Even Length String+";

      assertEquals(out, Format.CenterText(20, in, "-", "+"));

      in = "Odd Length String";
      out = "-   Odd Length String   +";
      assertEquals(out, Format.CenterText(25, in, "-", "+"));
   }

   /**
    * Test method for {@link org.fross.library.Format#CenterText(int, java.lang.String)}.
    */
   @Test
   void testCenterTextInString() {
      String in = "Even Length String";
      String out = " Even Length String ";

      assertEquals(out, Format.CenterText(20, in));

      in = "Odd Length String";
      out = "    Odd Length String    ";
      assertEquals(out, Format.CenterText(25, in));
   }

   /**
    * Test CenterTextSpacesToAdd
    */
   @Test
   void testCenterTextSpacesToAdd() {
      assertEquals(20, Format.CenterTextSpacesToAdd(80,
            "This is a fairly large even sized String", "", ""));

      assertEquals(4, Format.CenterTextSpacesToAdd(80,
            "It's a dog eat dog world out there, and I'm wearing milk bone underwear", "", ""));

      assertEquals(35, Format.CenterTextSpacesToAdd(80,
            "Hi There!", "", ""));

      assertEquals(-23, Format.CenterTextSpacesToAdd(25,
            "It's a dog eat dog world out there, and I'm wearing milk bone underwear", "", ""));

   }


   /**
    * Test method for {@link org.fross.library.Format#Capitalize(java.lang.String)}.
    */
   @Test
   void testCapitalize() {
      assertEquals("CapitalizeD first CHAracter", Format.Capitalize("capitalizeD first CHAracter"));
   }

}
