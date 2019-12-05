package main;

import java.util.Iterator;
import java.util.LinkedList;

public class FragmentBuilder{
	
	private int startGaps;
	private int endGaps;	
	
	private LinkedList<Byte> byteList;
	
	public FragmentBuilder() {
		byteList = new LinkedList<Byte>();
	}
	
	public byte byteAt(int index) {
		if (index >= startGaps && index < size())
			return byteList.get(index-startGaps);
		return 0;
	}
	
	public void addFirst(byte b) {
		byteList.addFirst(b);
	}
	
	public void addLast(byte b) {
		byteList.addLast(b);
	}
	
	public void insert(int index, byte b) {
		if (index == startGaps)
			addFirst(b);
		else if (index == size()-1)
			addLast(b);
		else if (index > startGaps && index < size()-1)
			byteList.add(index-startGaps, b);
		else if (index < startGaps && b == 0)
			startGaps++;
		else if (index >= size() && b == 0)
			endGaps++;
		else
			throw new IllegalArgumentException("index must be comprised between startGaps and size()-endGaps");
	}
	
	public int size() {
		return startGaps+byteList.size();
	}
	
	public int totalSize() {
		return startGaps+byteList.size()+endGaps;
	}
	
	public LinkedList<Byte> innerList() {
		return byteList;
	}
	
	public int getStartGaps() {
		return startGaps;
	}
	
	public void addStartGaps(int number) {
		startGaps += number;
	}
	
	public void addEndGaps(int number) {
		endGaps += number;
	}
	
	public int getEndGaps() {
		return endGaps;
	}
	
	public Iterator<Byte> innerIterator() {
		return byteList.iterator();
	}
	
	@Override
	public String toString() {
		return new Fragment(this).toString();
	}
	
}
