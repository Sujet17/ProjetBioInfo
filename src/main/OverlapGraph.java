package main;

import java.util.ArrayList;
import java.util.PriorityQueue;


public class OverlapGraph {

	private FragmentList fragments;	
	private PriorityQueue<OrientedEdge> edges;
	
	private int[] included;
	
	public OverlapGraph(FragmentList fragments) {
		this.fragments = fragments;
		
		int size = fragments.size();
		included = new int[size];
		for (int i=0; i<size; i++)
			included[i] = -1;
		
		edges = new PriorityQueue<OrientedEdge>();		
		
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
		
		Fragment f = fragments.get(indexF);
		Fragment g = fragments.get(indexG);
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		
		int weight1 = sga.getAlignmentFG();
		int weight2 = sga.getAlignmentGF();
		
		buildArc(weight1, indexF, indexG, false, false); //f -> g
		buildArc(weight1, indexG, indexF, true, true); // g' -> f'

		buildArc(weight2, indexG, indexF, false, false); // g -> f
		buildArc(weight2, indexF, indexG, true, true); // f' -> g

		
		SemiGlobalAlignment sga2 = new SemiGlobalAlignment(f, g.getComplementary());
		
		weight1 = sga2.getAlignmentFG();
		weight2 = sga2.getAlignmentGF();

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
			edges.add(new OrientedEdge(indexSource, indexDest, complSource, complDest, weight));
		else if (weight < -1)
			throw new IllegalArgumentException("Le poids d'un arc doit etre superieur a -1");
	}
	
	public PriorityQueue<OrientedEdge> getEdges() {
		return edges;
	}
	

	public ArrayList<OrientedEdge> hamiltonPath() {
		int len = fragments.size();
		int[] in = new int[len]; //in[x] = 0 si rien n'entre dans le noeud x, 1 si un arc vers x a ete choisi, 2 si un arc vers x' a ete choisi
		int[] out = new int[len]; //out[x] = 0 si rien de sort de x, 1 si un arc sort de x, 2 si un arc sort de x'
		
		ArrayList<OrientedEdge> path = new ArrayList<OrientedEdge>();
		
		UnionFind struct = new UnionFind(len);
		
		OrientedEdge edge = edges.poll();
		while (edge != null) {
			int f = edge.getSource();
			int g = edge.getDestination();
			if (in[g] == 0 && out[f] == 0 && struct.find(f) != struct.find(g)) {
				path.add(edge);
				in[g] = getVal(edge, true);
				out[f] = getVal(edge, false);
				struct.union(f, g);
			}
			if (struct.onlyOneSet())
				break;
			edge = edges.poll();
		} 
		return path;
	}
	
	public boolean isAvailableEdge(UnionFind struct, OrientedEdge edge, int[] in, int[] out) {
		int f = edge.getSource();
		int g = edge.getDestination();
		 if (included[f] == -1 && included[g] == -1 ) { //Si aucun des fragments n'est inclus dans un autre du graphe
			if (in[g] == 0 && out[f] == 0) { //Si arc choisi ne va vers g ni ne sort de f
				/*
				 *  Si la sortie du noeud correspond avec l'entree
				 *  Par exemple, on ne peut pas avoir l'arc f -> g' puis l'arc g -> h
				 */
				if ( (out[g] == 0 || out[g] == getVal(edge, false)) && (in[f] == 0 || in[f] == getVal(edge, true)) )
					return struct.find(f) != struct.find(g);
			}
		}
		return false;
	}
	
	private int getVal(OrientedEdge edge, boolean isSource) {
		if ((isSource && edge.isComplSource()) || (!isSource && edge.isComplDest()))
			return 2;
		return 1;
	}		
}
