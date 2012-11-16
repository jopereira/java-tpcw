/*-------------------------------------------------------------------------
 * rbe.util.TestPattern
 * Timothy Heil
 * 10/5/99
 *
 * Testing StringPattern classes.
 *------------------------------------------------------------------------
 *
 * This is part of the the Java TPC-W distribution,
 * written by Harold Cain, Tim Heil, Milo Martin, Eric Weglarz, and Todd
 * Bezenek.  University of Wisconsin - Madison, Computer Sciences
 * Dept. and Dept. of Electrical and Computer Engineering, as a part of
 * Prof. Mikko Lipasti's Fall 1999 ECE 902 course.
 *
 * Copyright (C) 1999, 2000 by Harold Cain, Timothy Heil, Milo Martin, 
 *                             Eric Weglarz, Todd Bezenek.
 *
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 * Everyone is granted permission to copy, modify and redistribute
 * this code under the following conditions:
 *
 * This code is distributed for non-commercial use only.
 * Please contact the maintainer for restrictions applying to 
 * commercial use of these tools.
 *
 * Permission is granted to anyone to make or distribute copies
 * of this code, either as received or modified, in any
 * medium, provided that all copyright notices, permission and
 * nonwarranty notices are preserved, and that the distributor
 * grants the recipient permission for further redistribution as
 * permitted by this document.
 *
 * Permission is granted to distribute this code in compiled
 * or executable form under the same conditions that apply for
 * source code, provided that either:
 *
 * A. it is accompanied by the corresponding machine-readable
 *    source code,
 * B. it is accompanied by a written offer, with no time limit,
 *    to give anyone a machine-readable copy of the corresponding
 *    source code in return for reimbursement of the cost of
 *    distribution.  This written offer must permit verbatim
 *    duplication by anyone, or
 * C. it is distributed by someone who received only the
 *    executable form, and is accompanied by a copy of the
 *    written offer of source code that they received concurrently.
 *
 * In other words, you are welcome to use, share and improve this codes.
 * You are forbidden to forbid anyone else to use, share and improve what
 * you give them.
 *
 ************************************************************************/

package rbe.util;

import java.util.Random;

public class TestPattern {
  public static void main(String [] args) {
    CharSetStrPattern sp = new CharSetStrPattern();

    int i,c,c2,j;
    boolean [] buf = new boolean[256];
    Random r = new Random(1);

    for (i=0;i<10000;i++) {
      c = Math.abs(r.nextInt()) % 256;
      sp.set((char) c);
      buf[c] = true;

      testBuf(buf, sp);

      c = Math.abs(r.nextInt()) %256;
      sp.clear((char) c);
      buf[c] = false;

      testBuf(buf, sp);
    }

    System.out.println("Testing ranges.");
    for (i=0;i<10000;i++) {
      c = Math.abs(r.nextInt()) % 256;
      c2 = (Math.abs(r.nextInt()) % (256-c))+c;
      // System.out.println("Set " + c + " to " + c2);
      sp.set((char) c, (char) c2);
      for (j=c;j<=c2;j++) {
	buf[j] = true;
      }
      testBuf(buf, sp);

      c = Math.abs(r.nextInt()) % 256;
      c2 = (Math.abs(r.nextInt()) % (256-c))+c;
      // System.out.println("Clear " + c + " to " + c2);
      sp.clear((char) c, (char) c2);
      for (j=c;j<=c2;j++) {
	buf[j] = false;
      }

      testBuf(buf, sp);
    }


    StringPattern p = new CharStrPattern(args[0].charAt(0));
    test(p,args[1]);

    if (args[0].length()>2) {
      p = new CharRangeStrPattern(args[0].charAt(0), args[0].charAt(1));
      test(p,args[1]);
    }

    p = new StrStrPattern(args[0]);
    test(p,args[1]);

    sp.clear((char) 0, (char) 255);
    sp.set(args[0]);
    test(sp,args[1]);
  }

  private static void test(StringPattern p, String s) 
  {
    System.out.println("find " + p.find(s) + " end " + p.end());
    System.out.println("find(5) " + p.find(s, 5) + " end " + p.end());
    System.out.println("find(5,10) " + p.find(s, 5, 10) + 
		       " end " + p.end());

    System.out.println("match " + p.match(s) + " end " + p.end());
    System.out.println("match(5) " + p.match(s, 5) + " end " + p.end());
    System.out.println("match(5,10) " + p.match(s, 5, 10) + 
		       " end " + p.end());
    System.out.println("match(5,5) " + p.match(s, 5, 5) + 
		       " end " + p.end());
    System.out.println("matchWithin(5,10) " + p.matchWithin(s, 5, 10) + 
		       " end " + p.end());
  }

  private static void testBuf(boolean [] buf, CharSetStrPattern sp)
  {
    int j;

    for (j=0;j<256;j++) {
      if (buf[j]!=sp.get((char) j)) {
	System.out.println("Unmatch on " + j);
	System.out.println("buf is " + buf[j] + " sp is " + sp.get((char) j));
	System.exit(-1);
      }
    }
  }
}
