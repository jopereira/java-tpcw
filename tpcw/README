The Java TPC-W Distribution version 1.0
====================================================================

This TPC-W distribution was orginally written as part of Prof. Mikko
Lipasti's ECE 902 course at the University of Wisconsin-Madison. The
students involved in this effort were: Todd Bezenek, Harold Cain, Ross
Dickson, Timothy Heil, Milo Martin, Collin McCurdy, Ravi Rajwar, Eric
Weglarz, and Craig Zilles.

Please see the file COPYRIGHT for legal information regarding use and
restrictions of this code.

CAVEAT: 

This TPC-W implementation strays from the official TPC-W
specification, version 1.0.1, in the following ways:

1. The TPC-W database population program does not  use the DBGEN utility
for generating random author names and book titles.

2. The buy confirm web interaction does not include communication with
an external payment gateway emulator (PGE). Also, we do not provide an
implementation of this PGE.

To the best of our knowledge, this implementation is fully consistent in 
all other ways with the official TPC-W specification version 1.0.1.

INSTALLATION:

The process of setting up TPC-W takes the following steps:

1. Install the Java Virtual Machine of your choice, as well as the 
   Java Servlet Development Kit (JSDK) 2.0 or later
2. Install database of your choice. This distribution has only been 
   tested using IBM DB2 v. 6.1
3. Populate the database
   a. Set the NUM_EBS, NUM_ITEMS, dbName, and driverName variables 
      appropriately in the file TPCW_Populate.java
   b. Compile the TPCW_Populate program, using the Makefile in the populate
      directory
   c. Run the TPCW_Populate program. This will take several hours, 
      depending on the size of your database.

4. Build the TPC provided image creation program, which resides in the
   ImgGen directory

5. Run the image population script, which creates an image for every item
   in the database.
   a. Make sure perl is installed
   b. Change the variables $NUM_ITEMS and $DEST_DIR in the file 
      populate/populate_images sctript for your specific installation 
   c. Run the populate/populate_images script

6. Copy all of the image files from the images directory into the
   same directory where the image population script created all of the
   images.

7. Build the java servlets
   a. Edit the makefile in the servlets directory, pointing the TPCW_HOME,
      SERVLET_HOME, and CLASS_HOME directories to the correct location
      for your particular web server. For most web servers, the correct
      location for the servlets is in a directory called servlets, and
      the correct location for other miscellaneous classes is in a directory
      called classes.
   b. Make sure that your CLASSPATH points to a current version of the JSDK.
      We have tested this implementation with JSDK version 2.0
   c. Edit the file servlets/TPCW_Database.java, modifying the variables 
      "driverName" and "jdbcPath" for your particular DB installation.
   d. Build the servlets, using servlets/Makefile

8. Build the RBE
   a. javac rbe/* from the root tpcw directory. Make sure the . is in your
      CLASSPATH.

9. Run the RBE, see the runtpcw file for examples

DIRECTORY STRUCTURE:

ImgGen/ - Contains the random image generator program provided by the TPC.

images/ - Contains the standard images for the TPC-W web pages, like navigation
	  buttons, logos, etc.

matlab/ -This directory contains a set of matlab scripts which can be used 
         to visualize statistics regarding a single TPC-W run. These scripts
         make use of a matlab file created by the RBE.

populate/ - This directory contains a Java program which can be used to 
            populate the DB2 database, called TPCW_Populate.java. This
            program abides by all of the TPC specified database
            scaling rules. Before populating a database, one must first create
            a database (which must have support for the JDBC
            interface). Before running the TPCW_Populate program, you will
            need to set the NUM_EBS and NUM_ITEMS variables, which are used
            to set the appropriate DB size. (See clause 4.3 from the TPC-W
            specification for more information on database scaling).

            You will also want to change the driverName and dbName 
            variables to suit your specific database installation.

rbe/ - This directory contains all of the source code for the remote browser
       implementation. In order to build the rbe, run "javac rbe/*" from the  
       tpcw installation directory. A sample command line for running the 
       rbe is provided in the file "runtpcw".

       More information on the RBE can be found in the README file inside
       the rbe directory.
 
       Depending on how your web server encodes session ids (using URL
       rewriting) you may need to change the "yourSessionID" variable in
       the file RBE.java.

servlets/ - This directory contains the source code for all of the TPC-W
       servlets, and the support classes that they use. The makefile in this 
       directory can be used for building these classes. Before running the 
       makefile, you will want to edit it, pointing the TPCW_HOME, 
       SERVLET_HOME, and CLASS_HOME variables to the appropriate directories 
       for your web server. This distribution has been tested with the Apache
       Jserv, Sun Java Web Server 2.0, IBM WebSphere Standard Edition, and 
       Jigsaw web servers.


PECULIARTIES OF THIS IMPLEMENTATION:

We do not use cookies to implement session tracking, we instead do URL
rewriting for each page. Because of this, cookies must be turned off
while browsing the TPC-W web pages using a standard web browser (Netscape).

Our implementation stores the shopping cart in the database, because the
shopping cart must stay persistent across any single point of failure 
according to the specification.

Please direct comments/questions to cain@cs.wisc.edu, however bear in mind that
support for this release will be limited.
