package org.phantomapi.wraith;

import org.phantomapi.Phantom;

public class WraithHandle implements WraithHandler
{
	private Wraith wraith;
	
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
