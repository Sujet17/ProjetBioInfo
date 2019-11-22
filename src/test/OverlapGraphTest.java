package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.junit.Test;

import main.Fragment;
import main.FragmentList;
import main.Arc;
import main.OverlapGraph;
import main.SemiGlobalAlignment;
import main.UnionFind;

public class OverlapGraphTest {

	@Test
	public void testPriorityQueue() {
		
    	PriorityQueue<Arc> pq = new PriorityQueue<Arc>();
    	
    	Arc arc1 = new Arc(0, 0, true, true, 5);
    	Arc arc2 = new Arc(0, 0, true, true, 6);
    	Arc arc3 = new Arc(0, 0, true, true, 10);
    	Arc arc4 = new Arc(0, 0, true, true, 2);
    	Arc arc5 = new Arc(0, 0, true, true, 4);
		
    	pq.add(arc1);
    	pq.add(arc2);
    	pq.add(arc3);
    	pq.add(arc4);
    	pq.add(arc5);
    	
    	assertEquals(pq.poll().getWeight(), 10);
    	assertEquals(pq.poll().getWeight(), 6);
    	assertEquals(pq.poll().getWeight(), 5);
    	assertEquals(pq.poll().getWeight(), 4);
    	assertEquals(pq.poll().getWeight(), 2);
    	assertNull(pq.poll());
	}
	
	@Test
	public void testBuildArcs() {
		Fragment f = new Fragment("actgggtaccgtatag");
		Fragment g = new Fragment("accgactgatgcactg");
		
		Fragment fPrime = f.getComplementary();
		Fragment gPrime = g.getComplementary();
		
		SemiGlobalAlignment sga1 = new SemiGlobalAlignment(f, g);
		SemiGlobalAlignment sga2 = new SemiGlobalAlignment(gPrime, fPrime);
		
		assertEquals(sga1.getAlignmentFG(), sga2.getAlignmentFG());
	}
	
	
	@Test
	public void testAvailablearc() {
		OverlapGraph graph = new OverlapGraph(3);
		
		Arc arc1 = new Arc(0, 1, false, false, 4);
		Arc arc2 = new Arc(0, 1, false, true, 2);
		Arc arc3 = new Arc(0, 2, true, false, 5);
		Arc arc4 = new Arc(2, 1, false, true, 1);
		Arc arc5 = new Arc(2, 1, true, true, 4);
		
		int[] in = new int[5];
		int[] out = new int[5];
		
		UnionFind struct = new UnionFind(3);	
		assertTrue(graph.isAvailableArc(struct, arc1, in, out));
		assertTrue(graph.isAvailableArc(struct, arc2, in, out));
		assertTrue(graph.isAvailableArc(struct, arc3, in, out));
		assertTrue(graph.isAvailableArc(struct, arc4, in, out));
		assertTrue(graph.isAvailableArc(struct, arc5, in, out));
		
		in[2] = 1;
		out[0] = 2;
		struct.union(0, 2);
		
		assertFalse(graph.isAvailableArc(struct, arc1, in, out));
		assertFalse(graph.isAvailableArc(struct, arc2, in, out));
		assertTrue(graph.isAvailableArc(struct, arc4, in, out));
		assertFalse(graph.isAvailableArc(struct, arc5, in, out));
		
		out[2] = 1;
		in[1] = 2;
		struct.union(1, 2);
		
		assertTrue(struct.onlyOneSet());
		assertFalse(graph.isAvailableArc(struct, arc1, in, out));
		assertFalse(graph.isAvailableArc(struct, arc2, in, out));
		assertFalse(graph.isAvailableArc(struct, arc3, in, out));
		assertFalse(graph.isAvailableArc(struct, arc4, in, out));
		assertFalse(graph.isAvailableArc(struct, arc5, in, out));
		
	}
	
	@Test
	public void testHamiltonPath() {
		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");
		
		OverlapGraph graph = new OverlapGraph(fl);
		
		ArrayList<Arc> l = new ArrayList<Arc>();
		l.add(new Arc(3, 1, false, false, 4));
		l.add(new Arc(1, 4, false, false, 4));
		l.add(new Arc(4, 2, false, false, 2));
		l.add(new Arc(0, 3, false, false, 2));
		
		ArrayList<Arc> path = graph.hamiltonPath();
		
		assertEquals(l, path);
		
	}
	
}
