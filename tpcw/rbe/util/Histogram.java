/*-------------------------------------------------------------------------
 * rbe.util.Histogram.java
 * Timothy Heil
 * 10/19/99
 *
 * Java embodiment of my Histogram class.
 *
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

package rbe.util;

import java.io.PrintStream;

public class Histogram
{

  private int [] hist;   // Actually contains the histogram data.
  private long sum;      // Keeps a running sum of the sampled data.
  private long sumSq;    /* Keeps a running sum of the squares of the
			  * sampled data. */
  int binSize;           // Size of each bin.

  public Histogram(int bins, int binSize)
  /*------------------------------------------------------------------------*\
   | Constructor.  Creates a histogram with bins number of bins.
   |  Bins are all initially zero.  The last bin is used for all higher
   |  bins.  Thus, Histogram(5) will create a histogram with bins for
   |  0,1,2,3, and 4 and higher sample values.  
   |
   | Bin numbers should NOT be reduced to the number of bins in the 
   |  histogram before adding sample values to the histogram.  The 
   |  Histogram will do this for you.
  \*------------------------------------------------------------------------*/
  {
    Debug.assert(bins>0, "Histogram: number of bins < 0");

    hist = new int[bins];
    this.binSize = binSize;

    sum = 0;
    sumSq = 0;
  }

  public void add(int bin)
  /*------------------------------------------------------------------------*\
   | Adds one sample to a specified bin.  The bin must be non-negative, 
   |  but can be of any size.
  \*------------------------------------------------------------------------*/
  {
    add(bin,1);
  }

  public void add(int bin, int amount)
  /*------------------------------------------------------------------------*\
   | Adds multiple samples to a specified bin.  The bin must be non-negative, 
   |  but can be of any size.
  \*------------------------------------------------------------------------*/
  {
    Debug.assert (bin >= 0, "Histogram.Add: bin less than 0.");

    long lbin = bin;
    bin = bin / binSize;
    if (bin >=hist.length) bin = hist.length -1;
    
    synchronized(this) {
      sum = sum + lbin*amount;
      sumSq = sumSq + lbin*lbin*amount;
    
      hist[bin]+=amount;
    }
  }


  public synchronized void clear()
  /*------------------------------------------------------------------------*\
   | Clears out the histogram for reuse.
  \*------------------------------------------------------------------------*/
  {
    int i;
    
    sum =0;
    sumSq = 0;
    for (i=0;i<hist.length;i++) hist[i]=0;
  }

  public int bin(int bin)
  /*------------------------------------------------------------------------*\
   | Returns the number of samples added to a bin.  The bin must be 
   |  non-negative, but can be of any size.  
  \*------------------------------------------------------------------------*/
  {
    Debug.assert (bin>=0, "Histogram.Bin: bin less than 0.");

    bin = bin/binSize;
    if (bin >= hist.length) bin = hist.length -1;
    return(hist[bin]);
  }

  public int samples()
  /*------------------------------------------------------------------------*\
   | Returns the total number of samples added to all bins.
  \*------------------------------------------------------------------------*/
  {
    int samp, i;
    
    for(samp = 0,i=0;i<hist.length;samp = samp + hist[i], i++);
    
    return(samp);
  }

  public long sum()
  /*------------------------------------------------------------------------*\
   | Returns the sum of all sample bin values added to the histogram.  
   | Mathematically, this is the sum of the bin times the number of samples
   | in the bin.  However, due to histogram truncation, this does not work
   | computationally.  The histogram class specifically sums the values
   | of all samples added to the histogram, to avoid this problem.
   |
   | The average of the sample values is Sum()/Samples();
  \*------------------------------------------------------------------------*/
  {
    return(sum);
  }

  public long sumSq()
  /*------------------------------------------------------------------------*\
   | Like Sum(), returns the sum of all the sample values squared.  This is
   | useful in calculating the variance and standard deviation of the 
   | sample values.  Again, the sum of the squares is totaled as the histogram
   | is updated, to avoid histogram truncation problems.
   |
   | The variance is ( Samples()*SumSq()-Sum()*Sum() ) / Samples()/Samples();
   | The standard deviation is the square root of the variance.
  \*------------------------------------------------------------------------*/
  {
    return(sumSq);
  }

  public double average()
  /*------------------------------------------------------------------------*\
   | Returns the average of the sample values, calculated as explained under
   |  Sum().
  \*------------------------------------------------------------------------*/
  {
    return( ((double) sum)/samples() );
  }

  public double variance()
  /*------------------------------------------------------------------------*\
   | Returns the variance of the sample values, calculated as explained under
   | SumSq(). 
  \*------------------------------------------------------------------------*/
  {
    double samp;
    
    samp = samples();
    
    return( ( samp * sumSq - ((double) sum) * sum ) / (samp * samp) );
  }


  public void Print(PrintStream out)
  /*------------------------------------------------------------------------*\
   | Prints out the histogram data to the output stream specified.
  \*------------------------------------------------------------------------*/
  {
    int i;
    
    for (i=0;i<hist.length;i++) {
      out.println(Pad.r(6,i*binSize)+"  " + Pad.r(12, hist[i]));
    }
  }

  public void PrintStats(PrintStream out)
  /*------------------------------------------------------------------------*\
   | Prints out Samples(), Sum(), SumSq(), Average(), and standard deviation
   |  to the output stream specified.
  \*------------------------------------------------------------------------*/
  {
    out.println("Samples           : " + samples());
    out.println("Sum               : " + sum());
    out.println("Sum of Squares    : " + sumSq());
    out.println("Average           : " + average());
    out.println("Standard Deviation: " + Math.sqrt(variance()));
  }

  public void printMFile(PrintStream out, String name)
  {
	 out.println(name + ".h = [");
	 Print(out);
	 out.println("];");
	 out.println(name + ".samples = " + samples() + ";");
	 out.println(name + ".sum     = " + sum() + ";");
	 out.println(name + ".sumSq   = " + sumSq() + ";");
	 out.println(name + ".avg     = " + average() + ";");
	 out.println(name + ".stddev  = " + Math.sqrt(variance()) + ";");
	 out.println();
  }
};

