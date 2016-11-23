package org.phantomapi.ppa;

import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.util.Fingerprint;
import org.phantomapi.util.M;

/**
 * Represents a PPA which can be sent to other servers without the need of
 * bungeecord, or online players by using redis.
 * 
 * @author cyberpwn
 */
public class PPA extends DataCluster
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a PPA packet
	 * 
	 * @param type
	 *            the type
	 * @param destination
	 *            the destination server
	 */
	public PPA(String type, String destination)
	{
		super();
		
		set("ppak", type);
		set("ppad", destination);
		set("ppas", Phantom.getPPAID());
		set("ppaf", Fingerprint.randomFingerprint("ph1"));
		set("ppat", M.ms());
	}
	
	/**
	 * Create a PPA packet which has a destination to all servers
	 * 
	 * @param type
	 *            the type
	 */
	public PPA(String type)
	{
		this(type, "all");
	}
	
	/**
	 * Send this packet
	 */
	public void send()
	{
		Phantom.instance().getPpaController().send(this);
	}
	
	/**
	 * Create a response packet using this packet's source and type. You would
	 * use this when you receive a packet and want to respond to it
	 * 
	 * @return the response packet
	 */
	public PPA createResponse()
	{
		return new PPA(getType() + "-response", getSource());
	}
	
	/**
	 * Get the destination of this packet
	 * 
	 * @return the destination server
	 */
	public String getDestination()
	{
		return getString("ppad");
	}
	
	/**
	 * Get the source of this packet
	 * 
	 * @return the source server
	 */
	public String getSource()
	{
		return getString("ppas");
	}
	
	/**
	 * Get the fingerprint of this packet
	 * 
	 * @return the fingerprint
	 */
	public String getFingerprint()
	{
		return getString("ppaf");
	}
	
	/**
	 * Get the type of this packet
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return getString("ppak");
	}
	
	/**
	 * Get the time this packet was created
	 * 
	 * @return the time in milliseconds
	 */
	public long getTime()
	{
		return getLong("ppat");
	}
}
