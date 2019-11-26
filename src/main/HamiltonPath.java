package main;

import java.util.Comparator;
import java.util.TreeSet;

public class HamiltonPath extends TreeSet<Arc>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HamiltonPath() {
		super(new Comparator<Arc>() {
			public int compare(Arc o1, Arc o2) {
				return o1.getSource() - o2.getSource();
			}
		});
	}
	
	public Arc getArc(int arcSource) {
		return ceiling(new Arc(arcSource, 0, false, false, 0));
	}
	
}
