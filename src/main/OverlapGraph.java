package main;

import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;

public class OverlapGraph {
	
	/**
	 * The list of the fragments
	 */
	private FragmentList fragments;	
	
	/**
	 * An heap to store by decreasing order all the arcs of the graph
	 */
	private PriorityBlockingQueue<Arc> arcs;
	
	/**
	 * An array to manage the inclusions between fragments.
	 * Let included[i] == x, 
	 * If x == -1, that means that the ith fragment is not included to another fragment in the list;
	 * Else, that means that the ith fragment is included in the xth.
	 */
	private Vector<Integer> included;
	
	
	public OverlapGraph(FragmentList fragments) {
		this.fragments = fragments;
		
		int size = fragments.size();
		included = new Vector<Integer>(size);
		for (int i=0; i<size; i++)
			included.add(-1);
		
		arcs = new PriorityBlockingQueue<Arc>();		
		
		/*
		 * If the multithreading is activated
		 */
		if (Project.multithreading)
			IntStream.range(0, size).forEach(i -> IntStream.range(i+1,  size).parallel().forEach(j -> buildArcs(i, j)));
			
		else {
			for (int i=0; i<size; i++) {
				for (int j=i+1; j<size; j++) 
					buildArcs(i, j);
			}
		}
				
	}
	
	/*
	 * Used only for testing
	 */
	public OverlapGraph(int size) {
		included = new Vector<Integer>(size);
		for (int i=0; i<size; i++)
			included.add(-1);
	}
	
	/**
	 * Build the 8 arcs (f->g, f'->g, g->f, g->f', ...) between the nodes f and g.
	 * The inclusions between fragments are detected and managed here.
	 * @param indexF the index of the node f
	 * @param indexG the index of the node g
	 */
	private void buildArcs(int indexF, int indexG) {
		
		Fragment f = fragments.get(indexF);
		Fragment g = fragments.get(indexG);
		
		if ((included.get(indexF) != -1 && f.size() < g.size()) || (included.get(indexG) != -1 && f.size() > g.size()))
			return;
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g.getComplementary());
		
		int weight1 = sga.getScoreFG();
		int weight2 = sga.getScoreGF();

		int weight3 = sga1.getScoreFG();
		int weight4 = sga1.getScoreGF();
		
		if (weight1 == -1 || weight2 == -1 || weight3 == -1 || weight4 == -1) {
			if (f.size() < g.size())
				included.set(indexF, indexG);
			else
				included.set(indexG, indexF);
			return ;
		}
		
		buildArc(weight1, indexF, indexG, false, false); //f -> g
		buildArc(weight1, indexG, indexF, true, true); // g' -> f'

		buildArc(weight2, indexG, indexF, false, false); // g -> f
		buildArc(weight2, indexF, indexG, true, true); // f' -> g'
		
		buildArc(weight3, indexF, indexG, false, true); // f -> g'
		buildArc(weight3, indexG, indexF, false, true); // g -> f'
		
		buildArc(weight4, indexG, indexF, true, false); // g' -> f
		buildArc(weight4, indexF, indexG, true, false); // f' -> g
		
	}
	
	
	/**
	 * Instantiate an arc and add it to the "arcs" attribute 
	 * @param weight the weight of the arc
	 * @param indexSource the index of the node from which the arc comes out
	 * @param indexDest the index of the node where the arc goes
	 * @param complSource true if the arc concerns the reverse complement fragment which is in the source node
	 * @param complDest true if the arc concerns the reverse complement fragment which is in the destination node
	 */
	private void buildArc(int weight, int indexSource, int indexDest, boolean complSource, boolean complDest) {
		if (weight >= 0)
			arcs.add(new Arc(indexSource, indexDest, complSource, complDest, weight));
		else
			throw new IllegalArgumentException("The weight of an arc must >= 0");
	}

	/**
	 * Find the hamilton path on this graph using a greedy algorithm. 
	 * Note that this method modifies the "arcs" attribute and thus cannot be called twice on the same object.
	 * @return the Hamilton Path
	 */
	public HamiltonPath getHamiltonPath() {
		int len = fragments.size();
		int[] in = new int[len]; //in[x] = 0 si rien n'entre dans le noeud x, 1 si un arc vers x a ete choisi, 2 si un arc vers x' a ete choisi
		int[] out = new int[len]; //out[x] = 0 si rien de sort de x, 1 si un arc sort de x, 2 si un arc sort de x'
		
		//ArrayList<Arc> path = new ArrayList<Arc>();
		
		HamiltonPath path = new HamiltonPath();
		
		UnionFind struct = new UnionFind(len);
		
		manageIncludedFragments(struct);
		
		int oldWeight;
		
		Arc arc = arcs.poll();
		oldWeight = arc.getWeight();
		while (arc != null) {
			if (arc.getWeight()>oldWeight)
				System.out.println("ERREUR");
			else
				oldWeight = arc.getWeight();
			if (isAvailableArc(struct, arc, in, out)) {
				int f = arc.getSource();
				int g = arc.getDestination();
				
				path.add(arc);
				in[g] = getVal(arc, false);
				out[f] = getVal(arc, true);
				struct.union(f, g);
			}	
			if (struct.onlyOneSet())
				break;
			arc = arcs.poll();
		} 
		
		for (Integer i : path.keySet()) {
			if (in[i] == 0) {
				path.setStartNode(i);
				break;
			}
		}
		return path;
	}
	
	/**
	 * Used to ignore the included fragments
	 * @param struct
	 */
	public void manageIncludedFragments(UnionFind struct) {
		for (int i=0; i<included.size(); i++) {
			if (included.get(i) != -1) 
				struct.union(i, included.get(i));
		}
	}
	
	/**
	 * Check if the given arc can be added to the hamilton path. 
	 * @param struct the UnionFind structure that is used to check if the adding of the arc to the hamilton path will create a cycle
	 * @param arc the arc which is checked
	 * @param in an array that is used to check if an arc already comes in the destination node of the arc
	 * @param out an array that is used to check if an arc already comes out of the source node of the arc
	 * @return true if the given arc can be added to the hamilton path, false else
	 */
	public boolean isAvailableArc(UnionFind struct, Arc arc, int[] in, int[] out) {
		int f = arc.getSource();
		int g = arc.getDestination();
		if (included.get(f) == -1 && included.get(g) == -1 ) { //Check inclusions
			if (in[g] == 0 && out[f] == 0) { //Check that no arc comes out from f or go to g
				/*
				 *  If an arc already comes out from the destination node and concerns the reverse-complement fragment of the node,
				 *  the arc that is checked must also concerns the reverse-complement.
				 *  The situation is similar with the source node
				 *  For instance, if f -> g' is already selected, g -> h cannot
				 */
				if ( (out[g] == 0 || out[g] == getVal(arc, false)) && (in[f] == 0 || in[f] == getVal(arc, true)) ) 
					return struct.find(f) != struct.find(g); //The arc cannot create a cycle
			}
		}
		return false;
	}
	
	/**
	 * Used to store the used fragments in the in and out arrays (while building the hamilton path)
	 * @param arc
	 * @param isSource true if the checking concerns the source of the arc, else the checking concerns the destination
	 * @return 2 if in the checked node the reverse-complement is selected by the arc, 1 else
	 */
	private int getVal(Arc arc, boolean isSource) {
		if ((isSource && arc.isComplSource()) || (!isSource && arc.isComplDest()))
			return 2;
		return 1;
	}	
	
}
