package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author cyberpwn
 *
 */
public class YAMLDataOutput extends DataOutput
{
	@Override
	public void save(DataCluster cluster, File file) throws IOException
	{
		super.save(cluster, file);
		
		PrintWriter pw = new PrintWriter(new FileWriter(file, false));
		
		for(String i : cluster.toLines(true))
		{
			pw.write(i + "\n");
		}
		
		pw.close();
	}
}
