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

@Ticked(5)
public class PlayerTagController extends Controller
{
	private GList<Tagger> taggers;
	private GMap<Player, TaggedPlayer> tags;
	private GMap<Player, GList<Player>> hr;
	private GMap<Player, GList<Player>> hrc;
	
	public PlayerTagController(Controllable parentController)
	{
		super(parentController);
		
		tags = new GMap<Player, TaggedPlayer>();
		taggers = new GList<Tagger>();
		hr = new GMap<Player, GList<Player>>();
		hrc = new GMap<Player, GList<Player>>();
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
					if(j.equals(i))
					{
						continue;
					}
					
					if(i.getLocation().distanceSquared(j.getLocation()) <= 16)
					{
						if(hr.containsKey(i) && hr.get(i).contains(j))
						{
							hr.get(i).remove(j);
							tags.get(i).getTagBuilder().rebuild();
						}
						
						if(j.isSneaking())
						{
							if(hrc.containsKey(i) && hrc.get(i).contains(j))
							{
								tags.get(i).getTagBuilder().rebuild();
								tags.get(i).showContext(j);
								hrc.get(i).remove(j);
							}
						}
						
						else
						{
							tags.get(i).hideContext(j);
							
							if(!hrc.containsKey(i))
							{
								hrc.put(i, new GList<Player>());
							}
							
							if(!hrc.get(i).contains(j))
							{
								hrc.get(i).add(j);
							}
						}
					}
					
					else
					{
						tags.get(i).hideContent(j);
						tags.get(i).hideContext(j);
						
						if(!hr.containsKey(i))
						{
							hr.put(i, new GList<Player>());
						}
						
						if(!hr.get(i).contains(j))
						{
							hr.get(i).add(j);
						}
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
