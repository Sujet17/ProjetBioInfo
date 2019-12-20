package main;

/**
 * The main class
 */
public class Project {
	
	public static final String targetSize = "10000";
	public static final int collectionNumber = 1;
	public static final Boolean writeOnfile = true;
	
	/**
	 * A boolean to activate or not the multithreading
	 */
	public static final boolean multithreading = true;
	
	/**
	 * The timeMonitor object used to measure the execution time of each step of the program
	 */
	public static TimeMonitor timeMonitor;
	
    public static void main(String args[])
    {	
    	if (args.length == 5) {
    		
    		String fileToRead = args[0];
    		String output = "stdOutput.txt";
    		String outputIC = "stdOutputIC.txt";
    		
    		final String errorMsg = "Le premier argument de la ligne de commande doit être le fichier d'entrée, ensuite "
					+ "doivent être spécifiés les fichiers de sortie après les options -out et -out-ic";
    		
    		if (args[1].equals("-out") && (args[3].equals("-out-ic"))) { //Check if the arguments are correctly given
    			output = args[2];
    			outputIC = args[4];
    		}
    		else {
    			System.out.println(errorMsg);
    			return;
    		}
    		
    		FragmentList fl = FragmentList.getFragmentsFromFile(fileToRead);     		
    		OverlapGraph graph = new OverlapGraph(fl);    	//Build the graph
    		HamiltonPath path = graph.getHamiltonPath(); //Build the hamilton path
    		
    		GapPropagator gp = new GapPropagator(fl); 
    		FragmentBuilder[] t = gp.propagateGaps(path); //Gap propagation step
    		
    		Fragment f = ConsensusVote.consensusVote(t); //Consensus vote step
        	
        	FragmentList.writeToFile(f, fl.getCollectionNum(), output);
    		FragmentList.writeToFile(f.getComplementary(), fl.getCollectionNum(), outputIC);

    	}
    	
    	/*
    	 * Used for developpement
    	 */
    	else if (args.length == 0) { 

        	timeMonitor = new TimeMonitor();

        	FragmentList fl = FragmentList.getFragmentsFromFile("Collections/"+targetSize+"/collection"+collectionNumber+".fasta");
    		OverlapGraph graph = new OverlapGraph(fl);    	
    		
    		timeMonitor.measure("Time to build graph");
    		
    		HamiltonPath path = graph.getHamiltonPath();
    				
    		timeMonitor.measure("Time to find hamilton path");
    		
    		GapPropagator gp = new GapPropagator(fl);
    		
    		FragmentBuilder[] t = gp.propagateGaps(path);
    		
    		timeMonitor.measure("Time to propagate gaps");
    		
    		Fragment f = ConsensusVote.consensusVote(t);
    		
    		timeMonitor.measure("Time to vote the consensus");
    		
    		if (writeOnfile) {
    			FragmentList.writeToFile(f, fl.getCollectionNum(), "Collections/"+targetSize+"/result"+fl.getCollectionNum()+".fasta");
    			FragmentList.writeToFile(f.getComplementary(), fl.getCollectionNum(), "Collections/"+targetSize+"/result-ic"+fl.getCollectionNum()+".fasta");
    		}
    	}
    }
}
