package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author cyberpwn
 *
 */
public interface DataInputtable
{
	public void load(DataCluster cluster, File file) throws IOException;
}
