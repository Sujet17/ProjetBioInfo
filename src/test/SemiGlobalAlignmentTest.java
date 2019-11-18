package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Fragment;
import main.SemiGlobalAlignment;

public class SemiGlobalAlignmentTest {
	
	Fragment h = new Fragment("gtcc");
	Fragment y = new Fragment("atgt");
	
	int[][] testMatrix = new int[5][5];
	

	
	SemiGlobalAlignment sga2Test = new SemiGlobalAlignment(y, y);
	SemiGlobalAlignment sga1Test = new SemiGlobalAlignment(h, y);
	
	@Test
	public void testMatrix() {
		
		testMatrix[0][0]=0;
		testMatrix[0][1]=0;
		testMatrix[0][2]=0;
		testMatrix[0][3]=0;
		testMatrix[0][4]=0;
		testMatrix[1][0]=0;
		testMatrix[2][0]=0;
		testMatrix[3][0]=0;
		testMatrix[4][0]=0;
		testMatrix[1][1]=-1;
		testMatrix[2][1]=-1;
		testMatrix[3][1]=-1;
		testMatrix[4][1]=-1;
		testMatrix[1][2]=-1;
		testMatrix[1][3]=1;
		testMatrix[1][4]=-1;
		testMatrix[2][2]=0;
		testMatrix[2][3]=-1;
		testMatrix[2][4]=2;
		testMatrix[3][2]=-2;
		testMatrix[4][2]=-2;
		testMatrix[3][3]=-1;
		testMatrix[3][4]=0;
		testMatrix[4][3]=-3;
		testMatrix[4][4]=-2;
		
		int[][] alignmentMatrix1 = new int[h.size()][y.size()];
		
		for (int i=0; i<h.size(); i++) 
			alignmentMatrix1[i][0] = 0;
		
		for (int i=0; i<y.size(); i++) 
			alignmentMatrix1[0][i] = 0;
		
		for (int i=1; i<h.size(); i++) {
			for (int j=1; j<y.size(); j++) {
				if(h.byteAt(i-1) == y.byteAt(j-1)) {
					int score = alignmentMatrix1[i-1][j-1] + 1;
					score = Math.max(score, alignmentMatrix1[i-1][j]-2);
					score = Math.max(score, alignmentMatrix1[i][j-1]-2);
					alignmentMatrix1[i][j] = score;
				}
				else {
					int score = alignmentMatrix1[i-1][j-1] - 1;
					score = Math.max(score, alignmentMatrix1[i-1][j]-2);
					score = Math.max(score, alignmentMatrix1[i][j-1]-2);
					alignmentMatrix1[i][j] = score;
				}

			}
		}
		
		
		for(int i =0; i<4;i++) {
			for(int j =0; j<4 ;j++){
				assertEquals(testMatrix[i][j], alignmentMatrix1[i][j]);
			}
		}
		
	
	}
	
	@Test
	public void testMatchValue() {
		int res1 =0;
		int res2 =0;
		for (int i=0;i<4;i++) {
			if(h.byteAt(i)==y.byteAt(i)) {
				res1+=1;
			}
			if(h.byteAt(i)==h.byteAt(i)) {
				res2+=1;
			}
			if(h.byteAt(i)!=y.byteAt(i)) {
				res1-=1;
			}
			if(h.byteAt(i)!=h.byteAt(i)) {
				res2-=1;
			}
		}
		assertEquals(res1,-2);
		assertEquals(res2,4);
		
	}
	
	@Test
	public void testMaxLine() {
		assertEquals(sga1Test.getMaxLastLine(),0);
		assertEquals(sga2Test.getMaxLastLine(),4);
	}
	
	@Test
	public void testMaxColumn() {
		assertEquals(sga1Test.getMaxLastColumn(),2);
		assertEquals(sga2Test.getMaxLastLine(),4);
	}
	
	@Test
	public void testAlignementFG() {
		assertEquals(sga1Test.getAlignmentFG(),0);
		assertEquals(sga2Test.getAlignmentFG(),-1);
	}
	
	@Test
	public void testAlignementGF() {
		assertEquals(sga1Test.getAlignmentGF(),2);
		assertEquals(sga2Test.getAlignmentFG(),-1);
	}

}
