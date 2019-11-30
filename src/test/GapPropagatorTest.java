package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Arc;
import main.Fragment;
import main.FragmentList;
import main.GapPropagator;
import main.HamiltonPath;
import main.OverlapGraph;
import main.SemiGlobalAlignment;

public class GapPropagatorTest {

	@Test
	public void testPropagateGaps() {

		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
		
		OverlapGraph graph = new OverlapGraph(fl);
		
		GapPropagator gp = new GapPropagator(fl);
		
		HamiltonPath path = graph.getHamiltonPath();
		
		Fragment[] tab = gp.propagateGaps(path);
		
		System.out.println(path);
		
		assertEquals(path.getStart().getSource(), 3);
		
		for (int i=0; i<4; i++) {
			Arc arc = path.get(i);
			Fragment f = fl.get(arc.getSource());
			if (arc.isComplSource())
				f = f.getComplementary();
			Fragment g = fl.get(arc.getDestination());
			if (arc.isComplDest())
				g = g.getComplementary();
			SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
			System.out.println(i+"   :    "+sga.getScoreFG(true));
			System.out.println(sga.fAligned);
			System.out.println(sga.gAligned);
		}
		
		for (int i=0; i<tab.length; i++) 
			System.out.println("ici - "+tab[i]);
		
		//assertEquals(tab[0], new Fragment("tccgaagtctgct-------"));
	}

}
