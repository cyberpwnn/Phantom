package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.data.DataPack;
import org.phantomapi.lang.GMap;

@Ticked(100)
public class CacheController extends Controller
{
	private File cache;
	private GMap<String, DataCluster> caches;
	private boolean dirty;
	
	public CacheController(Controllable parentController)
	{
		super(parentController);
		
		dirty = false;
		cache = new File(Phantom.instance().getDataFolder(), "cache");
		cache.mkdirs();
		caches = new GMap<String, DataCluster>();
		
		try
		{
			loadCache();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadCache() throws IOException
	{
		for(File i : cache.listFiles())
		{
			DataPack d = new DataPack();
			DataCluster cc = new DataCluster();
			d.fromData(FileUtils.readFileToByteArray(i));
			cc.fromDataPack(d);
			caches.put(i.getName().split("\\.")[0], cc);
			w("Loaded Cache" + i.getPath());
		}
	}
	
	public void onTick()
	{
		if(dirty)
		{
			new A()
			{
				@Override
				public void async()
				{
					try
					{
						saveCache();
					}
					
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			};
		}
	}
	
	public void saveCache() throws IOException
	{
		for(String i : caches.k())
		{
			DataCluster cc = caches.get(i);
			DataPack d = cc.toDataPack();
			FileUtils.writeByteArrayToFile(new File(cache, i + ".ka"), d.toData());
			w("Saved Cache" + new File(cache, i + ".ka").getPath());
		}
	}
	
	public void update(String s, DataCluster cc)
	{
		caches.put(s, cc.copy());
		dirty = true;
	}

	@Override
	public void onStart()
	{
		
	}

	@Override
	public void onStop()
	{
		try
		{
			saveCache();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public DataCluster get(String name)
	{
		return caches.get(name);
	}
}
