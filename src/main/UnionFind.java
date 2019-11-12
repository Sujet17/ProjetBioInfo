package main;

//https://fr.wikipedia.org/wiki/Union-find

public class UnionFind {
	
	private int distinctSets;
	
	private int[] parent;
	private int[] rank;
	
	//Do the makeSet operation for sets 0 to size-1
	public UnionFind(int size) {
		distinctSets = size-1;
		
		parent = new int[size];
		rank = new int[size];
		
		for (int i=0; i<size; i++) 
			parent[i] = i;
	}
	
	public int find(int node) {
		if (parent[node] != node)
			parent[node] = find(parent[node]);
		return parent[node];
	}

	public void union(int nodeX, int nodeY) {
		int xRoot = find(nodeX);
		int yRoot = find(nodeY);
		if (xRoot != yRoot) {
			distinctSets--;
			if (rank[xRoot] < rank[yRoot]) 
				parent[xRoot] = yRoot;
			else {
				parent[yRoot] = xRoot;
				if (rank[xRoot] == rank[yRoot])
					rank[xRoot]++;
			}
		}
	}
	
	public boolean onlyOneSet() {
		return distinctSets == 0;
	}
	
}
