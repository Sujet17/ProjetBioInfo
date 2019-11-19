package test;

import static org.junit.Assert.*;

import java.util.PriorityQueue;

import org.junit.Test;

import main.OrientedEdge;

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

}
