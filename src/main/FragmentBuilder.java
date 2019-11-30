package main;

import java.util.LinkedList;

public class FragmentBuilder extends LinkedList<Byte> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return new Fragment(this).toString();
	}
	
}
