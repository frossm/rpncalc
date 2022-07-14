# Trigonometry Functions
|Command|Description|
|-------|-----------|
|sin, cos, tan [rad]|Calculate the [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Angles are input as degrees by default unless the **rad** parameter is given in which case the angles will be in [radians](https://en.wikipedia.org/wiki/Radian).  Example: `tan` will calculate the tangent using row 1 as the angle in degrees.  Use `tan rad` if row 1 contains the angle in radians|
|asin, acos, atan [rad]|Calculate the arc [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Result is returned in degrees unless **rad** parameter is provided|
|hypot <br> hypotenuse| Assumes the top two stack items are the right triangles legs and this function returns the hypotenuse using the [Pythagorean theorem](https://en.wikipedia.org/wiki/Pythagorean_theorem).  Specifically, it returns SQRT( (line1)^2 + (line2)^2 )|