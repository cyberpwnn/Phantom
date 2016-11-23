package org.phantomapi.ppa;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.phantomapi.Phantom;
import org.phantomapi.event.PPAReceiveEvent;

/**
 * Represents a ppa endpoint which can receive packets and send back responses
 * (and receive those responses). Essentially, you should implement this on
 * both servers communicating to handle responses.
 * 
 * @author cyberpwn
 */
public abstract class PPAEndPoint implements Listener
{
	private String type;
	
	/**
	 * Create an endpoint listening on a type. This will also listen on your
	 * type + "-response" for .createResponse() packets.
	 * 
	 * @param type
	 *            the type of the packet to listen on
	 */
	public PPAEndPoint(String type)
	{
		this.type = type;
		
		Phantom.instance().registerListener(this);
	}
	
	/**
	 * Close this end point
	 */
	public void close()
	{
		Phantom.instance().unRegisterListener(this);
	}
	
	@EventHandler
	public void on(PPAReceiveEvent e)
	{
		if(e.getPpa().getType().equals(type))
		{
			onReceived(e.getPpa()).send();
		}
		
		if(e.getPpa().getType().equals(type + "-response"))
		{
			onResponded(e.getPpa());
		}
	}
	
	/**
	 * Called when the end point receives a packet with your type expecting you
	 * to send back a response using .createResponse().
	 * 
	 * @param packet
	 *            the packet received
	 * @return the packet to respond back to the sender
	 */
	public abstract PPA onReceived(PPA packet);
	
	/**
	 * Called when the end point has received a response back from the packet
	 * this server previously sent. If you implemented your endpoint on the
	 * responding server, you should expect to receive your response packet here
	 * 
	 * @param packet
	 *            the response packet
	 */
	public abstract void onResponded(PPA packet);
}
