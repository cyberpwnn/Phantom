package org.phantomapi.ppa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;

public class PPA extends DataCluster
{
	private static final long serialVersionUID = 1L;
	
	public PPA(String destination)
	{
		super();
		
		set("ppad", destination);
		set("ppas", Phantom.getPPAID());
	}
	
	public PPA()
	{
		this("all");
	}
	
	public String getPPAData()
	{
		try
		{
			return new String(compress(), StandardCharsets.UTF_8);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
