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

/**
 * Debug contains static methods to maintain the debug state and display messages when enabled.
 * 
 * @author michael.d.fross
 *
 */
public class Debug {
	// Class Variables
	private static boolean clDebug = false;

	/**
	 * Query(): Query current state of this object's debug setting
	 * 
	 * @return
	 */
	public static boolean query() {
		return clDebug;
	}

	/**
	 * Enable(): Turn debugging on for this object
	 */
	public static void enable() {
		clDebug = true;
	}

	/**
	 * Disable(): Disable debugging for this object
	 */
	public static void disable() {
		clDebug = false;
	}

	/**
	 * displaySysInfo(): Display some system level information used in Debug Mode
	 */
	public static void displaySysInfo() {
		Output.debugPrintln("------------------------------------------------------------");
		Output.debugPrintln("System Information:");
		Output.debugPrintln("  - class.path:     " + System.getProperty("java.class.path"));
		Output.debugPrintln("  - java.home:      " + System.getProperty("java.home"));
		Output.debugPrintln("  - java.vendor:    " + System.getProperty("java.vendor"));
		Output.debugPrintln("  - java.version:   " + System.getProperty("java.version"));
		Output.debugPrintln("  - os.name:        " + System.getProperty("os.name"));
		Output.debugPrintln("  - os.version:     " + System.getProperty("os.version"));
		Output.debugPrintln("  - os.arch:        " + System.getProperty("os.arch"));
		Output.debugPrintln("  - user.name:      " + System.getProperty("user.name"));
		Output.debugPrintln("  - user.home:      " + System.getProperty("user.home"));
		Output.debugPrintln("  - user.dir:       " + System.getProperty("user.dir"));
		Output.debugPrintln("  - file.separator: " + System.getProperty("file.separator"));
		Output.debugPrintln("  - library.path:   " + System.getProperty("java.library.path"));
		Output.debugPrintln("------------------------------------------------------------");
	}

}