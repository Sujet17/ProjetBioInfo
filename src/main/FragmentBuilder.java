package main;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is used to represent fragments in construction (as a Fragment object is a byte array, it is not practical to insert new byte).
 * So the data structure used here to store the bytes of the word is a LinkedList, which ensure easy inserting.
 * As in the gap propagation step the aligned fragments contain a important number of gaps at the beginning and at the end, all this startGaps
 * and endGaps are stored by two integers (see startGaps and endGaps attributes) instead of creating a lot of new LinkedList nodes.
 */
public class FragmentBuilder{
	
	/**
	 * The number of gaps at the beginning of the word
	 */
	private int startGaps;
	
	/**
	 * The number of gaps at the end of the word
	 */
	private int endGaps;	
	
	/**
	 * The linkedList that contains the bytes of the word which are between the startGaps and the endGaps
	 */
	private LinkedList<Byte> innerList;
	
	/**
	 * Create an empty word
	 */
	public FragmentBuilder() {
		innerList = new LinkedList<Byte>();
	}
	
	/**
	 * 
	 * @param index
	 * @return the byte at the specified index
	 */
	public byte byteAt(int index) {
		if (index >= startGaps && index < size())
			return innerList.get(index-startGaps);
		return 0;
	}
	
	/**
	 * Add the specified byte at the start of the inner word (in other words just after the startGaps)
	 * @param b 
	 */
	public void addFirst(byte b) {
		innerList.addFirst(b);
	}
	
	/**
	 * Add the specified byte at the end of the inner word (in other words just before the endGaps)
	 * @param b 
	 */
	public void addLast(byte b) {
		innerList.addLast(b);
	}
	
	public void insert(int index, byte b) {
		if (index == startGaps)
			addFirst(b);
		else if (index == size()-1)
			addLast(b);
		else if (index > startGaps && index < size()-1)
			innerList.add(index-startGaps, b);
		else if (index < startGaps && b == 0)
			startGaps++;
		else if (index >= size() && b == 0)
			endGaps++;
		else
			throw new IllegalArgumentException("index must be comprised between startGaps and size()-endGaps");
	}
	
	/**
	 * 
	 * @return the size of the word without the endGaps
	 */
	public int size() {
		return startGaps+innerList.size();
	}
	
	/**
	 * the total size is the number of startGaps + the size of the innerWord(innerList) + the number of endGaps
	 * @return the total size of the word
	 */
	public int totalSize() {
		return startGaps+innerList.size()+endGaps;
	}
	
	/**
	 * 
	 * @return a reference to the innerList attribute
	 */
	public LinkedList<Byte> innerList() {
		return innerList;
	}
	
	/**
	 * 
	 * @return the number of gaps at the beginning of the word
	 */
	public int getStartGaps() {
		return startGaps;
	}
	
	/**
	 * Add the specified number of gaps at the beginning of the word
	 * @param number
	 */
	public void addStartGaps(int number) {
		startGaps += number;
	}
	
	/**
	 * Add the specified number of gaps at the end of the word
	 * @param number
	 */
	public void addEndGaps(int number) {
		endGaps += number;
	}
	
	/**
	 * 
	 * @return the number of gaps at the end of the word
 	 */
	public int getEndGaps() {
		return endGaps;
	}
	
	/**
	 * 
	 * @return an iterator over the innerList attribute
	 */
	public Iterator<Byte> innerIterator() {
		return innerList.iterator();
	}
	
	@Override
	public String toString() {
		return new Fragment(this).toString();
	}
	
}
