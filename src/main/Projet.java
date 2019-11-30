package main;;

public class Projet {
	
    public static void main(String args[])
    {
    	
		//FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
    	

    	/*Fragment f = fl.get(1);
    	Fragment g = fl.get(3).getComplementary();
		SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g);
		System.out.println(sga1);
		
		System.out.println(f);
		System.out.println(g);
		
		System.out.println(sga1.getScoreFG(true));
		
		System.out.println(new Fragment(sga1.fAligned));
		System.out.println(new Fragment(sga1.gAligned));
		

		System.out.println(sga1.getScoreGF(true));

		System.out.println(new Fragment(sga1.fAligned));
		System.out.println(new Fragment(sga1.gAligned));
		
		System.out.println("----------");
		//System.out.println(sga1);
		*/
    	
    	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/10000/collection1.fasta");
    	
		OverlapGraph graph = new OverlapGraph(fl);    	
    	
		System.out.println(graph.getHamiltonPath());
    }
	
}
