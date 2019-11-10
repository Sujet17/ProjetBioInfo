package main;

public class SemiGlobalAlignment {
	
	public static final int GAP_PENALTY = -2;
	public static final int MATCH_SCORE = 1;
	public static final int MISMATCH_SCORE = -1;	
	
	/**
	 * The longest fragment
	 */
	private Fragment f1;
	
	/**
	 * The shortest fragment
	 */
	private Fragment f2;
	
	/**
	 * The size of f1
	 */
	private int n;
	
	/**
	 * The size of f2
	 */
	private int m;
	
	/**
	 * An n-lines m-columns matrix
	 */
	private int[][] alignmentMatrix;
	
	public SemiGlobalAlignment(Fragment f1, Fragment f2) {
		n = f1.size();
		m = f2.size();
		
		if (n > m) {
			this.f1 = f1;
			this.f2 = f2;
		}
		else {
			this.f1 = f2;
			this.f2 = f1;
			n = m;
			m = f2.size();
		}
		alignmentMatrix = new int[n][m];
		buildMatrix();
	}
	
	private void buildMatrix() {
		//Initialisation 1ere colonne 
		for (int i=0; i<n; i++) 
			alignmentMatrix[i][0] = 0;
		//Initialisation 1ere ligne
		for (int i=0; i<m; i++) 
			alignmentMatrix[0][i] = 0;
		
		for (int i=1; i<n; i++) {
			for (int j=1; j<m; j++) {
				int score;
				if (f1.byteAt(i) == f2.byteAt(j))
					score = alignmentMatrix[i-1][j-1]+MATCH_SCORE;
				else
					score = alignmentMatrix[i-1][j-1]+MISMATCH_SCORE;
				score = Math.max(score, alignmentMatrix[i-1][j]+GAP_PENALTY);
				score = Math.max(score, alignmentMatrix[i][j-1]+GAP_PENALTY);
				alignmentMatrix[i][j] = score;
			}
		}
	}
	
	/**
	 * 
	 * @return a tuple of three elements : 1) the max score, 2) the x-coordinate of the case where this score appears, 
	 * 3) the y-coordinate of the same case 
	 */
	public int[] getMaxAlignment() {
		int x = 0;
		int y = m;
		int score = 0;
		if (n != m) {
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[i][m] > score) {
					x = i;
					score = alignmentMatrix[i][m];
				}
			}
		}
		else {
			//Check the last column
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[i][m] > score) {
					x = i;
					score = alignmentMatrix[i][m];
				}
			}
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[n][i] > score) {
					x = n;
					y = i;
					score = alignmentMatrix[n][i];
				}
			}
			//Check the last line
		}
		int tab[] = {score, x, y};
		return tab;
	}
	
	
}
