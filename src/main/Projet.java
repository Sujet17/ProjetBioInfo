package main;

public class Projet {
	
    public static void main(String args[])
    {
    	/*
    	System.out.println("Ca compile");
    	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/10000/collection1.fasta");
    	System.out.println(fl);
    	
    	Fragment f = new Fragment("acccgtcattcg");
    	System.out.println(f);
    	System.out.println(f.getComplementary());

    	*/  	
		Fragment f = new Fragment("cagcacttggattctcgg");
		Fragment g = new Fragment("cagcgtgg");
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		
		System.out.println(sga);
		
		sga.getAlignmentGF();
    }
	
}
