package main;

import java.util.ArrayList;

public class OverlapGraph {

	private FragmentList nodes;
	private int[] successors;
	
	
	public OverlapGraph(FragmentList fragments) {
		nodes = fragments;
		for (int i=0; i<nodes.size(); i++) {
			for (int j=0; j<nodes.size(); j++) {
				//Voir s'il existe un arc de nodes.get(i) a nodes.get(j)
			}
		}
	}
	
	private int getArcWeight(Fragment f1, Fragment f2) {

		SemiGlobalAlignment sga = new SemiGlobalAlignment(f1, f2);
		int weight = sga.getAlignmentFG();
		if (weight < 0) {
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
	
	private Arc[] getDecrescentArcs() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	//Possibilite de changer la structure contenant le chemin
	public ArrayList<Arc> hamiltonPath() {
		int len = nodes.size();
		int[] in = new int[len];
		int[] out = new int[len];
		
		ArrayList<Arc> path = new ArrayList<Arc>();
		
		UnionFind struct = new UnionFind(len);
		
		Arc[] arcs = getDecrescentArcs();
		int numberArcs = 0;
		
		for (int i=0; i<numberArcs; i++) {
			int f, g; 
			f = 0;
			g = 0;
			//f = arcs[i].getStart();
			//g = arcs[i].getEnd();
			//ou un truc comme ca
			if (in[g] == 0 && out[f] == 0 && struct.find(f) != struct.find(g)) {
				path.add(arcs[i]);
				in[g] = 1;
				out[f] = 1;
				struct.union(f, g);
			}
			if (struct.onlyOneSet())
				break;
		}
		return path;
	}
}
