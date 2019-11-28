package main;

import java.util.ArrayList;

public class GapPropagation {
	
	private class CoupleFragments {
		
		private FragmentBuilder f;
		private FragmentBuilder g;
		
		private CoupleFragments(FragmentBuilder f, FragmentBuilder g) {
			this.f = f;
			this.g = g;
		}
		
	}
	
	private FragmentList fragments;
	
	private ArrayList<FragmentBuilder> result;
	
	/*
	private FragmentBuilder fAligned;
	private FragmentBuilder gAligned;
	*/
	
	public GapPropagation(FragmentList fl) {
		fragments = fl;
	}
	
	public void propagateGaps(HamiltonPath path) {
		Arc arc = path.last();
		CoupleFragments c1 = getFragmentsFromArc(arc);
		
		// !!!! Ne pas oublier de gerer la propagation correctement quand h est le tout premier fragment choisi
		
		FragmentBuilder f, g1, g2, h;
		
		f = c1.f;
		
		g1 = c1.g;
		
		for (int i=1; i<path.size(); i++) {
			arc = path.getArc(arc.getDestination());
			
			c1 = getFragmentsFromArc(arc);
			
			g2 = c1.f;
			h = c1.g;
			
			int j = 0;
			while (j < g1.size() && j < g2.size()) {
				if (g1.get(j) != g2.get(j)) {
					if (g1.get(j) == 4) 
						g2.add(j, (byte) 4);
					else if (g2.get(j) == 4) {
						insertGap(j);
					}
					j++;
				}
			}
				
			//comparer les machins
			
		}
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	private void insertGap(int index) {
		for (FragmentBuilder fb : result)
			fb.add(index, (byte)(4));
	}
	
	private CoupleFragments getFragmentsFromArc(Arc arc) {

		Fragment f = fragments.get(arc.getSource());
		Fragment g = fragments.get(arc.getDestination());
		if (arc.isComplSource())
			f = f.getComplementary();
		if (arc.isComplDest())
			g = g.getComplementary();
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		sga.getScoreFG(true);
		CoupleFragments cf = new CoupleFragments(sga.fAligned, sga.gAligned);
		return cf;
	}
	
}
