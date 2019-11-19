package main;

public class Projet {
	
    public static void main(String args[])
    {
    	
    	System.out.println("Ca compile");
    	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/10000/collection1.fasta");
    	System.out.println(fl);
      	
    	
    	/*
		Fragment f = new Fragment("cagcacttggattctcgg");
		Fragment g = new Fragment("cagcgtgg");
		*/
    	

    	//sga.getAlignmentGF();
    }
	
}
