package main;

import java.util.concurrent.PriorityBlockingQueue;
//import java.util.stream.IntStream;


public class OverlapGraph {
	
	
	/**
	 * The list of the fragments
	 */
	private FragmentList fragments;	
	
	/**
	 * An heap to store all the arcs of the graph
	 */
	private PriorityBlockingQueue<Arc> arcs;
	
	/**
	 * An array to manage the inclusions between fragments.
	 * Let included[i] == x, 
	 * If x == -1, that means that the ith fragment is not included to another fragment in the list;
	 * Else, that means that the ith fragment is included in the xth.
	 */
	private int[] included;
	
	
	public OverlapGraph(FragmentList fragments) {
		this.fragments = fragments;
		
		int size = fragments.size();
		included = new int[size];
		for (int i=0; i<size; i++)
			included[i] = -1;
		
		arcs = new PriorityBlockingQueue<Arc>();		
		
		
    	//IntStream.range(0, size).forEach(i -> IntStream.range(i+1,  size).parallel().forEach(j -> buildArcs(i, j)));
			
		
		for (int i=0; i<size; i++) {
			for (int j=i+1; j<size; j++) {
				buildArcs(i, j);
			}
		}
				
	}
	
	/*
	 * Used only for testing
	 */
	public OverlapGraph(int size) {
		included = new int[size];
		for (int i=0; i<size; i++)
			included[i] = -1;
	}
	
	/**
	 * Build the 8 arcs (f->g, f'->g, g->f, g->f', ...) between the nodes f and g.
	 * The inclusions between fragments are detected and managed here.
	 * @param indexF the index of the node f
	 * @param indexG the index of the node g
	 */
	private void buildArcs(int indexF, int indexG) {
		
		if (included[indexF] != -1 || included[indexG] != -1)
			return;
		
		Fragment f = fragments.get(indexF);
		Fragment g = fragments.get(indexG);
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g.getComplementary());
		
		int weight1 = sga.getScoreFG();
		int weight2 = sga.getScoreGF();

		int weight3 = sga1.getScoreFG();
		int weight4 = sga1.getScoreGF();
		
		if (weight1 == -1 || weight2 == -1 || weight3 == -1 || weight4 == -1) {
			if (f.size() < g.size())
				included[indexF] = indexG;
			else
				included[indexG] = indexF;
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
	 * @param weight 
	 * @param indexSource 
	 * @param indexDest
	 * @param complSource
	 * @param complDest
	 */
	private void buildArc(int weight, int indexSource, int indexDest, boolean complSource, boolean complDest) {
		if (weight >= 0)
			arcs.add(new Arc(indexSource, indexDest, complSource, complDest, weight));
		else
			throw new IllegalArgumentException("Le poids d'un arc doit etre superieur (non strictement) a 0");
	}

	/**
	 * Find the hamilton path on this graph. Note that this method modifies the "arcs" attribute and thus cannot be called twice on the same object.
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
		
		Arc arc = arcs.poll();
		while (arc != null) {
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
	 * print included fragments
	 */
	public void printIncluded() {
		for (int i=0; i<fragments.size(); i++) 
			System.out.print(included[i]+" ");
	}
	
	/**
	 * Used to ignore the included fragments 
	 * @param struct
	 */
	public void manageIncludedFragments(UnionFind struct) {
		int cnt = 0;
		for (int i=0; i<included.length; i++) {
			System.out.print(included[i]+" ");
			if (included[i] != -1) {
				struct.union(i, included[i]);
				cnt++;
			}
		}
		System.out.println();
		System.out.println((float)cnt/fragments.size()+" "+cnt+" "+fragments.size());
	}
	
	/**
	 * Check if the given arc can be added to the hamilton path. 
	 * @param struct the UnionFind structure that is used to check if the adding of the arc to the hamilton path will create a cycle
	 * @param arc 
	 * @param in 
	 * @param out
	 * @return true if the given arc can be added to the hamilton path, false else
	 */
	public boolean isAvailableArc(UnionFind struct, Arc arc, int[] in, int[] out) {
		int f = arc.getSource();
		int g = arc.getDestination();
		if (included[f] == -1 && included[g] == -1 ) { //Si aucun des fragments n'est inclus dans un autre du graphe
			if (in[g] == 0 && out[f] == 0) { //Si aucun arc choisi ne va vers g ni ne sort de f
				/*
				 *  Si la sortie du noeud correspond avec l'entree
				 *  Par exemple, on ne peut pas avoir l'arc f -> g' puis l'arc g -> h
				 */
				if ( (out[g] == 0 || out[g] == getVal(arc, false)) && (in[f] == 0 || in[f] == getVal(arc, true)) ) 
					return struct.find(f) != struct.find(g);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param arc
	 * @param isSource
	 * @return 2 if ..., 1 else
	 */
	private int getVal(Arc arc, boolean isSource) {
		if ((isSource && arc.isComplSource()) || (!isSource && arc.isComplDest()))
			return 2;
		return 1;
	}	
	
}
