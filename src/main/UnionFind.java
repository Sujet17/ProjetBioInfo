package main;

//https://fr.wikipedia.org/wiki/Union-find

public class UnionFind {
	
	/**
	 * The number of remaining distinct sets
	 */
	private int distinctSets;
	
	private int[] parent;
	private int[] rank;
	
	/**
	 * 
	 * @param size the number of distinct sets at the start
	 */
	public UnionFind(int size) {
		distinctSets = size-1;
		
		parent = new int[size];
		rank = new int[size];
		
		for (int i=0; i<size; i++) 
			parent[i] = i;
	}
	
	/**
	 * Used to identify the sets to which belongs the given node
	 * @param node
	 * @return
	 */
	public int find(int node) {
		if (parent[node] != node)
			parent[node] = find(parent[node]);
		return parent[node];
	}
	
	/**
	 * Merge the set that contains nodeX with the one that contains nodeY
	 * Do nothing if the sets are already merged
	 * @param nodeX
	 * @param nodeY
	 */
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
