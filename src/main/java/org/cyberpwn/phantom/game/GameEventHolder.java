package org.cyberpwn.phantom.game;

public class GameEventHolder<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	private final G game;
	
	public GameEventHolder(G game)
	{
		this.game = game;
	}
	
	public G getGame()
	{
		return game;
	}
}
