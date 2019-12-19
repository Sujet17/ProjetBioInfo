package main;

/**
 * The main class
 */
public class Project {
	
	public static final String targetSize = "10000";
	public static final int collectionNumber = 1;
	public static final Boolean writeOnfile = true;
	
	public static final boolean multithreading = true;
	
	public static TimeMonitor timeMonitor;
	
    public static void main(String args[])
    {	
    	if (args.length < 0) {

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
    	
    	else {
    		
    		for (int i = 0; i<args.length; i++)
    			System.out.println(args[i]);
    		String fileToRead = args[0];
    		String output = "stdOutput.txt";
    		String outputIC = "stdOutputIC.txt";
    		
    		final String errorMsg = "Le premier argument de la ligne de commande doit être le fichier d'entrée, ensuite "
					+ "doivent être spécifiés les fichiers de sortie après les options -out et -out-ic";
    		
    		if (args[1].equals("-out")) 
    			output = args[2];
    		else if (args[1].equals("-out-ic"))
    			outputIC = args[2];
    		else {
    			System.out.println(errorMsg);
    			return;
    		}
    			
    		if (args[3].equals("-out-ic")) 
    			outputIC = args[4];
  
    		else if (args[3].equals("-out"))
    			outputIC = args[4];
    		else {
    			System.out.println(errorMsg);
    			return;
    		}
    		
    		FragmentList fl = FragmentList.getFragmentsFromFile(fileToRead);
    		
    		OverlapGraph graph = new OverlapGraph(fl);    	
    		HamiltonPath path = graph.getHamiltonPath();
    		
    		GapPropagator gp = new GapPropagator(fl);
    		FragmentBuilder[] t = gp.propagateGaps(path);
    		
    		Fragment f = ConsensusVote.consensusVote(t);
        	
        	FragmentList.writeToFile(f, fl.getCollectionNum(), output);
    		FragmentList.writeToFile(f.getComplementary(), fl.getCollectionNum(), outputIC);

    	}
    	
    }
}
