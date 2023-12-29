/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2011-2024 Michael Fross
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
 ******************************************************************************/
package org.fross.rpncalc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.prefs.Preferences;

import org.junit.jupiter.api.Test;

class ConfigurationTest {

	// Class variables
	Preferences prefConfig = Preferences.userRoot().node("/org/fross/rpn/config");

	// Save current settings so they can be reinstated after the test
	String align = prefConfig.get("alignment", Main.CONFIG_DEFAULT_ALIGNMENT);
	int width = prefConfig.getInt("programwidth", Main.CONFIG_DEFAULT_PROGRAM_WIDTH);
	int memSlots = prefConfig.getInt("memoryslots", Main.CONFIG_DEFAULT_MEMORY_SLOTS);

	@Test
	void testSetMemSlots() {
		// Test MemSlots
		Configuration.cmdSet("memslots 1123");
		assertEquals(1123, StackMemory.memorySlots.length);

		Configuration.cmdSet("memslots 100");
		assertEquals(100, StackMemory.memorySlots.length);

		Configuration.cmdSet("memslots 71");
		assertEquals(71, StackMemory.memorySlots.length);

		// Rest configuration to original user value
		prefConfig.putInt("memoryslots", memSlots);
	}

	@Test
	void testSetWidth() {
		// Test Width
		Configuration.cmdSet("width 100");
		assertEquals(100, Main.configProgramWidth);
		assertEquals("100", prefConfig.get("programwidth", ""));

		// Rest configuration to original user value
		prefConfig.putInt("programwidth", width);
	}

	@Test
	void testSetAlignment() {
		// Test Align
		Configuration.cmdSet("alignment d");
		assertEquals("d", Main.configAlignment);
		assertEquals("d", prefConfig.get("alignment", ""));

		Configuration.cmdSet("alignment R");
		assertEquals("r", Main.configAlignment);
		assertEquals("r", prefConfig.get("alignment", ""));

		Configuration.cmdSet("alignment l");
		assertEquals("l", Main.configAlignment);
		assertEquals("l", prefConfig.get("alignment", ""));

		Configuration.cmdSet("align d");
		assertEquals("d", Main.configAlignment);
		assertEquals("d", prefConfig.get("alignment", ""));

		Configuration.cmdSet("align r");
		assertEquals("r", Main.configAlignment);
		assertEquals("r", prefConfig.get("alignment", ""));

		Configuration.cmdSet("align L");
		assertEquals("l", Main.configAlignment);
		assertEquals("l", prefConfig.get("alignment", ""));

		// Rest configuration to original user value
		prefConfig.put("alignment", align);

	}

	@Test
	void testResetCommand() {
		// Test Reset
		Configuration.cmdReset();
		assertEquals(Main.CONFIG_DEFAULT_PROGRAM_WIDTH, Main.configProgramWidth);
		assertEquals(Main.CONFIG_DEFAULT_PROGRAM_WIDTH, Integer.parseInt(prefConfig.get("programwidth", "")));

		assertEquals(Main.CONFIG_DEFAULT_ALIGNMENT, Main.configAlignment);
		assertEquals(Main.CONFIG_DEFAULT_ALIGNMENT, prefConfig.get("alignment", ""));

		assertEquals(Main.CONFIG_DEFAULT_MEMORY_SLOTS, Main.configMemorySlots);
		assertEquals(Main.CONFIG_DEFAULT_MEMORY_SLOTS, Integer.parseInt(prefConfig.get("memoryslots", "")));

		// Rest configuration to original user value
		prefConfig.putInt("programwidth", width);
		prefConfig.put("alignment", align);
		prefConfig.putInt("memoryslots", memSlots);
	}

}
