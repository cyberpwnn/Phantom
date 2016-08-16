package org.phantomapi.example;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

//Notice we add <PlayerData, Player>
//This means we want the configurable object to be PlayerData.class
//And we want the identifier to be a Player.class type
public class PlayerDataController extends DataController<PlayerData, Player>
{
	public PlayerDataController(Controllable parentController)
	{
		super(parentController);
	}

	@Override
	public PlayerData onLoad(Player identifier)
	{
		//This is called when we need to load a playerdata instance
		//We are given a player identifier
		
		PlayerData pd = new PlayerData(identifier);
		loadCluster(pd);
		
		return pd;
	}

	@Override
	public void onSave(Player identifier)
	{
		//Called when we need to save a player
		//We can use the get() method to get it from the cache
		
		saveCluster(get(identifier));
	}

	@Override
	public void onStart()
	{
		
	}

	@Override
	public void onStop()
	{
		//To save all of the data in the cache
		saveAll();
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		//Save the player when they quit
		save(e.getPlayer());
	}
}
