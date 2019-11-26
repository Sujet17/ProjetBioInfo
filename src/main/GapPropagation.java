package main;

import java.util.ArrayList;

public class GapPropagation {
	
	private FragmentList fragments;
	
	/*
	private FragmentBuilder fAligned;
	private FragmentBuilder gAligned;
	*/
	
	
	public GapPropagation(FragmentList fl) {
		fragments = fl;
	}
	
	public void propagateGaps(HamiltonPath path) {
		Arc arc = path.getArc(0);
		ArrayList<FragmentBuilder> fl = getFragmentsFromArc(arc);
		
		for (int i=1; i<path.size(); i++) {
			FragmentBuilder f1 = fl.get(1);
			arc = path.getArc(arc.getDestination());
			
			//comparer les machins
			
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
