/**
 * 
 */
package org.fross.library;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class DateTest {

	static final String[] monthsLong = { "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "Novemember",
			"December" };
	static final String[] monthsShort = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	static java.util.Calendar jc = java.util.Calendar.getInstance();

	/**
	 * Test method for {@link org.fross.library.Date#getCurrentMonth()}.
	 */

	@Test
	void testGetCurrentMonth() {
		assertEquals(jc.get(java.util.Calendar.MONTH) + 1, Date.getCurrentMonth());
	}

	/**
	 * Test method for {@link org.fross.library.Date#getCurrentDay()}.
	 */
	@Test
	void testGetCurrentDay() {
		assertEquals(jc.get(java.util.Calendar.DAY_OF_MONTH), Date.getCurrentDay());
	}

	/**
	 * Test method for {@link org.fross.library.Date#getCurrentYear()}.
	 */
	@Test
	void testGetCurrentYear() {
		assertEquals(jc.get(java.util.Calendar.YEAR), Date.getCurrentYear());
	}

	/**
	 * Test method for {@link org.fross.library.Date#getCurrentMonthNameLong()}.
	 */
	@Test
	void testGetCurrentMonthNameLong() {
		assertEquals(monthsLong[jc.get(java.util.Calendar.MONTH) + 1], Date.getCurrentMonthNameLong());
	}

	/**
	 * Test method for {@link org.fross.library.Date#getCurrentMonthNameShort()}.
	 */
	@Test
	void testGetCurrentMonthNameShort() {
		assertEquals(monthsShort[jc.get(java.util.Calendar.MONTH) + 1], Date.getCurrentMonthNameShort());
	}

	/**
	 * Test method for {@link org.fross.library.Date#getMonthNameLong(int)}.
	 */
	@Test
	void testGetMonthNameLong() {
		assertEquals("January", Date.getMonthNameLong(1));
		assertEquals("May", Date.getMonthNameLong(5));
		assertEquals("December", Date.getMonthNameLong(12));
	}

	/**
	 * Test method for {@link org.fross.library.Date#getMonthNameShort(int)}.
	 */
	@Test
	void testGetMonthNameShort() {
		assertEquals("Jan", Date.getMonthNameShort(1));
		assertEquals("May", Date.getMonthNameShort(5));
		assertEquals("Dec", Date.getMonthNameShort(12));
	}

}
