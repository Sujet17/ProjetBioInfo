package main;

/**
 * A class who stores two fragmentBuilders. 
 * It is used to match with an arc f->g. (with f that doesn't start with gaps but ends with and the opposite for g).
 * But there is no checking to ensure that the stored fragmentBuilders match to an arc.
 */
public class AlignedFragments {
	
	public FragmentBuilder f;
	public FragmentBuilder g;
	
	AlignedFragments(FragmentBuilder f, FragmentBuilder g) {
		this.f = f;
		this.g = g;
	}
	
}
