package org.phantomapi.example;

import org.bukkit.entity.Player;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.Tabled;

//This is not needed for yml configs, but
//In case we ever choose to use SQL, its here
@Tabled("player_title_data")
public class PlayerData implements Configurable
{
	private Player player;
	private DataCluster cc;
	
	@Keyed("cool.title")
	public String cooTitle = "Title";
	
	public PlayerData(Player player)
	{
		this.player = player;
		this.cc = new DataCluster();
	}
	
	@Override
	public void onNewConfig()
	{
		// Dynamic
	}

	@Override
	public void onReadConfig()
	{
		// Dynamic
	}

	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}

	@Override
	public String getCodeName()
	{
		//This makes each config from this type different 
		//Based on the player's uuid.
		return player.getUniqueId().toString();
	}
	
}
