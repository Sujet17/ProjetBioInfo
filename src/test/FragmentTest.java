package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Fragment;

public class FragmentTest {

	@Test
	public void testByteFromChar() {
		assertTrue(Fragment.byteFromChar('a') == 1);
		assertTrue(Fragment.byteFromChar('c') == 2);
		assertTrue(Fragment.byteFromChar('t') == -1);
		assertTrue(Fragment.byteFromChar('g') == -2);
		assertTrue(Fragment.byteFromChar('-') == 0);	
	}

	@Test
	public void testCharFromByte() {
		assertTrue(Fragment.charFromByte((byte)1) == 'a');	
		assertTrue(Fragment.charFromByte((byte)2) == 'c');	
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
