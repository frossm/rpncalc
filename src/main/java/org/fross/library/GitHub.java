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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHub {
	/**
	 * latestRelease: Query GitHub's tag API and determine the latest release of the application
	 * 
	 * Examples of application names would be "quoter" or "rpncalc"
	 * 
	 * @param applicationName
	 * @return
	 */
	public static String updateCheck(String app) {
		String finalURL = "https://api.github.com/repos/frossm/" + app + "/tags";
		String returnString = null;

		Output.debugPrintln("URL for UpdateCheck: " + finalURL);

		try {
			// Read the tags from the GitHub Tags API
			String githubPage = URLOperations.ReadURL(finalURL);

			// Pull out the latest version
			Pattern pattern = Pattern.compile("name.: *\"(.*?)\"", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(githubPage);

			if (matcher.find()) {
				returnString = matcher.group(1);
			} else {
				throw new Exception();
			}
		} catch (Exception ex) {
			returnString = "Unable to determine latest release";
		}

		return returnString;
	}

}
