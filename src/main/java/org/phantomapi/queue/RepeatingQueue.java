package org.phantomapi.queue;

import java.util.Collection;
import org.phantomapi.lang.GList;

/**
 * Repeating queue
 * 
 * @author cyberpwn
 * @param <T>
 *            the queued type
 */
public class RepeatingQueue<T extends Queued> extends BaseQueue<T>
{
	private GList<T> holds;
	
	public RepeatingQueue()
	{
		super();
		
		holds = new GList<T>();
	}
	
	public void reset()
	{
		queue.clear();
		queue.add(holds.copy());
	}
	
	@Override
	public void queue(Collection<T> queued)
	{
		for(T i : queued)
		{
			queue(i);
		}
	}
	
	@Override
	public void queue(T queued)
	{
		if(!allowDuplicates && holds.contains(queued))
		{
			holds.add(queued);
		}
		
		super.queue(queued);
	}
	
	@Override
	public boolean isEmpty()
	{
		return holds.isEmpty();
	}
	
	@Override
	public void dequeue(T queued)
	{
		holds.remove(queued);
	}
	
	@Override
	public void dequeue()
	{
		holds.clear();
	}
	
	@Override
	public void cycle()
	{
		super.cycle();
		
		if(queue.isEmpty())
		{
			reset();
		}
	}
}
