package main;;

public class Projet {
	
    public static void main(String args[])
    {
    	
		//FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
    	
    	Fragment g1 = new Fragment("taaaaaaaaaaaa");
    	Fragment g2 = new Fragment("taaaaaaaaaaaa");
    	Fragment g3 = new Fragment("tcaaaagaaaaaa");
    	Fragment g4 = new Fragment("tcaaaagaaaaaa");
    	Fragment g5 = new Fragment("tcaaaagaaaaaa");
    	Fragment g6 = new Fragment("acaaaagaaaaaa");
    	Fragment g7 = new Fragment("acaaaagaaaaaa");
    	Fragment g8 = new Fragment("aaaaaaaaaaaaa");
    	
    	Fragment[] tab = new Fragment[8];
    	tab[0]=g1;
    	tab[1]=g2;
    	tab[2]=g3;
    	tab[3]=g4;
    	tab[4]=g5;
    	tab[5]=g6;
    	tab[6]=g7;
    	tab[7]=g8;
    	

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

    	System.out.print(graph.ConsensusVote(tab));
    	
    }
	
}
