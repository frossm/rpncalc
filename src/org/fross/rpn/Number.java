package org.fross.rpn;

import java.util.Stack;
import java.text.DecimalFormat;

/**
 * Perform Math functions for the calculator
 * @author michael.d.fross
 */
public class Number {

   // Length of number displayed when in Money Mode
   public static final int MONEYMODELENGTH = 12;

   /**
    * Perform Simple math functions of Addition, Subtraction, Multiplication,
    * Division, and Power.  All are static functions.  The function is sent the
    * operand (char) and a stack with the top two items to be processed.
    * @param Operand
    * @param Stk
    * @return
    */
   public static Stack SimpleMath(char Operand, Stack Stk) {
      double Num1 = Double.valueOf(Stk.pop().toString());
      double Num2 = Double.valueOf(Stk.pop().toString());

      Main.DebugPrint("DEBUG:  Operand Entered: '" + Operand + "'");
      Main.DebugPrint("DEBUG:  Num1 Popped:     '" + Num1 + "'");
      Main.DebugPrint("DEBUG:  Num2 Popped:     '" + Num2 + "'");

      switch (Operand) {
         case '+':
            Stk.push(Num1 + Num2);
            break;

         case '-':
            Stk.push(Num2 - Num1);
            break;

         case '*':
            Stk.push(Num2 * Num1);
            break;

         case '/':
            Stk.push(Num2 / Num1);
            break;

         case '^':
            Stk.push(Math.pow(Num1, Num2));
            break;
      }
      return Stk;
   }

   /**
    * Return a string with comma separators at the correct intervals
    * @param num
    * @return
    */
   public static String Comma(String num) {
      DecimalFormat myFormatter = null;

      try {
         myFormatter = new DecimalFormat("###,###.########");
      } catch (Exception Ex) {
         System.out.println("ERROR Adding Commas to numbers:\n" + Ex.getMessage());
      }

      return (myFormatter.format(Double.valueOf(num)));
   }

   /**
    * Returns a string in "Money" format.  Dollar sign, commas and
    * two decimals places in addition to being right justified.
    */
   public static String MoneyFormat(String num) {
      DecimalFormat myFormatter = null;

      try {
         myFormatter = new DecimalFormat("#,###.00");
      } catch (Exception Ex) {
         System.out.println("ERROR Adding Commas to numbers:\n" + Ex.getMessage());
      }

      // Pad the beginning so that the number is Right justified.
      String Result = myFormatter.format(Double.valueOf(num));
      while (Result.length() < MONEYMODELENGTH) {
         Result = " " + Result;
      }

      return ("$ " + Result);
   }
}
