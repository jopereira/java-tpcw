/*-------------------------------------------------------------------------
 * rbe.args.ArgDB
 * Timothy Heil
 * 10/29/99
 *
 * Abstract command line argument parsing class.
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

package rbe.args;

import java.util.Vector;

import java.io.PrintStream;

public class ArgDB {
  private Vector argsList = new Vector(0);

  public ArgDB() {};

  public void add(Arg a) {
	 argsList.addElement(a);
  }

  public final void parse(String [] args)
		 throws Arg.Exception
  {
	 int i;
	 int a,a2;

	 for (a=0;a<args.length;) {
		  // System.out.println("ArgDB " + a + " " + args[a]);
		a2 = a;
		for (i=0;i<argsList.size();i++) {
		  a = ((Arg) argsList.elementAt(i)).parse(args, a);
		  if (a2!=a) break;
		}
		if (i==argsList.size()) {
		  Arg.Exception e = 
			 new Arg.Exception("Unknown argument (" + args[a] + ").", a);
		  e.start = a;
		  throw(e);
		}
	 }

	 String req = "";
	 int numErr = 0;

	 for (i=0;i<argsList.size();i++) {
		Arg e = ((Arg) argsList.elementAt(i));
		if (e.required() && !e.set()) {
		  req = req + e.toString() + "\n";
		  numErr++;
		}
	 }

	 if (numErr>0) {
		Arg.Exception e = 
		  new Arg.Exception("" + numErr + " arguments not given.\n" + req, 
								  args.length-1);
		e.start = 0;
		throw e;
	 }
  }

  public void print(PrintStream out) {
	 int a;

	 for (a=0;a<argsList.size();a++) {
		out.println("% "+ argsList.elementAt(a));
	 }	 
  }
}
