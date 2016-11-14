package org.phantomapi.clust;

/**
 * A linked data cluster is cropped from a data cluster and can flush
 * information back to the parent data cluster
 * 
 * @author cyberpwn
 */
public class LinkedDataCluster extends DataCluster
{
	private static final long serialVersionUID = 1L;
	
	private DataCluster parent;
	private String sect;
	
	/**
	 * Create a linked data cluster
	 * 
	 * @param parent
	 *            the parent
	 * @param sect
	 *            the section
	 */
	public LinkedDataCluster(DataCluster parent, String sect)
	{
		this.parent = parent;
		this.sect = sect;
	}
	
	/**
	 * Flush written data back into the data cluster. New keys will be created
	 * with the sect in front
	 */
	public void flushParent()
	{
		for(String i : keys())
		{
			parent.getData().put(sect + "." + i, getData().get(i));
		}
	}
}
