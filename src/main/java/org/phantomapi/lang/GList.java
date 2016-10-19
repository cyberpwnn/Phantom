package org.phantomapi.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.phantomapi.Phantom;
import org.phantomapi.async.Callback;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.ExecutiveRunnable;

/**
 * GLists are Arraylists with special enhancements
 * 
 * @author cyberpwn
 * @param <T>
 *            the type of list T
 */
public class GList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = 4480457702775755227L;
	
	/**
	 * Create an empty GList
	 */
	public GList()
	{
		super();
	}
	
	/**
	 * Create a new GList from a Set of the same type
	 * 
	 * @param set
	 *            the given set
	 */
	public GList(Set<T> set)
	{
		super();
		
		for(T i : set)
		{
			add(i);
		}
	}
	
	/**
	 * Create a new GList from a Collection of the same type
	 * 
	 * @param set
	 *            the given collection
	 */
	public GList(Collection<T> set)
	{
		super();
		
		for(T i : set)
		{
			add(i);
		}
	}
	
	/**
	 * Create a glist by iterating through an iterator
	 * 
	 * @param it
	 *            the iterator
	 */
	public GList(Iterator<T> it)
	{
		super();
		
		while(it.hasNext())
		{
			add(it.next());
		}
	}
	
	/**
	 * Create a GList with an array of the same type
	 * 
	 * @param array
	 *            the array to start off this list
	 */
	public GList(T[] array)
	{
		super();
		add(array);
	}
	
	/**
	 * Create a GList with an existing list of the same type
	 * 
	 * @param array
	 *            a list of the same type (essentially a clone) but from any
	 *            type implementing List<T>
	 */
	public GList(List<T> array)
	{
		super();
		
		if(array == null)
		{
			return;
		}
		
		add(array);
	}
	
	/**
	 * Returns a copy of this list in the form of an executive iterator
	 * 
	 * @param runnable
	 *            the runnable to process each item
	 * @return the iterator
	 */
	public ExecutiveIterator<T> iterator(ExecutiveRunnable<T> runnable)
	{
		return new ExecutiveIterator<T>(copy())
		{
			@Override
			public void onIterate(T next)
			{
				runnable.run(next);
			}
		};
	}
	
	/**
	 * Schedule an executive iterator to be executed via the channeled pool
	 * executor. You can assign an execution channel to avoid bandwidth issues
	 * 
	 * @param channel
	 *            the unique channel to schedule this execution on
	 * @param runnable
	 *            the runnable
	 */
	public void schedule(String channel, ExecutiveRunnable<T> runnable)
	{
		Phantom.schedule(channel, iterator(runnable));
	}
	
	/**
	 * Schedule an executive iterator to be executed via the channeled pool
	 * executor.
	 * 
	 * @param runnable
	 *            the runnable
	 */
	public void schedule(ExecutiveRunnable<T> runnable)
	{
		Phantom.schedule(iterator(runnable));
	}
	
	/**
	 * Get the most common element in the list, may return any if no duplicates
	 * 
	 * @return the most common element
	 */
	public T mostCommon()
	{
		GMap<T, Integer> common = new GMap<T, Integer>();
		Iterator<T> it = iterator();
		
		while(it.hasNext())
		{
			T i = it.next();
			
			if(!common.containsKey(i))
			{
				common.put(i, 0);
			}
			
			common.put(i, common.get(i) + 1);
		}
		
		int sm = 0;
		T v = null;
		
		for(T i : common.keySet())
		{
			if(common.get(i) > sm)
			{
				sm = common.get(i);
				v = i;
			}
		}
		
		return v;
	}
	
	/**
	 * Get a shuffled copy of this list. A COPY.
	 * 
	 * @return a Glist of the same type as this, shuffled.
	 */
	public GList<T> shuffleCopy()
	{
		GList<T> o = copy();
		Collections.shuffle(o);
		return o;
	}
	
	/**
	 * Shuffle this list. (randomize)
	 */
	public void shuffle()
	{
		Collections.shuffle(this);
	}
	
	/**
	 * Cuts this list in half, returns a list of this list type, Basically
	 * List
	 * - 1
	 * - 2
	 * - 3
	 * - 4
	 * Would return
	 * List
	 * - List
	 * - - 1
	 * - - 2
	 * - List
	 * - - 3
	 * - - 4
	 * 
	 * @return a split set of lists
	 */
	@SuppressWarnings("unchecked")
	public GList<GList<T>> split()
	{
		GList<GList<T>> mtt = new GList<GList<T>>();
		GList<T> ma = new GList<T>();
		GList<T> mb = new GList<T>();
		
		for(int i = 0; i < size() / 2; i++)
		{
			if(hasIndex(i))
			{
				break;
			}
			
			ma.add(get(i));
		}
		
		for(int i = (size() / 2) - 1; i < size(); i++)
		{
			if(hasIndex(i))
			{
				break;
			}
			
			mb.add(get(i));
		}
		
		mtt.add(ma, mb);
		
		return null;
	}
	
	/**
	 * Does this list contain the given index?
	 * 
	 * @param i
	 *            the given index
	 * @return true if the list has the given index
	 */
	public boolean hasIndex(int i)
	{
		return i < size();
	}
	
	/**
	 * Pick a random element in the list
	 * 
	 * @return the randomly picked element
	 */
	public T pickRandom()
	{
		Random random = new Random();
		return get(random.nextInt(size()));
	}
	
	/**
	 * Get a GList of Strings from the elements in this list. Essentially
	 * creates a list of objects toString'd into a new list
	 * 
	 * @return the String list
	 */
	public GList<String> stringList()
	{
		GList<String> s = new GList<String>();
		
		for(T i : this)
		{
			s.add(i.toString());
		}
		
		return s;
	}
	
	/**
	 * Do something for each
	 * 
	 * @param callback
	 *            the something to do things for something
	 */
	public void forEach(Callback<T> callback)
	{
		for(T i : this)
		{
			callback.run(i);
		}
	}
	
	/**
	 * Get the last index of the list
	 * 
	 * @return the last index
	 */
	public int last()
	{
		return size() - 1;
	}
	
	/**
	 * Get the index at the given index (same) OR if that index does not exist,
	 * get the LAST index of this list
	 * 
	 * @param index
	 *            the index
	 * @return the same index, or the last index of the list if the given inxex
	 *         does not exist
	 */
	public int getIndexOrLast(int index)
	{
		if(hasIndex(index))
		{
			return index;
		}
		
		return last();
	}
	
	/**
	 * Crop out the end of the list by supplying a START index to be the next 0
	 * of the new cropped list
	 * <br/>
	 * <br/>
	 * Example List a, b, c, d
	 * <br/>
	 * cropFrom(1) > c, d
	 * 
	 * @param from
	 *            the from index to be the new beginning of the next index
	 * @return the cropped glist
	 */
	public GList<T> cropFrom(int from)
	{
		return crop(from, size() - 1);
	}
	
	public GList<GList<T>> split(int factor)
	{
		GList<GList<T>> factors = new GList<GList<T>>();
		
		int size = size() / factor;
		
		if(size < 1)
		{
			factors.add(copy());
		}
		
		else
		{
			for(int i = 0; i < factor; i++)
			{
				GList<T> t = new GList<T>();
				
				for(int j = 0; j < size(); j++)
				{
					try
					{
						t.add(get(j * (factor + 1)));
					}
					
					catch(Exception e)
					{
						
					}
				}
				
				factors.add(t);
			}
		}
		
		return factors;
	}
	
	/**
	 * Crop out the beginning of the list by supplying an END index to be the
	 * next end index of the new cropped list
	 * <br/>
	 * <br/>
	 * Example List a, b, c, d
	 * <br/>
	 * cropFrom(1) > a, b
	 * 
	 * @param from
	 *            the from index to be the new beginning of the next index
	 * @return the cropped glist
	 */
	public GList<T> cropTo(int to)
	{
		return crop(0, to);
	}
	
	/**
	 * Crop the glist
	 * <br/>
	 * <br/>
	 * Example List a, b, c, d
	 * <br/>
	 * cropFrom(1, 2) > b, c
	 * 
	 * @param from
	 *            the from index
	 * @param to
	 *            the to index
	 * @return the cropped glist
	 */
	public GList<T> crop(int from, int to)
	{
		GList<T> crop = new GList<T>();
		
		if(!isEmpty() && from >= 0 && hasIndex(from) && hasIndex(to) && from <= to)
		{
			for(int i = from; i <= to; i++)
			{
				crop.add(get(i));
			}
		}
		
		return crop;
	}
	
	/**
	 * Remove all duplicates from this list (by creating a set and adding them
	 * back to this list. NOT A COPY.
	 * 
	 * @return the new list
	 */
	public GList<T> removeDuplicates()
	{
		Set<T> set = new LinkedHashSet<T>(this);
		clear();
		addAll(set);
		
		return this;
	}
	
	/**
	 * Remove all of the given object in the list.
	 * 
	 * @param t
	 *            the given object
	 */
	public void removeAll(T t)
	{
		while(contains(t))
		{
			remove(t);
		}
	}
	
	/**
	 * Does this list have duplicates?
	 * 
	 * @return true if there is at least one duplicate element
	 */
	public boolean hasDuplicates()
	{
		return size() != new LinkedHashSet<T>(this).size();
	}
	
	/**
	 * Sort the list by comparing them via toStrings
	 */
	public void sort()
	{
		Collections.sort(this, new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});
	}
	
	/**
	 * Add a new element to the list, and remove the first element if the limit
	 * is reached (size)
	 * 
	 * @param value
	 *            the new element
	 * @param limit
	 *            the limit of this list's size
	 */
	public void push(T value, int limit)
	{
		add(value);
		
		while(size() > limit && !isEmpty())
		{
			remove(0);
		}
	}
	
	/**
	 * Add an array of items of the same type, or all of them (...)
	 * 
	 * @param array
	 *            the array
	 */
	@SuppressWarnings("unchecked")
	public void add(T... array)
	{
		for(T i : array)
		{
			add(i);
		}
	}
	
	/**
	 * Add an element to the list and return it, great for chaining
	 * 
	 * @param t
	 *            the element to add to the end
	 * @return this list (for chaining)
	 */
	public GList<T> qadd(T t)
	{
		this.add(t);
		return this;
	}
	
	/**
	 * Add a list of elements to the list (same type)
	 * 
	 * @param array
	 *            the list
	 */
	public void add(List<T> array)
	{
		for(T i : array)
		{
			add(i);
		}
	}
	
	/**
	 * Get a string of this list with a split string added between the elements.
	 * For example if you pass in ", " it would return a comma+space separated
	 * list.
	 * 
	 * @param split
	 *            the split string
	 * @return a string
	 */
	public String toString(String split)
	{
		if(isEmpty())
		{
			return "";
		}
		
		if(size() == 1)
		{
			if(get(0) != null)
			{
				return get(0).toString();
			}
			
			return "";
		}
		
		String s = "";
		
		if(split == null)
		{
			split = "";
		}
		
		for(Object i : this)
		{
			s = s + split + i.toString();
		}
		
		if(s.length() == 0)
		{
			return "";
		}
		
		return s.substring(split.length());
	}
	
	/**
	 * Get a reversed copy of the list
	 * 
	 * @return the reversed list (copied)
	 */
	public GList<T> reverse()
	{
		GList<T> m = this.copy();
		Collections.reverse(m);
		return m;
	}
	
	/**
	 * Comma, space, separated, list, representation
	 */
	@Override
	public String toString()
	{
		return toString(", ");
	}
	
	/**
	 * Copy is an implementation specific clone
	 * 
	 * @return cloned list
	 */
	public GList<T> copy()
	{
		GList<T> c = new GList<T>();
		
		for(T i : this)
		{
			c.add(i);
		}
		
		return c;
	}
	
	/**
	 * Delete chain an item
	 * 
	 * @param t
	 *            the element
	 * @return the new list
	 */
	public GList<T> qdel(T t)
	{
		remove(t);
		return this;
	}
	
	/**
	 * Return the first element in the list (0), then delete it
	 * 
	 * @return the popped element
	 */
	public T pop()
	{
		if(isEmpty())
		{
			return null;
		}
		
		T t = get(0);
		remove(0);
		return t;
	}
}
