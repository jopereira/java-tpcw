/*------------------------------------------------------------------------
 * rbe.EBWBuyConfTrans.java
 * Timothy Heil
 * 10/13/99
 *
 * ECE902 Fall '99
 *
 * TPC-W home transition.  Requests the home page, and sends CID and
 *  shopping ID if known.
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

import rbe.util.StrStrPattern;
import rbe.util.CharStrPattern;

public class EBWBuyConfTrans extends EBTransition {

  private static final StrStrPattern fnamePat =
    new StrStrPattern("Firstname:</TD><TD>");
  private static final StrStrPattern lnamePat =
    new StrStrPattern("Lastname: </TD><TD>");
  private static final CharStrPattern ltPat =
    new CharStrPattern('<');

  public String request(EB eb, String html) {

    RBE rbe = eb.rbe;
	 String url = rbe.buyConfURL;
	 String n;

	 n = findName(rbe, fnamePat, html, eb.fname, "First name");
	 if (eb.fname == null) {
		eb.fname = n;
	 }

	 n = findName(rbe, lnamePat, html, eb.lname, "Last name");
	 if (eb.lname == null) {
		eb.lname = n;
	 }

	 if (eb.cid==eb.ID_UNKNOWN) {
	     rbe.stats.error("CID not known transitioning to buy confirm page.", "???");
	 }
	 url = url + "?" + rbe.field_cctype + "=" + rbe.unifCCType(eb.rand);
	 url = url + "&" + rbe.field_ccnumber + "=" + rbe.nstring(eb.rand, 16,16);
	 
	 url = url + "&" + rbe.field_ccname + "=" + eb.fname + "+" + eb.lname;
	 url = url + "&" + rbe.field_ccexp + "=" + rbe.unifExpDate(eb.rand);

	 if (eb.nextInt(100)<5) {
		url = url + "&" + rbe.field_street1 + "=" + rbe.astring(eb.rand, 15, 40);
		url = url + "&" + rbe.field_street2 + "=" + rbe.astring(eb.rand, 15, 40);

		// Odd difference between this and BuyReq cit (astring(eb.rand, 10,30))
		url = url + "&" + rbe.field_city + "=" + rbe.astring(eb.rand, 4, 30);
		url = url + "&" + rbe.field_state + "=" + rbe.astring(eb.rand, 2, 20);
		url = url + "&" + rbe.field_zip + "=" + rbe.astring(eb.rand, 5, 10);
		url = url + "&" + rbe.field_country + "=" + rbe.unifCountry(eb.rand);
	 }
	 else {
		url = url + "&" + rbe.field_street1 + "=";
		url = url + "&" + rbe.field_street2 + "=";

		// Odd difference between this and BuyReq cit (astring(eb.rand, 10,30))
		url = url + "&" + rbe.field_city + "=";
		url = url + "&" + rbe.field_state + "=";
		url = url + "&" + rbe.field_zip + "=";
		url = url + "&" + rbe.field_country + "=";
	 }

	 url = url + "&" + rbe.field_shipping + "=" + rbe.unifShipping(eb.rand);

	 if (0>0) {
		System.out.println("Final URL: " + url);
	 }
	 return(eb.addIDs(url));
  }

  private String findName(RBE rbe, StrStrPattern namePat, String html, 
								  String prev, String nameType)
  {
	 String name;

	 int i = namePat.find(html);
	 if (i==-1) {
		rbe.stats.error("Unable to find " + nameType + 
							 " in HTML for buy request page.", "???");
		return("");
	 }

	 i = i + namePat.length();
	 int j = ltPat.find(html,i);
	 if (j==-1) {
		rbe.stats.error("Unable to find " + nameType + 
							 " in HTML for buy request page.", "???");
		return("");
	 }
	 
	 name = html.substring(i,j);
	 if (name.length()>15) {
		System.out.println("WARNING: Name found is too long (" + 
								 name.length() + ") (" + name + ").");
	 }
	 if (0>0) {
		System.out.println("Name found:  " + name);
	 }
	 name = RBE.mungeURL(name);
	 if (0>0) {
		System.out.println("Name munged: " + name);
	 }
	 if ((prev!=null) && (!prev.equals(name))) {
	     /*rbe.stats.error("Known " + nameType + " (" + prev + 
							 ") does not equal name found in HTML (" + name + 
							 ") in buy request page.",
							 "????");*/
	 }

	 return(name);
  }
}

