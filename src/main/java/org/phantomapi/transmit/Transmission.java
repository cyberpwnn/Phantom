package org.phantomapi.transmit;

import java.io.IOException;
import java.util.UUID;
import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.util.M;

/**
 * A Transmission packet
 * 
 * @author cyberpwn
 */
public abstract class Transmission extends DataCluster
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		
		if(contains("t.r"))
		{
			setPayload(getString("t.r"));
		}
		
		else
		{
			setPayload(UUID.randomUUID().toString());
		}
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
	
	public abstract void onResponse(Transmission response);
	
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
		
		if(contains("t.r"))
		{
			setPayload(getString("t.r"));
		}
		
		else
		{
			setPayload(UUID.randomUUID().toString());
		}
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
		
		if(contains("t.r"))
		{
			setPayload(getString("t.r"));
		}
		
		else
		{
			setPayload(UUID.randomUUID().toString());
		}
	}
	
	/**
	 * Clone the transmission
	 */
	public Transmission clone()
	{
		Transmission th = this;
		Transmission t = new Transmission(getType())
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onResponse(Transmission t)
			{
				th.onResponse(t);
			}
		};
		
		t.setData(getData());
		
		if(t.contains("t.r"))
		{
			t.setPayload(getString("t.r"));
		}
		
		else
		{
			t.setPayload(UUID.randomUUID().toString());
		}
		
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
		Phantom.instance().getBungeeController().transmit(this);
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
	
	/**
	 * Get the timestamp of the packet when it was sent
	 * 
	 * @return the timestamp from when it was sent
	 */
	public Long getTimeStamp()
	{
		return getLong("t.m");
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Transmission)
		{
			Transmission t = (Transmission) o;
			
			if(t.getData().equals(getData()))
			{
				return true;
			}
		}
		
		return false;
	}
}
