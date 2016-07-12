package permafrost.game;

import permafrost.core.lang.GList;
import permafrost.core.module.ModuleManager;
import permafrost.core.module.PermafrostModule;

public class GameController<T extends Playable> extends PermafrostModule
{
	protected final GList<T> games;
	
	public GameController(ModuleManager manager)
	{
		super(manager);
		
		this.games = new GList<T>();
	}
	
	public GList<T> getGames()
	{
		return games.copy();
	}
	
	public void onEnable()
	{
		
	}
	
	public void onDisable()
	{
		for(T i : getGames())
		{
			i.stop();
		}
		
		games.clear();
	}
}
