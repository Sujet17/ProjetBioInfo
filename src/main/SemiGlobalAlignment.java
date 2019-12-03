package main;

public class SemiGlobalAlignment {
	
	public static final short GAP_PENALTY = -2;
	public static final short MATCH_SCORE = 1;
	public static final short MISMATCH_SCORE = -1;	
	
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
	private short[][] alignmentMatrix;
	
	public SemiGlobalAlignment(Fragment f, Fragment g) {
		n = f.size()+1;
		m = g.size()+1;
		
		this.f = f;
		this.g = g;
		
		alignmentMatrix = new short[n][m];
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
				alignmentMatrix[i][j] = (short) score;
			}
		}
	}
	
	/**
	 * Check match or mismatch
	 * @param x index of the first value to compare
	 * @param y index of the second value to compare
	 * @return true if values match, false if not
	 */
	public short matchValue(int x, int y) {
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
		for (int i=1; i<n-1; i++) {
			if (alignmentMatrix[i][m-1] > score) {
				iMax = i;
				score = alignmentMatrix[i][m-1];
			}
		}
		if (alignmentMatrix[n-1][m-1] > score) { //Si on se trouve sur la case en bas a droite
			MatrixMove nextCase = getNextCase(n-1, m-1);
			if (nextCase.getLine() != n-2 || nextCase.getColumn() != m-1)
				iMax = n-1;
		}
		return iMax;
	}
		
	public short[][] getMatrix() {
		return alignmentMatrix;
	}
	
	/**
	 * 
	 * @return the index of the maximum on last line
	 */
	public int getMaxLastLine() {
		int iMax = 0;
		int score = alignmentMatrix[n-1][0];
		//Check the last line
		for (int i=1; i<m-1; i++) {
			if (alignmentMatrix[n-1][i] > score) {
				iMax = i;
				score = alignmentMatrix[n-1][i];
			}
		}
		if (alignmentMatrix[n-1][m-1] > score) { //Si on se trouve sur la case en bas a droite
			MatrixMove nextCase = getNextCase(n-1, m-1);
			if (nextCase.getLine() != n-1 || nextCase.getColumn() != m-2)
				iMax = m-1;
		}
		return iMax;
	}
	
	/**
	 * 
	 * @return -1 if F is in G, 0 if no edge from F to G or score of global alignment if there is an edge from F to G
	 */
	public int getScoreFG() {
		int iMax = getMaxLastLine();

		if (iMax == 0) 	// f -> g de poids 0
			return 0;
		else {
			MatrixMove startCase = new MatrixMove(n-1, iMax);
			MatrixMove endCase = buildAlignment(startCase, false, null, null);
			
			if (endCase.getLine() > 0) // f -> g avec comme poids le score de l'alignement semi-global
				return alignmentMatrix[startCase.getLine()][startCase.getColumn()];
			else 	// f inclus a g
				return -1;
		}
	}
	
	/**
	 * 
	 * @return -1 if G is in F, the score of global alignment from f to g else
	 */
	public int getScoreGF() {
		int iMax = getMaxLastColumn();
		// g -> f de poids 0
		if (iMax == 0) 
			return 0;
		else {
			MatrixMove startCase = new MatrixMove(iMax, m-1);
			MatrixMove endCase = buildAlignment(startCase, false, null, null);

			if (endCase.getColumn() > 0) // g -> f
				return alignmentMatrix[startCase.getLine()][startCase.getColumn()];
			else // g inclus a f
				return -1;
		}
	}
	
	/**
	 * Write the founded words on the fAligned and gAligned attributes
	 * @param start
	 * @return
	 */
	public CoupleFragments retrieveWordsAligned() {

		FragmentBuilder fAligned = new FragmentBuilder();
		FragmentBuilder gAligned = new FragmentBuilder();		
		
		MatrixMove start = new MatrixMove(n-1, getMaxLastLine());
		
		for (int i=0; i<n-1-start.getLine(); i++) {
			fAligned.addFirst(f.byteAt(n-2-i));
			gAligned.addFirst((byte)4);
		}
		for (int i=0; i<m-1-start.getColumn(); i++) {
			fAligned.addFirst((byte)4);
			gAligned.addFirst(g.byteAt(m-2-i));
		}
		
		MatrixMove end = buildAlignment(start, true, fAligned, gAligned);
		
		for (int i=end.getLine(); i>0; i--) {
			fAligned.addFirst(f.byteAt(i-1));
			gAligned.addFirst((byte)4);
		}
		for (int i=end.getColumn(); i>0; i--) {
			fAligned.addFirst((byte)4);
			gAligned.addFirst(g.byteAt(i-1));
		}
		
		return new CoupleFragments(fAligned, gAligned);
	}
	
	/**
	 * 
	 * @param startCase
	 */
	private MatrixMove buildAlignment(MatrixMove startCase, boolean saveAlignment, FragmentBuilder fList, FragmentBuilder gList) {
		MatrixMove nextCase = new MatrixMove(startCase.getLine(), startCase.getColumn());
		int x = 0, y = 0;
		
		while (nextCase.getLine()>=1 && nextCase.getColumn()>=1) {
			if (saveAlignment) {
				x = nextCase.getLine();
				y = nextCase.getColumn();
			}
			nextCase = getNextCase(nextCase.getLine(), nextCase.getColumn());
			if (saveAlignment) {
				manageNextCase(x, y, nextCase.getMove(), fList, gList);
			}
		}
		
		return nextCase;
	}
	
	private void manageNextCase(int line, int column, MatrixMove.MoveType oldMove, FragmentBuilder fBuilder, FragmentBuilder gBuilder) {

		if (oldMove == MatrixMove.MoveType.LEFT) {
			fBuilder.addFirst((byte)4);
			gBuilder.addFirst(g.byteAt(column-1));
		}
		else if (oldMove == MatrixMove.MoveType.UP) {
			fBuilder.addFirst(f.byteAt(line-1));
			gBuilder.addFirst((byte)4);
		}
		else if (oldMove == MatrixMove.MoveType.DIAG) {  
			fBuilder.addFirst(f.byteAt(line-1));
			gBuilder.addFirst(g.byteAt(column-1));
		}
		//else
		//	throw new IllegalArgumentException("MoveType ne peut pas être null");
		 
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private MatrixMove getNextCase(int x, int y) {
		int i = x;
		int j = y;
		int score = alignmentMatrix[i][j];
		int scoreDiag = alignmentMatrix[i-1][j-1];
		int scoreUp = alignmentMatrix[i-1][j];
		//int scoreLeft = alignmentMatrix[i][y-1];
		MatrixMove.MoveType move = null;
		
		if (score == scoreDiag + matchValue(i, j)) {
			i = i-1;
			j = j-1;
			move = MatrixMove.MoveType.DIAG;
		}
		else if (score == scoreUp + GAP_PENALTY) {
			i = i-1;
			move = MatrixMove.MoveType.UP;
		}
		else {
			j = j-1;
			move = MatrixMove.MoveType.LEFT;
		}
		return new MatrixMove(i, j, move);
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
