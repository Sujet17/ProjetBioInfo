package main;

public class SemiGlobalAlignment {
	
	public static final int GAP_PENALTY = -2;
	public static final int MATCH_SCORE = 1;
	public static final int MISMATCH_SCORE = -1;	
	
	private Fragment f;
	private Fragment g;
	
	/**
	 * The size of f
	 */
	private int n;
	
	/**
	 * The size of g
	 */
	private int m;
	
	/**
	 * An n-lines m-columns matrix
	 */
	private int[][] alignmentMatrix;
	
	public SemiGlobalAlignment(Fragment f, Fragment g) {
		n = f.size()+1;
		m = g.size()+1;
		
		this.f = f;
		this.g = g;
		
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
				int score = alignmentMatrix[i-1][j-1] + matchValue(i, j);
				score = Math.max(score, alignmentMatrix[i-1][j]+GAP_PENALTY);
				score = Math.max(score, alignmentMatrix[i][j-1]+GAP_PENALTY);
				alignmentMatrix[i][j] = score;
			}
		}
	}
	
	/**
	 * Check match or mismatch
	 * @param x , index of the first value to compare
	 * @param y , index of the second value to compare
	 * @return true if values match, false if not
	 */
	private int matchValue(int x, int y) {
		int xTest = x-1;
		int yTest = y-1;
		if (f.byteAt(xTest) == g.byteAt(yTest))
			return MATCH_SCORE;
		return MISMATCH_SCORE;
	}
	
	/**
	 * 
	 * @return The index of the max score on the last column
	 */
	public int getMaxLastColumn() {
		int iMax = 0;
		int score = alignmentMatrix[0][m-1];
		//Check the last column
		for (int i=1; i<n; i++) {
			if (alignmentMatrix[i][m-1] > score) {
				iMax = i;
				score = alignmentMatrix[i][m-1];
			}
		}
		return iMax;
	}
		
	/**
	 * 
	 * @return the index of the maximum on last line
	 */
	public int getMaxLastLine() {
		int iMax = 0;
		int score = alignmentMatrix[n-1][0];
		//Check the last line
		for (int i=1; i<m; i++) {
			if (alignmentMatrix[n-1][i] > score) {
				iMax = i;
				score = alignmentMatrix[n-1][i];
			}
		}
		return iMax;
	}
	
	/**
	 * 
	 * @return -1 if F is in G, 0 if no edge from F to G or score of global alignment if there is an edge from F to G
	 */
	public int getAlignmentFG() {
		int iMax = getMaxLastLine();
		// Pas d'arc f -> g
		if (iMax == 0) 
			return 0;
		else {
			MatrixCase startCase = new MatrixCase(n-1, iMax);
			MatrixCase endCase = buildAlignment(startCase);
			// f -> g avec comme poids le score de l'alignement semi-global
			if (endCase.getLine() > 0)
				return alignmentMatrix[startCase.getLine()][startCase.getColumn()];
			// f inclus a g
			else 
				return -1;
		}
	}
	
	/**
	 * 
	 * @return -1 if G is in F, 0 if no edge from G to F or score of global alignment if there is an edge from G to F
	 */
	public int getAlignmentGF() {
		int iMax = getMaxLastColumn();
		//Pas d'arc g -> f
		if (iMax == 0) 
			return 0;
		else {
			MatrixCase startCase = new MatrixCase(iMax, m-1);
			MatrixCase endCase = buildAlignment(startCase);
			// g -> f
			if (endCase.getColumn() > 0)
				return alignmentMatrix[startCase.getLine()][startCase.getColumn()];
			// g inclus a f
			else 
				return -1;
		}
	}
	
	/**
	 * 
	 * @param startCase
	 */
	public MatrixCase buildAlignment(MatrixCase startCase) {
		int i = startCase.getLine();
		int j = startCase.getColumn();
		while (i>=1 && j>=1) {
			int score = alignmentMatrix[i][j];
			
			int scoreDiag = alignmentMatrix[i-1][j-1];
			int scoreUp = alignmentMatrix[i-1][j];
			//int scoreLeft = alignmentMatrix[i][y-1];
			if (score == scoreDiag + matchValue(i, j)) {
				i = i-1;
				j = j-1;
			}
			else if (score == scoreUp + GAP_PENALTY) {
				i = i-1;
			}
			else {
				j = j-1;
			}
		}
		return new MatrixCase(i, j);
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<n; i++) {
			for (int j=0; j<m; j++) {
				if (alignmentMatrix[i][j] >= 0)
					builder.append(" ");
				builder.append(alignmentMatrix[i][j]);
				builder.append(" ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
}
