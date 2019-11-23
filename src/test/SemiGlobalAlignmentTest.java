package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Fragment;
import main.SemiGlobalAlignment;

public class SemiGlobalAlignmentTest {
	
	Fragment h = new Fragment("gtcc");
	Fragment y = new Fragment("atgt");
	
	
	SemiGlobalAlignment sga1Test = new SemiGlobalAlignment(h, y);
	SemiGlobalAlignment sga2Test = new SemiGlobalAlignment(y, y);
	
	@Test
	public void testMatrix() {
		
		int[][] testMatrix = new int[5][5];
		
		testMatrix[0][0] = 0;
		testMatrix[0][1] = 0;
		testMatrix[0][2] = 0;
		testMatrix[0][3] = 0;
		testMatrix[0][4] = 0;
		testMatrix[1][0] = 0;
		testMatrix[2][0] = 0;
		testMatrix[3][0] = 0;
		testMatrix[4][0] = 0;
		testMatrix[1][1] = -1;
		testMatrix[2][1] = -1;
		testMatrix[3][1] = -1;
		testMatrix[4][1] = -1;
		testMatrix[1][2] = -1;
		testMatrix[1][3] = 1;
		testMatrix[1][4] = -1;
		testMatrix[2][2] = 0;
		testMatrix[2][3] = -1;
		testMatrix[2][4] = 2;
		testMatrix[3][2] = -2;
		testMatrix[4][2] = -2;
		testMatrix[3][3] = -1;
		testMatrix[3][4] = 0;
		testMatrix[4][3] = -3;
		testMatrix[4][4] = -2;
		
		
		int[][] alignmentMatrix = sga1Test.getMatrix();
		
		for(int i =0; i<4;i++) {
			assertArrayEquals(alignmentMatrix[i], testMatrix[i]);
		}
		
	}
	
	@Test
	public void testMatchValue() {
		
		for(int i=1; i<4; i++) {
			for(int j=1; j<4 ; j++) {
				if(i==j) {
					assertEquals(sga2Test.matchValue(i, j),1);
				}
			}
		}
		assertEquals(sga1Test.matchValue(1, 1),-1);
		assertEquals(sga1Test.matchValue(2, 2),1);
		assertEquals(sga1Test.matchValue(3, 3),-1);
		assertEquals(sga1Test.matchValue(4, 4),-1);
		
	}
	
	@Test
	public void testMaxLine() {
		assertEquals(sga1Test.getMaxLastLine(), 0);
		assertEquals(sga2Test.getMaxLastLine(), 4);
	}
	
	@Test
	public void testMaxColumn() {
		assertEquals(sga1Test.getMaxLastColumn(), 2);
		assertEquals(sga2Test.getMaxLastLine(), 4);
	}
	
	@Test
	public void testAlignementFG() {
		assertEquals(sga1Test.getAlignmentFG(), 0);
		assertEquals(sga2Test.getAlignmentFG(), -1);
	}
	
	@Test
	public void testAlignementGF() {
		assertEquals(sga1Test.getAlignmentGF(), 2);
		assertEquals(sga2Test.getAlignmentFG(), -1);
	}

}
