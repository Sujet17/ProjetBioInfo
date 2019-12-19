package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.FragmentList;
import main.HamiltonPath;
import main.OverlapGraph;
import main.Fragment;
import main.FragmentBuilder;
import main.GapPropagator;


import main.Arc;
import main.AlignedFragments;
import main.SemiGlobalAlignment;


public class GapPropagatorTest {

	@Test
	public void testPropagateGaps() {

		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
		
		OverlapGraph graph = new OverlapGraph(fl);
		
		
		HamiltonPath path = graph.getHamiltonPath();
		
		assertEquals(path.getStart().getSource(), 0);
		
		
		GapPropagator gp = new GapPropagator(fl);
		FragmentBuilder[] fbArray = gp.propagateGaps(path);		
		
		Fragment[] tab = new Fragment[fbArray.length];
		for(int i=0; i<fbArray.length; i++)
			tab[i] = new Fragment(fbArray[i]);		
		
		//printDebug(path, fl, tab);
		
		assertEquals(tab[0], new Fragment("actactaggcc--------------------------------------------"));
		assertEquals(tab[1], new Fragment("------aggtcaactgatc------------------------------------"));
		assertEquals(tab[2], new Fragment("----------caactg-ccaaaaa-------------------------------"));
		assertEquals(tab[3], new Fragment("-----------aac---ccaaaaagggg---------------------------"));
		assertEquals(tab[4], new Fragment("------------------------gggggggtcggt-------------------"));
		assertEquals(tab[5], new Fragment("-----------------------------------tccgaagtctgct-------"));
		assertEquals(tab[6], new Fragment("--------------------------------------------tgctgctggag"));
		
	}
	
	public static FragmentBuilder[] getExample2() {
		FragmentList fl = new FragmentList();
		fl.add(new Fragment("cccccacg"));
		fl.add(new Fragment("acggttaag"));
		fl.add(new Fragment("ggttaaggggg"));
		fl.add(new Fragment("gttccaaggggttt"));
		fl.add(new Fragment("tccggaaggggttcc"));
		fl.add(new Fragment("gaaggttcc"));
		fl.add(new Fragment("aggttccaaaaa"));
		
		HamiltonPath path = new HamiltonPath();
		
		path.setStartNode(0);
		path.add(new Arc(0, 1, false, false, 3));
		path.add(new Arc(1, 2, false, false, 7));
		path.add(new Arc(2, 3, false, false, 4));
		path.add(new Arc(3, 4, false, false, 6));
		path.add(new Arc(4, 5, false, false, 5));
		path.add(new Arc(5, 6, false, false, 4));
		
		GapPropagator gp = new GapPropagator(fl);
		FragmentBuilder[] fbArray = gp.propagateGaps(path);		
		
		return fbArray;
	}
	
	@Test
	public void test2() {
		FragmentBuilder[] fbArray = getExample2();
		Fragment[] tab = new Fragment[fbArray.length];
		for(int i=0; i<fbArray.length; i++)
			tab[i] = new Fragment(fbArray[i]);			
		
		//printDebug(path, fl, tab);
		
		assertEquals(tab[0], new Fragment("cccccacg----------------------"));
		assertEquals(tab[1], new Fragment("-----acggtt-a--ag-------------"));
		assertEquals(tab[2], new Fragment("-------ggtt-a--aggggg---------"));
		assertEquals(tab[3], new Fragment("--------gttcc--aaggggttt------"));
		assertEquals(tab[4], new Fragment("----------tccggaaggggttcc-----"));
		assertEquals(tab[5], new Fragment("--------------gaa--ggttcc-----"));
		assertEquals(tab[6], new Fragment("----------------a--ggttccaaaaa"));
	}
	
	/*
	 * Used for debugging
	 */
	@SuppressWarnings("unused")
	private void printDebug(HamiltonPath path, FragmentList fl, Fragment[] tab) {
		for (int i : path.keySet()) {
			Arc arc = path.get(i);
			Fragment f = fl.get(arc.getSource());
			if (arc.isComplSource())
				f = f.getComplementary();
			Fragment g = fl.get(arc.getDestination());
			if (arc.isComplDest())
				g = g.getComplementary();
			SemiGlobalAlignment sga = new SemiGlobalAlignment(f, g);
			System.out.println(i+" : "+arc.getDestination()+" ; "+sga.getScoreFG()+ " ; f :"+f+" ; g :"+g);
			AlignedFragments af = sga.retrieveWordsAligned();
			System.out.println(af.f);
			System.out.println(af.g);
		}

		for (int i=0; i<tab.length; i++) 
			System.out.println("ici - "+tab[i]);
	}

}
