package org.phantomapi.example;

import org.phantomapi.construct.PhantomPlugin;

public class ExamplePlugin extends PhantomPlugin
{
	private ExampleController exampleController;
	
	@Override
	public void enable()
	{
		exampleController = new ExampleController(this);
		
		register(exampleController);
	}

	@Override
	public void disable()
	{
		
	}
	
	
}
