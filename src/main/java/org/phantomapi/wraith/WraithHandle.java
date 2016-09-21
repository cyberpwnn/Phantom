package org.phantomapi.wraith;

import org.phantomapi.Phantom;

/**
 * Wraith handler
 * 
 * @author cyberpwn
 */
public class WraithHandle implements WraithHandler
{
	private Wraith wraith;
	
	/**
	 * Create a handler for a wraith
	 * 
	 * @param wraith
	 *            the wraith to handle
	 */
	public WraithHandle(Wraith wraith)
	{
		this.wraith = wraith;
		
		((WraithHandled) wraith).registerHandler(this);
		Phantom.instance().registerListener(this);
	}
	
	@Override
	public Wraith getWraith()
	{
		return wraith;
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString()
	{
		return wraith.getName() + "<" + getName() + ">";
	}
	
	@Override
	public void unregister()
	{
		Phantom.instance().unRegisterListener(this);
	}
}
