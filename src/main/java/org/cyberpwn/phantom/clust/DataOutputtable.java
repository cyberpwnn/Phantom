package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

public interface DataOutputtable
{
	public void save(DataCluster cluster, File file) throws IOException;
}
