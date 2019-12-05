package main;

public class SemiGlobalAlignment {
	
	/**
	 * An inner class that stores two int corresponding to the two indices to get an element in the alinmentMatrix
	 *
	 */
	private class MatrixIndices {
		
		private int line;
		private int column;
		
		private MatrixIndices(int line, int column) {
			this.line = line;
			this.column = column;
		}
	}
	
	public static final short GAP_PENALTY = -2;
	public static final short MATCH_SCORE = 1;
	public static final short MISMATCH_SCORE = -1;	
	
	private Fragment f;
	private Fragment g;
	
	/**
	 * (size of f)+1
	 */
	private int n;
	
	/**
	 * (size of g)+1
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
	 * Used for testing
	 * @return the alignmentMatrix
	 */
	public short[][] getMatrix() {
		return alignmentMatrix;
	}
	
	/**
	 * Check if the xth byte of f match the yth of g
	 * @param x index of the first byte to compare
	 * @param y index of the second byte to compare
	 * @return the score of a match if the bytes compared are equals, the score of a mismatch else
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
			MatrixIndices nextCase = getNextCase(n-1, m-1);
			if (nextCase.line != n-2 || nextCase.column != m-1)
				iMax = n-1;
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
		for (int i=1; i<m-1; i++) {
			if (alignmentMatrix[n-1][i] > score) {
				iMax = i;
				score = alignmentMatrix[n-1][i];
			}
		}
		if (alignmentMatrix[n-1][m-1] > score) { //Si on se trouve sur la case en bas a droite
			MatrixIndices nextCase = getNextCase(n-1, m-1);
			if (nextCase.line != n-1 || nextCase.column != m-2)
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
			MatrixIndices startCase = new MatrixIndices(n-1, iMax);
			MatrixIndices endCase = buildAlignment(startCase);
			
			if (endCase.line > 0) // f -> g avec comme poids le score de l'alignement semi-global
				return alignmentMatrix[startCase.line][startCase.column];
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
			MatrixIndices startCase = new MatrixIndices(iMax, m-1);
			MatrixIndices endCase = buildAlignment(startCase);

			if (endCase.column > 0) // g -> f
				return alignmentMatrix[startCase.line][startCase.column];
			else // g inclus a f
				return -1;
		}
	}
	
	/**
	 * Return the semi-global alignment f->g. This method doesn't work if the score of f->g is equal to zero. 
	 * @return A pair of FragmentBuilder. The first is f and the second is g and both are aligned following the f->g alignment
	 */
	public AlignedFragments retrieveWordsAligned() {

		FragmentBuilder fAligned = new FragmentBuilder();
		FragmentBuilder gAligned = new FragmentBuilder();		
		
		MatrixIndices start = new MatrixIndices(n-1, getMaxLastLine());
		
		for (int i=0; i<m-1-start.column; i++) 
			gAligned.addFirst(g.byteAt(m-2-i));
		fAligned.addEndGaps(m-1-start.column);
		
		if (n-1-start.line>0)
			throw new IllegalArgumentException("ICIIII" + Integer.toString(m-1-start.column)+ " ; m = "+m+" "+start.column+" ; n = "+n+" "+start.line);
		
		/*
		for (int i=0; i<n-1-start.line; i++) 
			fAligned.addFirst(f.byteAt(n-2-i));
		gAligned.addEndGaps(n-1-start.line);
		*/
		
		MatrixIndices end = buildAlignment(start, fAligned, gAligned);

		for (int i=end.line; i>0; i--) 
			fAligned.addFirst(f.byteAt(i-1));
		gAligned.addStartGaps(end.line);
		
		if (end.column>0)
			throw new IllegalArgumentException("ICIIII");
		
		/*
		for (int i=end.line; i>0; i--) 
			fAligned.addFirst(f.byteAt(i-1));
		fAligned.addStartGaps(end.line);
		*/
		
		return new AlignedFragments(fAligned, gAligned);
	}
	
	/**
	 * Used to build the alignment from the last byte to the first without storing the alignment itself
	 * @param startCase 
	 * @return
	 */
	private MatrixIndices buildAlignment(MatrixIndices startCase) {
		MatrixIndices nextCase = new MatrixIndices(startCase.line, startCase.column);

		while (nextCase.line>=1 && nextCase.column>=1) 
			nextCase = getNextCase(nextCase.line, nextCase.column);
		
		return nextCase;
	}
	
	/**
	 * Used to build the alignment from the last byte to the first and storing the alignment in fAligned and gAligned
	 * @param startCase
	 * @param fAligned
	 * @param gAligned
	 * @return
	 */
	private MatrixIndices buildAlignment(MatrixIndices startCase, FragmentBuilder fAligned, FragmentBuilder gAligned) {
		MatrixIndices nextCase = new MatrixIndices(startCase.line, startCase.column);
		int x, y;
		
		while (nextCase.line>=1 && nextCase.column>=1) {
			x = nextCase.line;
			y = nextCase.column;

			addNextByte(x, y, fAligned, gAligned);
			nextCase = getNextCase(nextCase.line, nextCase.column);
		}
		
		return nextCase;
	}
	
	private void addNextByte(int line, int column, FragmentBuilder fBuilder, FragmentBuilder gBuilder) {
		if (matchDiag(line, column)) {  
			fBuilder.addFirst(f.byteAt(line-1));
			gBuilder.addFirst(g.byteAt(column-1));
		}
		else if (matchUp(line, column)) {
			fBuilder.addFirst(f.byteAt(line-1));
			gBuilder.addFirst((byte)0);
		}
		else {
			fBuilder.addFirst((byte)0);
			gBuilder.addFirst(g.byteAt(column-1));
		}
	}
	
	/**
	 * Determines if the score in a specified case of the matrix comes from the above, the left or the top-left case and return this case.
	 * @param x the line of the matrix case
	 * @param y the column of the matrix case
	 * @return the matrix case from which comes the one given as input
	 */
	private MatrixIndices getNextCase(int x, int y) {
		int i = x;
		int j = y;
		
		if (matchDiag(i, j)) {
			i = i-1;
			j = j-1;
		}
		else if (matchUp(i, j)) 
			i = i-1;
		else //if (matchLeft(i, j))
			j = j-1;
	
		return new MatrixIndices(i, j);
	}
	
	private boolean matchUp(int i, int j) {
		int score = alignmentMatrix[i][j];
		int scoreUp = alignmentMatrix[i-1][j];
		return score == scoreUp + GAP_PENALTY;
	}
	
	private boolean matchDiag(int i, int j) {
		int score = alignmentMatrix[i][j];
		int scoreDiag = alignmentMatrix[i-1][j-1];
		return score == scoreDiag + matchValue(i, j);
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
