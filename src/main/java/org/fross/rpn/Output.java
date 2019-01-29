/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: GNU General Public License v3.
 *           http://www.gnu.org/licenses/gpl-3.0.html
 *           
 ******************************************************************************/
/* Color library:  https://github.com/dialex/JCDP */

package org.fross.rpn;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

public class Output {

	public static void Red(String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.RED).build();
		cp.setAttribute(Attribute.LIGHT);
		cp.println(msg);
		cp.clear();
	}

	public static void Cyan(String msg, Boolean nl) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.CYAN).build();
		cp.setAttribute(Attribute.DARK);
		if (nl) {
			cp.println(msg);
		} else {
			cp.print(msg);
		}
		cp.clear();
	}

	public static void Cyan(String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.CYAN).build();
		cp.setAttribute(Attribute.DARK);
		cp.println(msg);
		cp.clear();
	}

	public static void Yellow(String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.YELLOW).build();
		cp.setAttribute(Attribute.LIGHT);
		cp.println(msg);
		cp.clear();
	}

	public static void White(String msg) {
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.WHITE).build();
		cp.setAttribute(Attribute.LIGHT);
		cp.println(msg);
		cp.clear();
	}

	public static void Print(String msg) {
		System.out.println(msg);
	}
}