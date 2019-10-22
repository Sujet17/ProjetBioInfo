package main;

public class Fragment {
	
	private String s;
	
	public Fragment(String s) {
		this.s = s;
	}
	
	public Fragment getComplementary() {
		StringBuilder compl = new StringBuilder();
		for (int i=0; i<s.length(); i++) 
			compl.append(getComplementaryChar(s.charAt(i)));
		compl.reverse();
		return new Fragment(compl.toString());
	}
	
	private char getComplementaryChar(char x) {
		switch(x) {
		case 'a':
			return 't';
		case 't':
			return 'a';
		case 'c':
			return 'g';
		case 'g':
			return 'c';
		default:
			return 'K';
		}
	}
	
	
	@Override
	public String toString() { 
		return s;
	}
	
}
