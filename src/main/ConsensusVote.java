package main;

public class ConsensusVote {
	
	private ConsensusVote() {}
	
	private static final byte[] byteFromInt = {1, -1, 2, -2};
	
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
	 * @param fragTab An array of aligned fragments (i.e., all of them have the same size)
	 * @return the "consensus fragment"
	 */
	public static Fragment consensusVote(Fragment[] fragTab) {
		
		int fragmentSize = fragTab[0].size();
		Fragment consensusFragment = new Fragment(new byte[fragmentSize]);
		
		short[] cntTab = new short[4];
		
		//Pour chaque colonne, compter le nombre de 'a'(0), 'c'(1), 't'(2), 'g'(3), '-'(4)
		for(int j=0; j<fragmentSize; j++) {
			for(int i=0; i<fragTab.length; i++) {
				if (fragTab[i].byteAt(j) != 0)
					cntTab[getIndexFromByte(fragTab[i].byteAt(j))] ++;
			}
			
			consensusFragment.set(j, byteFromInt[maxIndex(cntTab)]);
			
			//Reinitialisation de cntTab
			for (int i=0; i<4; i++)
				cntTab[i] = 0;
		}
		
		return consensusFragment;
	}

}
