/*------------------------------------------------------------------------
 * rbe.EBWProdDetTrans.java
 * Timothy Heil
 * 11/10/99
 *
 * ECE902 Fall '99
 *
 * TPC-W transistion to product detail.  From new products page, best 
 *  sellers page and search results page.  Also used for the "curl" 
 *  transition from the product detail page back to itself.  Pick a 
 *  uniform random item ID from the HTML, and make a request to the
 *  product detail page.
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

package rbe;

import java.util.Vector;

import rbe.util.StrStrPattern;
import rbe.util.CharRangeStrPattern;
import rbe.util.CharSetStrPattern;

public class EBWProdDetTrans extends EBTransition {

  public static final StrStrPattern itemPat = 
    new StrStrPattern("I_ID=");

  public String request(EB eb, String html) {
	 Vector iid = new Vector(0);
    RBE rbe = eb.rbe;
	 int i;

	 // Save HTML for the <CURL> transistion.  
	 //  See TPC-W Spec. Clause 2.14.5.4 and chart in Clause 1.1
	 eb.prevHTML = html;

	 // Scan html for items.
	 for (i = itemPat.find(html);i!=-1;i=itemPat.find(html, i)) {
		i=i+itemPat.length();
		int s = CharSetStrPattern.digit.find(html,i);
		int e = CharSetStrPattern.notDigit.find(html,s+1);
		iid.addElement(new Integer(Integer.parseInt(html.substring(s,e))));
	 }

	 if (iid.size()==0) {
		rbe.stats.error("Unable to find any items for product detail.", "???");
		return("");
	 }

	 i = eb.nextInt(iid.size());
	 i = ((Integer) iid.elementAt(i)).intValue();

	 String url=rbe.prodDetURL + "?" + rbe.field_iid + "=" + i;
    return(eb.addIDs(url));
  }

}
