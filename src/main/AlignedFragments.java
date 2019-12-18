package main;

/**
 * A class which stores two fragmentBuilders. 
 * It is used to match with an arc f->g. (f doesn't start with gaps but ends with it but it's the opposite for g).
 * There is no checking to ensure that the stored fragmentBuilders match an arc.
 */
public class AlignedFragments {
	
	public FragmentBuilder f;
	public FragmentBuilder g;
	
	AlignedFragments(FragmentBuilder f, FragmentBuilder g) {
		this.f = f;
		this.g = g;
	}
	
}
