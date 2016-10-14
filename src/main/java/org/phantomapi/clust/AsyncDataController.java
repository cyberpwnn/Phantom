package org.phantomapi.clust;

import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;

/**
 * A Data Controller
 * 
 * @author cyberpwn
 * @param <T>
 *            the configurable object
 * @param <V>
 *            the identifier object
 */
public abstract class AsyncDataController<T extends AsyncConfigurable, V> extends Controller implements AsyncMultiDataHandler<T, V>
{
	protected GMap<V, T> cache;
	
	public AsyncDataController(Controllable parentController)
	{
		super(parentController);
		
		cache = new GMap<V, T>();
	}
	
	@Override
	public boolean checkLoad(V identifier)
	{
		if(contains(identifier))
		{
			return cache.get(identifier).isLoaded();
		}
		
		return false;
	}
	
	@Override
	public boolean isLoading(V identifier)
	{
		if(contains(identifier))
		{
			return !checkLoad(identifier);
		}
		
		return false;
	}
	
	@Override
	public boolean isLoaded(V identifier)
	{
		return cache.containsKey(identifier) && !isLoading(identifier);
	}
	
	@Override
	public T get(V identifier) throws ClustAsyncAlreadyLoadingException
	{
		if(!contains(identifier))
		{
			load(identifier);
		}
		
		else if(isLoading(identifier))
		{
			throw new ClustAsyncAlreadyLoadingException();
		}
		
		return cache.get(identifier);
	}
	
	@Override
	public void load(V identifier)
	{
		if(!contains(identifier))
		{
			cache.put(identifier, onLoad(identifier));
		}
	}
	
	@Override
	public void save(V identifier) throws ClustAsyncAlreadyLoadingException
	{
		if(contains(identifier))
		{
			if(isLoading(identifier))
			{
				throw new ClustAsyncAlreadyLoadingException();
			}
			
			onSave(identifier);
			cache.remove(identifier);
		}
	}
	
	@Override
	public abstract T onLoad(V identifier);
	
	@Override
	public abstract void onSave(V identifier);
	
	@Override
	public void saveAll()
	{
		for(V i : cache.k())
		{
			try
			{
				save(i);
			}
			
			catch(ClustAsyncAlreadyLoadingException e)
			{
				Phantom.instance().getDms().f("Cannot Save Cluster (STILL LOADING) " + i.toString() + " @ " + this.getClass().getSimpleName());
			}
		}
		
		cache.clear();
	}
	
	@Override
	public boolean contains(V identifier)
	{
		return cache.contains(identifier);
	}
	
	@Override
	public GMap<V, T> getCache()
	{
		return cache.copy();
	}
}
