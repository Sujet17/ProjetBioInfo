package main;


public class Arc implements Comparable<Arc> {
	
	/**
	 * The index of the source node of this arc
	 */
	private int source;
	
	/**
	 * The index of the destination node of this arc
	 */
	private int dest;
	
	/**
	 * True if this arc starts from the complementary-reversed fragment of the source node
	 */
	private boolean complSource;
	
	/**
	 * True if this arc comes to the complementary-reversed fragment of the destination node
	 */
	private boolean complDest;
	
	/**
	 * The weight of the arc
	 */
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
	
	/**
	 * Used to class the arcs by decreasing weight
	 */
	public int compareTo(Arc other) {
		int tieBreakCriter = 0;
		if (complSource)
			tieBreakCriter--;
		return (other.weight - weight)*1000+tieBreakCriter;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof Arc) {
			Arc other = (Arc) o;
			return source == other.source 
					&& dest == other.dest
					&& complSource == other.complSource 
					&& complDest == other.complDest
					&& weight == other.weight;
		}
		return false;
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
