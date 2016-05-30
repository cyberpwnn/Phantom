package org.cyberpwn.phantom.test;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;

public class TestControllerA extends Controller
{
	private TestControllerB tb;
	
	public TestControllerA(Controllable parentController)
	{
		super(parentController);
		
		tb = new TestControllerB(this);
		
		register(tb);
	}
}
