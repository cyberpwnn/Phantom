package org.cyberpwn.phantom.gui;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.cyberpwn.phantom.lang.GList;

public interface Window
{
	public Window open();
	
	public Window close();
	
	public Window build();
	
	public boolean contains(Slot slot);
	
	public boolean contains(Element element);
	
	public Element getElement(Slot slot);
	
	public Window addElement(Element element);
	
	public Window removeElement(Element element);
	
	public String getTitle();
	
	public Window setTitle(String title);
	
	public GList<Element> getElements();
	
	public Window setElements(GList<Element> elements);
	
	public Player getViewer();
	
	public Inventory getInventory();
	
	public UUID getId();
	
	public Element getBackground();
	
	public Window setBackground(Element background);
	
	public Integer getViewport();
	
	public Window setViewport(Integer viewport);
	
	public Window setInventory(Inventory inventory);
	
	public boolean isOpen();
	
	public void setOpen(boolean open);
}
