package org.phantomapi.core.bukkit;

import java.io.File;

import org.cyberpwn.glog.L;
import org.phantomapi.core.CorePlugin;

public class PhantomBukkit extends BukkitPlugin
{
	public CorePlugin core;

	@Override
	public void onInit()
	{
		try
		{
			core = new CorePlugin(new File("plugins"));
			core.onInit();
		}

		catch(Throwable e)
		{
			L.l("Failed to start phantom core");
			e.printStackTrace();
		}
	}

	@Override
	public void onLoad()
	{
		core.onLoad();
	}

	@Override
	public void onEnable()
	{
		core.onEnable();
	}

	@Override
	public void onDisable()
	{
		core.onDisable();
	}
}
