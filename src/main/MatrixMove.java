package main;

public class MatrixMove {
	
	public static enum MoveType {
			UP,
			DIAG,
			LEFT;
	}
	
	private int line;
	private int column;
	
	private MoveType move;
	
	public MatrixMove(int line, int column) {
		this.line = line;
		this.column = column;
		move = null;
	}
	
	public MatrixMove(int line, int column, MoveType move) {
		this(line, column);
		this.move = move;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	public MoveType getMove() {
		return move;
	}
	
}
