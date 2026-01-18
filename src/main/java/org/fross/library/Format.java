/* ------------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------------*/
package org.fross.library;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Format {

	/**
	 * Comma Return a string with comma separators at the correct intervals. Supports decimal places and a negative sign.
	 * 
	 * @param num - Number to comma-ize
	 * @return
	 */
	public static String Comma(Double num) {
		DecimalFormat df = null;

		try {
			df = new DecimalFormat("#,###.00#######");
		} catch (Exception Ex) {
			Output.printColorln(Output.RED, "ERROR Adding Commas to numbers:\n" + Ex.getMessage());
		}

		return (String.valueOf(df.format(num)));
	}

	/**
	 * Comma Return a string with comma separators at the correct intervals. Supports decimal places and a negative sign.
	 * 
	 * @param num - Number to comma-ize
	 * @return
	 */
	public static String Comma(Long num) {
		return NumberFormat.getInstance().format(num);
	}
	
	/**
	 * Comma Return a string with comma separators at the correct intervals. Supports decimal places and a negative sign.
	 * 
	 * @param num - Number to comma-ize
	 * @return
	 */
	public static String Comma(String num) {
		DecimalFormat df = null;

		try {
			df = new DecimalFormat("#,###.00#######");
		} catch (Exception Ex) {
			Output.printColorln(Output.RED, "ERROR Adding Commas to numbers:\n" + Ex.getMessage());
		}

		return (String.valueOf(df.format(new BigDecimal(num))));
	}


	/**
	 * HumanReadableBytes(): Take a long number in bytes and return a more human readable format Reference:
	 * https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
	 * https://www.geeksforgeeks.org/how-to-calculate-log-base-2-of-an-integer-in-java/?ref=leftbar-rightbar
	 * 
	 * @param numInBytes
	 * @return String
	 */
	public static String humanReadableBytes(long numInBytes) {
		String[] descriptor = { "K", "M", "G", "T", "P", "E" };
		int unit = 1000; // As opposed to 1024

		// If the size is < than 1k, just return as bytes
		if (numInBytes < unit) {
			return numInBytes + "  B";
		}

		// Math.log uses base 10. You have to divide to get the base we want..in this case unit
		// exp is the number of times you have to multiply a base to get the inputed number
		int exp = (int) (Math.log(numInBytes) / Math.log(unit));

		// Determine the descriptor to use based on the log
		String prefix = descriptor[exp - 1];

		// Create the string and return it
		return String.format("%.3f %sB", numInBytes / Math.pow(unit, exp), prefix);
	}

	/**
	 * CenterText(): Center the text within the width provided adding the pre and post string. Returns a string that can be
	 * printed.
	 * 
	 * Approach: ((Width - length of text - length of pre - length of post) / 2 ) = spaces to add at the front & back
	 * 
	 * @param width
	 * @param lineToCenter
	 * @param pre
	 * @param post
	 * @return
	 */
	public static String CenterText(int width, String lineToCenter, String pre, String post) {
		String result;

		int spacesToAdd = ((width - lineToCenter.length() - pre.length() - post.length()) / 2);
		result = pre + " ".repeat(spacesToAdd) + lineToCenter + " ".repeat(spacesToAdd) + post;

		return (result);
	}

	/**
	 * CenterTextSpacesToAdd(): Return the number of spaced to add at the start and end of the text to center it
	 * 
	 * @param width
	 * @param lineToCenter
	 * @param pre
	 * @param post
	 * @return
	 */
	public static int CenterTextSpacesToAdd(int width, String lineToCenter, String pre, String post) {
		int spacesToAdd = ((width - lineToCenter.length() - pre.length() - post.length()) / 2);
		return (spacesToAdd);
	}

	/**
	 * CenterText(): Center the text within the width provided. If no pre and post are provided, assume spaces
	 * 
	 * @param width
	 * @param lineToCenter
	 * @return
	 */
	public static String CenterText(int width, String lineToCenter) {
		return CenterText(width, lineToCenter, " ", " ");
	}

	/**
	 * Capitalize(): Return the provided string with the first character capitalized
	 * 
	 * @param str
	 * @return
	 */
	public static String Capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

}
