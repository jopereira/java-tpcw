/*------------------------------------------------------------------------
 * rbe.EBWCustRegTrans.java
 * Timothy Heil
 * 10/13/99
 *
 * ECE902 Fall '99
 *
 * TPC-W customer registeration transition to the customer registration 
 *  page from the shopping cart page.
 *------------------------------------------------------------------------*/

package rbe;

public class EBWCustRegTrans extends EBTransition {

  public String request(EB eb, String html) {

    RBE rbe = eb.rbe;

    return(eb.addIDs(rbe.custRegURL));
  }
}
