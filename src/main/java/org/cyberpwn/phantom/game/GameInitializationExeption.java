package org.cyberpwn.phantom.game;

public class GameInitializationExeption extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Game fail on init
	 * @param message the issue
	 */
	public GameInitializationExeption(String message)
	{
		super(message);
	}
}
