package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.construct.PhantomPlugin;

//This is the plugin file, this will be called on start
public class MinigamePlugin extends PhantomPlugin
{
	//Define the GameController with the construct api.
	private ExampleGameController controller;
	
	//Use ENABLE not onEnable. This sets up the controllers
	public void enable()
	{
		//Initialize the controller as this plugin being the parent
		controller = new ExampleGameController(this);
		
		//Register the controller so it maps its 
		// - START <> Plugin Start
		// - STOP <> Plugin Stop
		register(controller);
	}
}
