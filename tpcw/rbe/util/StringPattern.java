/*-------------------------------------------------------------------------
 * rbe.util.StringPattern.java
 * Timothy Heil
 * 10/5/99
 *
 * Abstract class representing a pattern to match in a string.
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

public abstract class StringPattern {
  int start, end;  // Last match.

  // Find a match in the given string.
  //  Return index of first character of pattern, if the
  //  pattern is found.
  //  Returns -1 if no pattern is found.

  // Search whole string.
  public int find(String s) 
  {
    return(find(s, 0, s.length()-1));
  }

  // Search starting at index start.
  public int find(String s, int start)
  {
    return(find(s, start, s.length()-1));
  }

  // Search from index start to end, inclusive.
  //  Note that the ENTIRE pattern must fit between start and end,
  //   not just begin matching before end.
  public abstract int find(String s, int start, int end);

  // See if pattern matches first part of string.
  public boolean match(String s)
  {
    return(match(s, 0, s.length()-1));
  }

  // See if pattern matches at index pos.
  public boolean match(String s, int pos)
  {
    return(matchWithin(s, pos, s.length()-1));
  }

  // See if pattern matches exactly characters pos to end, inclusive.
  public boolean match(String s, int pos, int end) 
  {
    int saveStart = start;
    int saveEnd   = this.end;

    if (matchWithin(s, pos, end)) {
      if (this.end==end) {
	return(true);
      }
      this.start = saveStart;
      this.end = saveEnd;
    }
      
      return(false);
  }

  // Find a complete match starting at pos and stopping before or at end.
  public abstract boolean matchWithin(String s, int pos, int end);

  // Returns the index of the last charcter that matched the
  //  pattern.
  public int end() {return(end);}
  
  // Returns the index of the first character that last matched
  //  the pattern.
  public int start() {return(start);}

  // Minimum and maximum lengths.
  protected abstract int minLength();
  protected abstract int maxLength();
}



