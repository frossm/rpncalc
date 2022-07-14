# Conversions

The conversion commands make assumption as to the unit of the numbers and will convert to another unit base.  The example I use this for mostly, is inch to millimeters and the reverse.  I can easily add additional conversions if requested

|Command|Description|
|-------|-----------|
|frac [base]|Display a fractional estimate of the last stack item with the maximum granularity of 1/base.  The default base is 64 which corresponds to 1/64th.  Only decimals are stored on the stack but this command will display the results.  For example, if you had **1.1234** on the stack, `frac` would show you `1.1234 is approximately 1 1/8`  It would have used a base of 64 (which means maximum granularity would be 1/64.  However, it auto reduces which is why you get the `1 1/8`. if you entered `frac 2` (which means 1/2 is maximum granularity, you get `1.1234 is approximately 1 0/1` or just one.  Need to fix that display oddity.  See above for a more detailed description|
|in2mm|Converts the value in line1 from inches to millimeters. `2mm` command will also work|
|mm2in|Converts the value in line1 from millimeters to inches. `2in` command will also work|
|deg2rad|Convert line1 from degrees into [radians](https://en.wikipedia.org/wiki/Radian). `2rad` would also work|
|rad2deg|Convert line1 from radians into degrees.  `2deg` would also work|