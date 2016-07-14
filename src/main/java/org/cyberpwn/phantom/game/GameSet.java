package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.lang.GList;

public class GameSet<G>
{
	private final GList<G> games;
	private final Integer playerLimit;
	private final Integer gameLimit;
	private final GameAdapter gameAdapter;
	
	public GameSet(GameAdapter gameAdapter, int playerLimit, int gameLimit)
	{
		this.gameAdapter = gameAdapter;
		this.games = new GList<G>();
		this.playerLimit = playerLimit;
		this.gameLimit = gameLimit;
	}

	public GList<G> getGames()
	{
		return games;
	}

	public Integer getPlayerLimit()
	{
		return playerLimit;
	}

	public Integer getGameLimit()
	{
		return gameLimit;
	}

	public GameAdapter getGameAdapter()
	{
		return gameAdapter;
	}
}
