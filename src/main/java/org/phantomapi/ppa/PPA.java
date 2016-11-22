package org.phantomapi.ppa;

import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.util.Fingerprint;
import org.phantomapi.util.M;

public class PPA extends DataCluster
{
	private static final long serialVersionUID = 1L;
	
	public PPA(String destination)
	{
		super();
		
		set("ppad", destination);
		set("ppas", Phantom.getPPAID());
		set("ppaf", Fingerprint.randomFingerprint("ph1"));
		set("ppat", M.ms());
	}
	
	public PPA()
	{
		this("all");
	}
	
	public String getDestination()
	{
		return getString("ppad");
	}
	
	public String getSource()
	{
		return getString("ppas");
	}
	
	public String getFingerprint()
	{
		return getString("ppaf");
	}
	
	public long getTime()
	{
		return getLong("ppat");
	}
}
