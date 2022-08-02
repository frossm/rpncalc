/**
 * 
 */
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Michael Fross (michael@fross.org)
 *
 */
class StackManagementTest {
	/**
	 * Test method for {@link org.fross.rpncalc.StackManagement#QueryCurrentStackNum()}.
	 */
	@Test
	void testQueryCurrentStackNum() {
		StackObj stk1 = new StackObj();
		StackObj stk2 = new StackObj();

		assertEquals(1, StackManagement.QueryCurrentStackNum());

		StackOperations.cmdSwapStack(stk1, stk2);
		assertEquals(2, StackManagement.QueryCurrentStackNum());

		StackOperations.cmdSwapStack(stk1, stk2);
		assertEquals(1, StackManagement.QueryCurrentStackNum());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackManagement#ToggleCurrentStackNum()}.
	 */
	@Test
	void testToggleCurrentStackNum() {
		StackManagement.ToggleCurrentStackNum();
		assertEquals(2, StackManagement.QueryCurrentStackNum());
		StackManagement.ToggleCurrentStackNum();
		assertEquals(1, StackManagement.QueryCurrentStackNum());
	}

	/**
	 * Test method for {@link org.fross.rpncalc.StackManagement#QueryLoadedStack()}.
	 * 
	 * Test method for {@link org.fross.rpncalc.StackManagement#SetLoadedStack(java.lang.String)}.
	 */
	@Test
	void testQueryLoadedStack() {
		StackObj stk = new StackObj();

		stk.setStackNameAndRestore("junittest", "1");
		assertEquals("junittest", stk.queryStackName());
	}

}