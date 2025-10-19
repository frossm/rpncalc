/**************************************************************************************************************
 * Library Project
 * 
 *  Library holds methods and classes frequently used by my programs.
 * 
 *  Copyright (c) 2018-2024 Michael Fross
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
 ***************************************************************************************************************/
package org.fross.library;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.Ansi;

/**
 * Spinner is a simple class that displays a text based graphic that can be shown while other work is being done.
 * 
 * The spinner process starts a new thread and when the work in the main program is complete, it's stopped.
 * 
 * Usage: 
 *   Start Spinner: 
 *     SpinnerBouncyBall spinner = new SpinnerBouncyBall(); 
 *     spinner.start();
 * 
 *   Stop Spinner: 
 *     spinner.interrupt();
 * 
 * @author Michael
 *
 */
public class Spinner extends Thread {
	protected final int SPINNER_DELAY = 120;

	String[] spinnerSymbols = { "|", "/", "-", "\\" };
	int currentSpinner = 0;

	/**
	 * run(): Overrides Thread run() method interface and is the main thread execution loop
	 */
	public void run() {
		// Keep calling the update spinner until the thread is interrupted
		while (Thread.currentThread().isInterrupted() == false) {
			// Spin the spinner
			spinSpinner();

			// Delay before next thread symbol is displayed
			try {
				TimeUnit.MILLISECONDS.sleep(SPINNER_DELAY);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		// Erase the last spinner symbol character
		System.out.println(" ");
	}

	/**
	 * spinSpinner(): Show the spinner symbol and advance to the next index
	 * 
	 */
	public void spinSpinner() {
		// Display the Spinner
		Output.printColor(Ansi.Color.YELLOW, spinnerSymbols[currentSpinner]);

		// Move cursor back one spot
		System.out.print(ansi().cursorLeft(1));

		// Advance the spinner to the next symbol
		currentSpinner++;

		// Loop it back around when we hit the end
		if (currentSpinner >= spinnerSymbols.length) {
			currentSpinner = 0;
		}
	}

	/**
	 * main(): Simply here to test the spinner
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Running Spinner for 10 seconds:");

		// Define and start the spinner
		Spinner spinner = new Spinner();
		spinner.start();

		// Sleep for 10 seconds
		try {
			TimeUnit.MILLISECONDS.sleep(10000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// Stop the spinner
		spinner.interrupt();

		System.out.println("\nSpinner Complete\n\n");
	}

} // END CLASS