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
	
	FragmentBuilder fAligned;
	FragmentBuilder gAligned;
	
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
	 * @param x , index of the first value to compare
	 * @param y , index of the second value to compare
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
	public int getScoreFG(boolean saveFoundAlignment) {
		int iMax = getMaxLastLine();
		// Pas d'arc f -> g
		if (iMax == 0) 
			return 0;
		else {
			MatrixMove startCase = new MatrixMove(n-1, iMax);
			MatrixMove endCase;
			if (saveFoundAlignment) 
				endCase = retrieveWordsAligned(startCase);					
			else
				endCase = buildAlignment(startCase, false, null, null);
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
	public int getScoreGF(boolean saveFoundAlignment) {
		int iMax = getMaxLastColumn();
		//Pas d'arc g -> f
		if (iMax == 0) 
			return 0;
		else {
			MatrixMove startCase = new MatrixMove(iMax, m-1);
			MatrixMove endCase;
			if (saveFoundAlignment) 
				endCase = retrieveWordsAligned(startCase);
			else
				endCase = buildAlignment(startCase, false, null, null);
			// g -> f
			if (endCase.getColumn() > 0)
				return alignmentMatrix[startCase.getLine()][startCase.getColumn()];
			// g inclus a f
			else 
				return -1;
		}
	
	}
	
	/**
	 * Write the founded words on the fAligned and gAligned attributes
	 * @param start
	 * @return
	 */
	private MatrixMove retrieveWordsAligned(MatrixMove start) {

		fAligned = new FragmentBuilder();
		gAligned = new FragmentBuilder();		
		
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
		
		return end;
	}
	
	/**
	 * 
	 * @param startCase
	 */
	private MatrixMove buildAlignment(MatrixMove startCase, boolean saveAlignment, FragmentBuilder fList, FragmentBuilder gList) {
		MatrixMove nextCase = new MatrixMove(startCase.getLine(), startCase.getColumn());
		
		while (nextCase.getLine()>=1 && nextCase.getColumn()>=1) {
			if (saveAlignment)
				manageNextCase(nextCase, fList, gList);
			nextCase = getNextCase(nextCase.getLine(), nextCase.getColumn());
		}
		
		return nextCase;
	}
	
	private void manageNextCase(MatrixMove nextCase, FragmentBuilder fBuilder, FragmentBuilder gBuilder) {
		if (nextCase.getMove() == MatrixMove.MoveType.LEFT) {
			fBuilder.add((byte)4);
			gBuilder.add(g.byteAt(nextCase.getColumn()-1));
		}
		else if (nextCase.getMove() == MatrixMove.MoveType.UP) {
			fBuilder.add(f.byteAt(nextCase.getLine()-1));
			gBuilder.add((byte)4);
		}
		else {  // if (nextCase.getMove() == MatrixMove.MoveType.DIAG or nextCase.getMove() == null) 
			fBuilder.add(f.byteAt(nextCase.getLine()-1));
			gBuilder.add(g.byteAt(nextCase.getColumn()-1));
		}
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
