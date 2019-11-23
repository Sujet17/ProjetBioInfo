package main;

public class Projet {
	
    public static void main(String args[])
    {
    	
    	System.out.println("Ca compile");
		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
    	//System.out.println(fl);
      	
    	//Fragment f = new Fragment("tag");
    	//Fragment g = new Fragment("acgta");
    	Fragment f = fl.get(0);
    	Fragment g = fl.get(3);
    	
		SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g);
		System.out.println(f);
		System.out.println(g);
		
		System.out.println(sga1.getScoreFG(true));
		System.out.println(sga1.getScoreGF(true));
		
		System.out.println(sga1.fAlignedFG);
		System.out.println(sga1.gAlignedFG);

		System.out.println(sga1.fAlignedGF);
		System.out.println(sga1.gAlignedGF);
		
		System.out.println("----------");
		System.out.println(sga1);
		
    	/*
		Fragment f = new Fragment("cagcacttggattctcgg");
		Fragment g = new Fragment("cagcgtgg");
		*/
    	
    	//sga.getAlignmentGF();
    }
	
}
