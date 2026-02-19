<img align="right" width="125" src="../Images/Trig.png">

## Trigonometry Functions

RPNCalc does not consider itself a fully fledged scientific calculator (at least not currently). However, it's certainly beyond a very basic calculator. The trigonometry
commands listed here are basic, but do fill a need if you need to do a few basic calculations.

Lastly, I'm happy to add more capabilities if there is a desire (and an offer to help test).

## Command Table

| <div style="width:90px">Command</div>  | Description                                                                                                                                                                                                                                                                                                                                                                                                                |
|----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| sin [rad]<br>cos [rad]<br>tan [rad]    | Calculate the [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function requested.  Angles are input as degrees by default unless the optional `rad` parameter is given in which case the angles will be in [radians](https://en.wikipedia.org/wiki/Radian).<br><br>**Example:** `tan` will calculate the tangent using `line1` as the angle in degrees.  Use `tan rad` if `line1` contains the angle in radians |
| asin [rad]<br>acos [rad]<br>atan [rad] | Calculate the arc [trigonometry](https://en.wikipedia.org/wiki/Trigonometry) function.  Like the above, the result is returned in degrees unless `rad` parameter is provided                                                                                                                                                                                                                                               |
| hypot<br>hypotenuse                    | Assumes the top two stack items are the right triangle' legs and this function returns the hypotenuse using the [Pythagorean theorem](https://en.wikipedia.org/wiki/Pythagorean_theorem).  Specifically, it returns SQRT( (`line1`^2 + `line2`^2 )).  The hypotenuse will replace the values of the two legs on the stack                                                                                                  |
