/*-------------------------------------------------------------------------
 * rbe.RBE.java
 * Timothy Heil
 * ECE902 Fall '99
 *
 * TPC-W Remote Browser Emulator.
 *
 * Main program.
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
 ************************************************************************
 *
 * Changed 2003 by Jan Kiefer.
 *
 ************************************************************************/

package rbe;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Random;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import rbe.args.Arg;
import rbe.args.ArgDB;
import rbe.args.IntArg;
import rbe.args.PrintStreamArg;
import rbe.args.DateArg;
import rbe.args.DoubleArg;
import rbe.args.StringArg;
import rbe.args.BooleanArg;

import rbe.util.StrStrPattern;
import rbe.util.CharSetStrPattern;
import rbe.util.CharRangeStrPattern;

import rbe.util.Pad;

public class RBE {

  // URLs
  public static String www1 = "@standardUrl@";
  public static String www;
  public static String homeURL;
  public static String shopCartURL;
  public static String orderInqURL;
  public static String orderDispURL;
  public static String searchReqURL;
  public static String searchResultURL;
  public static String newProdURL;
  public static String bestSellURL;
  public static String prodDetURL;
  public static String custRegURL;
  public static String buyReqURL;
  public static String buyConfURL;
  public static String adminReqURL;
  public static String adminConfURL;
  public static Date startTime;

  public static final StrStrPattern yourCID = 
    new StrStrPattern("C_ID=");
  public static final StrStrPattern yourShopID = 
    new StrStrPattern("SHOPPING_ID=");
  public static final StrStrPattern yourSessionID = 
      //new StrStrPattern("JIGSAW-SESSION-ID=");
      //      new StrStrPattern("JServSessionIdroot=");
    new StrStrPattern(";@sessionIdString@");
  public static final StrStrPattern endSessionID =
    new StrStrPattern("?");

  static {
	 setURLs();
  }

  public static void setURLs()
  {
	 www = www1 + "@servletUrlPath@";
	 String wwwTPCW = www1 + "@tpcwUrlPath@";
	 homeURL = www+"TPCW_home_interaction";
	 shopCartURL = www+"TPCW_shopping_cart_interaction";
	 orderInqURL = www+"TPCW_order_inquiry_servlet";
	 orderDispURL = www+"TPCW_order_display_servlet";
	 searchReqURL = www+"TPCW_search_request_servlet";
	 searchResultURL = www+"TPCW_execute_search";
	 newProdURL = www+"TPCW_new_products_servlet";
	 bestSellURL = www+"TPCW_best_sellers_servlet";
	 prodDetURL = www+"TPCW_product_detail_servlet";
	 custRegURL = www+"TPCW_customer_registration_servlet";
	 buyReqURL = www+"TPCW_buy_request_servlet";
	 buyConfURL = www+"TPCW_buy_confirm_servlet";
	 adminReqURL = www+"TPCW_admin_request_servlet";
	 adminConfURL = www+"TPCW_admin_response_servlet";
  }

  // FIELD NAMES
  public static final String field_cid = "C_ID";
    //public static final String field_sessionID = "JServSessionIdroot";
    
    //public static final String field_sessionID = "JIGSAW-SESSION-ID";
  public static final String field_sessionID = ";@sessionIdString@";
  public static final String field_shopID = "SHOPPING_ID";
  public static final String field_uname = "UNAME";
  public static final String field_passwd = "PASSWD";
  public static final String field_srchType = "search_type";
  public static final String   authorType = "author";
  public static final String   subjectType = "subject";
  public static final String   titleType = "title";
  public static final String field_srchStr = "search_string";
  public static final String field_addflag = "ADD_FLAG";
  public static final String field_iid = "I_ID";
  public static final String field_qty = "qty";
  public static final String field_subject = "subject";
  public static final String field_retflag = "RETURNING_FLAG";
  public static final String field_fname = "FNAME";
  public static final String field_lname = "LNAME";
  public static final String field_street1 = "STREET_1";
  public static final String field_street2 = "STREET_2";
  public static final String field_city = "CITY";
  public static final String field_state = "STATE";
  public static final String field_zip = "ZIP";
  public static final String field_country = "COUNTRY";
  public static final String field_phone = "PHONE";
  public static final String field_email = "EMAIL";
  public static final String field_birthdate = "BIRTHDATE";
  public static final String field_data = "DATA";
  public static final String field_cctype = "CC_TYPE";
  public static final String field_ccnumber = "CC_NUMBER";
  public static final String field_ccname = "CC_NAME";
  public static final String field_ccexp = "CC_EXPIRY";
  public static final String field_shipping = "SHIPPING";
  public static final String field_newimage = "I_NEW_IMAGE";
  public static final String field_newthumb = "I_NEW_THUMBNAIL";
  public static final String field_newcost = "I_NEW_COST";

  public static boolean getImage; // Whether to fetch images.
  public static boolean monitor; //Whether or not to do monitoring
  public static boolean incremental;  

  public int numCustomer = 1000;  // Number of initial customers
  public int cidA = 1023;         // Used for generating random CIDs
                                  //  See TPC-W spec Clause 2.3.2

  // TPC-W spec values for cidA.  See TPC-W spec Clause 2.3.2
  public static final int [][] stdCIDA = 
  // For NUM_CUSTOMERS in this range    Value for A
  {
    {          1,      9999,                     1023 },
    {      10000,     39999,                     4095 },
    {      40000,    159999,                    16383 },
    {     160000,    639999,                    65535 },
    {     640000,   2559999,                   262143 },
    {    2560000,  10239999,                  1048575 },
    {   10240000,  40959999,                  4194303 },
    {   40960000, 163839999,                 16777215 },
    {  163840000, 655359999,                 67108863 }
  };

  public static int numItem = 10000;     // Number of items for sale.
  public static int numItemA = 511;      // Used for search strings.  See
                                         //  TPC-W Spec. 2.10.5.1

  public static final int [][] stdNumItemA =
  // For NUM_ITEMS    Value for A
  {
    {      1000,               63 },
    {     10000,              511 },
    {    100000,             4095 },
    {   1000000,            32767 },
    {  10000000,           524287 }
  };

  public int maxImageRd=10;       // Maximum number of images to read at once.
  public EBStats stats;

  public double slowDown;         // Time slow down factor.
  //  1000 means one thousand real seconds equals one simulated second.
  public double speedUp;          // 1/slowDown.

  public static final BufferedReader bin = 
    new BufferedReader(new InputStreamReader(System.in));

  public static void main(String [] args) {
    RBE rbe = new RBE();
    EBTestFactory ebtf = new EBTestFactory();
    int i,a,num;
    int maxState=0;
    Vector ebs = new Vector(0);

    System.out.println("Remote Browser Emulator for TPC-W.");
    System.out.println("  ECE 902  Fall '99");
    System.out.println("  Version 1.5");

    ArgDB db = new ArgDB();

    startTime = new Date();

    EBFactoryArg ebfArg =
      new EBFactoryArg("-EB", "EB Factory",
		       "% Factory class used to create EBs.  " + 
		       "<class> <#> <factory args...>.", 
		       rbe, ebs, db);

    PrintStreamArg oFile =
      new PrintStreamArg("-OUT", "Output file",
			 "% Name of matlab .m output file for results.", db);

    DateArg st = 
      new DateArg("-ST", "Starting time for ramp-up",
		  "% Time (such as Nov 2, 1999 11:30:00 AM CST) " + 
		  "at which to start ramp-up." +
		  "  Useful for synchronizing multiple RBEs.", 
		  System.currentTimeMillis()+2000L, db);

    IntArg ru = new IntArg("-RU", "Ramp-up time", 
	       "% Seconds used to warm-up the simulator.", 10*60, db);

    IntArg mi = new IntArg("-MI", "Measurement interval",
	       "% Seconds used for measuring SUT performance.", 30*60, db);

    IntArg rd = new IntArg("-RD", "Ramp-down time", 
	       "% Seconds of steady-state operation following " + 
	       "measurment interval.", 5*60, db);

    DoubleArg slow = 
      new DoubleArg("-SLOW", "Slow-down factor",
		    "% 1000 means one thousand real seconds equals one " + 
		    "simulated second.  " + 
		    "Accepts factional values and E notation.", 1.0, db);

    DoubleArg tt_scale = 
      new DoubleArg("-TT", "Think time multiplication.",
		    "% Used to increase (>1.0) or decrease (<1.0) think time.  " +
		    "In addition to slow-down factor.", 1.0, db);

	 BooleanArg key =
		new BooleanArg("-KEY", "Interactive control.",
			       "% Require user to hit RETURN before every interaction.  Overrides think time.",
			       false, db);

	 BooleanArg getImage =
		new BooleanArg("-GETIM", "Request images.",
							"% True will cause RBE to request images.  False suppresses image requests.",
							true, db);

    IntArg img = new IntArg("-CON", "Image connections",
	       "% Maximum number of images downloaded at once.", 4, db);

    IntArg cust = 
      new IntArg("-CUST", "Number of customers",
		 "% Number of customers in the database.  " + 
		 "Used to generated random CIDs.", 1000, db);

    IntArg custa = 
      new IntArg("-CUSTA", "CID NURand A",
		 "% Used to generate random CIDs.  " + 
		 "See TPC-W Spec. Clause 2.3.2.  " +
		 "-1 means use TPC-W spec. value.", -1, db);

    IntArg item = 
      new IntArg("-ITEM", "Number of items",
		 "% Number of items in the database. " +
		 "Used to generate random searches.", 10000, db);

    IntArg itema = 
      new IntArg("-ITEMA", "Item NURand A",
		 "% Used to generate random searches.  " + 
		 "See TPC-W Spec. Clause 2.10.5.1.  " +
		 "-1 means use TPC-W spec. value.", -1, db);

    IntArg debug = 
      new IntArg("-DEBUG", "Debug message.",
		 "% Increase this to see more debug messages ~1 to 10.",
		 0, db);

    IntArg maxErr = 
      new IntArg("-MAXERROR", "Maximum errors allowed.",
		 "% RBE will terminate after this many errors.  0 implies no limit.",
		 1, db);

	 StringArg wwwArg = 
		new StringArg("-WWW", "Base URL",
						  "% The root URL for the TPC-W pages.",
						  RBE.www1, db);
	BooleanArg monArg = 
	    new BooleanArg("-MONITOR", "Do utilization monitoring", 
			   "% TRUE=do monitoring, FALSE=Don't do monitoring",
			   false, db);
	BooleanArg incrArg = 
	    new BooleanArg("-INCREMENTAL", "Start EBs Incrementally", 
			   "% TRUE=do them in increments, FALSE=Do them all at once",
			   false, db);

    if (args.length==0) {
      Usage(args, db);
      return;
    }

    try {
      db.parse(args);
    }
    catch (Arg.Exception ae) {
      System.out.println("Error:");
      System.out.println(ae);
      Usage(args, db);
      return;
    }

    Usage(args, db);

    // Copy in parameters.
    rbe.maxImageRd  = img.num;
    rbe.numCustomer = cust.num;
    rbe.cidA        = custa.num;
    rbe.numItem     = item.num;
    rbe.numItemA    = itema.num;
    RBE.www1        = wwwArg.s;
    RBE.getImage    = getImage.flag;
    RBE.monitor     = monArg.flag;
    RBE.incremental = incrArg.flag;
    RBE.setURLs();
    EB.DEBUG = debug.num;
    EBStats.maxError = maxErr.num;

    // Check for numCustomer valid. 
    if (rbe.numCustomer < 1) {
      System.out.println("Number of customers (" + rbe.numCustomer + 
			 ")  must be >= 1.");
      return;
    }

    if (rbe.numCustomer > stdCIDA[stdCIDA.length-1][1]) {
      System.out.println("Number of customers (" + rbe.numCustomer + 
			 ")  must be <= " +
			 stdCIDA[stdCIDA.length-1][1] + ".");
      return;
    }

    // Compute default CID A value, if needed.
    if (rbe.cidA == -1) {
      for (i=0;i<stdCIDA.length;i++) {
	if ((rbe.numCustomer >= stdCIDA[i][0]) &&
	    (rbe.numCustomer <= stdCIDA[i][1])) {
	  rbe.cidA = stdCIDA[i][2];
	  System.out.println("Choose " + rbe.cidA + " for -CUSTA.");
	  break;
	}
      }
    }

    // Check num item is valid.
    for (i=0;i<stdNumItemA.length;i++) {
      if (rbe.numItem == stdNumItemA[i][0]) break;
    }

    if (i==stdNumItemA.length) {
      System.out.println("Number of items (" + rbe.numItem + 
		       ") must be one of ");
      for (i=0;i<stdNumItemA.length;i++) {
	System.out.println("    " + stdNumItemA[i][0]);
      }
      return;
    }

    // Compute standard item A value, if needed.
    if (rbe.numItemA == -1) {
      rbe.numItemA = stdNumItemA[i][1];
      System.out.println("Choose " + rbe.numItemA + " for -ITEMA.");
    }

    // Check max image read parameter.
    if (rbe.maxImageRd < 1) {
      System.out.println("-CON must be >= 1.");
      return;
    } 

    // Correct ramp-up time by start time.
    long start = st.d.getTime();
    long addRU = start - System.currentTimeMillis();

    if (addRU < 0L) {
      System.out.println("Warning: start time " + (((double) addRU)/1000.0) + 
			 " seconds before current time.\n" +
			 "Resetting to current time.");
      start = System.currentTimeMillis();
    }

    rbe.slowDown = slow.num;
    rbe.speedUp  = 1/rbe.slowDown;

    // Create statistics object.
    rbe.stats = new EBStats(rbe,
			    60000, 50, 75000, 100, ebfArg.maxState, start,
			    1000L*ru.num, 1000L*mi.num, 1000L*rd.num);
    String pidStr = null;
    if(monitor){
	try {
	    int j;
	    char c[] = new char[10];
	    Runtime r = Runtime.getRuntime();
	    Process proc = r.exec("/usr/local/bin/monitor -log /bigvol2/monitor_traces/java.mon -interval 10 -sample 10 -L -Toplog -F");
	    //	    InputStream i= proc.getInputStream();
	    InputStreamReader reader= new InputStreamReader(proc.getInputStream());
	    reader.skip(40);
	    
	    reader.read(c, 0, 10);
	    for(j = 0; c[j]!= '\n'; j++);
	    pidStr = new String(c,0,j);
	    System.out.println("Pid = " + pidStr);
	    
	} catch(java.lang.Exception ex){
	    System.out.println("Unable to monitor process");
	}
    }

    // Re-initialize EBs.  This is necessary to correctly scale usmd for
    //  the slow-down factor which was not computed when EBs were created.
    for (i=0;i<ebs.size();i++) {
      EB e = (EB) ebs.elementAt(i);
      e.initialize();
      e.tt_scale = tt_scale.num;
      e.waitKey = key.flag;
      e.setName("TPC-W Emulated Broswer " + (i+1));
      e.setDaemon(true);
    }

    // Start EBs...
    System.out.println("Starting " + ebs.size() + " EBs.");

    for (i=0;i<ebs.size();i++) {
      EB e = (EB) ebs.elementAt(i);
      try {
	  if(RBE.incremental && ((i+1)%10 == 0)){
	      System.out.println(i + " EBs Alive!");
	      //Thread.currentThread().sleep((ru.num*1000L)/ebs.size());
	      Thread.currentThread().sleep(10000L);
	  }
	  //	  else {
	  //  Thread.currentThread().sleep((ru.num*1000L)/ebs.size());
	  //}
      }catch(java.lang.Exception ex){
	  System.out.println("Unable to sleep");
      }
      e.start();
    }
    System.out.println("All of the EBs are alive!");

    // Wait for ramp down.
    rbe.stats.waitForRampDown();

    if (monitor){
	try {
	    Runtime r = Runtime.getRuntime();
	    Process proc = r.exec("/bin/kill "+pidStr);
	}
	catch(java.lang.Exception ex){
	    System.out.println("Unable to destroy monitor process");
	}
    }
    System.out.println("Terminating EBs...");
    // Terminate all EBs
    EB.terminate = true;
    try {
      Thread.currentThread().sleep(10000L);
    }
    catch (InterruptedException ie) { System.out.println("Unable to sleep!");}

    for(i=0; i < ebs.size(); i++) {
	EB e = (EB) ebs.elementAt(i);
	System.out.println(e + ": alive: " + e.isAlive());
    }

    for (i=0;i<ebs.size();i++) {
      EB e = (EB) ebs.elementAt(i);
      System.out.println("main thread: About to interrupt: " + i);
      //e.interrupt();
      e.stop();
      try {
	e.join();
      }
      catch (InterruptedException inte) {
	inte.printStackTrace();
	return;
      }
    }

    System.out.println("EBs finished.");

    if (!oFile.set()) {
      oFile.s = System.out;
    }

    oFile.s.println("% Start time: " + startTime);
    oFile.s.println("% System under test: " + www);
    Date endTime = new Date();
    oFile.s.println("% End time: " + endTime);
    oFile.s.println("% Transaction Mix: " + ebfArg.className);
    db.print(oFile.s);
    //ofile.s.println("% Database parameters: num items=" + rbe.numItem + 
    //	    " num customers= " + rbe.numCustomer);
    //oFile.s.println("% Images On?: " + RBE.getImage + 
    //	    " Think Time Scale Factor: " + e.tt_scale); 

    rbe.stats.print(oFile.s);
    oFile.s.close();
    System.out.println("Really finishing RBE!.");
  }

  public static void getKey() {
	 System.out.println("Type RETURN to continue...");
	 try {
		bin.readLine();
	 }
	 catch (java.io.IOException e) {
		e.printStackTrace();		
	 }
  }

  public static void Arguments(String [] args) {
    int a;

    for (a=0;a<args.length;a++) {
      System.out.println("#" + Pad.l(3,""+(a+1)) + "  " + args[a]);
    }
  }

  public static void Usage(String [] args, ArgDB db) {
    System.out.println("Input command-line arguments");
    Arguments(args);

    System.out.println("\nOptions");
    db.print(System.out);
  }

  // Static random functions.

  // Defined in TPC-W Spec Clause 2.1.13
  public static final int NURand(Random rand, int A, int x, int y)
  {
    return((( (nextInt(rand, A+1)) | (nextInt(rand, y-x+1)+x)) % (y-x+1)) +x);
  }

  public final long slow(long t) { return((long) (slowDown*t+0.5)); }
  public final long speed(long t) { return((long) (speedUp*t+0.5)); }

  // Negative exponential distribution used by 
  //  TPC-W spec for Think Time (Clause 5.3.2.1) and USMD (Clause 6.1.9.2)
  public final long negExp(Random rand, long min, double lMin,
			   long max, double lMax,
			   double mu)
  {
    double r = rand.nextDouble();
    
    if (r < lMax) {
      return(slow(max));
    }

	 return(slow((long) (-mu*Math.log(r))));
  }

  // Defined in TPC-W Spec Clause 4.6.2.8
  private static final String [] digS = {
    "BA","OG","AL","RI","RE","SE","AT","UL","IN","NG"
  };
  
  public static String digSyl(int d, int n)
  {
    String s = "";

    if (n==0) return(digSyl(d));

    for (;n>0;n--) {
      int c = d % 10;
      s = digS[c]+s;
      d = d /10;
    }

    return(s);
  }

  public static String digSyl(int d)
  {
    String s = "";

    for (;d!=0;d=d/10) {
      int c = d % 10;
      s = digS[c]+s;      
    }

    return(s);
  }

  // Gets the username and password fields according to TPC-W Spec. 4.6.2.9 ff.
  public static String unameAndPass(int cid)
  {
    String un = digSyl(cid);
    return(field_uname + "=" + un +
	   "&" + field_passwd + "=" + un.toLowerCase());
  }

  // Subject list.  See TPC-W Spec. 4.6.2.11
  public static final String [] subjects = {
    "ARTS",
    "BIOGRAPHIES",
    "BUSINESS",
    "CHILDREN",
    "COMPUTERS",
    "COOKING",
    "HEALTH",
    "HISTORY",
    "HOME",
    "HUMOR",
    "LITERATURE",
    "MYSTERY",
    "NON-FICTION",
    "PARENTING",
    "POLITICS",
    "REFERENCE",
    "RELIGION",
    "ROMANCE",
    "SELF-HELP",
    "SCIENCE-NATURE",
    "SCIENCE-FICTION",
    "SPORTS",
    "YOUTH",
    "TRAVEL"
  };

  // Select a subject string randomly and uniformly from above list.
  //   See TPC-W Spec. 2.10.5.1
  public static String unifSubject(Random rand) {
    return(subjects[nextInt(rand, subjects.length)]);
  }

  // Select a subject string randomly and uniformly from above list.
  //   See TPC-W Spec. 2.10.5.1
  // NOTE:  The "YOUTH" and "TRAVEL" subjects are missing from the home page.
  //   I believe this to be an error, but cannot be sure.
  // Change this function if this is determined to not be an error.
  public static String unifHomeSubject(Random rand) 
    { return(unifSubject(rand)); }

  // Adds a field to a HTTP request.
  //  field name = f, value = v.
  public static String addField(String i, String f, String v)
  {
    if (i.indexOf((int) '?')==-1) {
      // First field
      i = i + '?';
    }
    else {
      // Another additional field.
      i = i + '&';
    }
    i = i + f + "=" + v;

    return(i);
  }

  public static String addSession(String i, String f, String v){
    StringTokenizer tok = new StringTokenizer(i, "?");
    String return_val = null;
    try {
      return_val = tok.nextToken();
      return_val = return_val + f + v;
      return_val = return_val + "?" + tok.nextToken();
    }
    catch (NoSuchElementException e) { 
    }

    return(return_val);
  }

  public static final String [] nchars = { 
	 "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
  };

  public static final String [] achars = { 
	 "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
	 "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
	 "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
	 "u", "v", "w", "x", "y", "z", 
	 "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
	 "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
	 "U", "V", "W", "X", "Y", "Z", 

	 "%21",  // "!"
	 "%40",  // "@"
	 "%23",  // "#"
	 "%24",  // "$"
	 "%25",  // "%"
	 "%5E",  // "^"
	 "%26",  // "&"
	 "*",    // "*"
	 "%28",  // "("
	 "%29",  // ")"
	 //	 "_",    // "_"
	 "%5F", 
	 "-",    // "-"
	 "%3D",  // "="
	 "%2B",  // "+"
	 "%7B",  // "{"
	 "%7D",  // "}"
	 "%5B",  // "["
	 "%5D",  // "]"
	 "%7C",  // "|"
	 "%3A",  // ":"
	 "%3B",  // ";"
	 "%2C",  // ","
	 ".",    // "."
	 "%3F",  // "?"
	 "%2F",  // "/"
	 "%7E",  // "~"
	 "+"     // " "
  };

  public static String astring(Random rand, int min, int max)
  {
	 return(rstring(rand, min,max,achars));
  }

  public static String nstring(Random rand, int min, int max)
  {
	 return(rstring(rand, min,max,nchars));
  }

  private static String rstring(Random rand, int min, int max, String [] cset)
  {
    int l = cset.length;
    int r = nextInt(rand, max-min+1)+min;
    String s;

    for (s="";s.length()<r;s = s + cset[nextInt(rand, l)]);

    return(s);
  }

  public static final String [] countries = {
	 "United+States",                    "United+Kingdom",
	 "Canada",                           "Germany",
	 "France",                           "Japan",
	 "Netherlands",                      "Italy",
	 "Switzerland",                      "Australia",
	 "Algeria",                          "Argentina",
	 "Armenia",                          "Austria",
	 "Azerbaijan",                       "Bahamas",
	 "Bahrain",                          "Bangla+Desh",
	 "Barbados",                         "Belarus",
	 "Belgium",                          "Bermuda",
	 "Bolivia",                          "Botswana",
	 "Brazil",                           "Bulgaria",
	 "Cayman+Islands",                   "Chad",
	 "Chile",                            "China",
	 "Christmas+Island",                 "Colombia",
	 "Croatia",                          "Cuba",
	 "Cyprus",                           "Czech+Republic",
	 "Denmark",                          "Dominican+Republic",
	 "Eastern+Caribbean",                "Ecuador",
	 "Egypt",                            "El+Salvador",
	 "Estonia",                          "Ethiopia",
	 "Falkland+Island",                  "Faroe+Island",
	 "Fiji",                             "Finland",
	 "Gabon",                            "Gibraltar",
	 "Greece",                           "Guam",
	 "Hong+Kong",                        "Hungary",
	 "Iceland",                          "India",
	 "Indonesia",                        "Iran",
	 "Iraq",                             "Ireland",
	 "Israel",                           "Jamaica",
	 "Jordan",                           "Kazakhstan",
	 "Kuwait",                           "Lebanon",
	 "Luxembourg",                       "Malaysia",
	 "Mexico",                           "Mauritius",
	 "New+Zealand",                      "Norway",
	 "Pakistan",                         "Philippines",
	 "Poland",                           "Portugal",
	 "Romania",                          "Russia",
	 "Saudi+Arabia",                     "Singapore",
	 "Slovakia",                         "South+Africa",
	 "South+Korea",                      "Spain",
	 "Sudan",                            "Sweden",
	 "Taiwan",                           "Thailand",
	 "Trinidad",                         "Turkey",
	 "Venezuela",                        "Zambia",
  };

  public static String unifCountry(Random rand) {
	 return(countries[nextInt(rand, countries.length)]);
  }

  public static final Calendar c = new GregorianCalendar(1880,1,1);
  public static final long dobStart = c.getTime().getTime();;
  public static final long dobEnd =  System.currentTimeMillis();;

    /*static {
	 Calendar c = new GregorianCalendar(1880,1,1);
	 dobStart = c.getTime().getTime();

	 // FIXME:  This has some drawbacks.  Assums the day does not change
	 //  during benchmark execution.  Also, does not give the current
	 //  date the same chance as all the other dates.
	 //  In the end, this simplification will not affect anything.
	 dobEnd   = System.currentTimeMillis();
	 
	 }*/

  public static String unifDOB(Random rand) {
	 long t = ((long) (rand.nextDouble()*(dobEnd-dobStart)))+dobStart;
	 Date d = new Date(t);
	 Calendar c = new GregorianCalendar();
	 c.setTime(d);

	 return("" + c.get(Calendar.DAY_OF_MONTH) + "%2f" + 
			  c.get(Calendar.DAY_OF_WEEK) + "%2f" + c.get(Calendar.YEAR));
  }

  public static final String [] ccTypes = {
	 "VISA", "MASTERCARD", "DISCOVER", "DINERS", "AMEX"
  };

  public static String unifCCType(Random rand) {
	 return(ccTypes[nextInt(rand, ccTypes.length)]);
  }

  public static String unifExpDate(Random rand) {
	 Date d = new Date(System.currentTimeMillis() + 
							 ((long) (nextInt(rand, 730))+1)*24L*60L*60L*1000L);
	 Calendar c = new GregorianCalendar();
	 c.setTime(d);

	 return("" + c.get(Calendar.DAY_OF_MONTH) + "%2f" + 
			  c.get(Calendar.DAY_OF_WEEK) + "%2f" + c.get(Calendar.YEAR));

  }

  public static int unifDollars(Random rand) {
	 return (nextInt(rand, 9999)+1);
  }
  public static int unifCents(Random rand) {
	 return (nextInt(rand, 100));
  }

  public static final String [] shipTypes = {
	 "AIR",
	 "UPS",
	 "FEDEX",
	 "SHIP",
	 "COURIER",
	 "MAIL"
  };

  public static String unifShipping(Random rand) {
	 int i = nextInt(rand, shipTypes.length);
	 return(shipTypes[i]);
  }

  // FIXME:  These need to be implemented with specifics from
  //  the data-base/ web-server people.
  public static String unifImage(Random rand) {
	 int i = nextInt(rand, numItem)+1;
	 int grp = i % 100;
	 return(mungeURL("img" + grp + "/image_" + i + ".gif"));
  }

  public static String unifThumbnail(Random rand) {
	 int i = nextInt(rand, numItem)+1;
	 int grp = i % 100;
	 return(mungeURL("img" + grp + "/thumb_" + i + ".gif"));
  }

  // Needed, because Java 1.1 did not have Random.nextInt(int range)
  public static int nextInt(Random rand, int range) {
    int i = Math.abs(rand.nextInt());
    return (i % (range));
  }

  // Finds any non-URLable characters and converts them to URL form.
  //  All alpha numerics are left alone.
  //  <space> goes to '+'
  //  All others go to %xx (hexadecimal representation.)
  public static String mungeURL(String url) {
	 int i;
	 String mURL = "";

	 // System.out.println("Munging: " + url);
	 for (i=0;i<url.length();i++) {
		char ch = url.charAt(i);
		if ( ((ch >= '0') && (ch <='9')) ||
			  ((ch >= 'a') && (ch <='z')) ||
			  ((ch >= 'A') && (ch <='Z')) || 
		     ((ch == '.') || (ch =='/')) ) {
		  mURL = mURL + ch;
		}
		else if ( ch == ' ') {
		  mURL = mURL + '+';
		}
		else {
		  int d = ch;
		  int d1 = d>>4;
		  int d2 = d & 0xf;
		  char c1 = (char) ( (d1 > 9) ? ( 'A'+d1-10 ) : '0'+d1);
		  char c2 = (char) ( (d2 > 9) ? ( 'A'+d2-10 ) : '0'+d2);
		  mURL = mURL + "%" + c1 + c2;
		}
		// System.out.println("munging: " + mURL);
	 }
	 
	 return(mURL);
  }
}
