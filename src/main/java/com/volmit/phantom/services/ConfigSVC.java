package com.volmit.phantom.services;

import java.io.File;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.plugin.AR;
import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.SimpleService;
import com.volmit.phantom.text.C;

public class ConfigSVC extends SimpleService
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
		mod.getStructure().configModified(i.getName());
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
