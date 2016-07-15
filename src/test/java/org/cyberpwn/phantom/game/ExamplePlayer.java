package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;

public class ExamplePlayer extends PhantomGamePlayer<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer>
{
	//This is a playerdata object. Used for references per player
	//Things like score, shields, ability cooldowns and more.
	public ExamplePlayer(Player player, ExampleGame game)
	{
		super(player, game);
	}
}
