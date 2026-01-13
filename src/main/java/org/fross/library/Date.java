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

import java.util.Calendar;

public class Date {
	static final String[] monthsLong = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
			"December" };
	static final String[] monthsShort = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	/**
	 * getCurrentMonth(): Return an integer value of the current month
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int month = jc.get(java.util.Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * getCurrentDay(): Return an integer value of the current day
	 * 
	 * @return
	 */
	public static int getCurrentDay() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int day = jc.get(java.util.Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * getCurrentYear(): Return an integer value of the current year
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int year = jc.get(java.util.Calendar.YEAR);
		return year;
	}

	/**
	 * getCurrentHour(): Return an integer value for the current hour
	 * 
	 * @return
	 */
	public static int getCurrentHour() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int hour = jc.get(Calendar.HOUR);
		return hour;
	}

	/**
	 * getCurrentHour24(): Return an integer value for the current hour in 24 hour format
	 *
	 * @return
	 */
	public static int getCurrentHour24() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int hour = jc.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/**
	 * getCurrentMinute(): Return an integer value for the current minute
	 * 
	 * @return
	 */
	public static int getCurrentMinute() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int min = jc.get(java.util.Calendar.MINUTE);
		return min;
	}

	/**
	 * getCurrentSecond(): Return an integer value for the current second
	 * 
	 * @return
	 */
	public static int getCurrentSecond() {
		java.util.Calendar jc = java.util.Calendar.getInstance();
		int sec = jc.get(java.util.Calendar.SECOND);
		return sec;
	}

	/**
	 * getCurrentMonthNameLong(): Return full name of the current month
	 * 
	 * @return
	 */
	public static String getCurrentMonthNameLong() {
		return monthsLong[getCurrentMonth()];
	}

	/**
	 * getCurrentMonthNameShort(): Return three letter short name for current month
	 * 
	 * @return
	 */
	public static String getCurrentMonthNameShort() {
		return monthsShort[getCurrentMonth()];
	}

	/**
	 * getMonthNameLong(): Return long name of month for provided month number
	 * 
	 * @param mon
	 * @return
	 */
	public static String getMonthNameLong(int mon) {
		return monthsLong[mon];
	}

	/**
	 * getMonthNameShort(): Return short month name for provided month number
	 * 
	 * @param mon
	 * @return
	 */
	public static String getMonthNameShort(int mon) {
		return monthsShort[mon];
	}

}
