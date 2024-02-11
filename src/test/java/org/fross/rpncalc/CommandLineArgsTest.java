/* ------------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------------*/
package org.fross.rpncalc;

import com.beust.jcommander.JCommander;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michael Fross (michael@fross.org)
 */
class CommandLineArgsTest {
   @Test
   void testShortCommandLineArgs() {
      // Test Short Options
      String[] argv1 = {"-D", "-z", "-h", "-v", "-L", "-l", "LoadFile.txt"};

      CommandLineArgs cli = new CommandLineArgs();
      JCommander jc = new JCommander();
      jc.setProgramName("RPNCalc");

      jc = JCommander.newBuilder().addObject(cli).build();
      jc.parse(argv1);

      assertTrue(cli.clDebug);
      assertTrue(cli.clNoColor);
      assertTrue(cli.clLicense);
      assertTrue(cli.clVersion);
      assertTrue(cli.clHelp);
      assertEquals("LoadFile.txt", cli.clLoad);
   }

   @Test
   void testLongCommandLineArgs() {
      // Test Long Options
      String[] argv2 = {"--debug", "--no-color", "--help", "--license", "--version", "--load", "LongLoadFile.txt"};

      CommandLineArgs cli = new CommandLineArgs();
      JCommander jc = new JCommander();
      jc.setProgramName("RPNCalc");

      jc = JCommander.newBuilder().addObject(cli).build();
      jc.parse(argv2);

      assertTrue(cli.clDebug);
      assertTrue(cli.clNoColor);
      assertTrue(cli.clLicense);
      assertTrue(cli.clVersion);
      assertTrue(cli.clHelp);
      assertEquals("LongLoadFile.txt", cli.clLoad);
   }

   @Test
   void testMixedCommandLineArgs1() {
      // Test Mix of Options
      String[] argv3 = {"--no-color", "-?", "--load", "MixedLoadFile.txt"};

      CommandLineArgs cli = new CommandLineArgs();
      JCommander jc = new JCommander();
      jc.setProgramName("RPNCalc");

      jc = JCommander.newBuilder().addObject(cli).build();
      jc.parse(argv3);

      assertFalse(cli.clDebug);
      assertTrue(cli.clNoColor);
      assertFalse(cli.clVersion);
      assertTrue(cli.clHelp);
      assertEquals("MixedLoadFile.txt", cli.clLoad);
   }

   @Test
   void testMixedCommandLineArgs2() {
      // Test Mix of Options
      String[] argv4 = {"--debug", "-h", "--license", "-l", "MixedLoadFile2.txt"};

      CommandLineArgs cli = new CommandLineArgs();
      JCommander jc = new JCommander();
      jc.setProgramName("RPNCalc");

      jc = JCommander.newBuilder().addObject(cli).build();
      jc.parse(argv4);

      assertTrue(cli.clDebug);
      assertFalse(cli.clNoColor);
      assertFalse(cli.clVersion);
      assertTrue(cli.clHelp);
      assertTrue(cli.clLicense);
      assertEquals("MixedLoadFile2.txt", cli.clLoad);
   }
}