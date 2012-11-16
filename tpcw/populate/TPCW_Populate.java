/*
 * TPCW_Populate.java - database population program
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


//CAVEAT:
//These TPCW DB Population routines stray from the TPCW Spec in the 
//following ways:
//1. The a_lname field in the AUTHOR table is not generated using the DBGEN
//   utility, because of the current unavailability of this utility.
//2. Ditto for the I_TITLE field of the ITEM table.

import java.io.*;
import java.sql.*;
import java.util.*;
import java.lang.Math.*;

class TPCW_Populate {
    
    private static Connection con;
    private static Random rand;
    
    //These variables are dependent on the JDBC database driver used.
    private static final String driverName = "@jdbc.driver@";
    private static final String dbName = "@jdbc.path@";
    
    //ATTENTION: The NUM_EBS and NUM_ITEMS variables are the only variables
    //that should be modified in order to rescale the DB.
    private static final int NUM_EBS = @num.eb@;
    private static final int NUM_ITEMS = @num.item@;

    private static final int NUM_CUSTOMERS = NUM_EBS * 2880;
    private static final int NUM_ADDRESSES = 2 * NUM_CUSTOMERS;
    private static final int NUM_AUTHORS = (int) (.25 * NUM_ITEMS);
    private static final int NUM_ORDERS = (int) (.9 * NUM_CUSTOMERS);

    //    private static final int NUM_ADDRESSES = 10;
    public static void main(String[] args){
	System.out.println("Beginning TPCW Database population.");
	rand = new Random();
	getConnection();
	deleteTables();
	createTables();
	populateAddressTable();
	populateAuthorTable();
	populateCountryTable();
	populateCustomerTable();
	populateItemTable();
	//Need to debug
	populateOrdersAndCC_XACTSTable();
	addIndexes();
	System.out.println("Done");
	closeConnection();
    }

    private static void addIndexes(){
	System.out.println("Adding Indexes");
	try {
	    PreparedStatement statement1 = con.prepareStatement
		("create index author_a_lname on author(a_lname)");
	    statement1.executeUpdate();
	    PreparedStatement statement2 = con.prepareStatement
		("create index address_addr_co_id on address(addr_co_id)");
	    statement2.executeUpdate();
	    PreparedStatement statement3 = con.prepareStatement
		("create index addr_zip on address(addr_zip)");
	    statement3.executeUpdate();
	    PreparedStatement statement4 = con.prepareStatement
		("create index customer_c_addr_id on customer(c_addr_id)");
	    statement4.executeUpdate();
	    PreparedStatement statement5 = con.prepareStatement
		("create index customer_c_uname on customer(c_uname)");
	    statement5.executeUpdate();
	    PreparedStatement statement6 = con.prepareStatement
		("create index item_i_title on item(i_title)");
	    statement6.executeUpdate();
	    PreparedStatement statement7 = con.prepareStatement
		("create index item_i_subject on item(i_subject)");
	    statement7.executeUpdate();
	    PreparedStatement statement8 = con.prepareStatement
		("create index item_i_a_id on item(i_a_id)");
	    statement8.executeUpdate();
	    PreparedStatement statement9 = con.prepareStatement
		("create index order_line_ol_i_id on order_line(ol_i_id)");
	    statement9.executeUpdate();
	    PreparedStatement statement10 = con.prepareStatement
		("create index order_line_ol_o_id on order_line(ol_o_id)");
	    statement10.executeUpdate();
	    PreparedStatement statement11 = con.prepareStatement
		("create index country_co_name on country(co_name)");
	    statement11.executeUpdate();
	    PreparedStatement statement12 = con.prepareStatement
		("create index orders_o_c_id on orders(o_c_id)");
	    statement12.executeUpdate();
	    PreparedStatement statement13 = con.prepareStatement
		("create index scl_i_id on shopping_cart_line(scl_i_id)");
	    statement13.executeUpdate();

	    con.commit();
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to add indexes");
	    ex.printStackTrace();
	    System.exit(1);
	}
    }
    
    private static void populateCustomerTable(){
	String C_UNAME, C_PASSWD, C_LNAME, C_FNAME;
	int C_ADDR_ID, C_PHONE;
	String C_EMAIL;
	java.sql.Date C_SINCE, C_LAST_LOGIN;
	java.sql.Timestamp C_LOGIN, C_EXPIRATION;
	double C_DISCOUNT, C_BALANCE, C_YTD_PMT;
	java.sql.Date C_BIRTHDATE;
	String C_DATA;
	int i;
	System.out.println("Populating CUSTOMER Table with " + 
			   NUM_CUSTOMERS + " customers");
	System.out.print("Complete (in 10,000's): ");
	try {
	    PreparedStatement statement = con.prepareStatement
	    ("INSERT INTO customer (c_id,c_uname,c_passwd,c_fname,c_lname,c_addr_id,c_phone,c_email,c_since,c_last_login,c_login,c_expiration,c_discount,c_balance,c_ytd_pmt,c_birthdate,c_data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
	    for(i = 1; i <= NUM_CUSTOMERS; i++) {
		if(i%10000 == 0)
		    System.out.print(i/10000 + " ");
		C_UNAME = DigSyl(i, 0);
		C_PASSWD = C_UNAME.toLowerCase();
		C_LNAME = getRandomAString(8,15);
		C_FNAME = getRandomAString(8,15);
		C_ADDR_ID = getRandomInt(1, 2*NUM_CUSTOMERS);
		C_PHONE = getRandomNString(9,16);
		C_EMAIL = C_UNAME+"@"+getRandomAString(2,9)+".com";

		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR,-1*getRandomInt(1,730));
		C_SINCE = new java.sql.Date(cal.getTime().getTime());
		cal.add(Calendar.DAY_OF_YEAR,getRandomInt(0,60)); 
		if(cal.after(new GregorianCalendar()))
		    cal = new GregorianCalendar();
		
		C_LAST_LOGIN = new java.sql.Date(cal.getTime().getTime());
		C_LOGIN = new java.sql.Timestamp(System.currentTimeMillis());
		cal = new GregorianCalendar();
		cal.add(Calendar.HOUR, 2);
		C_EXPIRATION = new java.sql.Timestamp(cal.getTime().getTime());
		
		C_DISCOUNT = (double) getRandomInt(0, 50)/100.0;
		C_BALANCE=0.00;
		C_YTD_PMT = (double) getRandomInt(0, 99999)/100.0;
		int year = getRandomInt(1880, 2000);
		int month = getRandomInt(0,11);
		int maxday = 31;
		int day;
		if (month == 3 | month ==5 | month == 8 | month == 10)
		    maxday = 30;
		else if (month == 1)
		    maxday = 28;
		day = getRandomInt(1, maxday);
		cal = new GregorianCalendar(year,month,day);
		C_BIRTHDATE = new java.sql.Date(cal.getTime().getTime());

		C_DATA = getRandomAString(100,500);

		try {// Set parameter
		    statement.setInt(1, i);
		    statement.setString(2, C_UNAME);
		    statement.setString(3, C_PASSWD);
		    statement.setString(4, C_FNAME);
		    statement.setString(5, C_LNAME);
		    statement.setInt(6, C_ADDR_ID);
		    statement.setInt(7, C_PHONE);
		    statement.setString(8, C_EMAIL);
		    statement.setDate(9, C_SINCE);
		    statement.setDate(10, C_LAST_LOGIN);
		    statement.setTimestamp(11, C_LOGIN);
		    statement.setTimestamp(12, C_EXPIRATION);
		    statement.setDouble(13, C_DISCOUNT);
		    statement.setDouble(14, C_BALANCE);
		    statement.setDouble(15, C_YTD_PMT);
		    statement.setDate(16, C_BIRTHDATE);
		    statement.setString(17, C_DATA);
		    statement.executeUpdate();
		    if(i % 1000 == 0)
			con.commit();
		}catch (java.lang.Exception ex) {
		    System.err.println("Unable to populate CUSTOMER table");
		    System.out.println("C_ID=" + i + " C_UNAME=" + C_UNAME + 
				       " C_PASSWD=" + C_PASSWD + " C_FNAME=" + 
				       C_FNAME + " C_LNAME=" + C_LNAME + " C_ADDR_ID=" 
				       +C_ADDR_ID+ " C_PHONE=" + C_PHONE + " C_EMAIL="
				       + C_EMAIL + " C_SINCE=" + C_SINCE + " C_LAST_LOGIN=" + C_LAST_LOGIN + " C_LOGIN= " + C_LOGIN + " C_EXPIRATION=" + C_EXPIRATION + " C_DISCOUNT=" + C_DISCOUNT + " C_BALANCE=" + C_BALANCE + " C_YTD_PMT" +
				       C_YTD_PMT + "C_BIRTHDATE=" + C_BIRTHDATE 
				       +"C_DATA=" + C_DATA);
		    ex.printStackTrace();
		    System.exit(1);
		}
	    }
	    System.out.print("\n");
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	    System.err.println("Unable to populate CUSTOMER table");
	    ex.printStackTrace();
	    System.exit(1);
	}
    }
    
    private static void populateAddressTable(){
	System.out.println("Populating ADDRESS Table with " + 
			   NUM_ADDRESSES + " addresses");
	System.out.print("Complete (in 10,000's): ");
	String ADDR_STREET1, ADDR_STREET2, ADDR_CITY, ADDR_STATE;
	String ADDR_ZIP;
	int ADDR_CO_ID;
	try {
	    PreparedStatement statement = con.prepareStatement
	    ("INSERT INTO address(addr_id,addr_street1,addr_street2,addr_city,addr_state,addr_zip,addr_co_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
	    for (int i = 1; i <= NUM_ADDRESSES; i++) {
		if(i % 10000 == 0)
		    System.out.print(i/10000+" ");
		ADDR_STREET1 = getRandomAString(15,40);
		ADDR_STREET2 = getRandomAString(15,40);
		ADDR_CITY    = getRandomAString(4,30);
		ADDR_STATE   = getRandomAString(2,20);
		ADDR_ZIP     = getRandomAString(5,10);
		ADDR_CO_ID   = getRandomInt(1,92);
		
		// Set parameter
		statement.setInt(1, i);
		statement.setString(2, ADDR_STREET1);
		statement.setString(3, ADDR_STREET2);
		statement.setString(4, ADDR_CITY);
		statement.setString(5, ADDR_STATE);
		statement.setString(6, ADDR_ZIP);
		statement.setInt(7, ADDR_CO_ID);
		statement.executeUpdate();
		if(i % 1000 == 0)
		    con.commit();
	    }
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	System.err.println("Unable to populate ADDRESS table");
	ex.printStackTrace();
	System.exit(1);
	}
	System.out.print("\n");
    }
    
    private static void populateAuthorTable(){
	String A_FNAME, A_MNAME, A_LNAME, A_BIO;
	java.sql.Date A_DOB;
	GregorianCalendar cal;
	
	System.out.println("Populating AUTHOR Table with " + NUM_AUTHORS +
			   " authors");
	
	try {
	    PreparedStatement statement = con.prepareStatement
		("INSERT INTO author(a_id,a_fname,a_lname,a_mname,a_dob,a_bio) VALUES (?, ?, ?, ?, ?, ?)");
	    for(int i = 1; i <= NUM_AUTHORS; i++){
		int month, day, year, maxday;
		A_FNAME = getRandomAString(3,20);
		A_MNAME = getRandomAString(1,20);
		A_LNAME = getRandomAString(1,20);
		year = getRandomInt(1800, 1990);
		month = getRandomInt(0,11);
		maxday = 31;
		if (month == 3 | month ==5 | month == 8 | month == 10)
		    maxday = 30;
		else if (month == 1)
		    maxday = 28;
		day = getRandomInt(1, maxday);
		cal = new GregorianCalendar(year,month,day);
		A_DOB = new java.sql.Date(cal.getTime().getTime());
		A_BIO = getRandomAString(125, 500);
		// Set parameter
		statement.setInt(1, i);
		statement.setString(2, A_FNAME);
		statement.setString(3, A_LNAME);
		statement.setString(4, A_MNAME);
		statement.setDate(5, A_DOB);
		statement.setString(6, A_BIO);
		statement.executeUpdate();
		if(i % 1000 == 0)
		    con.commit();
		
	    }
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	    System.err.println("Unable to populate AUTHOR table");
	    ex.printStackTrace();
	    System.exit(1);
	}
    }

    private static void populateCountryTable(){
	String[] countries = {"United States","United Kingdom","Canada",
			      "Germany","France","Japan","Netherlands",
			      "Italy","Switzerland", "Australia","Algeria",
			      "Argentina","Armenia","Austria","Azerbaijan",
			      "Bahamas","Bahrain","Bangla Desh","Barbados",
			      "Belarus","Belgium","Bermuda", "Bolivia",
			      "Botswana","Brazil","Bulgaria","Cayman Islands",
			      "Chad","Chile", "China","Christmas Island",
			      "Colombia","Croatia","Cuba","Cyprus",
			      "Czech Republic","Denmark","Dominican Republic",
			      "Eastern Caribbean","Ecuador", "Egypt",
			      "El Salvador","Estonia","Ethiopia",
			      "Falkland Island","Faroe Island", "Fiji", 
			      "Finland","Gabon","Gibraltar","Greece","Guam",
			      "Hong Kong","Hungary", "Iceland","India",
			      "Indonesia","Iran","Iraq","Ireland","Israel",
			      "Jamaica", "Jordan","Kazakhstan","Kuwait",
			      "Lebanon","Luxembourg","Malaysia","Mexico", 
			      "Mauritius", "New Zealand","Norway","Pakistan",
			      "Philippines","Poland","Portugal","Romania", 
			      "Russia","Saudi Arabia","Singapore","Slovakia",
			      "South Africa","South Korea", "Spain","Sudan",
			      "Sweden","Taiwan","Thailand","Trinidad",
			      "Turkey","Venezuela", "Zambia"};

	double[] exchanges = { 1, .625461, 1.46712, 1.86125, 6.24238, 121.907,
			       2.09715, 1842.64, 1.51645, 1.54208, 65.3851,
			       0.998, 540.92, 13.0949, 3977, 1, .3757, 
			       48.65, 2, 248000, 38.3892, 1, 5.74, 4.7304,
			       1.71, 1846, .8282, 627.1999, 494.2, 8.278,
			       1.5391, 1677, 7.3044, 23, .543, 36.0127, 
			       7.0707, 15.8, 2.7, 9600, 3.33771, 8.7,
			       14.9912, 7.7, .6255, 7.124, 1.9724, 5.65822,
			       627.1999, .6255, 309.214, 1, 7.75473, 237.23, 
			       74.147, 42.75, 8100, 3000, .3083, .749481,
			       4.12, 37.4, 0.708, 150, .3062, 1502, 38.3892,
			       3.8, 9.6287, 25.245, 1.87539, 7.83101,
			       52, 37.8501, 3.9525, 190.788, 15180.2, 
			       24.43, 3.7501, 1.72929, 43.9642, 6.25845, 
			       1190.15, 158.34, 5.282, 8.54477, 32.77, 37.1414,
			       6.1764, 401500, 596, 2447.7 };

	String[] currencies = { "Dollars","Pounds","Dollars","Deutsche Marks",
				"Francs","Yen","Guilders","Lira","Francs",
				"Dollars","Dinars","Pesos", "Dram",
				"Schillings","Manat","Dollars","Dinar","Taka",
				"Dollars","Rouble","Francs","Dollars", 
				"Boliviano", "Pula", "Real", "Lev","Dollars",
				"Franc","Pesos","Yuan Renmimbi","Dollars",
				"Pesos","Kuna","Pesos","Pounds","Koruna",
				"Kroner","Pesos","Dollars","Sucre","Pounds",
				"Colon","Kroon","Birr","Pound","Krone",
				"Dollars","Markka","Franc","Pound","Drachmas",
				"Dollars","Dollars","Forint","Krona","Rupees",
				"Rupiah","Rial","Dinar","Punt","Shekels",
				"Dollars","Dinar","Tenge","Dinar","Pounds",
				"Francs","Ringgit","Pesos","Rupees","Dollars",
				"Kroner","Rupees","Pesos","Zloty","Escudo",
				"Leu","Rubles","Riyal","Dollars","Koruna",
				"Rand","Won","Pesetas","Dinar","Krona",
				"Dollars","Baht","Dollars","Lira","Bolivar", 
				"Kwacha"};
	
	int NUM_COUNTRIES = 92;
	
	System.out.println("Populating COUNTRY with " + NUM_COUNTRIES+ 
			   " countries");
	
	try {
	    PreparedStatement statement = con.prepareStatement
		("INSERT INTO country(co_id,co_name,co_exchange,co_currency) VALUES (?,?,?,?)");
	    for(int i = 1; i <= NUM_COUNTRIES; i++){
		// Set parameter
		statement.setInt(1, i);
		statement.setString(2, countries[i-1]);
		statement.setDouble(3, exchanges[i-1]);
		statement.setString(4, currencies[i-1]);
		statement.executeUpdate();
	    }
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	    System.err.println("Unable to populate COUNTRY table");
	    ex.printStackTrace();
	    System.exit(1);
	}
    }

    private static void populateItemTable() {
	String I_TITLE;
	GregorianCalendar cal;
	int I_A_ID;
	java.sql.Date I_PUB_DATE;
	String I_PUBLISHER, I_SUBJECT, I_DESC;
	int I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5;
	String I_THUMBNAIL, I_IMAGE;
	double I_SRP, I_COST;
	java.sql.Date I_AVAIL;
        int I_STOCK;
	String I_ISBN;
	int I_PAGE;
	String I_BACKING;
	String I_DIMENSIONS;

	String[] SUBJECTS = { "ARTS", "BIOGRAPHIES", "BUSINESS", "CHILDREN",
			      "COMPUTERS", "COOKING", "HEALTH", "HISTORY",
			      "HOME", "HUMOR", "LITERATURE", "MYSTERY",
			      "NON-FICTION", "PARENTING", "POLITICS",
			      "REFERENCE", "RELIGION", "ROMANCE", 
			      "SELF-HELP", "SCIENCE-NATURE", "SCIENCE-FICTION",
			      "SPORTS", "YOUTH", "TRAVEL"};
	int NUM_SUBJECTS = 24;

	String[] BACKINGS = { "HARDBACK", "PAPERBACK", "USED", "AUDIO",
			      "LIMITED-EDITION"};
	int NUM_BACKINGS = 5;
	
	System.out.println("Populating ITEM table with " + NUM_ITEMS + 
			   " items");
	try {
	    PreparedStatement statement = con.prepareStatement
		("INSERT INTO item ( i_id, i_title , i_a_id, i_pub_date, i_publisher, i_subject, i_desc, i_related1, i_related2, i_related3, i_related4, i_related5, i_thumbnail, i_image, i_srp, i_cost, i_avail, i_stock, i_isbn, i_page, i_backing, i_dimensions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    for (int i = 1; i <= NUM_ITEMS; i++) {
		int month,day,year,maxday;
		I_TITLE= getRandomAString(14,60);
		if(i <= (NUM_ITEMS/4))
		    I_A_ID = i;
		else 
		    I_A_ID = getRandomInt(1, NUM_ITEMS/4);

		year = getRandomInt(1930, 2000);
		month = getRandomInt(0,11);
		maxday = 31;
		if (month == 3 | month ==5 | month == 8 | month == 10)
		    maxday = 30;
		else if (month == 1)
		    maxday = 28;
		day = getRandomInt(1, maxday);
		cal = new GregorianCalendar(year,month,day);
		I_PUB_DATE = new java.sql.Date(cal.getTime().getTime());

		I_PUBLISHER = getRandomAString(14,60);
		I_SUBJECT = SUBJECTS[getRandomInt(0,NUM_SUBJECTS-1)];
		I_DESC = getRandomAString(100,500);
		
		I_RELATED1 = getRandomInt(1, NUM_ITEMS);
		do {
		    I_RELATED2 = getRandomInt(1, NUM_ITEMS);
		} while(I_RELATED2 == I_RELATED1);
		do {
		    I_RELATED3 = getRandomInt(1, NUM_ITEMS);
		} while(I_RELATED3 == I_RELATED1 || I_RELATED3 == I_RELATED2);
		do {
		    I_RELATED4 = getRandomInt(1, NUM_ITEMS);
		} while(I_RELATED4 == I_RELATED1 || I_RELATED4 == I_RELATED2
			|| I_RELATED4 == I_RELATED3);
		do {
		    I_RELATED5 = getRandomInt(1, NUM_ITEMS);
		} while(I_RELATED5 == I_RELATED1 || I_RELATED5 == I_RELATED2
			|| I_RELATED5 == I_RELATED3 ||
			I_RELATED5 == I_RELATED4);
	       
		I_THUMBNAIL = new String("img"+i%100+"/thumb_"+i+".gif");
		I_IMAGE = new String("img"+i%100+"/image_"+i+".gif");
		I_SRP = (double) getRandomInt(100, 99999);
		I_SRP/=100.0;
		
		I_COST = I_SRP-((((double)getRandomInt(0, 50)/100.0))*I_SRP);

		cal.add(Calendar.DAY_OF_YEAR, getRandomInt(1,30));
		I_AVAIL = new java.sql.Date(cal.getTime().getTime());
		I_STOCK = getRandomInt(10,30);
		I_ISBN = getRandomAString(13);
		I_PAGE = getRandomInt(20,9999);
		I_BACKING = BACKINGS[getRandomInt(0, NUM_BACKINGS-1)];
		I_DIMENSIONS= ((double) getRandomInt(1,9999)/100.0) +"x"+
		    ((double) getRandomInt(1,9999)/100.0) + "x" +
		    ((double) getRandomInt(1,9999)/100.0);

		// Set parameter
		statement.setInt(1, i);
		statement.setString(2, I_TITLE);
		statement.setInt(3, I_A_ID);
		statement.setDate(4, I_PUB_DATE);
		statement.setString(5, I_PUBLISHER);
		statement.setString(6, I_SUBJECT);
		statement.setString(7, I_DESC);
		statement.setInt(8, I_RELATED1);
		statement.setInt(9, I_RELATED2);
		statement.setInt(10, I_RELATED3);
		statement.setInt(11, I_RELATED4);
		statement.setInt(12, I_RELATED5);
		statement.setString(13, I_THUMBNAIL);
		statement.setString(14, I_IMAGE);
		statement.setDouble(15, I_SRP);
		statement.setDouble(16, I_COST);
		statement.setDate(17, I_AVAIL);
		statement.setInt(18, I_STOCK);
		statement.setString(19, I_ISBN);
		statement.setInt(20, I_PAGE);
		statement.setString(21, I_BACKING);
		statement.setString(22, I_DIMENSIONS);

		statement.executeUpdate();
		if(i % 1000 == 0)
		    con.commit();
	    }
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	System.err.println("Unable to populate ITEM table");
	ex.printStackTrace();
	System.exit(1);
	}
    }

    private static void populateOrdersAndCC_XACTSTable(){
	GregorianCalendar cal;
	String[] credit_cards = {"VISA", "MASTERCARD", "DISCOVER", 
	                          "AMEX", "DINERS"};
	int num_card_types = 5;
	String[] ship_types = {"AIR", "UPS", "FEDEX", "SHIP", "COURIER",
			       "MAIL"};
	int num_ship_types = 6;
	
	String[] status_types = {"PROCESSING", "SHIPPED", "PENDING", 
				 "DENIED"};
	int num_status_types = 4;

	//Order variables
	int O_C_ID;
	java.sql.Timestamp O_DATE;
	double O_SUB_TOTAL;
	double O_TAX;
	double O_TOTAL;
	String O_SHIP_TYPE;
	java.sql.Timestamp O_SHIP_DATE;
	int O_BILL_ADDR_ID, O_SHIP_ADDR_ID;
	String O_STATUS;

	String CX_TYPE;
	int CX_NUM;
	String CX_NAME;
        java.sql.Date CX_EXPIRY;
	String CX_AUTH_ID;
	double CX_XACT_AMT;
	int CX_CO_ID;
	
	System.out.println("Populating ORDERS, ORDER_LINES, CC_XACTS with "
			   + NUM_ORDERS + " orders");
	
	System.out.print("Complete (in 10,000's): ");
	try {
	    PreparedStatement statement = con.prepareStatement
		    ("INSERT INTO orders(o_id, o_c_id, o_date, o_sub_total, o_tax, o_total, o_ship_type, o_ship_date, o_bill_addr_id, o_ship_addr_id, o_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	    PreparedStatement statement2 = con.prepareStatement
		    ("INSERT INTO order_line (ol_id, ol_o_id, ol_i_id, ol_qty, ol_discount, ol_comments) VALUES (?, ?, ?, ?, ?, ?)");
	    PreparedStatement statement3 = con.prepareStatement
		    ("INSERT INTO cc_xacts(cx_o_id,cx_type,cx_num,cx_name,cx_expire,cx_auth_id,cx_xact_amt,cx_xact_date,cx_co_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

	    for(int i = 1; i <= NUM_ORDERS; i++){
		if(i%10000 == 0)
		    System.out.print(i/10000 + " ");
		int num_items = getRandomInt(1,5);
		O_C_ID = getRandomInt(1, NUM_CUSTOMERS);
		cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -1*getRandomInt(1,60));
		O_DATE = new java.sql.Timestamp(cal.getTime().getTime());
		O_SUB_TOTAL = (double) getRandomInt(1000, 999999)/100;
		O_TAX = O_SUB_TOTAL * 0.0825;
		O_TOTAL = O_SUB_TOTAL + O_TAX + 3.00 + num_items;
		O_SHIP_TYPE = ship_types[getRandomInt(0,num_ship_types-1)];
		cal.add(Calendar.DAY_OF_YEAR, getRandomInt(0,7));
		O_SHIP_DATE = new java.sql.Timestamp(cal.getTime().getTime());

		O_BILL_ADDR_ID = getRandomInt(1, 2*NUM_CUSTOMERS);
		O_SHIP_ADDR_ID = getRandomInt(1, 2*NUM_CUSTOMERS);
		O_STATUS = status_types[getRandomInt(0,num_status_types-1)];
		
		// Set parameter
		statement.setInt(1, i);
		statement.setInt(2, O_C_ID);
		statement.setTimestamp(3, O_DATE);
		statement.setDouble(4, O_SUB_TOTAL);
		statement.setDouble(5, O_TAX);
		statement.setDouble(6, O_TOTAL);
		statement.setString(7, O_SHIP_TYPE);
		statement.setTimestamp(8, O_SHIP_DATE);
		statement.setInt(9, O_BILL_ADDR_ID);
		statement.setInt(10, O_SHIP_ADDR_ID);
		statement.setString(11, O_STATUS);
		statement.executeUpdate();

		for(int j = 1; j <= num_items; j++){
		    int OL_ID = j;
		    int OL_O_ID = i;
		    int OL_I_ID = getRandomInt(1, NUM_ITEMS);
		    int OL_QTY = getRandomInt(1, 300);
		    double OL_DISCOUNT = (double) getRandomInt(0,30)/100;
		    String OL_COMMENTS = getRandomAString(20,100);
		    statement2.setInt(1, OL_ID);
		    statement2.setInt(2, OL_O_ID);
		    statement2.setInt(3, OL_I_ID);
		    statement2.setInt(4, OL_QTY);
		    statement2.setDouble(5, OL_DISCOUNT);
		    statement2.setString(6, OL_COMMENTS);
		    statement2.executeUpdate();
		}
		
		CX_TYPE = credit_cards[getRandomInt(0, num_card_types-1)];
		CX_NUM = getRandomNString(16);
		CX_NAME = getRandomAString(14,30);
		cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, getRandomInt(10, 730));
		CX_EXPIRY = new java.sql.Date(cal.getTime().getTime());
		CX_AUTH_ID = getRandomAString(15);	      		
		CX_CO_ID = getRandomInt(1,92);
		statement3.setInt(1, i);
		statement3.setString(2, CX_TYPE);
		statement3.setInt(3, CX_NUM);
		statement3.setString(4, CX_NAME);
		statement3.setDate(5, CX_EXPIRY);
		statement3.setString(6, CX_AUTH_ID);
		statement3.setDouble(7, O_TOTAL);
		statement3.setTimestamp(8, O_SHIP_DATE);
		statement3.setInt(9, CX_CO_ID);
		statement3.executeUpdate();
		
		if(i % 1000 == 0)
		    con.commit();
	    }
	    con.commit();
	}
	catch (java.lang.Exception ex) {
	    System.err.println("Unable to populate CC_XACTS table");
	    ex.printStackTrace();
	    System.exit(1);
	}
	System.out.print("\n");
    }
     
    private static void getConnection(){
	try {
	    Class.forName(driverName);
	    con = DriverManager.getConnection(dbName);
	    con.setAutoCommit(false);
	}
	catch (java.lang.Exception ex) {
	    ex.printStackTrace();
	}
    }  
  
    private static void closeConnection(){
	try {
	    con.close();
	} catch (java.lang.Exception ex) {
	    ex.printStackTrace();
	}
    }
  
    private static void deleteTables(){
	int i;
	String[] tables = {"address", "author", "cc_xacts",
			   "country", "customer", "item",
			   "order_line", "orders",
			   "shopping_cart", 
			   "shopping_cart_line"};
	int numTables = 10;

	for(i = 0; i < numTables; i++){
	    try {
		//Delete each table listed in the tables array
		PreparedStatement statement = con.prepareStatement
		    ("DROP TABLE " + tables[i]);
		statement.executeUpdate();
		con.commit();
		System.out.println("Dropped table " + tables[i]);
	    } catch (java.lang.Exception ex) {
		System.out.println("Already dropped table " + tables[i]);
	    }
	    
	}
       System.out.println("Done deleting tables!");
    }

    private static void createTables(){
	int i;

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE address ( addr_id int not null, addr_street1 varchar(40), addr_street2 varchar(40), addr_city varchar(30), addr_state varchar(20), addr_zip varchar(10), addr_co_id int, PRIMARY KEY(addr_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table ADDRESS");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: ADDRESS");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE author ( a_id int not null, a_fname varchar(20), a_lname varchar(20), a_mname varchar(20), a_dob date, a_bio @sql.bigCharType@, PRIMARY KEY(a_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table AUTHOR");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: AUTHOR");
	    ex.printStackTrace();
	    System.exit(1);
	}
	
	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE cc_xacts ( cx_o_id int not null, cx_type varchar(10), cx_num varchar(20), cx_name varchar(30), cx_expire date, cx_auth_id char(15), cx_xact_amt double, cx_xact_date date, cx_co_id int, PRIMARY KEY(cx_o_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table CC_XACTS");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: CC_XACTS");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE country ( co_id int not null, co_name varchar(50), co_exchange double, co_currency varchar(18), PRIMARY KEY(co_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table COUNTRY");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: COUNTRY");
	    ex.printStackTrace();
	    System.exit(1);
	}
	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE customer ( c_id int not null, c_uname varchar(20), c_passwd varchar(20), c_fname varchar(17), c_lname varchar(17), c_addr_id int, c_phone varchar(18), c_email varchar(50), c_since date, c_last_login date, c_login timestamp, c_expiration timestamp, c_discount real, c_balance double, c_ytd_pmt double, c_birthdate date, c_data @sql.bigCharType@, PRIMARY KEY(c_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table CUSTOMER");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: CUSTOMER");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE item ( i_id int not null, i_title varchar(60), i_a_id int, i_pub_date date, i_publisher varchar(60), i_subject varchar(60), i_desc @sql.bigCharType@, i_related1 int, i_related2 int, i_related3 int, i_related4 int, i_related5 int, i_thumbnail varchar(40), i_image varchar(40), i_srp double, i_cost double, i_avail date, i_stock int, i_isbn char(13), i_page int, i_backing varchar(15), i_dimensions varchar(25), PRIMARY KEY(i_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table ITEM");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: ITEM");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE order_line ( ol_id int not null, ol_o_id int not null, ol_i_id int, ol_qty int, ol_discount double, ol_comments varchar(110), PRIMARY KEY(ol_id, ol_o_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table ORDER_LINE");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: ORDER_LINE");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE orders ( o_id int not null, o_c_id int, o_date date, o_sub_total double, o_tax double, o_total double, o_ship_type varchar(10), o_ship_date date, o_bill_addr_id int, o_ship_addr_id int, o_status varchar(15), PRIMARY KEY(o_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table ORDERS");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: ORDERS");
	    ex.printStackTrace();
	    System.exit(1);
	}

	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE shopping_cart ( sc_id int not null, sc_time timestamp, PRIMARY KEY(sc_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table SHOPPING_CART");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: SHOPPING_CART");
	    ex.printStackTrace();
	    System.exit(1);
	}
	try {
	    PreparedStatement statement = con.prepareStatement
		("CREATE TABLE shopping_cart_line ( scl_sc_id int not null, scl_qty int, scl_i_id int not null, PRIMARY KEY(scl_sc_id, scl_i_id))");
	    statement.executeUpdate();
	    con.commit();
	    System.out.println("Created table SHOPPING_CART_LINE");
	} catch (java.lang.Exception ex) {
	    System.out.println("Unable to create table: SHOPPING_CART_LINE");
	    ex.printStackTrace();
	    System.exit(1);
	}
	
        System.out.println("Done creating tables!");

    }

    //UTILITY FUNCTIONS BEGIN HERE
    private static String getRandomAString(int min, int max){
	String newstring = new String();
	int i;
	final char[] chars = {'a','b','c','d','e','f','g','h','i','j','k',
			      'l','m','n','o','p','q','r','s','t','u','v',
			      'w','x','y','z','A','B','C','D','E','F','G',
			      'H','I','J','K','L','M','N','O','P','Q','R',
			      'S','T','U','V','W','X','Y','Z','!','@','#',
			      '$','%','^','&','*','(',')','_','-','=','+',
			      '{','}','[',']','|',':',';',',','.','?','/',
			      '~',' '}; //79 characters
	int strlen = (int) Math.floor(rand.nextDouble()*((max-min)+1));
	strlen += min;
	for(i = 0; i < strlen; i++){
	    char c = chars[(int) Math.floor(rand.nextDouble()*79)];
	    newstring = newstring.concat(String.valueOf(c));
	}
	return newstring;
    }
    
    private static String getRandomAString(int length){
	String newstring = new String();
	int i;
	final char[] chars = {'a','b','c','d','e','f','g','h','i','j','k',
			      'l','m','n','o','p','q','r','s','t','u','v',
			      'w','x','y','z','A','B','C','D','E','F','G',
			      'H','I','J','K','L','M','N','O','P','Q','R',
			      'S','T','U','V','W','X','Y','Z','!','@','#',
			      '$','%','^','&','*','(',')','_','-','=','+',
			      '{','}','[',']','|',':',';',',','.','?','/',
			      '~',' '}; //79 characters
	for(i = 0; i < length; i++){
	    char c = chars[(int) Math.floor(rand.nextDouble()*79)];
	    newstring = newstring.concat(String.valueOf(c));
	}
	return newstring;
    }

    private static int getRandomNString(int num_digits){
	int return_num = 0;
	for(int i = 0; i < num_digits; i++){
	    return_num += getRandomInt(0, 9) * 
		(int) java.lang.Math.pow(10.0, (double) i);
	}
	return return_num;
    }

    private static int getRandomNString(int min, int max){
	int strlen = (int) Math.floor(rand.nextDouble()*((max-min)+1)) + min;
	return getRandomNString(strlen);
    }
    
    private static int getRandomInt(int lower, int upper){
	
	int num = (int) Math.floor(rand.nextDouble()*((upper+1)-lower));
	if(num+lower > upper || num+lower < lower){
	    System.out.println("ERROR: Random returned value of of range!");
	    System.exit(1);
	}
	return num + lower;
    }
  
    private static String DigSyl(int D, int N){
	int i;
	String resultString = new String();
	String Dstr = Integer.toString(D);
	
	if(N > Dstr.length()){
	    int padding = N - Dstr.length();
	    for(i = 0; i < padding; i++)
		resultString = resultString.concat("BA");
	}
	
	for(i = 0; i < Dstr.length(); i++){
	    if(Dstr.charAt(i) == '0')
		resultString = resultString.concat("BA");
	    else if(Dstr.charAt(i) == '1')
		resultString = resultString.concat("OG");
	    else if(Dstr.charAt(i) == '2')
		resultString = resultString.concat("AL");
	    else if(Dstr.charAt(i) == '3')
		resultString = resultString.concat("RI");
	    else if(Dstr.charAt(i) == '4')
		resultString = resultString.concat("RE");
	    else if(Dstr.charAt(i) == '5')
		resultString = resultString.concat("SE");
	    else if(Dstr.charAt(i) == '6')
		resultString = resultString.concat("AT");
	    else if(Dstr.charAt(i) == '7')
		resultString = resultString.concat("UL");
	    else if(Dstr.charAt(i) == '8')
		resultString = resultString.concat("IN");
	    else if(Dstr.charAt(i) == '9')
		resultString = resultString.concat("NG");
	}
	
	return resultString;
    }
}

