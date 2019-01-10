package com.volmit.phantom.lib.service;

import java.io.File;
import java.io.IOException;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.imp.plugin.Module;
import com.volmit.phantom.imp.plugin.Phantom;
import com.volmit.phantom.imp.plugin.SimpleService;

public class ModuleSVC extends SimpleService
{
	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public GList<Module> getLoadedModules()
	{
		return Phantom.getModuleManager().getModules();
	}

	public File getModuleFolder()
	{
		return Phantom.getModuleManager().getDataFolder();
	}

	public boolean deleteModule(Module m)
	{
		try
		{
			File f = m.getModuleFile();
			Phantom.getModuleManager().unloadModule(m);
			return f.delete();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public Module loadModule(File f)
	{
		try
		{
			return Phantom.getModuleManager().startModuleNow(Phantom.getModuleManager().loadModule(f));
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void reloadModule(Module md)
	{
		new S()
		{
			@Override
			public void run()
			{
				try
				{
					Phantom.getModuleManager().reinjectModule(md);
				}

				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		};
	}

	public boolean unloadModule(Module mod)
	{
		try
		{
			Phantom.getModuleManager().unloadModule(mod, true);
			return true;
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}

		return false;
	}
}
