package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Ticked;
import org.cyberpwn.phantom.lang.Alphabet;

//Tick the onTick function 20 times a second
@Ticked(0)
public class ExampleGame extends PhantomGame<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer>
{
	//This is the game object
	//Game stuff goes here, and it is a controller
	//IT can be ticked.
	public ExampleGame(Controllable parentController, ExampleMap map, Alphabet alpha)
	{
		super(parentController, map, alpha);
	}
}
