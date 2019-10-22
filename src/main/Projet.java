package main;

public class Projet {
	
    // Here is an example of how to use the calculator:
    public static void main(String args[])
    {
    	System.out.println("Ca compile");
    	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/10000/collection1.fasta");
    	//System.out.println(fl);
    	
    	Fragment f = new Fragment("acccgtcattcg");
    	System.out.println(f);
    	System.out.println(f.getComplementary());
    }
	
}
