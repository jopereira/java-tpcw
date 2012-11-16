/*------------------------------------------------------------------------
 * rbe.EBWShopCartTrans.java
 * Timothy Heil
 * 10/13/99
 *
 * ECE902 Fall '99
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
 ************************************************************************
 *
 * TPC-W shopping cart transition.  Requests the shopping cart page,
 *  and sends CID and shopping ID if known.  Sends ADD_FLAG, and
 *  possibly I_ID (comma separated list of item IDs, and QTY (comma
 *  separated list of item quantities).
 *  
 * NOTE:  The TPC-W Spec (Clause 2.4) talks alot about (I_ID, QTY) pairs.
 *  However, the I_ID (book item ID) is not supplied by the SUT in the
 *  Shopping cart page HTML.  Therefore the EB does not know the I_IDs.
 *
 * Looking at the suggested HTML, it is apparent that the EB does *not*
 *  ever supply I_IDs to the SUT when the add flag = 'N'.  The EB merely
 *  supplies a string of QTYs, in the same order as recieved.  The SUT
 *  must determine the I_IDs from the order of the QTYs, based on 
 *  the SCL_I_IDs in the shopping cart.  This mainly has to do with
 *  the refersh transition from the shopping cart page to the shopping
 *  cart page.
 *  
 * The only time ADD_FLAG=Y, is the <add to cart> transition from the
 *  product detail page to the shopping cart page.  In this case, the
 *  EB must supply the I_ID for the new item to be added.  Fortunatly
 *  the product detail HTML has the I_ID embedded in it in the form
 *  "BookID=<I_ID>".
 *
 * In summary, the shopping cart transition has three forms:
 *
 * ADD_FLAG = N, no QTYs or I_IDs.  From Home, Search Request, New Products
 *   Best Seller, Search Result, Buy Request.
 *  
 *   SUT just displays the current status of the cart.  Creates one if
 *    needed.  Adds a random item to it.
 *
 *   This class (EBWShopCartTrans) handles this case.
 *
 * ADD_FLAG = N, same number of QTYs as previously, randomly modified.
 *   From Shopping Cart.  SUT updates the cart with the new quantities.
 *
 *   EBWShopCartRefTrans handles this case.
 *
 * ADD_FLAG = Y, QTY = #, I_ID = BookID.  From Product Detail.
 *   SUT adds the new item to the cart.
 *
 *   EBWShopCartAddTrans handles this case.
 *
 *------------------------------------------------------------------------*/

package rbe;

import rbe.util.StrStrPattern;
import rbe.util.CharSetStrPattern;
import rbe.util.CharRangeStrPattern;

public class EBWShopCartTrans extends EBTransition {
  protected String url;

  public String request(EB eb, String html) {
	 int i;
    RBE rbe = eb.rbe;

	 url = rbe.shopCartURL + "?" + rbe.field_addflag + "=N";

    return(eb.addIDs(url));
  }

  /* Find C_ID and SHOPPING_ID, if not already known. */
  public void postProcess(EB eb, String html) {
	 if (eb.cid == eb.ID_UNKNOWN) {
		eb.cid = eb.findID(html, RBE.yourCID);
		// System.out.println("Found CID = " + eb.cid);
	 }

	 if (eb.sessionID == null) {
		eb.sessionID = findSessionID(eb, html, RBE.yourSessionID, RBE.endSessionID, 
									  "SESSIONID");
		// System.out.println("Found SESSIONID = " + eb.sessionID);
	 }

	 if (eb.shopID == eb.ID_UNKNOWN) {
		eb.shopID = findID(eb, html, RBE.yourShopID, "SHOPPING_ID");
	 }
  }

  private int findID(EB eb, String html, StrStrPattern tag, String name) {
	 int id = eb.findID(html, tag);

	 if (id==EB.ID_UNKNOWN) {
      eb.rbe.stats.error("findId: Unable to find " + name + 
								 " in shopping cart page.", url);
	 }
     
	 return(id);
  }

  private String findSessionID(EB eb, String html, StrStrPattern tag, 
									 StrStrPattern etag, String name) {
	 String id = eb.findSessionID(html, tag, etag);

	 if (id==null) {
      eb.rbe.stats.error("findSessionID: Unable to find " + name +
								 " tag in shopping cart page.", url);		
	 }

	 return(id);
  }
}
