/*-------------------------------------------------------------------------
 * rbe.args.Arg
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

import rbe.util.Pad;

public abstract class Arg {
  private String arg;      // Command line -arg name.
  private String name;     // Descriptive name.
  private String desc;     // Description.

  protected boolean set;   // Whether this argument was set.
  protected boolean req;   // This argument must be set.
  protected boolean def;   // This argument has a default value.

  public Arg(String arg, String name, String desc,
				 boolean req, boolean def,  ArgDB db) {
	 init(arg, name, desc, req, def);
	 db.add(this);
  }

  public Arg(String arg, String name, String desc, 
				 boolean req, boolean def) {
	 init(arg, name, desc, req, def);
  }

  private void init(String arg, String name, String desc, 
						  boolean req, boolean def) {
	 this.arg = arg.toUpperCase();
	 this.name = name;
	 this.desc = desc;
	 this.req = req;
	 this.def = def;
	 set = false;
  }

  public final boolean set() { return(set); };
  public final boolean required() { return(req); };

  public final int parse(String [] args, int a)
		 throws Arg.Exception
  {
	 if (arg.equals(args[a].toUpperCase())) {
		set = true;
		try {
		  return(parseMatch(args, a+1));
		}
		catch (Exception e) {
		  e.start = a;
		  throw(e);
		}
	 }
	 else {
		return(a);
	 }
  }

  public String toString() {
	 String v;
	 if (set) {
		v = value();
	 }
	 else {
		if (req) {
		  v = "required";
		}
		else if (def) {
		  v = value() + " (default)";
		}
		else {
		  v = "unset";
		}
	 }
	 return(Pad.l(8, arg) + " " + Pad.l(25, name) + " " + 
			  Pad.l(20, v) + "\n         " + desc);
  }

  // Customize to parse arguments.
  protected abstract int parseMatch(String [] args, int a)
		 throws Arg.Exception
  ;

  // Customize to produce String describing current value.
  protected abstract String value();


  // Throw one of these (or sub-class) for any parse errors.
  public static class Exception extends java.lang.Exception {
	 public int start, end;

	 public Exception(String message, int a) {
		super( message);
		this.end = a;
	 }

	 public String getMessage() {
		return(super.getMessage() + 
				 " arguments(" + (start+1) + " to " + (end+1) + ")");
	 }
  }
}
