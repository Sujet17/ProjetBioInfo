package main;

public class Arc implements Comparable<Arc> {
	
	/**
	 * The index of the source node of this edge
	 */
	private int source;
	
	/**
	 * The index of the destination node of this edge
	 */
	private int dest;
	
	/**
	 * True if this edge starts from the complementary-inverted fragment of the source node
	 */
	private boolean complSource;
	
	/**
	 * True if this edge comes to the complementary-inverted fragment of the destination node
	 */
	private boolean complDest;
	
	private int weight;
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param complementarySource
	 * @param complementaryDestination
	 * @param weight
	 */
	public Arc(int source, int destination, boolean complementarySource, boolean complementaryDestination, int weight) {
		this.source = source;
		dest = destination;
		complSource = complementarySource;
		complDest = complementaryDestination;
		this.weight = weight;
	}
	
	public int getSource() {
		return source;
	}
	
	public int getDestination() {
		return dest;
	}
	
	public boolean isComplSource() {
		return complSource;
	}
	
	public boolean isComplDest() {
		return complDest;
	}
	
	public int getWeight() {
		return weight;
	}

	public int compareTo(Arc other) {
		return other.weight - weight;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(source);
		if (complSource)
			builder.append("'");
		else
			builder.append(" ");
		builder.append(" ---");
		builder.append(weight);
		builder.append("---> ");
		builder.append(dest);
		if (complDest)
			builder.append("'");
		else
			builder.append(" ");
		return builder.toString();
	}
}
