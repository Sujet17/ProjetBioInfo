package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.AlignedFragments;
import main.Fragment;
import main.FragmentList;
import main.SemiGlobalAlignment;

public class SemiGlobalAlignmentTest {
	
	Fragment h = new Fragment("gtcc");
	Fragment y = new Fragment("atgt");
	
	
	SemiGlobalAlignment sga1Test = new SemiGlobalAlignment(h, y);
	SemiGlobalAlignment sga2Test = new SemiGlobalAlignment(y, y);
	
	@Test
	public void testMatrix() {
		
		short[][] testMatrix = new short[5][5];
		
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
		
		
		short[][] alignmentMatrix = sga1Test.getMatrix();
		
		for(int i =0; i<4;i++) {
			assertArrayEquals(alignmentMatrix[i], testMatrix[i]);
		}
		
	}
	
	
	@Test
	public void test() {
		
		FragmentList fl = FragmentList.getFragmentsFromFile("Collections/test/collectionTest.fasta");

		SemiGlobalAlignment sga = new SemiGlobalAlignment(fl.get(1), fl.get(4));
		assertEquals(4,sga.getScoreFG());
		AlignedFragments cf = sga.retrieveWordsAligned();
		assertEquals(new Fragment(cf.f), new Fragment("aggtcaactgatc-----") );
		assertEquals(new Fragment(cf.g), new Fragment("----caactg-ccaaaaa") );
		
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
		assertEquals(sga1Test.getScoreFG(), 0);
		assertEquals(sga2Test.getScoreFG(), -1);
	}
	
	@Test
	public void testAlignementGF() {
		assertEquals(sga1Test.getScoreGF(), 2);
		assertEquals(sga2Test.getScoreGF(), -1);
	}

}
