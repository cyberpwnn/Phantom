package com.volmit.phantom.api.registry;

import java.io.File;

import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.lib.service.HotloadService;

public class ConfigRegistry extends ModuleRegistry<File>
{
	public void register(File t, Runnable r)
	{
		registries.add(t);
		SVC.get(HotloadService.class).register(t, r);
	}

	@Override
	public boolean unregister(File t)
	{
		SVC.get(HotloadService.class).unregister(t);
		return registries.remove(t);
	}
}
