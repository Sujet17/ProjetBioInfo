package main;

import java.util.PriorityQueue;


public class OverlapGraph {

	private FragmentList fragments;	
	private PriorityQueue<Arc> arcs;
	
	private int[] included;
	
	public OverlapGraph(FragmentList fragments) {
		this.fragments = fragments;
		
		int size = fragments.size();
		included = new int[size];
		for (int i=0; i<size; i++)
			included[i] = -1;
		
		arcs = new PriorityQueue<Arc>();		
		
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
	 * Build the 8 arcs (f->g, f'->g, g->f, g->f', ...) 
	 * @param f1
	 * @param f2
	 * @return
	 */
	private void buildArcs(int indexF, int indexG) {
		
		if (included[indexF] != -1 || included[indexG] != -1)
			return;
		
		Fragment f = fragments.get(indexF);
		Fragment g = fragments.get(indexG);
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		
		int weight1 = sga.getScoreFG(false);
		int weight2 = sga.getScoreGF(false);
		
		if (weight1 == -1 || weight2 == -1) {
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
		
		
		SemiGlobalAlignment sga2 = new SemiGlobalAlignment(f, g.getComplementary());
		
		weight1 = sga2.getScoreFG(false);
		weight2 = sga2.getScoreGF(false);
		
		if (weight1 == -1 || weight2 == -1) {
			if (f.size() < g.size())
				included[indexF] = indexG;
			else
				included[indexG] = indexF;
			return ;
		}

		buildArc(weight1, indexF, indexG, false, true); // f -> g'
		buildArc(weight1, indexG, indexF, false, true); // g -> f'
		
		buildArc(weight2, indexG, indexF, true, false); // g' -> f
		buildArc(weight2, indexF, indexG, true, false); // f' -> g
		
	}
	
	
	/**
	 * Build the specified arc if he exists
	 * @param weight
	 * @param indexSource
	 * @param indexDest
	 * @param complSource
	 * @param complDest
	 */
	private void buildArc(int weight, int indexSource, int indexDest, boolean complSource, boolean complDest) {
		if (weight == -1) 
			included[indexSource] = indexDest; //La source est incluse a la destination
		else if (weight > 0)
			arcs.add(new Arc(indexSource, indexDest, complSource, complDest, weight));
		else if (weight < -1)
			throw new IllegalArgumentException("Le poids d'un arc doit etre superieur a -1");
	}
	
	/*
	 * Used for testing
	 */
	public PriorityQueue<Arc> getArcs() {
		return arcs;
	}
	

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
		return path;
	}
	
	public void printIncluded() {
		for (int i=0; i<fragments.size(); i++) 
			System.out.print(included[i]+" ");
	}
	
	public void manageIncludedFragments(UnionFind struct) {
		for (int i=0; i<fragments.size(); i++) {
			System.out.print(included[i]+" ");
			if (included[i] != -1)
				struct.union(i, included[i]);
		}
		System.out.println();
	}
	
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
	
	private int getVal(Arc arc, boolean isSource) {
		if ((isSource && arc.isComplSource()) || (!isSource && arc.isComplDest()))
			return 2;
		return 1;
	}		
}
