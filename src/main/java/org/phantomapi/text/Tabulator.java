package org.phantomapi.text;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.phantomapi.lang.GList;

public class Tabulator<T> extends GList<T>
{
	private static final long serialVersionUID = 1L;
	private int tabSize;
	
	/**
	 * Create an empty Tabulator
	 */
	public Tabulator(int tabSize)
	{
		super();
		
		this.tabSize = tabSize;
	}
	
	/**
	 * Create a new Tabulator from a Set of the same type
	 * 
	 * @param set
	 *            the given set
	 */
	public Tabulator(Set<T> set, int tabSize)
	{
		this(tabSize);
		
		for(T i : set)
		{
			add(i);
		}
	}
	
	/**
	 * Create a new Tabulator from a Collection of the same type
	 * 
	 * @param set
	 *            the given collection
	 */
	public Tabulator(Collection<T> set, int tabSize)
	{
		this(tabSize);
		
		for(T i : set)
		{
			add(i);
		}
	}
	
	/**
	 * Create a Tabulator by iterating through an iterator
	 * 
	 * @param it
	 *            the iterator
	 */
	public Tabulator(Iterator<T> it, int tabSize)
	{
		this(tabSize);
		
		while(it.hasNext())
		{
			add(it.next());
		}
	}
	
	/**
	 * Create a Tabulator with an array of the same type
	 * 
	 * @param array
	 *            the array to start off this list
	 */
	public Tabulator(T[] array, int tabSize)
	{
		this(tabSize);
		add(array);
	}
	
	/**
	 * Create a Tabulator with an existing list of the same type
	 * 
	 * @param array
	 *            a list of the same type (essentially a clone) but from any
	 *            type implementing List<T>
	 */
	public Tabulator(List<T> array, int tabSize)
	{
		this(tabSize);
		
		if(array == null)
		{
			return;
		}
		
		add(array);
	}
	
	/**
	 * Get the amount of tabs in this tabulator based on the size of the array
	 * 
	 * @return the tabs
	 */
	public int getTabCount()
	{
		return size() % getTabSize() != 0 ? (size() / getTabSize()) + 1 : size() / getTabSize();
	}
	
	/**
	 * Get the first index of the given tab from the list
	 * 
	 * @param tab
	 *            the tab
	 * @return the first index of that given tab or -1 for no tab index
	 */
	public int getFirstIndex(int tab)
	{
		if(hasTab(tab))
		{
			return getTabSize() * tab;
		}
		
		return -1;
	}
	
	/**
	 * Get the last list index for the given tab index (safe for shortened tabs)
	 * 
	 * @param tab
	 *            the tab index
	 * @return the last tab index or -1 for no tab index
	 */
	public int getLastIndex(int tab)
	{
		if(hasTab(tab))
		{
			return getIndexOrLast(getFirstIndex(tab) + tabSize - 1);
		}
		
		return -1;
	}
	
	/**
	 * Get the tab at the given index
	 * 
	 * @param t
	 *            the tab index
	 * @return the list of items in that given tab (or an enpty list)
	 */
	public GList<T> getTab(int t)
	{
		GList<T> tab = new GList<T>();
		
		if(hasTab(t))
		{
			tab = copy().crop(getFirstIndex(t), getLastIndex(t));
		}
		
		return tab;
	}
	
	/**
	 * Does the given tab address contain a tab?
	 * 
	 * @param tab
	 *            the tab address
	 * @return true if a tab has content under that address
	 */
	public boolean hasTab(int tab)
	{
		return Math.abs(tab) <= getTabCount();
	}
	
	/**
	 * Gets the size of a tab
	 * 
	 * @return the size of a tab
	 */
	public int getTabSize()
	{
		return tabSize;
	}
	
	/**
	 * Set the size of the tab
	 * 
	 * @param tabSize
	 *            the tabsize
	 */
	public void setTabSize(int tabSize)
	{
		this.tabSize = tabSize;
	}
}
