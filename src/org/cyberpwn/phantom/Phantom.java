package org.cyberpwn.phantom;

import org.cyberpwn.phantom.construct.PhantomPlugin;
import org.cyberpwn.phantom.test.TestControllerA;

public class Phantom extends PhantomPlugin
{
	private TestControllerA ta;
	
	public void enable()
	{
		ta = new TestControllerA(this);
		
		register(ta);
	}
	
	public void disable()
	{
		
	}
}
