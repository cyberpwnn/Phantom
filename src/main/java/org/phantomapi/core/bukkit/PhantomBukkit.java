package org.phantomapi.core.bukkit;

import java.io.File;

import org.bukkit.Bukkit;
import org.cyberpwn.gconcurrent.TICK;
import org.cyberpwn.glog.L;
import org.phantomapi.core.CorePlugin;

import phantom.util.threads.AsyncTickThread;

public class PhantomBukkit extends BukkitPlugin
{
	public CorePlugin core;
	public AsyncTickThread asyn;
	public int tid;

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

		asyn = new AsyncTickThread(new Runnable()
		{
			@Override
			public void run()
			{
				core.onTickAsync();
				TICK.atick++;
			}
		});

		tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{

			@Override
			public void run()
			{
				core.onTickSync();
				TICK.tick++;
			}
		}, 0, 0);
	}

	@Override
	public void onDisable()
	{
		asyn.interrupt();
		core.onDisable();
	}
}
