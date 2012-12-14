/*-------------------------------------------------------------------------
 * rbe.EBFactoryArg.java
 * Timothy Heil
 * 10/29/99
 *
 * Abstract command line argument parsing class.
 *------------------------------------------------------------------------*/

package rbe;

import java.util.Vector;
import rbe.args.Arg;
import rbe.args.ArgDB;

public class EBFactoryArg extends Arg {
  public Vector ebs;
  public RBE rbe;
  public int maxState;
  public String className;

  public EBFactoryArg(String arg, String name, String desc, 
							 RBE rbe, Vector ebs) {
	 super(arg, name, desc, true, false);
	 this.rbe = rbe;
	 this.ebs = ebs;
  }

  public EBFactoryArg(String arg, String name, String desc, 
							 RBE rbe, Vector ebs, ArgDB db) {
	 super(arg, name, desc, true, false, db);
	 this.rbe = rbe;
	 this.ebs = ebs;
  }

  // Customize to parse arguments.
  protected int parseMatch(String [] args, int a)
		 throws Arg.Exception
  {
	 int num;
	 int p;
	 int i;

	 if (a==args.length) {
		throw new Arg.Exception("Missing factory class name.", a);
	 }
	 
	 // Read in factory class.
	 String factoryClassName = args[a];
	 className = factoryClassName;
	 EBFactory factory;
	 try {
		Class factoryClass = Class.forName(factoryClassName);
		factory = (EBFactory) factoryClass.newInstance();
	 }
	 catch(ClassNotFoundException cnf) {
		throw new Arg.Exception("Unable to find factory class " + 
													 factoryClassName + ".", a);
	 }
	 catch(InstantiationException ie) {
		throw new 
		  Arg.Exception("Unable to instantiate factory class " +
										  factoryClassName + ".", a);
	 }
	 catch(IllegalAccessException iae) {
		throw new Arg.Exception("Unable to access constructor " + 
										  "for factory class " +
										  factoryClassName + ".", a);
	 }
	 catch(ClassCastException cce) {
		throw new Arg.Exception("Factory class " + factoryClassName + 
								 " is not a subclass of EBFactory.", a);
	 }

	 // Parse number of EBs to create with this factory.
	 a++;
	 if (a == args.length) {
		throw new Arg.Exception("Missing factory EB count.", a);
	 }
	 try {
		num = Integer.parseInt(args[a]);
	 }
	 catch(NumberFormatException nfe) {
		throw new Arg.Exception("Unable to parse number of EBs.", a);
	 }
	 a++;
	 p = factory.initialize(args, a);
	 if (p==-1) {
		// Factory was unable to parse the input args.
		throw new Arg.
		  Exception("Factory class " + factoryClassName + 
						" unable to parse input arguments.", a);
	 }
	 a=p;

	 // Create EBs
	 for (i=0;i<num;i++) {
		EB e = factory.getEB(rbe);
		if (e.states()>maxState) {
		  maxState = e.states();
		}
		ebs.addElement(e);
	 }

	 return(a);
  }
	
  public String value() { return("" + ebs.size() + " EBs"); }
}
