package main;

import java.util.ListIterator;

public class Fragment {
	
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
		list  = new byte[builder.size()];
		ListIterator<Byte> iterator = builder.listIterator();
		for (int i=0; i<list.length; i++)
			list[i] = (byte)iterator.next();
	}
	
	/**
	 * Convert char into byte
	 * 
	 * @param c, char to convert
	 * @return byte from 0 to 4, corresponding to the giver char
	 * 	 */
	public static byte byteFromChar(char c) {
		switch(c) {
		case 'a':
			return 0;
		case 'c':
			return 1;
		case 't':
			return 2;
		case 'g':
			return 3;
		case '-':
			return 4;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Convert byte into char
	 * @param b the byte to convert
	 * @return 'a' if byte is 0, 'c' if byte is 1, 't' if byte is 2, 'g' if byte is 3, '-' if byte is 4
	 */
	public static char charFromByte(byte b) {
		switch(b) {
		case 0:
			return 'a';
		case 1:
			return 'c';
		case 2:
			return 't';
		case 3:
			return 'g';
		case 4:
			return '-';
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * @return the complementary-reversed fragment
	 */
	public Fragment getComplementary() {
		int length = list.length;
		byte[] compl = new byte[length];
		for (int i=0; i<length; i++) {
			compl[length-i-1] = Fragment.complementaryByte(list[i]);
		}
		return new Fragment(compl);
	}
	
	/**
	 * 
	 * @param b a byte 
	 * @return complementary of b
	 */
	public static byte complementaryByte(byte b) {
		if (b >= 0 && b < 4)
			return (byte) ((b+2)%4);
		else
			throw new IllegalArgumentException();
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
