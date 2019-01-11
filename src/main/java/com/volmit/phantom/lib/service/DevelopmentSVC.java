package com.volmit.phantom.lib.service;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;
import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.lang.Profiler;
import com.volmit.phantom.api.service.AsyncTickService;
import com.volmit.phantom.main.Phantom;

public class DevelopmentSVC extends AsyncTickService
{
	private boolean busy;
	private File moduleFolder;
	private File moduleHotloadFolder;

	public DevelopmentSVC()
	{
		super(0);
	}

	@Override
	public void onBegin()
	{
		busy = false;
		moduleFolder = new File("modules");
		moduleHotloadFolder = new File(moduleFolder, "inject");
		moduleHotloadFolder.mkdirs();
		dump();
	}

	public void dump()
	{
		try
		{
			for(File i : moduleHotloadFolder.listFiles())
			{
				if(i.isFile() && i.getName().endsWith(".jar"))
				{
					for(File j : moduleFolder.listFiles())
					{
						if(j.getName().equals(i.getName()))
						{
							try
							{
								Files.copy(i, j);
								i.delete();
							}

							catch(IOException e)
							{
								e.printStackTrace();
							}
						}
					}

					w("Coldloaded Module " + i.getName());
				}
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onAsyncTick()
	{
		if(busy)
		{
			return;
		}

		try
		{
			for(File i : moduleHotloadFolder.listFiles())
			{
				if(i.isFile() && i.getName().endsWith(".jar"))
				{
					busy = true;
					Profiler px = new Profiler();
					px.begin();
					w("Hotloading Module " + i.getName());
					handleUnload(i.getName(), () ->
					{
						File jj = null;

						for(File j : moduleFolder.listFiles())
						{
							if(j.getName().equals(i.getName()))
							{
								try
								{
									Files.copy(i, j);
									jj = j;
									i.delete();
								}

								catch(IOException e)
								{
									e.printStackTrace();
								}
							}
						}

						px.end();
						busy = false;
						File jf = jj == null ? new File(moduleFolder, i.getName()) : jj;

						if(!jf.exists() && i.exists())
						{
							try
							{
								Files.copy(i, jf);
							}

							catch(IOException e)
							{
								e.printStackTrace();
							}

							i.delete();
						}

						J.a(() -> Phantom.getModuleManager().loadModule(jf));
						J.a(() -> w("Hotloaded Module " + i.getName() + " in " + F.time(px.getMillis(), 1)), 20);
					});
				}
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	private void handleUnload(String name, Runnable onUnload)
	{
		for(File i : moduleFolder.listFiles())
		{
			if(i.getName().equals(name))
			{
				if(Phantom.getModuleManager().hasModule(i))
				{
					Phantom.getModuleManager().unloadModule(Phantom.getModuleManager().getModule(i), onUnload);
					return;
				}
			}
		}

		onUnload.run();
	}

	@Override
	public void onEnd()
	{

	}
}
