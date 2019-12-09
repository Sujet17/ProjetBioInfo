package main;

public class Projet {
	
	public static final String targetSize = "16320";
	public static final String collectionNumber = "5";
	public static final Boolean writeOnfile = true;
	
	public static final boolean multithreading = true;
	
	public static TimeMonitor timeMonitor;
	
    public static void main(String args[])
    {	
    	if (args.length == 0) {

        	timeMonitor = new TimeMonitor();

        	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/"+targetSize+"/collection"+collectionNumber+".fasta");
    		OverlapGraph graph = new OverlapGraph(fl);    	
    		
    		timeMonitor.measure("Time to build graph");
    		
    		HamiltonPath path = graph.getHamiltonPath();
    				
    		timeMonitor.measure("Time to find hamilton path");
    		
    		GapPropagator gp = new GapPropagator(fl);
    		
    		Fragment[] t = gp.propagateGaps(path);
    		
    		timeMonitor.measure("Time to propagate gaps");
    		
    		Fragment f = ConsensusVote.consensusVote(t);
    		
    		timeMonitor.measure("Time to vote the consensus");
    		
    		if (writeOnfile) {
    			FragmentList.writeToFile(f, Integer.toString(10000), "Collections/"+targetSize+"/result"+collectionNumber+".fasta");
    			FragmentList.writeToFile(f.getComplementary(), Integer.toString(10000), "Collections/"+targetSize+"/result-ic"+collectionNumber+".fasta");
    		}
    	}
    	/*
    	else {
    		for (int i = 0; i<args.length; i++)
    			System.out.println(args[i]);
    		String fileToRead = args[0];
    		
    		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/"+targetSize+"/collection"+collectionNumber+".fasta");
    		
    		OverlapGraph graph = new OverlapGraph(fl);    	
    		HamiltonPath path = graph.getHamiltonPath();
    		
    		GapPropagator gp = new GapPropagator(fl);
    		Fragment[] t = gp.propagateGaps(path);
    		
    		Fragment f = ConsensusVote.consensusVote(t);
        	
        	String fileToWrite = args[2];
        	String fileToWrite_ic = args[4];
        	
        	FragmentList.writeToFile(f, Integer.toString(10000), fileToWrite);
    		FragmentList.writeToFile(f.getComplementary(), Integer.toString(10000), fileToWrite_ic);

    	}
    	*/
    }
	
    public void testInclusionRelation() {

    	Fragment f = new Fragment("aaa");
    	Fragment g = new Fragment("ggaaaggg");
    	Fragment h = new Fragment("tgggagggga");
    	
    	SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g);
    	SemiGlobalAlignment sga2 = new SemiGlobalAlignment(f, g.getComplementary());
    	
    	System.out.println(sga1.getScoreFG());
    	System.out.println(sga1.getScoreGF());
    	
    	System.out.println(sga2.getScoreFG());
    	System.out.println(sga2.getScoreGF());
    	
    	SemiGlobalAlignment sga3 = new SemiGlobalAlignment(g, h);
    	SemiGlobalAlignment sga4 = new SemiGlobalAlignment(g, h.getComplementary());
    	
    	System.out.println(sga3.getScoreFG());
    	System.out.println(sga3.getScoreGF());
    	
    	System.out.println(sga4.getScoreFG());
    	System.out.println(sga4.getScoreGF());
    	
    	sga3 = new SemiGlobalAlignment(f, h);
    	sga4 = new SemiGlobalAlignment(f, h.getComplementary());
    	
    	System.out.println(sga3.getScoreFG());
    	System.out.println(sga3.getScoreGF());
    	
    	System.out.println(sga4.getScoreFG());
    	System.out.println(sga4.getScoreGF());

    }
}
