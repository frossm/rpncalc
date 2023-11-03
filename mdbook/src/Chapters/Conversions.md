<img align="right" width="125" src="../Images/Conversion.png">

# Conversions

The conversion commands will simply convert from one unit to another.  As an example, I frequently use RPNCalc to convert from inches to millimeters or back.

I've included the ones I use the most, but I'm happy to include others if you'd find something else useful.

## Fractional Display

The RPNCalc stack only contains decimal numbers.  Therefore, we can't directly store fractional values on the stack.  If a fraction is entered, it is converted to a decimal.  There could be a loss of precision when this is done.  For example, there is no exact fractional equivalent for `PI` much like there is no exact decimal equivalent for `1/3`.  However, the difference is usually so small that it's acceptable.

The `frac [base]` command takes the item on the top of the stack (`line1`) and displays the approximate fractional equivalent.  `[base]` sets the precision of the calculation. If a `base` is not provided, RPNCalc will use `64` (1/64) as the default.  

**For example**, if you had `1.1234` on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  No base was given so it would have used a base of 64 (which means maximum granularity would be 1/64) and auto reduced the result which is why you get the `1 1/8`. 

if `frac 5` would have been entered (which means 1/5 is maximum granularity), you get `1.1234 is approximately 1 1/5`.


|<div style="width:90px">Command</div>|Description|
|-------|-----------|
|to%|Converts `line1` from a "number" to a percent.  For example, `0.4455` becomes `44.55%`. This is simply done by multiplying the number by 100|
|from%|Converts `line1` from a percent to a "number".  For example, `93.124%` becomes `.93124`. This is simply done by multiplying the number by 0.01|
|frac [base]|Display a fractional estimate of the last stack item (`line1`) with the maximum granularity of 1/[base]. See the above description for more detail|
|in2mm|Converts the value in `line1` from inches to millimeters|
|mm2in|Converts the value in `line1` from millimeters to inches|
|deg2rad|Convert `line1` from degrees into [radians](https://en.wikipedia.org/wiki/Radian)|
|rad2deg|Convert `line1` from radians into degrees|
|gram2oz<br>grams2oz|Convert `line1` from grams into ounces using the constant of 0.035274 ounces / gram|
|oz2gram<br>oz2grams|Convert `line1` from ounces into grams using the constant of 28.349523125 grams / ounce|
|kg2lbs<br>kgs2lbs|convert `line1` from kilograms to US pounds using the constant of 2.2046226218 lbs/kg|
|lbs2kg<br>lbs2kgs|convert `line1` from US pounds using the constant of 0.45359237 kg/lbs|
