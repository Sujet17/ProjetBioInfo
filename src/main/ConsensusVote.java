package main;

public class ConsensusVote {
	
	private ConsensusVote() {}
	
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
	
	public static Fragment consensusVote(Fragment[] fragTab) {
		short[] cntTab = new short[4];
		
		int fragmentSize = fragTab[0].size();
		
		Fragment consensusFragment = new Fragment(new byte[fragmentSize]);
		
		//Pour chaque colonne, compter le nombre de 'a'(0), 'c'(1), 't'(2), 'g'(3), '-'(4)
		for(int j=0; j<fragmentSize; j++) {
			for(int i=0; i<fragTab.length; i++) {
				if (fragTab[i].byteAt(j) < 4)
					cntTab[fragTab[i].byteAt(j)] ++;
			}
			
			consensusFragment.set(j, (byte)maxIndex(cntTab));
			
			//Reinitialisation de cntTab
			for (int i=0; i<4; i++)
				cntTab[i] = 0;
		}
		
		return consensusFragment;
	}
}
