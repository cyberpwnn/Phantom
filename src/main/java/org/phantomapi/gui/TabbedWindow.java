package org.phantomapi.gui;

import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

/**
 * Tabbed windows
 * 
 * @author cyberpwn
 */
public class TabbedWindow extends PhantomWindow
{
	private GMap<Integer, GList<Element>> elements;
	
	/**
	 * Create a new tabbed window.
	 * 
	 * @param title
	 *            the title
	 * @param viewer
	 *            the player viewer
	 */
	public TabbedWindow(String title, Player viewer)
	{
		super(title, viewer);
		
		elements = new GMap<Integer, GList<Element>>();
	}
	
	/**
	 * Bind tabs, dont add to windows
	 * 
	 * @param e
	 *            the element
	 * @param tab
	 *            the tab number
	 */
	public void bindTab(Element e, Integer tab)
	{
		if(!elements.containsKey(tab))
		{
			elements.put(tab, new GList<Element>());
		}
		
		elements.get(tab).add(e);
	}
	
	/**
	 * Change tabs
	 * 
	 * @param tab
	 *            the tab number
	 */
	public void setTab(int tab)
	{
		if(elements.containsKey(tab))
		{
			super.elements.clear();
			
			for(Element i : elements.get(tab))
			{
				addElement(i);
			}
			
			close();
			open();
		}
	}
}
