/*-------------------------------------------------------------------------
 * rbe.EBTPCW2Factory.java
 * Timothy Heil
 * 10/20/99
 *
 * Trey Cain
 * 2/26/2000
 * 
 * Produces TPCW EBs for transistion subset 1 (The Browsing Mix - WIPSb).
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

public class EBTPCW1Factory extends EBFactory {
  private static final EBTransition init = 
    new EBWInitTrans();
  private static final EBTransition admc =
    new EBWAdminConfTrans();
  private static final EBTransition admr =
    new EBWAdminReqTrans();
  private static final EBTransition bess =
    new EBWBestSellTrans();
  private static final EBTransition buyc =
    new EBWBuyConfTrans();
  private static final EBTransition buyr =
    new EBWBuyReqTrans();
  private static final EBTransition creg =
    new EBWCustRegTrans();
  private static final EBTransition home =
    new EBWHomeTrans();
  private static final EBTransition newp =
    new EBWNewProdTrans();
  private static final EBTransition ordd =
    new EBWOrderDispTrans();
  private static final EBTransition ordi =
    new EBWOrderInqTrans();
  private static final EBTransition prod =
    new EBWProdDetTrans();
  private static final EBTransition proc =
    new EBWProdCURLTrans();
  private static final EBTransition sreq =
    new EBWSearchReqTrans();
  private static final EBTransition sres =
    new EBWSearchResultTrans();
  private static final EBTransition shop =
    new EBWShopCartTrans();
  private static final EBTransition shoa =
    new EBWShopCartAddTrans();
  private static final EBTransition shor =
    new EBWShopCartRefTrans();

  private static final EBTransition [] trans = {
    init,
    admc,
    admr,
    bess,
    buyc,
    buyr,
    creg,
    home,
    newp,
    ordd,
    ordi,
    prod,
    sreq,
    sres,
    shop
  };

  private static final int [][] transProb = 
  {
    //           INIT  ADMC  ADMR  BESS  BUYC  BUYR  CREG  HOME  NEWP  ORDD  ORDI  PROD  SREQ  SRES  SHOP
    /*INIT  */ {    0,    0,    0,    0,    0,    0,    0, 9999,    0,    0,    0,    0,    0,    0,    0},

    /*ADMC  */ {    0,    0,    0,    0,    0,    0,    0, 9877,    0,    0,    0,    0, 9999,    0,    0},
    /*ADMR  */ {    0, 8999,    0,    0,    0,    0,    0, 9999,    0,    0,    0,    0,    0,    0,    0},
    /*BESS  */ {    0,    0,    0,    0,    0,    0,    0,  4607,    0,    0,    0,  5259, 9942,    0, 9999},
    /*BUYC  */ {    0,    0,    0,    0,    0,    0,    0,   342,    0,    0,    0,    0, 9999,    0,    0},
    /*BUYR  */ {    0,    0,    0,    0, 9199,    0,    0, 9595,    0,    0,    0,    0,    0,    0, 9999},
    /*CREG  */ {    0,    0,    0,    0,    0, 9145,    0, 9619,    0,    0,    0,    0, 9999,    0,    0},
    /*HOME  */ {    0,    0,    0, 3792,    0,    0,    0,    0, 7585,    0, 7688,    0, 9559,    0, 9999},
    /*NEWP  */ {    0,    0,    0,    0,    0,    0,    0,  299,    0,    0,    0, 9867, 9941,    0, 9999},
	 /*ORDD  */ {    0,    0,    0,    0,    0,    0,    0,   802,    0,    0,    0,    0, 9999,    0,    0},
	 /*ORDI  */ {    0,    0,    0,    0,    0,    0,    0,   523,    0, 8856,    0,    0, 9999,    0,    0},
    /*PROD  */ {    0,    0,   47,    0,    0,    0,    0,  8346,    0,    0,    0, 9749, 9890,    0, 9999},
    /*SREQ  */ {    0,    0,    0,    0,    0,    0,    0,  788,    0,    0,    0,    0,    0, 9955, 9999},
    /*SRES  */ {    0,    0,    0,    0,    0,    0,    0, 3674,    0,    0,    0, 9868, 9942,    0, 9999},
    /*SHOP  */ {    0,    0,    0,    0,    0,    0, 4099, 8883,    0,    0,    0,    0,    0,    0, 9999}
  };

  private static final EBTransition [][] transArray = 
  {
    //           INIT  ADMC  ADMR  BESS  BUYC  BUYR  CREG  HOME  NEWP  ORDD  ORDI  PROD  SREQ  SRES  SHOP
    /*INIT  */ { null, null, null, null, null, null, null, init, null, null, null, null, null, null, null},
    /*ADMC  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*ADMR  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*BESS  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*BUYC  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*BUYR  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*CREG  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*HOME  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*NEWP  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*ORDD  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*ORDI  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*PROD  */ { null, null, null, null, null, null, null, null, null, null, null, proc, null, null, shoa},
    /*SREQ  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*SRES  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
    /*SHOP  */ { null, null, null, null, null, null, null, null, null, null, null, null, null, null, shor}
  };

  private int count = 1;
  private boolean [] stateEnabled = new boolean[trans.length];
  private final int [][] cTransProb = new int[transProb.length][transProb.length];
  private final EBTransition [][] cTransArray = new EBTransition[transArray.length][transArray.length];

  public EB getEB(RBE rbe)
  {
    return(new EB(rbe, cTransProb, cTransArray, 1000000,
		  "TPCW Broswing Mix EB #" + (count++)));
  }

  public int initialize(String [] args, int firstArg) {
    int i;

    if ((args.length == firstArg) || (args[firstArg].charAt(0)=='-')) {
      for (i=0;i<stateEnabled.length;i++) {
	stateEnabled[i] = true;
      }
      initTransArray();

      return(firstArg);
    }
    else {
      for (i=0;i<stateEnabled.length;i++) {
	if (args[firstArg].charAt(i)!='_') {
	  stateEnabled[i] = true;
	}
      }

      initTransArray();

      return(firstArg+1);
    }
  };

  private void initTransArray()
  {
    int i,j;
    int max, maxCol;

    // Copy probabilities, disabling states.
    for (i=0;i<transProb.length;i++) {
      for (j=0;j<transProb.length;j++) {
	if (stateEnabled[i] && stateEnabled[j]) {
	  cTransProb[i][j] = transProb[i][j];
	  // System.out.println("CC " + i + " " + j);
	}
	else {
	  transArray[i][j] = null;
	}
      }
    }

    // Clean up any broken probabilities.
    for (i=0;i<transProb.length;i++) {
      max = cTransProb[i][0];maxCol = 0;
      for (j=1;j<transProb.length;j++) {
	if (cTransProb[i][j] > max) {
	  max = cTransProb[i][j];
	  maxCol = j;
	}
      }
      if (max < 9999) {
	cTransProb[i][maxCol] = 9999;
      }
    }

    // Copy in transitions.
    for (i=0;i<transProb.length;i++) {
      for (j=0;j<transProb.length;j++) {
	cTransArray[i][j] = transArray[i][j];
	if ((cTransProb[i][j]>0) && (cTransArray[i][j]==null)) {
	  cTransArray[i][j] = trans[j];
	}
      }
    }
  }
}
