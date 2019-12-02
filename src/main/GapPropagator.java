package main;

import java.util.LinkedList;

/**
 * 
 * This class manage the gap propagation step 
 *
 */
public class GapPropagator {
	
	private class CoupleFragments {
		
		private FragmentBuilder f;
		private FragmentBuilder g;
		
		private CoupleFragments(FragmentBuilder f, FragmentBuilder g) {
			this.f = f;
			this.g = g;
		}
	}
	
	private FragmentList fragments;
	
	private LinkedList<FragmentBuilder> result;
	
	/**
	 * 
	 * @param fl The fragmentList of the fragments
	 */
	public GapPropagator(FragmentList fl) {
		fragments = fl;
		result = new LinkedList<FragmentBuilder>();
	}
	
	/**
	 * 
	 * @param path the hamilton path on the fragmentList given in the constructor
	 * @return An array of fragments which contains all the aligned fragments from the hamilton path
	 */
	public Fragment[] propagateGaps(HamiltonPath path) {
		Arc arc = path.getStart();
		
		CoupleFragments couple = getFragmentsFromArc(arc);
		
		FragmentBuilder g1, g2, h=null;
		
		result.add(couple.f);
		g1 = couple.g;
		
		for (int i=1; i<path.size(); i++) {
			result.add(g1);
			arc = path.get(arc.getDestination());
			
			couple = getFragmentsFromArc(arc);
			
			g2 = couple.f;
			h = couple.g;
			
			int j = 0;
			while (j < g1.size() && j < g2.size()) {
				if (g1.get(j) != g2.get(j)) {
					if (g1.get(j) == 4) {
						g2.add(j, (byte) 4);
						h.add(j, (byte) 4);
					}
					else if (g2.get(j) == 4) {
						insertGap(j);
					}
				}
				j++;
			}
			if (j < g1.size()) {
				for (int k=0; k<g1.size()-j; k++) {
					g2.add((byte) 4);
					h.add((byte) 4);
				}
			}
			else if (j < g2.size()) {
				for (int k=0; k<g2.size()-j; k++)
					insertGap(-1);
			}
			
			g1 = h;	
		}
		result.add(h);
		
		
		Fragment[] tab = new Fragment[result.size()];
		int i = 0;
		for (FragmentBuilder fb : result) {
			tab[i] = new Fragment(fb);
			i++;
		}
		return tab;
	}
	
	private void insertGap(int index) {
		for (FragmentBuilder fb : result) {
			if (index == -1)
				fb.add((byte)4);
			else
				fb.add(index, (byte)(4));
		}
	}
	
	/**
	 * 
	 * @param arc An arc
	 * @return The two aligned fragments indicated by the arc in the FragmentList fragments attribute
	 */
	private CoupleFragments getFragmentsFromArc(Arc arc) {
		Fragment f = fragments.get(arc.getSource());
		Fragment g = fragments.get(arc.getDestination());
		if (arc.isComplSource())
			f = f.getComplementary();
		if (arc.isComplDest())
			g = g.getComplementary();
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		int score = sga.getScoreFG(true);
		if (score == 0)
			return juxtaposeFragments(f, g);
		CoupleFragments cf = new CoupleFragments(sga.fAligned, sga.gAligned);
		return cf;
	}
	
	/**
	 * Used when the arc f->g has an weight of 0. Put |f| gaps before g and |g| gaps after f to align them
	 * @param f The first fragment
	 * @param g The second fragment
	 * @return The alignedFraments in a coupleFragments object
	 */
	private CoupleFragments juxtaposeFragments(Fragment f, Fragment g) {
		FragmentBuilder fNew = new FragmentBuilder();
		FragmentBuilder gNew = new FragmentBuilder();
		
		for (int i=0; i<f.size(); i++) {
			fNew.add(f.byteAt(i));
			gNew.add((byte)4);
		}
		for (int i=0; i<g.size(); i++) {
			fNew.add((byte)4);
			gNew.add(g.byteAt(i));
		
		}
		return new CoupleFragments(fNew, gNew);
	}
	
}
