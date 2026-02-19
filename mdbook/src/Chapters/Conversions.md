<img align="right" width="125" src="../Images/Conversion.png">

## Fractional Display

The RPNCalc stack only contains decimal numbers ([BigDecimal](https://www.geeksforgeeks.org/java/bigdecimal-class-java/)<sup>**_[1]_**</sup> format for you Java developers.)
Therefore, we can't directly store fractional values on the stack. If a
fraction is entered, it is converted to a decimal. There could be a loss of precision when this is done. For example, there is no exact fractional equivalent for `PI` much
like there is no exact decimal equivalent for `1/3`.
However, the difference is usually so small that it's acceptable, especially since I'm using [BigDecimal](https://www.geeksforgeeks.org/java/bigdecimal-class-java/).

The `frac [base]` command takes the item on the top of the stack (`line1`) and displays the approximate fractional equivalent.  `[base]` sets the precision of the calculation.
If a `base` is not provided, RPNCalc will use `64` (1/64) as the default.

**For example**, if you had `1.1234` on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  No base was given so it would have used a base of 64 (which means
maximum granularity would be 1/64) and auto reduced the result which is why you get the `1 1/8`.

if `frac 5` would have been entered (which means 1/5 is maximum granularity), you get `1.1234 is approximately 1 1/5`.

Please note that while fractional display *is* a conversion, it does not change the stack. It only displays the fractional equivilents. Therefore, it does not require the
`convert`
command. It instead simply uses the `frac` command.

## Unit Conversions

The conversion command will simply convert from one unit to another. As an example, I frequently use RPNCalc to convert from inches to millimeters or back. The conversion
module requires an amount, normally pulled from the stack, as well as a **"from"** unit and a **"to"** unit. The supported units are listed below. You an only convert units
within the same categories. For example, you can't convert `grams` to `miles` (obviously). The converter will take the last item off of the stack (`line1`) and replace it
with the
converted value.

From within RPNCalc, the `convertunits` command (see below) will display the same list of supported conversion units as displayed below. It's much easier to display that
rather
than
come to this User Guide.

Convert will remove the top item on the stack (`line1`), perform the conversion from the `FromUnit` to the `ToUnit` and add it back to the stack. `conv` can also be used
as an abbreviation for `convert`.

**Usage:**<br>
convert _[amount]_ `FromUnit` `ToUnit`

The optional `amount` parameter can be used directly instead of having the amount pulled from the top of the stack. This is just a shortcut, but I find it convenient to simply
type:<br>
convert 11.125 in mm

If you perform a certain conversion often, you might want to create a User Defined Function (`UDF`). For example, you can create a UDF called `c2f` to perform `convert c f`.
See the User Defined Function chapter for additional information on recording, saving, and using your shiny new function.

I've included a fairly comprehensive list of common units, but I'm happy to include others if anyone has other ideas.  [just let me know]
(mailto://rpncalc@fross.org).

**Note:** All conversions are exact by definition except angle conversions. Unit abbreviations are case-insensitive.

**Examples of Usage:** <br>

- convert in mm
- con f c
- convert decimal %
- con 11.125 oz g

## Supported Unit Conversions

The RPNCalc command `convertunits` will display, within the program, the supported conversion units.

| **Category**    | **Unit**     | **Abbreviation** | **Notes**                 |
|-----------------|--------------|------------------|---------------------------|
| **Length**      | Millimeter   | mm               | Metric                    |
|                 | Centimeter   | cm               | Metric                    |
|                 | Meter        | m                | Metric (base unit)        |
|                 | Kilometer    | km               | Metric                    |
|                 | Inch         | in               | Imperial                  |
|                 | Foot         | ft               | Imperial                  |
|                 | Yard         | yd               | Imperial                  |
|                 | Mile         | mi               | Imperial                  |
| **Mass**        | Milligram    | mg               | Metric                    |
|                 | Gram         | g                | Metric                    |
|                 | Kilogram     | kg               | Metric (base unit)        |
|                 | Metric Ton   | tonne            | Metric                    |
|                 | Ounce        | oz               | Imperial                  |
|                 | Pound        | lb               | Imperial                  |
|                 | US Short Ton | ton              | Imperial                  |
| **Temperature** | Celsius      | c                |                           |
|                 | Fahrenheit   | f                |                           |
|                 | Kelvin       | k                |                           |
| **Time**        | Millisecond  | ms               |                           |
|                 | Second       | s                | Base unit                 |
|                 | Minute       | min              |                           |
|                 | Hour         | hr               |                           |
|                 | Day          | day              |                           |
|                 | Week         | week             |                           |
| **Volume**      | Milliliter   | ml               | Metric                    |
|                 | Liter        | l                | Metric (base unit)        |
|                 | Fluid Ounce  | floz             | US Liquid                 |
|                 | Cup          | cup              | US Liquid                 |
|                 | Pint         | pt               | US Liquid                 |
|                 | Quart        | qt               | US Liquid                 |
|                 | Gallon       | gal              | US Liquid                 |
| **Angle**       | Radian       | rad              | Base unit                 |
|                 | Degree       | deg              | π/180 (50 decimal places) |
| **Percentage**  | Number       | num              | Base unit (e.g., 0.5)     |
|                 | Percent      | percent, %       | e.g. convert 50 %         |

**[1]**
Java's BigDecimal is a special tool in programming used to handle numbers that require 100% precision, especially when dealing with money or high-accuracy scientific
calculations. Unlike standard number types (float or double) which can make tiny, accidental rounding errors, BigDecimal guarantees that the number you see is the exact number
stored.

The BigDecimal class provides operations on double numbers for arithmetic, scale handling, rounding, comparison, format conversion and hashing. It can handle very
large and
very small floating point numbers with great precision but compensating with the time complexity a bit. A BigDecimal consists of a random precision integer unscaled value
and a 32-bit integer scale. If greater than or equal to zero, the scale is the number of digits to the right of the decimal point. If less than zero, the unscaled value of
the number is multiplied by 10^(-scale).