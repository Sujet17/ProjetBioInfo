package main;

import java.util.ArrayList;

public class GapPropagation {
	
	private FragmentList fragments;
	
	public GapPropagation(FragmentList fl) {
		fragments = fl;
	}
	
	public void propagateGaps(ArrayList<Arc> path) {
		ArrayList<FragmentBuilder> fl;
		
		for (int i=1; i<path.size(); i++) {
			fl = getFragmentsFromArc(path.get(i));
		}
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private ArrayList<FragmentBuilder> getFragmentsFromArc(Arc arc) {
		Fragment f = fragments.get(arc.getSource());
		Fragment g = fragments.get(arc.getDestination());
		if (arc.isComplSource())
			f = f.getComplementary();
		if (arc.isComplDest())
			g = g.getComplementary();
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		sga.getScoreFG(true);
		ArrayList<FragmentBuilder> l = new ArrayList<FragmentBuilder>();
		l.add((FragmentBuilder) sga.fAligned);
		l.add((FragmentBuilder) sga.gAligned);
		return l;
	}
	
}
