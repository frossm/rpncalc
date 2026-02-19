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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * UnitConverter - Converts between various units of measurement
 * Returns a ConversionResult containing the converted value and target unit
 */
public class UnitConverter {

   private static final MathContext MC = new MathContext(34, RoundingMode.HALF_UP);

   // Conversion factors to base units
   private static final Map<String, ConversionFactor> CONVERSIONS = new HashMap<>();

   static {
      // Length (base: meters) - All exact by definition
      addConversion("mm", "LENGTH", new BigDecimal("0.001"));
      addConversion("cm", "LENGTH", new BigDecimal("0.01"));
      addConversion("m", "LENGTH", BigDecimal.ONE);
      addConversion("km", "LENGTH", new BigDecimal("1000"));
      addConversion("in", "LENGTH", new BigDecimal("0.0254"));      // Exact: 1 in = 25.4 mm
      addConversion("ft", "LENGTH", new BigDecimal("0.3048"));      // Exact: 12 in
      addConversion("yd", "LENGTH", new BigDecimal("0.9144"));      // Exact: 3 ft
      addConversion("mi", "LENGTH", new BigDecimal("1609.344"));    // Exact: 5280 ft

      // Mass (base: kilograms) - All exact by definition
      addConversion("mg", "MASS", new BigDecimal("0.000001"));
      addConversion("g", "MASS", new BigDecimal("0.001"));
      addConversion("kg", "MASS", BigDecimal.ONE);
      addConversion("oz", "MASS", new BigDecimal("0.028349523125")); // Exact: 1/16 lb
      addConversion("lb", "MASS", new BigDecimal("0.45359237"));     // Exact by definition
      addConversion("ton", "MASS", new BigDecimal("907.18474"));     // Exact: 2000 lb (US short ton)
      addConversion("tonne", "MASS", new BigDecimal("1000"));        // Exact: Metric ton

      // Temperature (special handling required)
      addConversion("c", "TEMP", null);
      addConversion("f", "TEMP", null);
      addConversion("k", "TEMP", null);

      // Time (base: seconds)
      addConversion("ms", "TIME", new BigDecimal("0.001"));
      addConversion("s", "TIME", BigDecimal.ONE);
      addConversion("min", "TIME", new BigDecimal("60"));
      addConversion("hr", "TIME", new BigDecimal("3600"));
      addConversion("day", "TIME", new BigDecimal("86400"));
      addConversion("week", "TIME", new BigDecimal("604800"));

      // Volume (base: liters) - US liquid measures, exact by definition
      addConversion("ml", "VOLUME", new BigDecimal("0.001"));
      addConversion("l", "VOLUME", BigDecimal.ONE);
      addConversion("gal", "VOLUME", new BigDecimal("3.785411784"));       // Exact: 231 cubic inches
      addConversion("qt", "VOLUME", new BigDecimal("0.946352946"));        // Exact: 1/4 gallon
      addConversion("pt", "VOLUME", new BigDecimal("0.473176473"));        // Exact: 1/8 gallon
      addConversion("cup", "VOLUME", new BigDecimal("0.2365882365"));      // Exact: 1/16 gallon
      addConversion("floz", "VOLUME", new BigDecimal("0.0295735295625"));  // Exact: 1/128 gallon

      // Angle (base: radians) - deg uses high precision π/180
      addConversion("rad", "ANGLE", BigDecimal.ONE);
      // π/180 to 50 decimal places for maximum precision
      addConversion("deg", "ANGLE", new BigDecimal("0.01745329251994329576923690768488612713442871888542"));

      // Percentage (base: number)
      addConversion("num", "PERCENT", BigDecimal.ONE);
      addConversion("percent", "PERCENT", new BigDecimal("0.01"));
      addConversion("%", "PERCENT", new BigDecimal("0.01"));
   }

   private static void addConversion(String unit, String category, BigDecimal factor) {
      CONVERSIONS.put(unit.toLowerCase(), new ConversionFactor(category, factor));
   }

   /**
    * Convert a value from one unit to another
    *
    * @param amount   The value to convert
    * @param fromUnit The source unit
    * @param toUnit   The target unit
    * @return ConversionResult containing the converted value and unit
    * @throws IllegalArgumentException if units are incompatible or unknown
    */
   public static ConversionResult convert(BigDecimal amount, String fromUnit, String toUnit) {
      String from = fromUnit.toLowerCase().trim();
      String to = toUnit.toLowerCase().trim();

      ConversionFactor fromFactor = CONVERSIONS.get(from);
      ConversionFactor toFactor = CONVERSIONS.get(to);

      // Ensure that the units provided exist and are in the same category
      if (fromFactor == null) {
         throw new IllegalArgumentException("Unknown 'from' unit: " + fromUnit);
      }
      if (toFactor == null) {
         throw new IllegalArgumentException("Unknown 'to' unit: " + toUnit);
      }
      if (!fromFactor.category.equals(toFactor.category)) {
         throw new IllegalArgumentException("Incompatible units: " + fromUnit + " and " + toUnit);
      }

      BigDecimal result;

      // Special handling for temperature
      if (fromFactor.category.equals("TEMP")) {
         result = convertTemperature(amount, from, to);
      } else {
         // Convert to base unit, then to target unit
         BigDecimal inBase = amount.multiply(fromFactor.factor, MC);
         result = inBase.divide(toFactor.factor, MC);
      }

      return new ConversionResult(result, toUnit);
   }

   private static BigDecimal convertTemperature(BigDecimal value, String from, String to) {
      // Convert to Celsius first
      BigDecimal celsius;
      switch (from) {
         case "c":
            celsius = value;
            break;
         case "f":
            celsius = value.subtract(new BigDecimal("32")).multiply(new BigDecimal("5")).divide(new BigDecimal("9"), MC);
            break;
         case "k":
            celsius = value.subtract(new BigDecimal("273.15"));
            break;
         default:
            throw new IllegalArgumentException("Unknown temperature unit: " + from);
      }

      // Convert from Celsius to target
      switch (to) {
         case "c":
            return celsius;
         case "f":
            return celsius.multiply(new BigDecimal("9")).divide(new BigDecimal("5"), MC).add(new BigDecimal("32"));
         case "k":
            return celsius.add(new BigDecimal("273.15"));
         default:
            throw new IllegalArgumentException("Unknown temperature unit: " + to);
      }
   }

   /**
    * Result of a unit conversion
    */
   public static class ConversionResult {
      private final BigDecimal value;
      private final String unit;

      public ConversionResult(BigDecimal value, String unit) {
         this.value = value;
         this.unit = unit;
      }

      public BigDecimal getValue() {
         return value;
      }

      public String getUnit() {
         return unit;
      }

      @Override
      public String toString() {
         return value.toPlainString() + " " + unit;
      }
   }

   private static class ConversionFactor {
      final String category;
      final BigDecimal factor;

      ConversionFactor(String category, BigDecimal factor) {
         this.category = category;
         this.factor = factor;
      }
   }
}