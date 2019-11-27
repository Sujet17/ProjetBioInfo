package main;

import java.util.ArrayList;

public class FragmentBuilder extends ArrayList<Byte> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FragmentBuilder() {
		super();
	}
	
	public FragmentBuilder(int size) {
		super(size);
	}

	public FragmentBuilder getReverse() {
		FragmentBuilder builder = new FragmentBuilder(size());
		int size = size();
		for (int i=0; i<size; i++) 
			builder.add(get(size-1-i));
		return builder;
	}
	
	@Override
	public String toString() {
		return new Fragment(this).toString();
	}
	
}
