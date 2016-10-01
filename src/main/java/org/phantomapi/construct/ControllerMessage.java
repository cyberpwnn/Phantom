package org.phantomapi.construct;

import org.phantomapi.clust.DataCluster;

/**
 * Represents a controller message
 * 
 * @author cyberpwn
 */
public class ControllerMessage extends DataCluster
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a controller message
	 * 
	 * @param controller
	 *            the sending controller
	 */
	public ControllerMessage(Controllable controller)
	{
		super();
		
		set("phantom.source.controller", controller.getName());
	}
	
	/**
	 * Create a controller message (clone)
	 * 
	 * @param message
	 *            the message (clone)
	 */
	public ControllerMessage(ControllerMessage message)
	{
		super(message.getData());
	}
	
	/**
	 * Get the source controller
	 * 
	 * @return the source controller
	 */
	public String getSource()
	{
		return getString("phantom.source.controller");
	}
	
	/**
	 * Set the source controller
	 * 
	 * @param controller
	 *            the source controller
	 */
	public void setSource(Controllable controller)
	{
		set("phantom.source.controller", controller.getName());
	}
	
	/**
	 * Clones the message
	 */
	public ControllerMessage copy()
	{
		return new ControllerMessage(this);
	}
}
