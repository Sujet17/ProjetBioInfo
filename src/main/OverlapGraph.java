package main;

import java.util.ArrayList;

public class OverlapGraph {

	private FragmentList nodes;
	private int[] successors;
	
	
	public OverlapGraph(FragmentList fragments) {
		nodes = fragments;
	}
	
	private int getArcWeight(Fragment f1, Fragment f2) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private Point[] getCrescentArcs() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	//Possibilite de changer la structure contenant le chemin
	public ArrayList<Point> hamiltonPath() {
		int len = nodes.size();
		int[] in = new int[len];
		int[] out = new int[len];
		
		ArrayList<Point> path = new ArrayList<Point>();
		
		UnionFind struct = new UnionFind(len);
		
		Point[] arcs = getCrescentArcs();
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
