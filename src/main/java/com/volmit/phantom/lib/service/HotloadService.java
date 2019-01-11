package com.volmit.phantom.lib.service;

import java.io.File;

import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.service.AsyncTickService;

public class HotloadService extends AsyncTickService
{
	private GMap<File, Runnable> tracked;
	private GMap<File, Long> trackedFiles;

	public HotloadService()
	{
		super(20);
	}

	@Override
	public void onBegin()
	{
		tracked = new GMap<>();
		trackedFiles = new GMap<>();
	}

	public void register(File f, Runnable r)
	{
		tracked.put(f, r);
		trackedFiles.put(f, f.lastModified());
		l("Registered Hotloadable Configuration: " + f.getPath());
	}

	public void unregister(File f)
	{
		trackedFiles.remove(f);
		tracked.remove(f);
		l("Unregistered Hotloadable Configuration: " + f.getPath());
	}

	@Override
	public void onEnd()
	{

	}

	@Override
	public void onAsyncTick()
	{
		for(File i : trackedFiles.k())
		{
			if(!trackedFiles.containsKey(i))
			{
				continue;
			}

			if(!tracked.containsKey(i))
			{
				continue;
			}

			long last = trackedFiles.get(i);

			if(last != i.lastModified())
			{
				try
				{
					tracked.get(i).run();
					l("Hotloaded config: " + i.getPath());
				}

				catch(Throwable e)
				{
					e.printStackTrace();
					w("Failed to hotload config: " + i.getPath());
				}

				if(trackedFiles.containsKey(i))
				{
					trackedFiles.put(i, i.lastModified());
				}
			}
		}
	}
}
