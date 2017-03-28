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
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.Task;
import org.phantomapi.util.CNum;

public abstract class BaseHud implements Hud, Listener
{
	protected Player player;
	protected GList<String> content;
	protected boolean open;
	protected Hologram holo;
	protected CNum selection;
	protected int maxPage;
	protected int startRange;
	protected boolean listening;
	protected GMap<String, Runnable> preListeners;
	protected int index;
	
	public BaseHud(Player player)
	{
		this.player = player;
		content = new GList<String>();
		open = false;
		holo = null;
		selection = new CNum(1);
		maxPage = 6;
		startRange = 0;
		listening = true;
		preListeners = new GMap<String, Runnable>();
		index = 0;
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
			
			new Task(0)
			{
				@Override
				public void run()
				{
					if(open)
					{
						update();
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
		if(open && player.equals(e.getPlayer()) && listening)
		{
			e.setCancelled(true);
			
			Click c = Click.LEFT;
			
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				c = Click.RIGHT;
			}
			
			onClick(c, player, getSelection(), getSelectionRow(), this);
			update();
			
			if(preListeners.containsKey(getSelection()))
			{
				preListeners.get(getSelection()).run();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerScrollEvent e)
	{
		if(open && player.equals(e.getPlayer()) && listening)
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
			if(selection.getMax() != content.size())
			{
				selection.setMax(content.size());
			}
			
			int sel = getSelectionRow();
			GList<String> con = new GList<String>();
			int st = 0;
			
			if(content.size() <= maxPage)
			{
				con = content.copy();
			}
			
			else
			{
				while(startRange + maxPage - 1 < sel)
				{
					startRange++;
				}
				
				while(startRange > sel)
				{
					startRange--;
				}
				
				st = startRange;
				
				for(int i = st; i < st + maxPage; i++)
				{
					if(content.hasIndex(i))
					{
						con.add(content.get(i));
					}
				}
			}
			
			GList<String> sv = new GList<String>();
			
			int k = st;
			
			for(String i : con)
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
			onUpdateInternal();
			onUpdate();
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
	
	protected abstract void onUpdateInternal();
	
	public GList<String> getContent()
	{
		return content;
	}
	
	public void setContent(GList<String> content)
	{
		this.content = content;
	}
	
	public int getMaxPage()
	{
		return maxPage;
	}
	
	public void setMaxPage(int maxPage)
	{
		this.maxPage = maxPage;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	public Hologram getHolo()
	{
		return holo;
	}
	
	public int getStartRange()
	{
		return startRange;
	}
	
	public void registerPreListener(String query, Runnable run)
	{
		preListeners.put(query, run);
	}
	
	public void unregisterPreListener(String query)
	{
		preListeners.remove(query);
	}
	
	public boolean isListening()
	{
		return listening;
	}
	
	public void setListening(boolean listening)
	{
		this.listening = listening;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public GMap<String, Runnable> getPreListeners()
	{
		return preListeners;
	}
}
