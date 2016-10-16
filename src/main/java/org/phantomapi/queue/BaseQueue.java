package org.phantomapi.queue;

import java.util.Collection;
import org.phantomapi.lang.GList;
import org.phantomapi.util.M;

/**
 * Basic queue
 * 
 * @author cyberpwn
 * @param <T>
 *            the queued type
 */
public class BaseQueue<T extends Queued>
{
	protected final GList<T> queue;
	protected boolean allowDuplicates;
	protected boolean random;
	
	/**
	 * Create a basic queue
	 */
	public BaseQueue()
	{
		queue = new GList<T>();
		allowDuplicates = false;
		random = false;
	}
	
	/**
	 * Queue the given queued objects
	 * 
	 * @param queued
	 *            the queued objects
	 */
	public void queue(Collection<T> queued)
	{
		for(T i : queued)
		{
			queue(i);
		}
	}
	
	/**
	 * Queue the given object
	 * 
	 * @param queued
	 *            the queued object
	 */
	public void queue(T queued)
	{
		if(!allowDuplicates && queue.contains(queued))
		{
			return;
		}
		
		queue.add(queued);
	}
	
	/**
	 * Is the queue empty?
	 * 
	 * @return true if it is
	 */
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}
	
	/**
	 * Remove the given object from the queue
	 * 
	 * @param queued
	 *            the queued object
	 */
	public void dequeue(T queued)
	{
		queue.removeAll(queued);
	}
	
	/**
	 * Remove all queued objects
	 */
	public void dequeue()
	{
		queue.clear();
	}
	
	/**
	 * Bulk process through the entire queue
	 * 
	 * @return the amount of objects processed
	 */
	public int cycleAll()
	{
		int processed = 0;
		
		while(!queue.isEmpty())
		{
			cycle();
			processed++;
		}
		
		return processed;
	}
	
	/**
	 * Cycle through the queue for a given amount of milliseconds
	 * 
	 * @param ms
	 *            the milliseconds
	 * @return the amount of objects processed
	 */
	public int cycleFor(double ms)
	{
		int processed = 0;
		long ns = M.ns();
		
		while(M.ns() - ns < ms * 1000000)
		{
			if(queue.isEmpty())
			{
				break;
			}
			
			cycle();
			processed++;
		}
		
		return processed;
	}
	
	/**
	 * Queue until the max processed is reached
	 * 
	 * @param max
	 *            the max
	 * @return the amount processed
	 */
	public int cycle(int max)
	{
		int processed = 0;
		
		for(int i = 0; i < max; i++)
		{
			if(queue.isEmpty())
			{
				break;
			}
			
			cycle();
			processed++;
		}
		
		return processed;
	}
	
	/**
	 * Cycle one object from the queue
	 */
	public void cycle()
	{
		if(queue.isEmpty())
		{
			return;
		}
		
		if(random)
		{
			T q = queue.get((int) (Math.random() * queue.size() - 1));
			queue.remove((int) (Math.random() * queue.size() - 1));
			q.onProcess();
		}
		
		else
		{
			queue.pop().onProcess();
		}
	}
	
	/**
	 * Does this queue allow duplicate entries
	 * 
	 * @return true if it does
	 */
	public boolean isAllowDuplicates()
	{
		return allowDuplicates;
	}
	
	/**
	 * Set if the queue should allow duplicate objects (will be processed twice)
	 * 
	 * @param allowDuplicates
	 *            set to true to allow duplicates
	 */
	public void setAllowDuplicates(boolean allowDuplicates)
	{
		this.allowDuplicates = allowDuplicates;
	}
	
	/**
	 * Does this queue pick a random queued object to process each cycle?
	 * 
	 * @return true if it does
	 */
	public boolean isRandom()
	{
		return random;
	}
	
	/**
	 * Set this queue to randomly pick the next object to process
	 * 
	 * @param random
	 *            set to true for random processing, set to false for sequential
	 *            processing (processes them in the order they are queued)
	 */
	public void setRandom(boolean random)
	{
		this.random = random;
	}
}
