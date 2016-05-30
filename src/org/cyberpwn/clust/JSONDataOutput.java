package org.cyberpwn.clust;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class JSONDataOutput extends DataOutput
{
	@Override
	public void save(DataCluster cluster, File file) throws IOException
	{
		super.save(cluster, file);
		
		JSONObject js = new JSONObject();
		
		for(String i : cluster.getData().keySet())
		{
			js.put(i, cluster.getAbstract(i));
		}
		
		PrintWriter pw = new PrintWriter(file);
		pw.write(js.toString(2));
		pw.close();
	}
}
