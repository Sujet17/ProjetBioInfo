package main;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An non-instantiable class who provides static method to make the consensus vote step
 */
public class ConsensusVote {
	
	private ConsensusVote() {}
	
	/**
	 * This array is used to know the byte corresponding to an index of cntTab (see consensusVote() method) 
	 */
	private static final byte[] byteFromInt = {1, -1, 2, -2};
	
	/**
	 * This method is used to know the index of cntTab (see consensusVote() method) corresponding to a byte 
	 * @param b a byte
	 * @return an index of cntTab
	 */
	private static int getIndexFromByte(byte b) {
		switch(b) {
		case 1:
			return 0;
		case -1:
			return 1;
		case 2:
			return 2;
		case -2:
			return 3;
		}
		throw new IllegalArgumentException(Byte.toString(b));
	}
	
	/**
	 * Used to find which is the most used character in a column
	 * @param tab an array of short
	 * @return the index of the max of the array
	 */
	private static int maxIndex(short[] tab) {
		int maxIndex = 0;
		int max = tab[0];
		for (int k=1; k<tab.length; k++) {
			if(tab[k] > max) {
				maxIndex = k;
				max = tab[k];
			}
		}
		return maxIndex;
	}
	
	/**
	 * Returns a fragment whose the i-th character is the most represented character in the i-th column of the given array
	 * @param fbArray An array of aligned fragments (i.e., all of them have the same size)
	 * @return the "consensus fragment"
	 */
	public static Fragment consensusVote(FragmentBuilder[] fbArray) {

		ArrayList<Iterator<Byte>> iterators = new ArrayList<Iterator<Byte>>(fbArray.length);
		for (FragmentBuilder fb : fbArray) 
			iterators.add(fb.innerIterator());
		

		int fragmentSize = fbArray[0].totalSize();
		Fragment consensusFragment = new Fragment(new byte[fragmentSize]);
		
		/*
		 * Each column of cntTab counts the number of times a character (or rather the associated byte) is present in a column of fragTab.
		 * The correspondence between an column and the associated byte is done with byteFromInt array and getIndexFromByte() method.
		 */
		short[] cntTab = new short[4];
		
		int startLine = 0;
		//For every row, count the number of -2(g), -1(t), 1(a) and 2(c). 0(gaps) are ignored. 
			
		for (int j=0; j<fragmentSize; j++) {
			while (startLine<fbArray.length-1 && j>fbArray[startLine].size()-1)
				startLine++;
			int i = startLine;
			while (i<fbArray.length && j>fbArray[i].getStartGaps()-1) {
				byte b = iterators.get(i).next();		
				if (b != 0)
					cntTab[getIndexFromByte(b)] ++;
				i++;
			}

			consensusFragment.set(j, byteFromInt[maxIndex(cntTab)]);
			
			//cntTab is reinitialized for the next column
			for (int k=0; k<4; k++)
					cntTab[k] = 0;
		}		
		
		return consensusFragment;
	}

}
