package main;

public class SemiGlobalAlignment {
	
	public static final int GAP_PENALTY = -2;
	public static final int MATCH_SCORE = 1;
	public static final int MISMATCH_SCORE = -1;	
	
	private Fragment f1;
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
	
	private Point maxCase;
	
	public SemiGlobalAlignment(Fragment f1, Fragment f2) {
		n = f1.size();
		m = f2.size();
		
		this.f1 = f1;
		this.f2 = f2;
		
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
		
		int maxScore = 0;
		maxCase = new Point(0, 0);
		
		for (int i=1; i<n; i++) {
			for (int j=1; j<m; j++) {
				int score = alignmentMatrix[i-1][j-1] + matchValue(i, j);
				score = Math.max(score, alignmentMatrix[i-1][j]+GAP_PENALTY);
				score = Math.max(score, alignmentMatrix[i][j-1]+GAP_PENALTY);
				if (score >= maxScore) {
					maxScore = score;
					maxCase = new Point(i, j);
				}
				alignmentMatrix[i][j] = score;
			}
		}
	}
	
	private int matchValue(int x, int y) {
		if (f1.byteAt(x) == f2.byteAt(y))
			return MATCH_SCORE;
		return MISMATCH_SCORE;
	}
	
	/**
	 * 
	 * @return a point which indicates the coordinates of the last case of the best alignment
	 */
	private Point getLastCase() {
		int x = 0;
		int y = m;
		int score = 0;
		if (m < n) {
			//Check the last column
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[i][m] > score) {
					x = i;
					score = alignmentMatrix[i][m];
				}
			}
		}
		else if (n < m) {
			//Check the last line
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[n][i] > score) {
					x = n;
					y = i;
					score = alignmentMatrix[n][i];
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
			//Check the last line
			for (int i=0; i<n; i++) {
				if (alignmentMatrix[n][i] > score) {
					x = n;
					y = i;
					score = alignmentMatrix[n][i];
				}
			}
		}
		return new Point(x, y);
	}
	
	public void buildAlignment() {
		Point xy = getLastCase();
		/*
		 * Si il faut commencer a partir du max du tableau:
		 * x = maxCase.getX()
		 * y = maxCase.getY()
		 */
		
		int x = xy.getX();
		int y = xy.getY();
		int score = alignmentMatrix[x][y];
		while (x>1 && y>1) {
			int scoreDiag = alignmentMatrix[x-1][y-1];
			int scoreUp = alignmentMatrix[x][y-1];
			//int scoreLeft = alignmentMatrix[x-1][y];
			if (score == scoreDiag + matchValue(x, y)) {
				x = x-1;
				y = y-1;
			}
			else if (score == scoreUp + GAP_PENALTY) {
				y = y-1;
			}
			else {
				x = x-1;
			}
		}
	}
	
}
