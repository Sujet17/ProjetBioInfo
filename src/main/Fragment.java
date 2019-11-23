package main;

import java.util.LinkedList;

public class Fragment {
	
	private int length;
	private byte[] list;
	
	public Fragment(String s) {
		length = s.length();
		list  = new byte[length];
		for (int i=0; i<s.length(); i++)
			list[i] = Fragment.byteFromChar(s.charAt(i));
	}
	
	public Fragment(byte[] list, int length) {
		this.length = length;
		this.list = list;
	}
	
	public Fragment(LinkedList<Byte> invertedList) {
		length = invertedList.size();
		list = new byte[length];		
		for (int i=0; i<length; i++) 
			list[i] = invertedList.pollLast();
	}
	
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
	
	public Fragment getComplementary() {
		byte[] compl = new byte[length];
		for (int i=0; i<length; i++) {
			compl[length-i-1] = Fragment.complementaryByte(list[i]);
		}
		return new Fragment(compl, length);
	}
	
	public static byte complementaryByte(byte b) {
		if (b >= 0 && b < 4)
			return (byte) ((b+2)%4);
		else
			throw new IllegalArgumentException();
	}
	
	public int size() {
		return length;
	}
	
	public byte byteAt(int index) {
		return list[index];
	}
	
	public char charAt(int index) {
		return Fragment.charFromByte(byteAt(index));
	}
	
	@Override
	public String toString() { 
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<length; i++)
			builder.append(charAt(i));
		return builder.toString();
	}
	
}
