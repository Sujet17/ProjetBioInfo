package main;

import java.util.HashMap;

/**
 * This class store hasmap that contains associates the source of an arc with this arc.
 *
 */

public class HamiltonPath extends HashMap<Integer, Arc> {

	private static final long serialVersionUID = 1L;
	
	// sum of weight of all arcs
	public int totalWeight;
	
	/**
	 * The index of the node which is the start of this hamilton path. If the path is 4->5->8->3->0, the start is 4.
	 */
	private int start;

	public Arc getStart() {
		return get(start);
	}

	public void setStartNode(int startIndex) {
		start = startIndex;
	}
	
	public void add(Arc arc) {
		totalWeight += arc.getWeight();
		put(arc.getSource(), arc);
	}
	
}
