package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.FragmentList;
import main.HamiltonPath;
import main.OverlapGraph;

/*
import main.Fragment;
import main.GapPropagator;
import main.Arc;
import main.SemiGlobalAlignment;
*/
public class GapPropagatorTest {

	@Test
	public void testPropagateGaps() {

		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
		
		OverlapGraph graph = new OverlapGraph(fl);
		
		
		HamiltonPath path = graph.getHamiltonPath();
		
		assertEquals(path.getStart().getSource(), 3);
		
		/*
		 * 
		GapPropagator gp = new GapPropagator(fl);
		Fragment[] tab = gp.propagateGaps(path);
		
		for (int i : path.keySet()) {
			Arc arc = path.get(i);
			Fragment f = fl.get(arc.getSource());
			if (arc.isComplSource())
				f = f.getComplementary();
			Fragment g = fl.get(arc.getDestination());
			if (arc.isComplDest())
				g = g.getComplementary();
			SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
			System.out.println(i+" : "+arc.getDestination()+" ; "+sga.getScoreFG(true)+ " ; f :"+f+" ; g :"+g);
			System.out.println(sga.fAligned);
			System.out.println(sga.gAligned);
		}
		
		for (int i=0; i<tab.length; i++) 
			System.out.println("ici - "+tab[i]);
		*/
	}

}
