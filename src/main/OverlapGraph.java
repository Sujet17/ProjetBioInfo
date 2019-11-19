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
	
	/**
	 * Build the 8 arcs (f1->f2, f1'->f2, f2->f1, f2->f1', ...) if they exist
	 * @param f1
	 * @param f2
	 * @return
	 */
	private int buildArcs(int indexF, int indexG) {
		
		Fragment f = fragments.get(indexF);
		Fragment g = fragments.get(indexG);
		
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		int weight = sga.getAlignmentFG();
		if (weight < 0) {
			included[indexF] = indexG; //Ou l'inverse, il faut verifier
			//Gerer inclusion des fragments
		}
		else if (weight == 0) {
			//Pas d'arc f1 -> f2
		}
		else {
			//Instancier arc avec score obtenu
		}
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	
	//Possibilite de changer la structure contenant le chemin
	public ArrayList<OrientedEdge> hamiltonPath() {
		int len = fragments.size();
		int[] in = new int[len];
		int[] out = new int[len];
		
		ArrayList<OrientedEdge> path = new ArrayList<OrientedEdge>();
		
		UnionFind struct = new UnionFind(len);
		
		OrientedEdge edge = edges.poll();
		while (edge != null) {
			int f = edge.getSource();
			int g = edge.getDestination();
			if (in[g] == 0 && out[f] == 0 && struct.find(f) != struct.find(g)) {
				path.add(edge);
				in[g] = 1;
				out[f] = 1;
				struct.union(f, g);
			}
			if (struct.onlyOneSet())
				break;
			edge = edges.poll();
		} 
		return path;
	}
}
