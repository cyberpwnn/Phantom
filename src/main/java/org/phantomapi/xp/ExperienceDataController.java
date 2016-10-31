package org.phantomapi.xp;

import org.bukkit.entity.Player;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

public class ExperienceDataController extends DataController<ExperiencedPlayer, Player>
{
	public ExperienceDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public ExperiencedPlayer onLoad(Player identifier)
	{
		ExperiencedPlayer ep = new ExperiencedPlayer(identifier);
		loadMysql(ep);
		return ep;
	}
	
	@Override
	public void onSave(Player identifier)
	{
		saveMysql(cache.get(identifier));
	}
	
	@Override
	public void onStart()
	{
		for(Player i : onlinePlayers())
		{
			load(i);
		}
	}
	
	@Override
	public void onStop()
	{
		saveAll();
	}
	
}
