README

TPC-W RBE

This Java code implements the remote browser emulator (RBE) for TPC-W,
following the draft specification.

This README as the following sections.

I.   Running
II.  Hacking
III. To-Do

I. Running

The main program is rbe.RBE.  To run, use:

  java rbe.RBE <arguments>.

Make sure your CLASSPATH environment variable includes the directory which
contains the rbe directory.

An example run command is:

  java rbe.RBE -EB rbe.EBTPCW1Factory 1 -OUT run1.m -RU 1 -MI 5 -RD 1 -WWW file:../html/

This will run 1 emulated browsers (EBs) for the TPC-W Browsing Mix.

Different interaction mixes can be selected by varying the EBTPCWFactory
class that is used. 

Browsing Mix = rbe.EBTPCW1Factory
Shopping Mix = rbe.EBTPCW2Factory
Ordering Mix = rbe.EBTPCW3Factory

Supplying no arguments, or supply erroneous arguments will provide the
following usage message.  The options are described in more detail below.

Options
     -EB                EB Factory             required
         Factory class used to create EBs.  <class> <#> <factory args...>.
    -OUT               Output file             required
         Name of matlab .m output file for results.
     -ST Starting time for ramp-up December 15, 1999 7:32:57 PM CST (default)
         Time (such as Nov 2, 1999 11:30:00 AM CST) at which to start ramp-up.  Useful for synchronizing multiple RBEs.
     -RU              Ramp-up time        600 (default)
         Seconds used to warm-up the simulator.
     -MI      Measurement interval       1800 (default)
         Seconds used for measuring SUT performance.
     -RD            Ramp-down time        300 (default)
         Seconds of steady-state operation following measurment interval.
   -SLOW          Slow-down factor        1.0 (default)
         1000 means one thousand real seconds equals one simulated second.  Accepts factional values and E notation.
     -TT Think time multiplication.        1.0 (default)
         Used to increase (>1.0) or decrease (<1.0) think time.  In addition to slow-down factor.
    -KEY      Interactive control.      false (default)
         Require user to hit RETURN before every interaction.  Overrides think time.
  -GETIM           Request images.       true (default)
         True will cause RBE to request images.  False suppresses image requests.
    -CON         Image connections          4 (default)
         Maximum number of images downloaded at once.
   -CUST       Number of customers       1000 (default)
         Number of customers in the database.  Used to generated random CIDs.
  -CUSTA              CID NURand A         -1 (default)
         Used to generate random CIDs.  See TPC-W Spec. Clause 2.3.2.  -1 means use TPC-W spec. value.
   -ITEM           Number of items      10000 (default)
         Number of items in the database. Used to generate random searches.
  -ITEMA             Item NURand A         -1 (default)
         Used to generate random searches.  See TPC-W Spec. Clause 2.10.5.1.  -1 means use TPC-W spec. value.
    -WWW                  Base URL http://ironsides.cs.wisc.edu:8001/ (default)
         The root URL for the TPC-W pages.

Detailed option descriptions

-EB <class> <#> <factory args...>

The RBE uses "factories" to produce many EBs, which then drive the
system-under-test (SUT).  A factory is just a Java class designed to
spit out EB objects.  You give the name of the factory class to use on
the command line.  Java's dynamic code-loading ability is used to
find, load, and use the factory class.  The next parameter is the
number of EBs that the factory should create.  Following this may come
factory-specific arguments.

The standard WIPS TPCW factory has one optional argument which can be
used to disable states (pages), and the transitions to them.  The
format of this argument is a string of 15 characters, each
representing a web page.  An underscore (`_`) character disables the
state.  Any other character leaves the state enabled.  The order of
the states can be see in EBTPCWFactory.java and is:

init:   Initial state.  Do *not* disable this state.
admc:   Administration confirmation
admr:   Administration request
bess:   Best sellers page
buyc:   Buy confirm page
buyr:   Buy request page
creg:   Customer registrations page
home:   Home page.  Probably shouldn't disable this one.
newp:   New products page
ordd:   Order display page
ordi:   Order inquiry page
prod:   Product detail page
sreq:   Search request page
sres:   Search result page
shop:   Shopping cart page

By default, all pages are enabled.

Example: Everything on but the administrative stuff.

   -EB EBTPCWFactory 10 I__BBBCHNOOPSSS

-OUT Name of matlab .m output file for results.

The RBE dumps out an output file in the format of Matlabs M-script.
It is relatively human-readable ASCII text.  The script is a Matlab function
that produces a data-structure containing all relavent simulation data.
I have other Matlab functions for plotting WIPS, WIRT, etc., based on
this structure.

-RU  Seconds used to warm-up the simulator.

The TPC-W spec requires that some time (a minimum of 10 minutes, the
default value) be used to warm-up the SUT, before benchmarking can begin.
This parameter allows the warm-up (or "ramp-up") time to be adjusted.

-MI  Seconds used for measuring SUT performance.

This is the period where the SUT is being benchmarked. This period immediately
follows the ramp-up period.

-RD  Seconds of steady-state operation following measurment interval.

The TPC-W spec requies that the SUT be held in a steady-state for some
time (a minimum of 5 minutes, the default) following the measurement
interval.  The parameter can be used to adjust this time.

-ST  Time (such as Nov 2, 1999 11:30:00 AM CST) at which to start ramp-up.  
       Useful for synchronizing multiple RBEs.

This parameter indicates when (in absolute time) to start counting the
ramp-up period.  This is intended to make it easier to start multiple RBEs
on several differnt machines, and have them all approximately synchronized.
The input format for the date and time is pretty unyeilding, so follow the
suggested format exactly.  Also place quotes around the date and time, as
the RBE expects it as a single command line argument.

   -SLOW          Slow-down factor        1.0 (default)
         1000 means one thousand real seconds equals one simulated second.  Accepts factional values and E notation.

How much slower the simulator is running the SUT.

     -TT Think time multiplication.        1.0 (default)

This parameter allows you to increase or decrease the think times.  The
default value of 1.0 will cause the EBs to wait between 7s and 70s.  The
think time is multiplied by the -TT parameter, so -TT 0.01 will cause
think times to range from 0.07s to 0.7s.  Note that the think times must
be in simulated time, so the think times are also multiplied by the 
slow-down factor (see -SLOW above).

    -CON Maximum number of images downloaded at once.
   -CUST Number of customers in the database.  Used to generated random CIDs.
  -CUSTA Used to generate random CIDs.  See TPC-W Spec. Clause 2.3.2.  -1 means use TPC-W spec. value.
   -ITEM Number of items in the database. Used to generate random searches.
  -ITEMA Used to generate random searches.  See TPC-W Spec. Clause 2.10.5.1.  -1 means use TPC-W spec. value.

I think the above are self-explanatory.

    -WWW The root URL for the TPC-W pages.

Point this at the top of the tpcw pages.  Other page URLs are derived
by concatonating a page-specific string (e.g. home.html) to this one.
Include the close "/".  An example set of HTML that the RBE can work
with is provided in ../html.  Point RBE at that.  Relative path names
seem to work.  Example: 
  
  -WWW file:../html/

II. Hacking

The RBE is structured as follows. The most important classes are all
in the rbe package.

* RBE.java

The main rbe.RBE class produces the EB objects, collects statistics
and provides random generation functions and string constants
particular to TPCW, and general controls the whole sh-bang.

* EBStats.java

The statistics are collected in rbe.EBStats. EBStats handles the
multi-threaded synchronization, accumulation, and producing the .m file.

* EB.java

The rbe.EB class is a single EB thread.  It produces all URL requests, and
receives and parses all HTML.  The HTML parsing is a little fragile, so there
may be some problems with getting the web-server to produce exactly what
the EBs expect.  This can be easily changed or extended, however.

The static DEBUG variable in EB controls alot of the debugging messages that
the EB spits out.  Increase it or decrease it to see more or less info.

The EB implements what is essentially a Markov model, using a table of
transition probabilities (encoded in the peculiar TPC-W way ala Clause
5.2), and transitions between the pages.  Each transition is described
by a particular sub-class of rbe.EBTransition.

* EBTransition.java
  EBWAdminConfTrans.java
  EBWAdminReqTrans.java
  EBWBestSellTrans.java
  EBWBuyConfTrans.java
  EBWBuyReqTrans.java
  EBWCustRegTrans.java
  EBWHomeTrans.java
  EBWInitTrans.java
  EBWNewProdTrans.java
  EBWOrderDispTrans.java
  EBWOrderInqTrans.java
  EBWProdCURLTrans.java
  EBWProdDetTrans.java
  EBWSearchReqTrans.java
  EBWSearchResultTrans.java
  EBWShopCartAddTrans.java
  EBWShopCartRefTrans.java
  EBWShopCartTrans.java

The EBTransition objects know how to parse HTML and build the next URL
request.  The transistions for TPC-W are all named rbe.EBW*Trans.  The
transitions may also use and update a small amount of per-EB state,
such as the customer ID of the customer that this EB is simulating.

* ImageReader.java

The EBs use the ImageReader class to download multiple images at once, and
retry if any download errors pop-up.  TPC-W requires that the EBs retry
until download is successful.  For this reason, if any images or HTML pages
are missing or miss-named, the EB gets stuck in an infinite loop trying
to download stuff that is not there.

* EBFactory.java
  EBTPCW1Factory.java
  EBTPCW2Factory.java
  EBTPCWFactory.java

The factories are used by the RBE during start-up to create the EBs.
This involves providing the transition probabilities table, (int [][])
and the transition objects (EBTransition []).

III. To-Do

Get field names and URLs to agree with what the web/database peopld
actually implement.  Most of this means correcting string constants
found at the beginning rbe.RBE.  However, also look for String
patterns (grep Pattern) in the transitions used for parsing the HTML.



