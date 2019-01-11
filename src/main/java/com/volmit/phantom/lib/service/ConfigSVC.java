package com.volmit.phantom.lib.service;

import java.io.File;

import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.module.Module;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.util.text.C;

public class ConfigSVC implements IService
{
	private GMap<File, Module> moduleConfigs;
	private GMap<File, Long> modified;
	private AR ar;

	@Override
	public void onStart()
	{
		moduleConfigs = new GMap<>();
		modified = new GMap<>();
		ar = new AR(6)
		{
			@Override
			public void run()
			{
				update();
			}
		};
	}

	private void update()
	{
		for(File i : modified.k())
		{
			if(i.lastModified() != modified.get(i))
			{
				hotloadConfig(i);
				modified.put(i, i.lastModified());
				D.as("Configuration Service").l("Hotloaded " + i.getPath());
			}
		}
	}

	private void hotloadConfig(File i)
	{
		Module mod = moduleConfigs.get(i);
		mod.configModified(i.getName());
	}

	@Override
	public void onStop()
	{
		try
		{
			ar.cancel();
		}

		catch(Throwable e)
		{

		}
	}

	public void unregisterConfigs(Module m)
	{
		for(File i : moduleConfigs.k())
		{
			if(moduleConfigs.get(i).getName().equals(m.getName()))
			{
				moduleConfigs.remove(i);
				modified.remove(i);
				D.as("Configuration Service").l("Stopped Tracking " + i.getPath() + " -/> " + m.getColor() + m.getName() + C.GRAY);
			}
		}
	}

	public void registerConfigForHotload(File f, Module m)
	{
		moduleConfigs.put(f, m);
		modified.put(f, f.lastModified());
		D.as("Configuration Service").l("Tracking " + f.getPath() + " -> " + m.getColor() + m.getName() + C.GRAY);
	}
}
