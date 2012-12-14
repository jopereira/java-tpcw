/*------------------------------------------------------------------------
 * rbe.EBWShopCartRefTrans.java
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
 *   EBWShopCartTrans handles this case.
 *
 * ADD_FLAG = N, same number of QTYs as previously, randomly modified.
 *   From Shopping Cart.  SUT updates the cart with the new quantities.
 *
 *   This class, EBWShopCartRefTrans, handles this case.
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

public class EBWShopCartRefTrans extends EBWShopCartTrans {
  /* protected String url; inherited. */

  private static final StrStrPattern qtyPat = new StrStrPattern("NAME=\"QTY");
  private static final StrStrPattern valuePat = new StrStrPattern("VALUE=\"");

  public String request(EB eb, String html) {
	 int i,c;
    RBE rbe = eb.rbe;

	 /* Find out how many items are on the page. */
	 for (c=0,i=qtyPat.find(html,0);i!=-1;i=qtyPat.find(html,i+1),c++);

	 if (c==0) {
	     rbe.stats.error("Unable to find QTY in shopping cart page.", "???");
	     System.out.println("html = " + html);
		return("");
	 }

	 /* Get the current quantities. */
	 int [] qty = new int[c];
	 for (c=0,i=qtyPat.find(html,0);i!=-1;i=qtyPat.find(html,i+1),c++) {
		int j = valuePat.find(html, i+qtyPat.length()) + valuePat.length();
		int e = CharSetStrPattern.notDigit.find(html,j);
		qty[c] = Integer.parseInt(html.substring(j,e));
	 }

	 int [] qtyNew = new int[c];
	 if (c==1) {
		// See TPC-W Clause 2.4.5.1
		qtyNew[0] = eb.nextInt(10)+1;
	 }
	 else {
		// See TPC-W Clause 2.4.5.1
		int r= eb.nextInt(c)+1;
		int [] idx = new int[c];
		for (i=0;i<c;idx[i]=i,i++);
		for (i=0;i<(c-1);i++) {
		  int d = eb.nextInt(c-i)+i;
		  int a= idx[d];
		  idx[d] = idx[i];
		  idx[i] = a;
		}

		for (i=0;i<r;i++) {
		  qtyNew[idx[i]] = eb.nextInt(9);
		  if (qtyNew[idx[i]]>=qty[idx[i]]) {
			 qtyNew[idx[i]]++;
		  }
		}
	 }

	 url = rbe.shopCartURL + "?" + rbe.field_addflag + "=N";

	 for (i=0;i<c;i++) {
		url = url + "&" + rbe.field_qty + "=" + qtyNew[i];
	 }

    return(eb.addIDs(url));
  }

  /* Find C_ID and SHOPPING_ID, if not already known. */
  /* public void postProcess(EB eb, String html)  
	  inherited from EBWShopCartTrans */
  /* NOTE: C_ID and SHOPPING_ID should already be known after this
	*  transistion, since we are coming from the shopping cart page.
	*  But, why not be redundant... */
}
