package main;

import java.util.HashMap;

public class HamiltonPath extends HashMap<Integer, Arc> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int start;

	public Arc getStart() {
		return get(start);
	}

	public void setStartNode(int startIndex) {
		start = startIndex;
	}
	
	public void add(Arc arc) {
		put(arc.getSource(), arc);
	}
	
}
