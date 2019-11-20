package test;

import static org.junit.Assert.*;

import java.util.PriorityQueue;

import org.junit.Test;

import main.Fragment;
import main.OrientedEdge;
import main.OverlapGraph;
import main.SemiGlobalAlignment;
import main.UnionFind;

public class OverlapGraphTest {

	@Test
	public void testPriorityQueue() {
		
    	PriorityQueue<OrientedEdge> pq = new PriorityQueue<OrientedEdge>();
    	
    	OrientedEdge arc1 = new OrientedEdge(0, 0, true, true, 5);
    	OrientedEdge arc2 = new OrientedEdge(0, 0, true, true, 6);
    	OrientedEdge arc3 = new OrientedEdge(0, 0, true, true, 10);
    	OrientedEdge arc4 = new OrientedEdge(0, 0, true, true, 2);
    	OrientedEdge arc5 = new OrientedEdge(0, 0, true, true, 4);
		
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
		assertEquals(sga1.getAlignmentGF(), sga2.getAlignmentGF());
	}
	
	@Test
	public void testAvailableEdge() {
		OverlapGraph graph = new OverlapGraph(3);
		
		OrientedEdge edge1 = new OrientedEdge(0, 1, false, false, 4);
		OrientedEdge edge2 = new OrientedEdge(0, 1, false, true, 2);
		OrientedEdge edge3 = new OrientedEdge(0, 2, true, false, 5);
		OrientedEdge edge4 = new OrientedEdge(2, 1, false, true, 1);
		OrientedEdge edge5 = new OrientedEdge(2, 1, true, true, 4);
		
		int[] in = new int[5];
		int[] out = new int[5];
		
		UnionFind struct = new UnionFind(3);	
		assertTrue(graph.isAvailableEdge(struct, edge1, in, out));
		assertTrue(graph.isAvailableEdge(struct, edge2, in, out));
		assertTrue(graph.isAvailableEdge(struct, edge3, in, out));
		assertTrue(graph.isAvailableEdge(struct, edge4, in, out));
		assertTrue(graph.isAvailableEdge(struct, edge5, in, out));
		
		in[2] = 1;
		out[0] = 2;
		struct.union(0, 2);
		
		assertFalse(graph.isAvailableEdge(struct, edge1, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge2, in, out));
		assertTrue(graph.isAvailableEdge(struct, edge4, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge5, in, out));
		
		out[2] = 1;
		in[1] = 2;
		struct.union(1, 2);
		
		assertTrue(struct.onlyOneSet());
		assertFalse(graph.isAvailableEdge(struct, edge1, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge2, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge3, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge4, in, out));
		assertFalse(graph.isAvailableEdge(struct, edge5, in, out));
		
		
	}
	
}
