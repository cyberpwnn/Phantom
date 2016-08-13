package org.phantomapi.clust;

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
public abstract class DataController<T extends Configurable, V> extends Controller implements MultiDataHandler<T, V>
{
	protected GMap<V, T> cache;
	
	public DataController(Controllable parentController)
	{
		super(parentController);
		
		cache = new GMap<V, T>();
	}
	
	@Override
	public T get(V identifier)
	{
		if(!contains(identifier))
		{
			load(identifier);
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
	public void save(V identifier)
	{
		if(contains(identifier))
		{
			onSave(identifier);
			cache.remove(identifier);
		}
	}
	
	@Override
	public abstract T onLoad(V identifier);
	
	@Override
	public abstract void onSave(V identifier);
	
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
