package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Fragment;

public class FragmentTest {

	@Test
	public void testByteFromChar() {
		assertTrue(Fragment.byteFromChar('a') == 0);
		assertTrue(Fragment.byteFromChar('c') == 1);
		assertTrue(Fragment.byteFromChar('t') == 2);
		assertTrue(Fragment.byteFromChar('g') == 3);
		assertTrue(Fragment.byteFromChar('-') == 4);		
	}

	@Test
	public void testCharFromByte() {
		assertTrue(Fragment.charFromByte((byte)1) == 'c');	
		assertTrue(Fragment.charFromByte((byte)2) == 't');	
	}

	@Test
	public void testGetComplementary() {
		Fragment f1 = new Fragment("aaaa");
		assertTrue(f1.getComplementary().toString().equals("tttt"));
		
		Fragment f2 = new Fragment("acgtactggct");
		assertTrue(f2.getComplementary().toString().equals("agccagtacgt"));
	}

	@Test
	public void testSize() {
		assertTrue(new Fragment("actgactgactgactgactgactgactg").size() == 28);
	}

}
