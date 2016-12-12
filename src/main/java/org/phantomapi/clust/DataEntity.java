package org.phantomapi.clust;

import java.io.IOException;

/**
 * Represents a data entity
 * 
 * @author cyberpwn
 */
public interface DataEntity
{
	/**
	 * Get data representing this object
	 * 
	 * @return the bytes of data
	 * @throws IOException
	 *             shit happens
	 */
	public byte[] toData() throws IOException;
	
	/**
	 * Read all data and load it into this object's state
	 * 
	 * @param data
	 *            the bytes of data
	 * @throws IOException
	 *             shit happens
	 */
	public void fromData(byte[] data) throws IOException;
}
