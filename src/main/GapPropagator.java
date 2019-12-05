package main;

import java.util.ListIterator;

/**
 * 
 * This class manage the gap propagation step 
 *
 */
public class GapPropagator {
	
	private FragmentList fragments;
	
	private int[] maxIndices;
	
	private FragmentBuilder[] result;
	
	/**
	 * 
	 * @param fl The fragmentList of the fragments
	 */
	public GapPropagator(FragmentList fl) {
		fragments = fl;
	}
	
	/**
	 * 
	 * @param path the hamilton path on the fragmentList given in the constructor
	 * @return An array of fragments which contains all the aligned fragments from the hamilton path
	 */
	public Fragment[] propagateGaps(HamiltonPath path) {
		maxIndices = new int[path.size()+1];
		result = new FragmentBuilder[path.size()+1];

		
		Arc arc = path.getStart();
		
		AlignedFragments couple = getFragmentsFromArc(arc);
		
		FragmentBuilder g1, g2, h=null;
		
		int i = 0;
		
		addToResult(0, couple.f);
		
		g1 = couple.g;
		
		for (i=1; i<path.size(); i++) {
			
			System.out.println("Cnt : "+i);
			
			addToResult(i, g1);
			arc = path.get(arc.getDestination());
			
			couple = getFragmentsFromArc(arc);
			
			g2 = couple.f;
			h = couple.g;
			
			int j = g1.getStartGaps();
			
			g2.addStartGaps(g1.getStartGaps());
			h.addStartGaps(g1.getStartGaps());
			
			ListIterator<Byte> iteratorG2 = g2.innerList().listIterator();
			ListIterator<Byte> iteratorH = h.innerList().listIterator();
			
			while (j < g1.size() && j < g2.size()) {
				byte byteG2 = iteratorG2.next();
				if (j >= h.getStartGaps())
					iteratorH.next();
				if (g1.byteAt(j) != byteG2) {
					if (g1.byteAt(j) == 0) {
						iteratorG2.previous();
						iteratorG2.add((byte)0);
						if (j >= h.getStartGaps()) {
							iteratorH.previous();
							iteratorH.add((byte)0);
						}
						else
							h.addStartGaps(1);
					}
					else if (byteG2 == 0) {
						insertGap(i, j);
					}
				}
				j++;
			}
			if (j < g1.size()) 
				h.addEndGaps(g1.size()-j);
			
			g1 = h;	
		}
		result[i] = h;		
		
		Fragment[] tab = new Fragment[result.length];
		i = 0;
		int size = h.size();
		for (FragmentBuilder fb : result) {
			fb.addEndGaps(size-fb.totalSize());
			tab[i] = new Fragment(fb);
			i++;
		}
		return tab;
	}
	
	private void addToResult(int index, FragmentBuilder fb) {
		result[index] = fb;
		maxIndices[index] = fb.size();
	}
	
	private void insertGap(int resultIndex, int gapIndex) {
		for (int i = resultIndex; i>=0; i--) {
			if (gapIndex < maxIndices[i]) {
				//System.out.println("Bonjour "+i);
				result[i].insert(gapIndex, (byte)0);
				maxIndices[i]++;
			}
			else
				break;
		}
	}
	
	/**
	 * 
	 * @param arc An arc
	 * @return The two aligned fragments indicated by the arc in the FragmentList fragments attribute
	 */
	private AlignedFragments getFragmentsFromArc(Arc arc) {
		Fragment f = fragments.get(arc.getSource());
		Fragment g = fragments.get(arc.getDestination());
		if (arc.isComplSource())
			f = f.getComplementary();
		if (arc.isComplDest())
			g = g.getComplementary();
		SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
		int score = sga.getScoreFG();
		if (score == 0)
			return juxtaposeFragments(f, g);
		return sga.retrieveWordsAligned();
	}
	
	/**
	 * Used when the arc f->g has an weight of 0. Put |f| gaps before g and |g| gaps after f to align them
	 * @param f The first fragment
	 * @param g The second fragment
	 * @return The alignedFraments in a coupleFragments object
	 */
	private AlignedFragments juxtaposeFragments(Fragment f, Fragment g) {
		FragmentBuilder fNew = new FragmentBuilder();
		FragmentBuilder gNew = new FragmentBuilder();
		
		for (int i=0; i<f.size(); i++) 
			fNew.addFirst(f.byteAt(i));
		gNew.addStartGaps(f.size());
		
		for (int i=0; i<g.size(); i++) 
			gNew.addFirst(g.byteAt(i));		
		fNew.addEndGaps(f.size());
		
		return new AlignedFragments(fNew, gNew);
	}
	
}
