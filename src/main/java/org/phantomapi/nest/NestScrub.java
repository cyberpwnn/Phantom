package org.phantomapi.nest;

/**
 * Scrubs stuff
 * 
 * @author cyberpwn
 */
public interface NestScrub
{
	/**
	 * Scans the given block
	 * 
	 * @param b
	 *            the block
	 */
	public void onScan(NestedBlock b);
	
	/**
	 * Scans the given chunk
	 * 
	 * @param c
	 *            the chunk
	 */
	public void onScan(NestedChunk c);
}
