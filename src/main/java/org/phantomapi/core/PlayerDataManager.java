package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.MySQL;
import org.phantomapi.clust.PlayerData;
import org.phantomapi.clust.PlayerDataHandler;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.S;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;

@Ticked(0)
public class PlayerDataManager extends PlayerDataHandler<PlayerData> implements Monitorable, Configurable
{
	@Keyed("flush-interval")
	public int interval = 20;
	
	private DataCluster cc;
	private GMap<Player, String> snapshots;
	private boolean flushing;
	private MySQL sql;
	private int inter;
	
	public PlayerDataManager(Controllable parentController)
	{
		super(parentController);
		
		inter = 0;
		cc = new DataCluster();
		snapshots = new GMap<Player, String>();
		flushing = false;
	}
	
	@Override
	public void onTick()
	{
		inter++;
		
		if(inter >= interval)
		{
			inter = 0;
			
			try
			{
				flush(true);
			}
			
			catch(Exception e)
			{
				flushing = false;
			}
		}
	}
	
	public void snapshot(Player player)
	{
		snapshots.put(player, get(player).getConfiguration().toJSON().toString());
	}
	
	public boolean isDirty(Player player)
	{
		return !get(player).getConfiguration().toJSON().toString().equals(snapshots.get(player));
	}
	
	public void flush(boolean async)
	{
		if(flushing)
		{
			return;
		}
		
		flushing = true;
		
		GList<PlayerData> flush = new GList<PlayerData>();
		
		for(Player i : onlinePlayers())
		{
			PlayerData pd = get(i);
			
			if(isDirty(i))
			{
				flush.add(pd);
			}
		}
		
		if(flush.isEmpty())
		{
			flushing = false;
			
			return;
		}
		
		if(async)
		{
			new A()
			{
				@Override
				public void async()
				{
					flush(flush);
					
					new S()
					{
						@Override
						public void sync()
						{
							for(Player i : onlinePlayers())
							{
								snapshot(i);
							}
							
							flushing = false;
						}
					};
				}
			};
		}
		
		else
		{
			flush(flush);
			
			for(Player i : onlinePlayers())
			{
				snapshot(i);
			}
			
			flushing = false;
		}
	}
	
	public void flush(GList<PlayerData> pd)
	{
		for(PlayerData i : pd)
		{
			flush(i);
		}
	}
	
	public void flush(PlayerData pd)
	{
		try
		{
			ConfigurationHandler.toMysql(pd, sql);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public PlayerData onLoad(Player identifier)
	{
		s("Loading Player " + identifier.getName());
		PlayerData pd = new PlayerData(identifier.getUniqueId());
		loadMysql(pd);
		
		new TaskLater()
		{
			@Override
			public void run()
			{
				snapshot(identifier);
			}
		};
		
		return pd;
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		cache.remove(e.getPlayer());
		snapshots.remove(e.getPlayer());
		s("Unloaded Player " + e.getPlayer().getName());
	}
	
	@Override
	public void onSave(Player identifier)
	{
		
	}
	
	@Override
	public void onStart()
	{
		sql = Phantom.instance().getMySQLConnectionController().getSql();
	}
	
	@Override
	public void onStop()
	{
		flush(false);
	}
	
	@Override
	public String getMonitorableData()
	{
		return "Cached: " + C.LIGHT_PURPLE + snapshots.size() + " " + C.RED + ((flushing ? "FLUSHING" : ""));
	}
	
	@Override
	public void onNewConfig()
	{
		
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "pdm";
	}
}
