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
 * Spinner is a simple class that displays a text based graphic that can be shown while other work is being done. The spinner
 * process starts a new thread and when the work in the main program is complete, it's stopped.
 * 
 * Usage: Start Spinner: SpinnerBouncyBall spinner = new SpinnerBouncyBall(); spinner.start();
 * 
 * Stop Spinner: spinner.interrupt();
 * 
 * @author Michael
 *
 */
public class SpinnerBouncyBall extends Thread {
	private int spinnerDelay = 70;
	private int numBallSlots = 6;
	private String leftWall = "[";
	private String rightWall = "]";
	private String ball = "o";

	// Position of the ball in it's journey
	int ballPosition = 0;

	// Direction the ball is heading. Positive is to the right
	int ballDirection = 1;

	/**
	 * configureDelay(): Set spinner's movement speed in milliseconds
	 *
	 * @param delay
	 */
	public void configureDelay(int delay) {
		spinnerDelay = delay;
	}

	/**
	 * configureSlots(): Set number of spinner slots the ball will traverse
	 * 
	 * @param slots
	 */
	public void configureSlots(int slots) {
		this.numBallSlots = slots;
	}

	/**
	 * configureLeftWall(): Set spinner's left wall character
	 * 
	 * @param lWall
	 */
	public void configureLeftWall(String lWall) {
		this.leftWall = lWall;
	}

	/**
	 * configureRightWall(): Set spinner's right wall character
	 * 
	 * @param rWall
	 */
	public void configureRightWall(String rWall) {
		this.rightWall = rWall;
	}

	/**
	 * configureBall(): Set spinner's ball character
	 * 
	 * @param ballChar
	 */
	public void configureBall(String ballChar) {
		this.ball = ballChar;
	}

	/**
	 * run(): Overrides Thread run() method interface and is the main thread execution loop
	 */
	@Override
	public void run() {
		// Keep calling the update spinner until the thread is interrupted
		while (Thread.currentThread().isInterrupted() == false) {
			// Bounce the ball
			bounceBall();

			// Delay before next thread symbol is displayed
			try {
				TimeUnit.MILLISECONDS.sleep(spinnerDelay);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * bounceBall(): Show the spinner symbol and advance to the next index
	 * 
	 */
	public void bounceBall() {
		// Display the bouncy ball and walls
		Output.printColor(Ansi.Color.WHITE, leftWall);
		System.out.print(" ".repeat(ballPosition));
		Output.printColor(Ansi.Color.YELLOW, ball);
		System.out.print(" ".repeat(numBallSlots - ballPosition));
		Output.printColor(Ansi.Color.WHITE, rightWall);

		// Move cursor back
		System.out.print(ansi().cursorLeft(numBallSlots + 3));

		// Determine next ball location
		if (ballDirection > 0) {
			// Ball moving to the right (positive direction)
			ballPosition++;
			if (ballPosition >= numBallSlots)
				ballDirection *= -1;
		} else {
			// Ball moving to the left
			ballPosition--;
			if (ballPosition <= 0)
				ballDirection *= -1;
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
		SpinnerBouncyBall spinner = new SpinnerBouncyBall();

		// Configure the spinner look and feel
//		spinner.configureDelay(40);
//		spinner.configureSlots(50);
//		spinner.configureLeftWall("<");
//		spinner.configureRightWall(">");
//		spinner.configureBall("*");

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
