package org.phantomapi.hud;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
import org.phantomapi.sync.Task;
import org.phantomapi.util.CNum;
import org.phantomapi.util.FinalInteger;

public abstract class EntityHud implements Hud, Listener
{
	private Player player;
	private GList<String> content;
	protected boolean open;
	private Hologram holo;
	private CNum selection;
	private Entity e;
	
	public EntityHud(Player player, Entity e)
	{
		this.player = player;
		this.e = e;
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
			((PhantomHologram) holo).setSplitDistance(0);
			update();
			
			FinalInteger k = new FinalInteger(0);
			int max = 30;
			
			new Task(0)
			{
				@Override
				public void run()
				{
					if(open)
					{
						if(k.get() > max)
						{
							update();
						}
						
						else
						{
							holo.setLocation(getBaseLocation());
							double m = (double) k.get() / (double) max;
							double v = 0.025 * m;
							((PhantomHologram) holo).setSplitDistance(v);
							
							k.add(1);
						}
					}
					
					else
					{
						cancel();
					}
				}
			};
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
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerScrollEvent e)
	{
		if(open && player.equals(e.getPlayer()))
		{
			selection.set(selection.get() - e.getMovement());
			update();
			onSelect(getSelection(), getSelectionRow());
		}
	}
	
	public void update()
	{
		if(open)
		{
			if(e.isDead() || player.getLocation().distanceSquared(e.getLocation()) > 64)
			{
				close();
				return;
			}
			
			GList<String> sv = new GList<String>();
			
			int k = 0;
			
			for(String i : content)
			{
				if(k == getSelectionRow())
				{
					sv.add(i);
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
	
	public Location getBaseLocation()
	{
		return e.getLocation();
	}
	
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
	
	public abstract void onClose();
	
	public abstract void onSelect(String selection, int slot);
	
	public abstract void onClick(Click c, Player p, String selection, int slot);
	
}
