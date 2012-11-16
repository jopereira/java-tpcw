/*-------------------------------------------------------------------------
 * rbe.util.CharSetStrPattern.java
 * Timothy Heil
 * 10/5/99
 *
 * StringPattern matching any set of characters.
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

import rbe.util.Debug;

public class CharSetStrPattern extends AbCharStrPattern {
  public static final CharRangeStrPattern digit =
    new CharRangeStrPattern('0', '9');

  public static final CharRangeStrPattern lower =
    new CharRangeStrPattern('a', 'z');

  public static final CharRangeStrPattern upper =
    new CharRangeStrPattern('A', 'Z');

  public static final CharSetStrPattern notDigit;

  static {
	 notDigit = new CharSetStrPattern();
	 notDigit.set((char) 0,(char) 255);
	 notDigit.clear('0', '9');
  }


  protected byte [] mask = new byte[32];

  public void set(char c) {
    int i = c>>3;
    int bit = 1<<(c&7);
    
    mask[i] |= bit;
  }

  public void set(char s, char e) {

    Debug.assert(s<=e, "CharSetStrPattern.set: s must be <= to e.");

    int si = s>>3;
    int ei = e>>3;

    int b;

    if (si == ei) {
      mask[si] |= ((1<<((e&7)-(s&7)+1))-1)<<(s&7);
    }
    else {
      for (b=si+1;b<ei;b++) {
	mask[b] = (byte) 0xff;
      }
      
      mask[si] |= 0xff<<(s&7);
      mask[ei] |= (1<<((e&7)+1)) -1;
    }
  }

  public void set(String c) {
    int i;
    for (i=0;i<c.length();i++) {
      set(c.charAt(i));
    }
  }

  public void clear(char c) {
    int i = c>>3;
    int bit = 1<<(c&7);
    
    mask[i] &= (0xff ^ bit);
  }

  public void clear(char s, char e) {

    Debug.assert(s<=e, "CharSetStrPattern.clear: s must be <= to e.");
    int si = s>>3;
    int ei = e>>3;

    int b;

    if (si == ei) {
      mask[si] &= 0xff ^ ((1<<((e&7)-(s&7)+1))-1)<<(s&7);
    }
    else {
      for (b=si+1;b<ei;b++) {
	mask[b] = (byte) 0;
      }
      
      mask[si] &= 0xff>>(8-(s&7));
      mask[ei] &= 0xfe<<(e&7);
    }
  }

  public void clear(String c) {
    int i;
    for (i=0;i<c.length();i++) {
      clear(c.charAt(i));
    }
  }

  public boolean get(char c) {
    int i = c>>3;
    int bit = 1<<(c&7);
   
    return((mask[i] & bit)!=0);
  }
  // Does this character match.
  protected boolean charMatch(char c) 
  {
    int i = c>>3;
    int bit = 1<<(c&7);

    return((mask[i] & bit) != 0);
  };
  
}
