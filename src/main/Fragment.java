package main;

import java.util.Iterator;

public class Fragment {
	
	/**
	 * The byte array that contains the bytes characterizing this fragment
	 */
	private byte[] list;
	
	
	public Fragment(String s) {
		list  = new byte[s.length()];
		for (int i=0; i<list.length; i++)
			list[i] = Fragment.byteFromChar(s.charAt(i));
	}
	
	public Fragment(byte[] list) {
		this.list = list;
	}
	
	public Fragment(FragmentBuilder builder) {
		list  = new byte[builder.totalSize()];
		Iterator<Byte> iterator = builder.innerIterator();
		for (int i=builder.getStartGaps(); i<builder.size(); i++)
			list[i] = (byte)iterator.next();
	}
	
	/**
	 * Convert char into byte
	 * @param c the char to convert
	 * @return byte from 0 to 4, corresponding to the giver char
	 */
	public static byte byteFromChar(char c) {
		switch(c) {
		case '-':
			return 0;
		case 'a':
			return 1;
		case 't':
			return -1;
		case 'c':
			return 2;
		case 'g':
			return -2;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Convert byte into char
	 * @param b the byte to convert
	 * @return 'a' if byte is 1, 'c' if byte is 2, 't' if byte is -1, 'g' if byte is -2, '-' if byte is 0
	 */
	public static char charFromByte(byte b) {
		switch(b) {
		case 0:
			return '-';
		case 1:
			return 'a';
		case -1:
			return 't';
		case 2:
			return 'c';
		case -2:
			return 'g';
		default:
			throw new IllegalArgumentException(Byte.toString(b));
		}
	}
	
	/**
	 * 
	 * @return the complementary-reversed fragment
	 */
	public Fragment getComplementary() {
		int length = list.length;
		byte[] compl = new byte[length];
		for (int i=0; i<length; i++) 
			compl[length-i-1] = Fragment.complementaryByte(list[i]);
		return new Fragment(compl);
	}
	
	/**
	 * @param b a byte 
	 * @return the complementary byte of b
	 */
	public static byte complementaryByte(byte b) {
		return (byte)-b;
	}
	
	public void set(int index, byte b) {
		list[index] = b;
	}
	
	public int size() {
		return list.length;
	}
	
	public byte byteAt(int index) {
		return list[index];
	}
	
	public char charAt(int index) {
		return Fragment.charFromByte(byteAt(index));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof Fragment) {
			Fragment other = (Fragment) o;
			return toString().equals(other.toString());
		}
		return false;
	}
	
	@Override
	public String toString() { 
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<list.length; i++)
			builder.append(charAt(i));
		return builder.toString();
	}
	
}
