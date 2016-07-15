package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.util.C;

public class ExampleTeam extends PhantomTeam<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer>
{
	//A team holds players. Some games can have one empty team
	//This is all hypothetical, so it wont make any difference
	public ExampleTeam(String name, C color, ExampleGame game)
	{
		super(name, color, game);
	}
}
