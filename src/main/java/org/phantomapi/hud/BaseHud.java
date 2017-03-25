package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.phantomapi.Phantom;
import org.phantomapi.event.PlayerScrollEvent;
import org.phantomapi.gui.Click;
import org.phantomapi.hologram.Hologram;
import org.phantomapi.hologram.PhantomHologram;
import org.phantomapi.lang.GList;
import org.phantomapi.util.CNum;

public abstract class BaseHud implements Hud, Listener
{
	private Player player;
	private GList<String> content;
	protected boolean open;
	private Hologram holo;
	private CNum selection;
	
	public BaseHud(Player player)
	{
		this.player = player;
		content = new GList<String>();
		open = false;
		holo = null;
		selection = new CNum(1);
	}
	
	@Override
	public void open()
	{
		if(!open)
		{
			open = true;
			onOpen();
			Phantom.instance().registerListener(this);
			holo = new PhantomHologram(getBaseLocation());
			holo.setDisplay(content.toString("\n"));
			holo.setExclusive(player);
			update();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent e)
	{
		if(open && player.equals(e.getPlayer()))
		{
			e.setCancelled(true);
			
			Click c = Click.LEFT;
			
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				c = Click.RIGHT;
			}
			
			onClick(c, player, getSelection(), getSelectionRow());
			update();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerScrollEvent e)
	{
		if(open && player.equals(e.getPlayer()))
		{
			selection.set(selection.get() - e.getMovement());
			onSelect(getSelection(), getSelectionRow());
			update();
		}
	}
	
	public void update()
	{
		if(open)
		{
			GList<String> sv = new GList<String>();
			
			int k = 0;
			
			for(String i : content)
			{
				if(k == getSelectionRow())
				{
					sv.add(onEnable(i));
				}
				
				else
				{
					sv.add(onDisable(i));
				}
				
				k++;
			}
			
			holo.setDisplay(sv.toString("\n"));
			holo.setLocation(getBaseLocation());
		}
	}
	
	public abstract Location getBaseLocation();
	
	@Override
	public void close()
	{
		if(open)
		{
			open = false;
			onClose();
			Phantom.instance().unRegisterListener(this);
			holo.destroy();
			holo = null;
		}
	}
	
	@Override
	public void setContents(GList<String> options)
	{
		content = options.copy();
		selection = new CNum(content.size());
	}
	
	@Override
	public GList<String> getContents()
	{
		return content.copy();
	}
	
	@Override
	public String getSelection()
	{
		return content.get(getSelectionRow());
	}
	
	@Override
	public int getSelectionRow()
	{
		return selection.get();
	}
	
	public abstract void onOpen();
	
	public abstract String onDisable(String s);
	
	public abstract String onEnable(String s);
	
	public abstract void onClose();
	
	public abstract void onSelect(String selection, int slot);
	
	public abstract void onClick(Click c, Player p, String selection, int slot);
	
}
