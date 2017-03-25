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
		inventory = null;
		viewport = 6;
		this.title = title;
		elements = new GList<Element>();
		open = false;
		this.viewer = viewer;
		background = new PhantomElement(Material.AIR, new Slot(0), "")
		{
			@Override
			public void onClick(Player p, Click c, Window w)
			{
				
			}
		};
		id = UUID.randomUUID();
	}
	
	@Override
	public Window open()
	{
		Phantom.instance().registerListener(this);
		viewer.openInventory(build().getInventory());
		open = true;
		
		return this;
	}
	
	@Override
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
	
	@Override
	public void update()
	{
		if(open)
		{
			rebuild();
		}
	}
	
	@Override
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
	
	public void rebuild()
	{
		if(open)
		{
			if(inventory != null)
			{
				if(inventory.getSize() == viewport * 9)
				{
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
				}
				
				else
				{
					close();
					open();
				}
			}
		}
	}
	
	@Override
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
	
	@Override
	public boolean contains(Element element)
	{
		return elements.contains(element);
	}
	
	@Override
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
	
	@Override
	public Window addElement(Element element)
	{
		if(!contains(element) && !contains(element.getSlot()))
		{
			elements.add(element);
		}
		
		return this;
	}
	
	@Override
	public Window removeElement(Element element)
	{
		elements.remove(element);
		
		return this;
	}
	
	@Override
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public Window setTitle(String title)
	{
		this.title = title;
		
		return this;
	}
	
	@Override
	public GList<Element> getElements()
	{
		return elements;
	}
	
	@Override
	public Window setElements(GList<Element> elements)
	{
		this.elements = elements;
		
		return this;
	}
	
	@Override
	public Player getViewer()
	{
		return viewer;
	}
	
	@Override
	public Inventory getInventory()
	{
		return inventory;
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public Element getBackground()
	{
		return background;
	}
	
	@Override
	public Window setBackground(Element background)
	{
		this.background = background;
		
		return this;
	}
	
	@Override
	public Integer getViewport()
	{
		return viewport;
	}
	
	@Override
	public Window setViewport(Integer viewport)
	{
		this.viewport = viewport;
		
		return this;
	}
	
	@Override
	public Window setInventory(Inventory inventory)
	{
		this.inventory = inventory;
		
		return this;
	}
	
	@Override
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
		if(e.getWhoClicked().equals(viewer) && e.getInventory() != null && e.getClickedInventory() != null && e.getInventory().equals(inventory) && e.getClickedInventory().getName().equals(inventory.getName()))
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
		
		if(e.getWhoClicked().equals(viewer))
		{
			e.setCancelled(true);
		}
	}
	
	@Override
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
