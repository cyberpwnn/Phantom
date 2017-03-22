package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.tag.TaggedPlayer;
import org.phantomapi.tag.Tagger;
import org.phantomapi.world.Area;

@Ticked(0)
public class PlayerTagController extends Controller
{
	private GList<Tagger> taggers;
	private GMap<Player, TaggedPlayer> tags;
	
	public PlayerTagController(Controllable parentController)
	{
		super(parentController);
		
		tags = new GMap<Player, TaggedPlayer>();
	}
	
	@Override
	public void onStart()
	{
		for(Player i : onlinePlayers())
		{
			j(i);
		}
	}
	
	@Override
	public void onStop()
	{
		for(Player i : onlinePlayers())
		{
			q(i);
		}
	}
	
	@Override
	public void onTick()
	{
		for(Player i : onlinePlayers())
		{
			if(tags.containsKey(i))
			{
				for(Tagger j : taggers)
				{
					j.updateTagger(tags.get(i));
				}
				
				tags.get(i).update();
				
				for(Player j : onlinePlayers())
				{
					if(Area.within(i.getLocation(), j.getLocation(), 5.0))
					{
						tags.get(i).showContent(j);
						
						if(j.isSneaking())
						{
							tags.get(i).showContext(j);
						}
					}
					
					else
					{
						tags.get(i).hideContent(j);
						tags.get(i).hideContext(j);
					}
				}
			}
		}
	}
	
	public void registerTagger(Tagger t)
	{
		taggers.remove(t);
		taggers.add(t);
	}
	
	public void j(Player p)
	{
		tags.put(p, new TaggedPlayer(p));
	}
	
	public void q(Player p)
	{
		tags.get(p).getTagBuilder().destroyContext();
		tags.remove(p);
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		j(e.getPlayer());
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		q(e.getPlayer());
	}
}
