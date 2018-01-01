package phantom.sl.generic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import phantom.sl.util.array.ARR;

/**
 * Fragmented array lists avoid recopying the array when modified (adding or
 * removing elements). Instead the array allows fragmentation and it's internal
 * size is not it's true size (countable elements).
 * 
 * Benefits: 
 * - Significantly reduced memory waste (not usage)
 * - Faster copying
 * - Faster adding of elements
 * - Faster removal of elements
 * 
 * Drawbacks: 
 * - Index manipulation slower than array lists
 * - Slightly larger memory use
 * 
 * This arraylist allows an input for a "jump buffer". Jump buffers define how 
 * often (or rare) the array should re-allocate additional space (by recreating
 * the array). For example the ArrayList would have a jump buffer of 1. Re-copying
 * every single add and removal. Setting the jump buffer to 16 would only copy 
 * the array in modulo of 16 changes. This increases memory use but reduces waste
 * increasing performance.
 * 
 * This arraylist also supports the use of a "max free" limit. How much do we have 
 * to remove from this list before it begins to shrink in "jump buffer" sizes. This
 * also helps with reducing a constant garbage load for ongoing "live" lists.
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the element in this arraylist
 */
public class FragmentedArrayList<T> implements List<T>
{
	private T[] array;
	private int free;
	private int size;
	private boolean dirty;
	private int maxFree;
	private int jumpBuffer;
	private int minSize;

	/**
	 * Create a fragmented list
	 * @param minSize the initial size and later minium size the list can ever be 
	 *                (internal, if there are no elements, you will still see a 
	 *                size of 0)
	 * @param maxFree the maximum allowed free indexes before reclaiming.
	 * @param jumpBuffer how much to allocate when one is needed.
	 */
	@SuppressWarnings("unchecked")
	public FragmentedArrayList(int minSize, int maxFree, int jumpBuffer)
	{
		this.minSize = minSize;
		this.maxFree = maxFree;
		this.jumpBuffer = jumpBuffer;
		this.array = (T[]) ARR.create(minSize);
		size = 0;
		free = minSize;
		dirty = false;
	}

	public void makeDirty()
	{
		dirty = true;
		size = -1;
		free = -1;
	}

	public void update()
	{
		if(isDirty())
		{
			forceUpdate();
		}
	}

	public void forceUpdate()
	{
		free = ARR.countFree(array);
		size = array.length - free;
		dirty = false;
	}

	public void defrag()
	{
		ARR.defrag(array);
	}

	public int size()
	{
		update();
		return size;
	}

	public boolean isEmpty()
	{
		return size() == 0;
	}

	public boolean contains(Object o)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null && array[i].equals(o))
			{
				return true;
			}
		}

		return false;
	}

	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			private int currentIndex = 0;

			@Override
			public boolean hasNext()
			{
				return currentIndex < size() && get(currentIndex) != null;
			}

			@Override
			public T next()
			{
				return get(currentIndex++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	public Object[] toArray()
	{
		Object[] t = new Object[size()];
		int j = 0;

		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null)
			{
				t[j++] = array[i];
			}
		}

		return t;
	}

	@SuppressWarnings(
	{ "unchecked", "hiding" })
	public <T> T[] toArray(T[] t)
	{
		int j = 0;

		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null)
			{
				t[j++] = (T) array[i];
			}
		}

		return t;
	}

	public boolean add(T e)
	{
		update();

		if(free <= 0)
		{
			allocate();
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private void allocate()
	{
		Object[] n = ARR.create(array.length + jumpBuffer);

		for(int i = 0; i < array.length; i++)
		{
			n[i] = array[i];
		}

		array = (T[]) n;
	}

	public boolean remove(Object o)
	{
		update();

		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null && o.equals(array[i]))
			{
				array[i] = null;
				makeDirty();
				return true;
			}
		}

		return false;
	}

	public boolean containsAll(Collection<?> c)
	{
		for(Object i : c)
		{
			if(!contains(i))
			{
				return false;
			}
		}

		return true;
	}

	public boolean addAll(Collection<? extends T> c)
	{
		for(T i : c)
		{
			add(i);
		}

		return true;
	}

	public boolean addAll(int index, Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c)
	{
		boolean r = false;

		for(Object i : c)
		{
			if(remove(i) && !r)
			{
				r = true;
			}
		}

		return r;
	}

	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public void clear()
	{
		this.array = (T[]) ARR.create(minSize);
		size = 0;
		free = minSize;
		dirty = false;
	}

	public T get(int index)
	{
		if(index > size())
		{
			throw new IndexOutOfBoundsException(index + " where size was " + size());
		}

		int j = 0;

		for(int i = 0; i < array.length; i++)
		{
			if(array[i] != null)
			{
				if(j == index)
				{
					return array[i];
				}

				j++;
			}
		}

		throw new IndexOutOfBoundsException("Unable to locate fragmented element past " + j + ". " + index + " was requested.");
	}

	public T set(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	public void add(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	public T remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object o)
	{
		throw new UnsupportedOperationException();
	}

	public int lastIndexOf(Object o)
	{
		throw new UnsupportedOperationException();
	}

	public ListIterator<T> listIterator()
	{
		throw new UnsupportedOperationException();
	}

	public ListIterator<T> listIterator(int index)
	{
		throw new UnsupportedOperationException();
	}

	public List<T> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}

	public int getMaxFree()
	{
		return maxFree;
	}

	public void setMaxFree(int maxFree)
	{
		this.maxFree = maxFree;
	}

	public int getJumpBuffer()
	{
		return jumpBuffer;
	}

	public void setJumpBuffer(int jumpBuffer)
	{
		this.jumpBuffer = jumpBuffer;
	}

	public int getMinSize()
	{
		return minSize;
	}

	public void setMinSize(int minSize)
	{
		this.minSize = minSize;
	}

	public int getFree()
	{
		return free;
	}

	public boolean isDirty()
	{
		return dirty;
	}
}
