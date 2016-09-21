package org.phantomapi.wraith;

public class WraithHandle implements WraithHandler
{
	private Wraith wraith;
	
	public WraithHandle(Wraith wraith)
	{
		this.wraith = wraith;
		
		((WraithHandled) wraith).registerHandler(this);
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
}
