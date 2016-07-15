package org.cyberpwn.phantom.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.Alphabet;

//We implement GameAdapter to handle starting games
//We extend Controller to be controlled by the parent controller
public class ExampleGameController extends Controller implements GameAdapter<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer>
{
	//Define a game set of our game type.
	//This handles player traffic and when to make new games.
	private GameSet<ExampleMap, ExampleGame, ExampleTeam, ExamplePlayer> gameSet;
	
	public ExampleGameController(Controllable parentController)
	{
		//We're a controller
		super(parentController);
		
		//Create the game set and set the  
		// - MAX PLAYERS PER GAME: 60
		// - MAX GAMES RUNNING: 4
		gameSet = new GameSet<>(this, 60, 4);
	}
	
	//Handle player joins
	public void joinPlayer(Player p)
	{
		//This will do a few things
		// - Create a player object with the adapter (onPlayerObjectCreate)
		// - Create a new game if all the others are full
		// - Returns false if no games can be joined.
		gameSet.join(p);
	}
	
	//Quit players
	public void quitPlayer(Player p)
	{
		//Quits any game they are in, and shuts down game if empty
		gameSet.quit(p);
	}

	//Adapter for creating a game.
	@Override
	public ExampleGame onGameCreate(Alphabet alphabet)
	{
		ExampleMap map = new ExampleMap(Bukkit.getWorld("game_world"));
		ExampleGame game = new ExampleGame(this, map, alphabet);
		
		return game;
	}

	//Adapter for creating player object wrappers
	@Override
	public ExamplePlayer onPlayerObjectCreate(ExampleGame g, Player p)
	{
		ExamplePlayer player = new ExamplePlayer(p, g);
		return player;
	}
}
