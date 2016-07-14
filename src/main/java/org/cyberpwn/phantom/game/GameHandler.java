package org.cyberpwn.phantom.game;

public interface GameHandler<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends GameListener
{
	public G getGame();
	
	public M getMap();
}
