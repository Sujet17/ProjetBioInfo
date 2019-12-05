package main;

public class Projet {
	
	public static final String targetSize = "10000";
	public static final String collectionNumber = "1";
	
	public static TimeMonitor timeMonitor;
	
    public static void main(String args[])
    {	
    	
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
		
		FragmentList.writeToFile(f, Integer.toString(10000), "Collections/"+targetSize+"/result"+collectionNumber+".fasta");
		FragmentList.writeToFile(f.getComplementary(), Integer.toString(10000), "Collections/"+targetSize+"/result-ic"+collectionNumber+".fasta");
		
		
		System.out.println();
		
    }
	
}
