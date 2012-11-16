/*------------------------------------------------------------------------
 * rbe.EBWBuyReqTrans.java
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

public class EBWBuyReqTrans extends EBTransition {

  public String request(EB eb, String html) {

    RBE rbe = eb.rbe;

	 String url = rbe.buyReqURL;

	 if (eb.cid!=eb.ID_UNKNOWN) {
		url = url + "?" + rbe.field_retflag + "=Y&" + rbe.unameAndPass(eb.cid);
	 }
	 else {
		url = url + "?" + rbe.field_retflag + "=N";
		eb.fname = rbe.astring(eb.rand, 8, 15);
		eb.lname = rbe.astring(eb.rand, 8, 15);
		url = url + "&" + rbe.field_fname + "=" + eb.fname;
		url = url + "&" + rbe.field_lname + "=" + eb.lname;
		url = url + "&" + rbe.field_street1 + "=" + rbe.astring(eb.rand, 15, 40);
		url = url + "&" + rbe.field_street2 + "=" + rbe.astring(eb.rand, 15, 40);
		url = url + "&" + rbe.field_city + "=" + rbe.astring(eb.rand, 10, 30);
		url = url + "&" + rbe.field_state + "=" + rbe.astring(eb.rand, 2, 20);
		url = url + "&" + rbe.field_zip + "=" + rbe.astring(eb.rand, 5, 10);
		url = url + "&" + rbe.field_country + "=" + rbe.unifCountry(eb.rand);
		url = url + "&" + rbe.field_phone + "=" + rbe.nstring(eb.rand, 9, 16);
		// For e-mail, see TPC-W spec clause 4.2.6.13
		//   However, user name is not known, because CID is not known.
		//   Useing random a-string [8, 15] instead.
		//   %40 == @ sign
		url = url + "&" + rbe.field_email + "=" + rbe.astring(eb.rand, 8,15) + 
		  "%40" + rbe.astring(eb.rand, 2,9) + ".com";
		// This is a little ambiguous in the TPC-W Spec.
		//  This definition is taken from Clause 4.7.1, C_BIRTHDATE
		url = url + "&" + rbe.field_birthdate + "=" + rbe.unifDOB(eb.rand);
		url = url + "&" + rbe.field_data + "=" + 
		  rbe.astring(eb.rand, 100, 500);	

	 }		  

	 return(eb.addIDs(url));
  }

  /* Find C_ID and SHOPPING_ID, if not already known. */
  public void postProcess(EB eb, String html) {
	 if (eb.cid == eb.ID_UNKNOWN) {
		eb.cid = eb.findID(html, RBE.yourCID);
		if (eb.cid == eb.ID_UNKNOWN) {
		  eb.rbe.stats.error("Unable to find C_ID in buy request page page.", "<???>");
		}
		// System.out.println("Found CID = " + eb.cid);
	 }
  }
}
