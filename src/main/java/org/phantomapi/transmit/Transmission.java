package org.phantomapi.transmit;

import java.io.IOException;
import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.util.M;

/**
 * A Transmission packet
 * 
 * @author cyberpwn
 */
public class Transmission extends DataCluster
{
	/**
	 * Create a transmission packet
	 * 
	 * @param type
	 *            the type (id)
	 * @param destination
	 *            the destination
	 */
	public Transmission(String type, String destination)
	{
		set("t.t", type);
		set("t.d", destination);
		set("t.s", Phantom.getServerName());
		set("t.m", M.ms());
	}
	
	public void setPayload(String s)
	{
		set("t.r", s);
	}
	
	public String getPayload()
	{
		return getString("t.r");
	}
	
	public boolean hasPayload()
	{
		return getPayload() != null;
	}
	
	public void onResponse(Transmission response)
	{
		
	}
	
	/**
	 * Create a packet wrapper from recieved data
	 * 
	 * @param data
	 *            the data
	 * @throws IOException
	 *             shit happens
	 */
	public Transmission(byte[] data) throws IOException
	{
		super(data);
	}
	
	/**
	 * Create a transmission packet which the destination is set to all servers
	 * 
	 * @param type
	 *            the type (id)
	 */
	public Transmission(String type)
	{
		this(type, "ALL");
	}
	
	/**
	 * Clone the transmission
	 */
	public Transmission clone()
	{
		Transmission t = new Transmission(getType());
		t.setData(getData());
		
		return t;
	}
	
	/**
	 * Transmit the packet
	 * 
	 * @throws IOException
	 *             something fucked up
	 */
	public void transmit() throws IOException
	{
		Phantom.instance().getBungeeController().transmit(clone());
	}
	
	/**
	 * Get the type of this packet (id)
	 * 
	 * @return the type of the packet (id)
	 */
	public String getType()
	{
		return getString("t.t");
	}
	
	/**
	 * Get the destination of the packet (where it needs to go or has gone)
	 * 
	 * @return the server destination or "ALL"
	 */
	public String getDestination()
	{
		return getString("t.d");
	}
	
	/**
	 * Get the source of the packet (where it came from)
	 * 
	 * @return the source of the packet
	 */
	public String getSource()
	{
		return getString("t.s");
	}
	
	public Long getTimeStamp()
	{
		return getLong("t.m");
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Transmission)
		{
			Transmission t = (Transmission)o;
			
			if(t.getData().equals(getData()))
			{
				return true;
			}
		}
		
		return false;
	}
}
