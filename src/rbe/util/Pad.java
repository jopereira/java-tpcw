/*-------------------------------------------------------------------------
 * util.Pad.java
 * Timothy Heil
 * 10/27/98
 *
 * A class so useful, you can't understand why it is not in the standard
 *  API.  Pads numbers, strings, etc on the right and left to a constant
 *  spacing.
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

public class Pad {
  /* Pad on the right with spaces. */
  public static String r(int size, String s)
  {
    return(r(size, " " , s));
  }
    
  public static String r(int size, double v)
  {
    return (r(size, String.valueOf(v)));
  }

  public static String r(int size, int v)
  {
    return (r(size, String.valueOf(v)));
  }

  public static String r(int size, char v)
  {
    return (r(size, String.valueOf(v)));
  }

  public static String r(int size, Object v)
  {
    return (r(size, v.toString()));
  }

  /* Pad on the right with zeros. */
  public static String rz(int size, String s)
  {
    return(r(size, "0" , s));
  }
    
  public static String rz(int size, double v)
  {
    return (rz(size, String.valueOf(v)));
  }

  public static String rz(int size, int v)
  {
    return (rz(size, String.valueOf(v)));
  }

  public static String rz(int size, char v)
  {
    return (rz(size, String.valueOf(v)));
  }

  public static String rz(int size, Object v)
  {
    return (rz(size, v.toString()));
  }

  /* Pad on the right with arbitrary characters. */
  public static String r(int size, String pad, String s)
  {
    if (s.length() >= size) {
      return(s);
    }

    return ( s+expandRight(size-s.length(),pad));
  }

  public static String r(int size, String pad, double v)
  {
    String s= String.valueOf(v);
    return(r(size, pad, s));
  }

  public static String r(int size, String pad, int v)
  {
    String s = String.valueOf(v);
    return(r(size, pad, s));
  }

  public static String r(int size, String pad, char v)
  {
    String s = String.valueOf(v);
    return(r(size, pad, s));
  }

  public static String r(int size, String pad, Object v)
  {
    String s = v.toString();
    return(r(size, pad, s));
  }

  /* Pad on the left with zeros. */
  public static String lz(int size, String s)
  {
    return(l(size, "0" , s));
  }
    
  public static String lz(int size, double v)
  {
    return (lz(size, String.valueOf(v)));
  }

  public static String lz(int size, int v)
  {
    return (lz(size, String.valueOf(v)));
  }

  public static String lz(int size, char v)
  {
    return (lz(size, String.valueOf(v)));
  }

  public static String lz(int size, Object v)
  {
    return (lz(size, v.toString()));
  }

  /* Pad on the left with spaces. */
  public static String l(int size, String s)
  {
    return(l(size, " " , s));
  }
    
  public static String l(int size, double v)
  {
    return (l(size, String.valueOf(v)));
  }

  public static String l(int size, int v)
  {
    return (l(size, String.valueOf(v)));
  }

  public static String l(int size, char v)
  {
    return (l(size, String.valueOf(v)));
  }

  public static String l(int size, Object v)
  {
    return (l(size, v.toString()));
  }

  /* Pad on the left with arbitrary characters. */
  public static String l(int size, String pad, String s)
  {
    if (s.length() >= size) {
      return(s);
    }

    return ( expandLeft(size-s.length(),pad)+s);
  }

  public static String l(int size, String pad, double v)
  {
    String s= String.valueOf(v);
    return(l(size, pad, s));
  }

  public static String l(int size, String pad, int v)
  {
    String s = String.valueOf(v);
    return(l(size, pad, s));
  }

  public static String l(int size, String pad, char v)
  {
    String s = String.valueOf(v);
    return(l(size, pad, s));
  }

  public static String l(int size, String pad, Object v)
  {
    String s = v.toString();
    return(l(size, pad, s));
  }

  public static String expandRight(int size, String s)
  {
    while (s.length()<size) s=s+s;

    return (s.substring(0, size));
  }

  public static String expandLeft(int size, String s)
  {
    while (s.length()<size) s=s+s;

    return (s.substring(s.length()-size));
  }
}
