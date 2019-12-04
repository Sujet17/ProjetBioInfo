package main;

import static java.lang.System.nanoTime;

public class Projet {
	
	public static final String targetSize = "10000";
	public static final String collectionNumber = "1";
	
    public static void main(String args[])
    {	
    	
    	long startTime = nanoTime();

    	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/"+targetSize+"/collection"+collectionNumber+".fasta");
		OverlapGraph graph = new OverlapGraph(fl);    	

		long currentTime = nanoTime();
		long timeElapsed = currentTime - startTime;
		System.out.println("Milliseconds time to build graph : " + timeElapsed/1000000);
		
		HamiltonPath path = graph.getHamiltonPath();
				
		long time = nanoTime();
		timeElapsed = time - currentTime;
		System.out.println("Milliseconds time to find hamilton path : " + timeElapsed/1000000);
		
		GapPropagator gp = new GapPropagator(fl);
		Fragment f = ConsensusVote.consensusVote(gp.propagateGaps(path));
		
		timeElapsed = nanoTime() - startTime;
		System.out.println("Total time in milliseconds : " + timeElapsed/1000000);
		
		FragmentList.writeToFile(f, Integer.toString(10000), "Collections/"+targetSize+"/result"+collectionNumber+".fasta");
		
		
		System.out.println();
		
    }
	
}
