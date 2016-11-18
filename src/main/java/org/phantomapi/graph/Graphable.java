package org.phantomapi.graph;

/**
 * Represents a graphable object
 * 
 * @author cyberpwn
 */
public interface Graphable
{
	/**
	 * Graph the object onto a graph
	 * 
	 * @param holder
	 *            the graph holder
	 */
	public void renderGraph(GraphHolder holder);
}
