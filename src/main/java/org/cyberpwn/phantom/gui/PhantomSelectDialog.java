package org.cyberpwn.phantom.gui;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;

public class PhantomSelectDialog<T> extends PhantomDialog implements SelectDialog<T>
{
	private GMap<Element, T> bindings;
	
	public PhantomSelectDialog(String title, GList<Element> elements, Player viewer, boolean cancellable)
	{
		super(title, elements, viewer, cancellable);
		
		bindings = new GMap<Element, T>();
	}

	public SelectDialog<T> bind(Element e, T t)
	{
		bindings.put(e, t);
		
		return this;
	}
	
	public boolean onClick(Element element, Player p)
	{
		if(bindings.containsKey(element))
		{
			onSelected(bindings.get(element), element);
		}
		
		return true;
	}

	public void onSelected(T clicked, Element e)
	{
		
	}
}
