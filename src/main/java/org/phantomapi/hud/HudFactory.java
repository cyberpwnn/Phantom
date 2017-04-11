package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.phantomapi.gui.Click;
import org.phantomapi.lang.Audible;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

public abstract class HudFactory
{
	public enum HudType
	{
		PLAYER,
		ENTITY,
		ROOTED
	}
	
	private Player viewer;
	private BaseHud hud;
	private GMap<String, Runnable> hits;
	private Object secondary;
	
	public HudFactory(Player viewer)
	{
		this.viewer = viewer;
		hits = new GMap<String, Runnable>();
		secondary = null;
	}
	
	public HudFactory build()
	{
		hud = buildHud(getKeys());
		hud.open();
		
		return this;
	}
	
	private BaseHud buildHud(GList<String> keys)
	{
		BaseHud h = null;
		
		switch(getType())
		{
			case ENTITY:
				h = new EntityHud(viewer, (Entity) secondary)
				{
					@Override
					public void onUpdate()
					{
						
					}
					
					@Override
					public void onSelect(String selection, int slot)
					{
						getScrollSound().play(viewer);
					}
					
					@Override
					public void onOpen()
					{
						getOpenSound().play(viewer);
					}
					
					@Override
					public String onEnable(String s)
					{
						return HudFactory.this.onEnable(s);
					}
					
					@Override
					public String onDisable(String s)
					{
						return HudFactory.this.onDisable(s);
					}
					
					@Override
					public void onClose()
					{
						getCloseSound().play(viewer);
					}
					
					@Override
					public void onClick(Click c, Player p, String selection, int slot, Hud h)
					{
						getClickSound().play(viewer);
					}
				};
			case PLAYER:
				h = new PlayerHud(viewer)
				{
					@Override
					public void onUpdate()
					{
						
					}
					
					@Override
					public void onSelect(String selection, int slot)
					{
						getScrollSound().play(viewer);
					}
					
					@Override
					public void onOpen()
					{
						getOpenSound().play(viewer);
					}
					
					@Override
					public String onEnable(String s)
					{
						return HudFactory.this.onEnable(s);
					}
					
					@Override
					public String onDisable(String s)
					{
						return HudFactory.this.onDisable(s);
					}
					
					@Override
					public void onClose()
					{
						getCloseSound().play(viewer);
					}
					
					@Override
					public void onClick(Click c, Player p, String selection, int slot, Hud h)
					{
						getClickSound().play(viewer);
					}
				};
			case ROOTED:
				h = new RootedHud(viewer, (Location) secondary)
				{
					@Override
					public void onUpdate()
					{
						
					}
					
					@Override
					public void onSelect(String selection, int slot)
					{
						getScrollSound().play(viewer);
					}
					
					@Override
					public void onOpen()
					{
						getOpenSound().play(viewer);
					}
					
					@Override
					public String onEnable(String s)
					{
						return HudFactory.this.onEnable(s);
					}
					
					@Override
					public String onDisable(String s)
					{
						return HudFactory.this.onDisable(s);
					}
					
					@Override
					public void onClose()
					{
						getCloseSound().play(viewer);
					}
					
					@Override
					public void onClick(Click c, Player p, String selection, int slot, Hud h)
					{
						getClickSound().play(viewer);
					}
				};
			default:
				break;
		}
		
		BaseHud hh = h;
		hh.setContent(keys);
		
		for(String i : keys)
		{
			GList<String> mKeys = getKeysFor(i);
			
			if(mKeys.isEmpty())
			{
				for(String j : hits.k())
				{
					if(j.endsWith(i + "." + j))
					{
						hh.registerPreListener(i, hits.get(j));
					}
				}
			}
			
			else
			{
				h.registerPreListener(i, new Runnable()
				{
					
					@Override
					public void run()
					{
						hh.setIndex(hh.getIndex() + 1);
						hh.setListening(false);
						
						BaseHud hhh = buildHud(mKeys);
						hhh.open();
					}
					
				});
			}
		}
		
		return h;
	}
	
	private GList<String> getKeys()
	{
		GList<String> m = new GList<String>();
		
		for(String i : hits.k())
		{
			if(i.contains("."))
			{
				m.add(i.split("\\.")[0]);
			}
			
			else
			{
				m.add(i);
			}
		}
		
		m.removeDuplicates();
		return m;
	}
	
	private GList<String> getKeysFor(String key)
	{
		GList<String> m = new GList<String>();
		
		for(String i : hits.k())
		{
			if(i.contains("."))
			{
				int ind = 0;
				
				for(String j : i.split("\\."))
				{
					if(j.equals(key))
					{
						if(i.split("\\.").length > ind + 1)
						{
							m.add(i.split("\\.")[ind + 1]);
						}
					}
					
					ind++;
				}
			}
		}
		
		m.removeDuplicates();
		
		return m;
	}
	
	public void setSecondary(Object o)
	{
		secondary = o;
	}
	
	public HudFactory addHandle(String path, Runnable runnable)
	{
		hits.put(path, runnable);
		
		return this;
	}
	
	public abstract HudType getType();
	
	public abstract Audible getScrollSound();
	
	public abstract Audible getClickSound();
	
	public abstract Audible getOpenSound();
	
	public abstract Audible getCloseSound();
	
	public abstract String onEnable(String s);
	
	public abstract String onDisable(String s);
}
