package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author cyberpwn
 *
 */
public class DataInput implements DataInputtable
{
	@Override
	public void load(DataCluster cluster, File file) throws IOException
	{
		if(file.exists())
		{
			if(file.isDirectory())
			{
				throw new IOException("Target Directory (" + file.getPath() + ") is not a file!");
			}
		}
		
		else
		{
			file.createNewFile();
		}
	}
}
