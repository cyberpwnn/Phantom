package org.phantomapi.game;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;

/**
 * Represents a phantom game instance implementation
 * 
 * @author cyberpwn
 */
public abstract class PhantomGame implements Game
{
	protected final GamePlugin plugin;
	protected final GameController controller;
	protected final GameState state;
	protected final GameProfile profile;
	private GameStatus status;
	
	/**
	 * Create a game plugin
	 * 
	 * @param plugin
	 *            the game plugin
	 */
	public PhantomGame(GamePlugin plugin)
	{
		this.plugin = plugin;
		this.controller = new PhantomGameController(this);
		this.state = new PhantomGameState(this);
		this.profile = new GameProfile(this);
		this.status = GameStatus.OFFLINE;
	}
	
	public abstract void onStart(String... lauchParameters);
	
	public abstract void onStop(String... endParameters);
	
	public abstract void onJoin(Player p);
	
	public abstract void onQuit(Player p);
	
	public void startGame(String... lauchParameters)
	{
		status = GameStatus.STARTING;
		onStart(lauchParameters);
	}
	
	public void stopGame(String... endParameters)
	{
		status = GameStatus.STOPPING;
		onStop(endParameters);
	}
	
	public GamePlugin getPlugin()
	{
		return plugin;
	}
	
	public GameController getController()
	{
		return controller;
	}
	
	public GameState getState()
	{
		return state;
	}
	
	public GameStatus getStatus()
	{
		return status;
	}
	
	public void setStatus(GameStatus status)
	{
		this.status = status;
	}
	
	public GameProfile getProfile()
	{
		return profile;
	}
	
	@Override
	public GList<Player> getPlayers()
	{
		return getState().getPlayers();
	}
	
	@Override
	public boolean contains(Player p)
	{
		return getState().contains(p);
	}
	
	@Override
	public GList<GameObject> getGameObjects()
	{
		return getState().getGameObjects();
	}
	
	@Override
	public boolean contains(GameObject o)
	{
		return getState().contains(o);
	}
	
	@Override
	public boolean joinGame(Player p)
	{
		if(getProfile().getCapacityProfile().canJoin(getPlayers().size()))
		{
			getState().addPlayer(p);
			onJoin(p);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void quitGame(Player p)
	{
		onQuit(p);
		getState().removePlayer(p);
	}
}
