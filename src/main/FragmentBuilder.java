package main;

import java.util.LinkedList;

public class FragmentBuilder extends LinkedList<Byte> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int realSize;
	
	public int getRealSize() {
		if (size() > realSize)
			return size();
		return realSize;
	}
	
	public void setRealSize(int x) {
		realSize = x;
	}
	
	@Override
	public String toString() {
		return new Fragment(this).toString();
	}
	
}
