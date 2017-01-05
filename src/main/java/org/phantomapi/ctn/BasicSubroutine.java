package org.phantomapi.ctn;

import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;

/**
 * Represents a basic subroutine
 * 
 * @author cyberpwn
 */
public abstract class BasicSubroutine implements Subroutine
{
	private String sname;
	private String sid;
	
	/**
	 * Create a subroutine
	 * 
	 * @param sname
	 *            the name
	 * @param sid
	 *            the id
	 */
	public BasicSubroutine(String sname, String sid)
	{
		this.sname = sname;
		this.sid = sid;
	}
	
	@Override
	public abstract DataCluster handleSubroutine(DataCluster data);
	
	@Override
	public String getSubroutineIdentifier()
	{
		return sid;
	}
	
	@Override
	public String getSubroutineName()
	{
		return sname;
	}
	
	@Override
	public void registerSubroutine()
	{
		Phantom.instance().getCtnController().registerSubroutine(this);
	}
	
	@Override
	public void unregisterSubroutine()
	{
		Phantom.instance().getCtnController().unregisterSubroutine(this);
	}
	
	@Override
	public boolean isSubroutineRegistered()
	{
		return Phantom.instance().getCtnController().isSubroutineRegistered(this);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((sid == null) ? 0 : sid.hashCode());
		result = prime * result + ((sname == null) ? 0 : sname.hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		BasicSubroutine other = (BasicSubroutine) obj;
		
		if(sid == null)
		{
			if(other.sid != null)
			{
				return false;
			}
		}
		
		else if(!sid.equals(other.sid))
		{
			return false;
		}
		
		if(sname == null)
		{
			if(other.sname != null)
			{
				return false;
			}
		}
		
		else if(!sname.equals(other.sname))
		{
			return false;
		}
		
		return true;
	}
}
