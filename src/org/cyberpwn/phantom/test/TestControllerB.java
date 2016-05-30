package org.cyberpwn.phantom.test;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;

public class TestControllerB extends Controller
{
	private TestControllerC tc;
	
	public TestControllerB(Controllable parentController)
	{
		super(parentController);
		
		tc = new TestControllerC(this);
		
		register(tc);
	}
}
