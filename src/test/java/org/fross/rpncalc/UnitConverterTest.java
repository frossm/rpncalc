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
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Comprehensive test suite for UnitConverter
 * Tests all supported unit conversions for accuracy
 */
public class UnitConverterTest {

   private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

   // Helper method to assert BigDecimal values are equal within tolerance
   private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual, String message) {
      assertBigDecimalEquals(expected, actual, new BigDecimal("0.0000000001"), message);
   }

   private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual, BigDecimal tolerance, String message) {
      BigDecimal difference = expected.subtract(actual).abs();
      assertTrue(difference.compareTo(tolerance) <= 0,
            message + " Expected: " + expected + ", Actual: " + actual + ", Difference: " + difference);
   }

   // LENGTH CONVERSIONS

   @Test
   @DisplayName("Length: Metric conversions")
   public void testLengthMetric() {
      // mm to cm
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("10"), "mm", "cm");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "10mm should equal 1cm");

      // cm to m
      result = UnitConverter.convert(new BigDecimal("100"), "cm", "m");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "100cm should equal 1m");

      // m to km
      result = UnitConverter.convert(new BigDecimal("1000"), "m", "km");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000m should equal 1km");

      // km to mm
      result = UnitConverter.convert(new BigDecimal("1"), "km", "mm");
      assertBigDecimalEquals(new BigDecimal("1000000"), result.getValue(), "1km should equal 1,000,000mm");
   }

   @Test
   @DisplayName("Length: Imperial conversions")
   public void testLengthImperial() {
      // in to ft
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("12"), "in", "ft");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "12in should equal 1ft");

      // ft to yd
      result = UnitConverter.convert(new BigDecimal("3"), "ft", "yd");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "3ft should equal 1yd");

      // yd to mi
      result = UnitConverter.convert(new BigDecimal("1760"), "yd", "mi");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1760yd should equal 1mi");

      // mi to in
      result = UnitConverter.convert(new BigDecimal("1"), "mi", "in");
      assertBigDecimalEquals(new BigDecimal("63360"), result.getValue(), "1mi should equal 63,360in");
   }

   @Test
   @DisplayName("Length: Metric to Imperial")
   public void testLengthMetricToImperial() {
      // 1 inch = 2.54 cm (exact)
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("2.54"), "cm", "in");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "2.54cm should equal 1in");

      // 1 meter ≈ 3.280839895... feet
      result = UnitConverter.convert(new BigDecimal("1"), "m", "ft");
      assertBigDecimalEquals(new BigDecimal("3.280839895"), result.getValue(),
            new BigDecimal("0.0001"), "1m should equal ~3.28084ft");

      // 1 mile = 1.609344 km (exact)
      result = UnitConverter.convert(new BigDecimal("1.609344"), "km", "mi");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1.609344km should equal 1mi");
   }

   // MASS CONVERSIONS

   @Test
   @DisplayName("Mass: Metric conversions")
   public void testMassMetric() {
      // mg to g
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("1000"), "mg", "g");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000mg should equal 1g");

      // g to kg
      result = UnitConverter.convert(new BigDecimal("1000"), "g", "kg");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000g should equal 1kg");

      // kg to tonne
      result = UnitConverter.convert(new BigDecimal("1000"), "kg", "tonne");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000kg should equal 1 tonne");
   }

   @Test
   @DisplayName("Mass: Imperial conversions")
   public void testMassImperial() {
      // oz to lb
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("16"), "oz", "lb");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "16oz should equal 1lb");

      // lb to ton
      result = UnitConverter.convert(new BigDecimal("2000"), "lb", "ton");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "2000lb should equal 1 ton");

      // ton to oz
      result = UnitConverter.convert(new BigDecimal("1"), "ton", "oz");
      assertBigDecimalEquals(new BigDecimal("32000"), result.getValue(), "1 ton should equal 32,000oz");
   }

   @Test
   @DisplayName("Mass: Metric to Imperial")
   public void testMassMetricToImperial() {
      // 1 lb = 453.59237 g (exact)
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("453.59237"), "g", "lb");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "453.59237g should equal 1lb");

      // 1 kg ≈ 2.204622621... lb
      result = UnitConverter.convert(new BigDecimal("1"), "kg", "lb");
      assertBigDecimalEquals(new BigDecimal("2.20462262"), result.getValue(),
            new BigDecimal("0.0001"), "1kg should equal ~2.20462lb");

      // 1 tonne ≈ 1.10231 ton
      result = UnitConverter.convert(new BigDecimal("1"), "tonne", "ton");
      assertBigDecimalEquals(new BigDecimal("1.10231"), result.getValue(),
            new BigDecimal("0.001"), "1 tonne should equal ~1.10231 ton");
   }

   // TEMPERATURE CONVERSIONS

   @Test
   @DisplayName("Temperature: Celsius conversions")
   public void testTemperatureCelsius() {
      // Freezing point
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("0"), "c", "f");
      assertBigDecimalEquals(new BigDecimal("32"), result.getValue(), "0°C should equal 32°F");

      result = UnitConverter.convert(new BigDecimal("0"), "c", "k");
      assertBigDecimalEquals(new BigDecimal("273.15"), result.getValue(), "0°C should equal 273.15K");

      // Boiling point
      result = UnitConverter.convert(new BigDecimal("100"), "c", "f");
      assertBigDecimalEquals(new BigDecimal("212"), result.getValue(), "100°C should equal 212°F");

      result = UnitConverter.convert(new BigDecimal("100"), "c", "k");
      assertBigDecimalEquals(new BigDecimal("373.15"), result.getValue(), "100°C should equal 373.15K");
   }

   @Test
   @DisplayName("Temperature: Fahrenheit conversions")
   public void testTemperatureFahrenheit() {
      // Freezing point
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("32"), "f", "c");
      assertBigDecimalEquals(new BigDecimal("0"), result.getValue(), "32°F should equal 0°C");

      result = UnitConverter.convert(new BigDecimal("32"), "f", "k");
      assertBigDecimalEquals(new BigDecimal("273.15"), result.getValue(), "32°F should equal 273.15K");

      // Boiling point
      result = UnitConverter.convert(new BigDecimal("212"), "f", "c");
      assertBigDecimalEquals(new BigDecimal("100"), result.getValue(), "212°F should equal 100°C");
   }

   @Test
   @DisplayName("Temperature: Kelvin conversions")
   public void testTemperatureKelvin() {
      // Absolute zero
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("0"), "k", "c");
      assertBigDecimalEquals(new BigDecimal("-273.15"), result.getValue(), "0K should equal -273.15°C");

      result = UnitConverter.convert(new BigDecimal("0"), "k", "f");
      assertBigDecimalEquals(new BigDecimal("-459.67"), result.getValue(), "0K should equal -459.67°F");

      // Room temperature
      result = UnitConverter.convert(new BigDecimal("293.15"), "k", "c");
      assertBigDecimalEquals(new BigDecimal("20"), result.getValue(), "293.15K should equal 20°C");
   }

   // TIME CONVERSIONS

   @Test
   @DisplayName("Time: Basic conversions")
   public void testTime() {
      // ms to s
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("1000"), "ms", "s");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000ms should equal 1s");

      // s to min
      result = UnitConverter.convert(new BigDecimal("60"), "s", "min");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "60s should equal 1min");

      // min to hr
      result = UnitConverter.convert(new BigDecimal("60"), "min", "hr");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "60min should equal 1hr");

      // hr to day
      result = UnitConverter.convert(new BigDecimal("24"), "hr", "day");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "24hr should equal 1day");

      // day to week
      result = UnitConverter.convert(new BigDecimal("7"), "day", "week");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "7 days should equal 1 week");

      // week to s
      result = UnitConverter.convert(new BigDecimal("1"), "week", "s");
      assertBigDecimalEquals(new BigDecimal("604800"), result.getValue(), "1 week should equal 604,800s");
   }

   // VOLUME CONVERSIONS

   @Test
   @DisplayName("Volume: Metric conversions")
   public void testVolumeMetric() {
      // ml to l
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("1000"), "ml", "l");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "1000ml should equal 1l");

      // l to ml
      result = UnitConverter.convert(new BigDecimal("1"), "l", "ml");
      assertBigDecimalEquals(new BigDecimal("1000"), result.getValue(), "1l should equal 1000ml");
   }

   @Test
   @DisplayName("Volume: US liquid conversions")
   public void testVolumeImperial() {
      // floz to cup
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("8"), "floz", "cup");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "8 fl oz should equal 1 cup");

      // cup to pt
      result = UnitConverter.convert(new BigDecimal("2"), "cup", "pt");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "2 cups should equal 1pt");

      // pt to qt
      result = UnitConverter.convert(new BigDecimal("2"), "pt", "qt");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "2pt should equal 1qt");

      // qt to gal
      result = UnitConverter.convert(new BigDecimal("4"), "qt", "gal");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "4qt should equal 1gal");

      // gal to floz
      result = UnitConverter.convert(new BigDecimal("1"), "gal", "floz");
      assertBigDecimalEquals(new BigDecimal("128"), result.getValue(), "1gal should equal 128 fl oz");
   }

   @Test
   @DisplayName("Volume: Metric to US liquid")
   public void testVolumeMetricToImperial() {
      // 1 gal = 3.785411784 l (exact)
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("3.785411784"), "l", "gal");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "3.785411784l should equal 1gal");

      // 1 l ≈ 33.8140227... fl oz
      result = UnitConverter.convert(new BigDecimal("1"), "l", "floz");
      assertBigDecimalEquals(new BigDecimal("33.8140227"), result.getValue(),
            new BigDecimal("0.001"), "1l should equal ~33.814 fl oz");
   }

   // ANGLE CONVERSIONS

   @Test
   @DisplayName("Angle: Degrees to Radians")
   public void testAngleDegToRad() {
      // 180° = π rad
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("180"), "deg", "rad");
      assertBigDecimalEquals(new BigDecimal("3.14159265358979"), result.getValue(),
            new BigDecimal("0.00000000000001"), "180° should equal π radians");

      // 90° = π/2 rad
      result = UnitConverter.convert(new BigDecimal("90"), "deg", "rad");
      assertBigDecimalEquals(new BigDecimal("1.57079632679490"), result.getValue(),
            new BigDecimal("0.00000000000001"), "90° should equal π/2 radians");

      // 360° = 2π rad
      result = UnitConverter.convert(new BigDecimal("360"), "deg", "rad");
      assertBigDecimalEquals(new BigDecimal("6.28318530717959"), result.getValue(),
            new BigDecimal("0.00000000000001"), "360° should equal 2π radians");
   }

   @Test
   @DisplayName("Angle: Radians to Degrees")
   public void testAngleRadToDeg() {
      // π rad = 180°
      UnitConverter.ConversionResult result = UnitConverter.convert(
            new BigDecimal("3.14159265358979"), "rad", "deg");
      assertBigDecimalEquals(new BigDecimal("180"), result.getValue(),
            new BigDecimal("0.00001"), "π radians should equal 180°");

      // 1 rad ≈ 57.2958°
      result = UnitConverter.convert(new BigDecimal("1"), "rad", "deg");
      assertBigDecimalEquals(new BigDecimal("57.2958"), result.getValue(),
            new BigDecimal("0.0001"), "1 radian should equal ~57.2958°");
   }

   // PERCENTAGE CONVERSIONS

   @Test
   @DisplayName("Percentage: Decimal to Percent")
   public void testPercentageDecimalToPercent() {
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("0.5"), "decimal", "percent");
      assertBigDecimalEquals(new BigDecimal("50"), result.getValue(), "0.5 should equal 50%");

      result = UnitConverter.convert(new BigDecimal("0.75"), "decimal", "%");
      assertBigDecimalEquals(new BigDecimal("75"), result.getValue(), "0.75 should equal 75%");

      result = UnitConverter.convert(new BigDecimal("1"), "decimal", "percent");
      assertBigDecimalEquals(new BigDecimal("100"), result.getValue(), "1 should equal 100%");

      result = UnitConverter.convert(new BigDecimal("0.125"), "decimal", "percent");
      assertBigDecimalEquals(new BigDecimal("12.5"), result.getValue(), "0.125 should equal 12.5%");
   }

   @Test
   @DisplayName("Percentage: Percent to Decimal")
   public void testPercentagePercentToDecimal() {
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("50"), "percent", "decimal");
      assertBigDecimalEquals(new BigDecimal("0.5"), result.getValue(), "50% should equal 0.5");

      result = UnitConverter.convert(new BigDecimal("25"), "%", "decimal");
      assertBigDecimalEquals(new BigDecimal("0.25"), result.getValue(), "25% should equal 0.25");

      result = UnitConverter.convert(new BigDecimal("100"), "percent", "decimal");
      assertBigDecimalEquals(new BigDecimal("1"), result.getValue(), "100% should equal 1");
   }

   // ERROR HANDLING TESTS

   @Test
   @DisplayName("Error: Unknown unit")
   public void testUnknownUnit() {
      assertThrows(IllegalArgumentException.class, () -> {
         UnitConverter.convert(new BigDecimal("1"), "xyz", "m");
      }, "Should throw exception for unknown unit");
   }

   @Test
   @DisplayName("Error: Incompatible units")
   public void testIncompatibleUnits() {
      assertThrows(IllegalArgumentException.class, () -> {
         UnitConverter.convert(new BigDecimal("1"), "m", "kg");
      }, "Should throw exception for incompatible units");

      assertThrows(IllegalArgumentException.class, () -> {
         UnitConverter.convert(new BigDecimal("1"), "c", "s");
      }, "Should throw exception for temperature to time conversion");
   }

   @Test
   @DisplayName("Return value includes correct unit")
   public void testReturnedUnit() {
      UnitConverter.ConversionResult result = UnitConverter.convert(new BigDecimal("100"), "cm", "m");
      assertEquals("m", result.getUnit(), "Returned unit should be 'm'");

      result = UnitConverter.convert(new BigDecimal("32"), "f", "c");
      assertEquals("c", result.getUnit(), "Returned unit should be 'c'");
   }

   @Test
   @DisplayName("Case insensitivity")
   public void testCaseInsensitivity() {
      UnitConverter.ConversionResult result1 = UnitConverter.convert(new BigDecimal("1"), "M", "CM");
      UnitConverter.ConversionResult result2 = UnitConverter.convert(new BigDecimal("1"), "m", "cm");

      assertBigDecimalEquals(result1.getValue(), result2.getValue(),
            "Case should not affect conversion results");
   }

   @Test
   @DisplayName("Bidirectional conversions are inverse")
   public void testBidirectionalConversions() {
      BigDecimal original = new BigDecimal("42.5");

      // m to ft and back
      UnitConverter.ConversionResult toFt = UnitConverter.convert(original, "m", "ft");
      UnitConverter.ConversionResult backToM = UnitConverter.convert(toFt.getValue(), "ft", "m");
      assertBigDecimalEquals(original, backToM.getValue(),
            new BigDecimal("0.00000001"), "m->ft->m should return to original");

      // kg to lb and back
      UnitConverter.ConversionResult toLb = UnitConverter.convert(original, "kg", "lb");
      UnitConverter.ConversionResult backToKg = UnitConverter.convert(toLb.getValue(), "lb", "kg");
      assertBigDecimalEquals(original, backToKg.getValue(),
            new BigDecimal("0.00000001"), "kg->lb->kg should return to original");

      // deg to rad and back
      UnitConverter.ConversionResult toRad = UnitConverter.convert(original, "deg", "rad");
      UnitConverter.ConversionResult backToDeg = UnitConverter.convert(toRad.getValue(), "rad", "deg");
      assertBigDecimalEquals(original, backToDeg.getValue(),
            new BigDecimal("0.00000001"), "deg->rad->deg should return to original");
   }

}