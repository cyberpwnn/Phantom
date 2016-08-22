package org.phantomapi.gui;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * A Window implementation
 * 
 * @author cyberpwn
 */
public class PhantomWindow implements Window, Listener
{
	protected Integer viewport;
	protected String title;
	protected GList<Element> elements;
	protected Inventory inventory;
	protected Element background;
	protected boolean open;
	protected final Player viewer;
	protected final UUID id;
	
	/**
	 * Create a new window instance
	 * 
	 * @param title
	 *            the title
	 * @param viewer
	 *            the player
	 */
	public PhantomWindow(String title, Player viewer)
	{
		this.inventory = null;
		this.viewport = 6;
		this.title = title;
		this.elements = new GList<Element>();
		this.open = false;
		this.viewer = viewer;
		this.background = new PhantomElement(Material.AIR, new Slot(0), "")
		{
			public void onClick(Player p, Click c, Window w)
			{
				
			}
		};
		this.id = UUID.randomUUID();
	}
	
	public Window open()
	{
		Phantom.instance().registerListener(this);
		viewer.openInventory(build().getInventory());
		open = true;
		
		return this;
	}
	
	public Window close()
	{
		if(open)
		{
			Phantom.instance().unRegisterListener(this);
			viewer.closeInventory();
			open = false;
		}
		
		return this;
	}
	
	public Window build()
	{
		inventory = Bukkit.createInventory(null, viewport * 9, title);
		
		for(int i = 0; i < viewport * 9; i++)
		{
			Slot slot = new Slot(i);
			ItemStack stack = null;
			
			if(contains(slot))
			{
				stack = getElement(slot).getStack();
			}
			
			else
			{
				stack = background.copy().setSlot(slot).getStack();
			}
			
			inventory.setItem(slot.getSlot(), stack);
		}
		
		return this;
	}
	
	public boolean contains(Slot slot)
	{
		for(Element i : elements)
		{
			if(i.getSlot().equals(slot))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean contains(Element element)
	{
		return elements.contains(element);
	}
	
	public Element getElement(Slot slot)
	{
		if(contains(slot))
		{
			for(Element i : elements)
			{
				if(i.getSlot().equals(slot))
				{
					return i;
				}
			}
		}
		
		return null;
	}
	
	public Window addElement(Element element)
	{
		if(!contains(element) && !contains(element.getSlot()))
		{
			elements.add(element);
		}
		
		return this;
	}
	
	public Window removeElement(Element element)
	{
		elements.remove(element);
		
		return this;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Window setTitle(String title)
	{
		this.title = title;
		
		return this;
	}
	
	public GList<Element> getElements()
	{
		return elements;
	}
	
	public Window setElements(GList<Element> elements)
	{
		this.elements = elements;
		
		return this;
	}
	
	public Player getViewer()
	{
		return viewer;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public UUID getId()
	{
		return id;
	}
	
	public Element getBackground()
	{
		return background;
	}
	
	public Window setBackground(Element background)
	{
		this.background = background;
		
		return this;
	}
	
	public Integer getViewport()
	{
		return viewport;
	}
	
	public Window setViewport(Integer viewport)
	{
		this.viewport = viewport;
		
		return this;
	}
	
	public Window setInventory(Inventory inventory)
	{
		this.inventory = inventory;
		
		return this;
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	@EventHandler
	public void on(InventoryCloseEvent e)
	{
		if(e.getPlayer().equals(viewer) && e.getInventory().equals(getInventory()))
		{
			close();
			onClose(this, (Player) e.getPlayer());
		}
	}
	
	@EventHandler
	public void on(InventoryDragEvent e)
	{
		if(e.getWhoClicked().equals(viewer) && e.getInventory().equals(inventory))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(InventoryClickEvent e)
	{
		if(e.getWhoClicked().equals(viewer) && e.getInventory().equals(inventory))
		{
			Slot s = new Slot(e.getSlot());
			Click c = Click.fromClickType(e.getClick());
			
			if(c != null)
			{
				if(contains(s))
				{
					Element element = getElement(s);
					
					if(onClick(element, (Player) e.getWhoClicked()))
					{
						element.onClick((Player) e.getWhoClicked(), c, this);
					}
				}
			}
			
			e.setCancelled(true);
		}
	}
	
	public boolean equals(Object object)
	{
		if(object instanceof Window)
		{
			return ((Window) object).getId().equals(getId());
		}
		
		return false;
	}
	
	@Override
	public boolean onClick(Element element, Player p)
	{
		return true;
	}

	@Override
	public void onClose(Window w, Player p)
	{
		
	}
}
