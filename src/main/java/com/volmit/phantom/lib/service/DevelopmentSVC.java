package com.volmit.phantom.lib.service;

import java.io.File;

import com.google.common.io.Files;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.lang.Profiler;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.imp.plugin.Module;
import com.volmit.phantom.imp.plugin.SimpleService;
import com.volmit.phantom.util.text.C;

public class DevelopmentSVC extends SimpleService
{
	private AR ar;
	private File moduleFolder;
	private File moduleHotloadFolder;

	@Override
	public void onStart()
	{
		moduleFolder = SVC.get(ModuleSVC.class).getModuleFolder();
		moduleHotloadFolder = new File(moduleFolder, "inject");
		new AR(5)
		{
			@Override
			public void run()
			{
				tick();
			}
		};
	}

	private void tick()
	{
		try
		{
			for(File i : moduleHotloadFolder.listFiles())
			{
				for(File j : moduleFolder.listFiles())
				{
					if(i.getName().equals(j.getName()))
					{
						Module mod = null;

						for(Module k : SVC.get(ModuleSVC.class).getLoadedModules())
						{
							if(k.getModuleFile() != null && k.getModuleFile().equals(j))
							{
								mod = k;
								break;
							}
						}

						if(mod != null)
						{
							Module md = mod;

							new S()
							{
								@Override
								public void run()
								{
									SVC.get(ModuleSVC.class).unloadModule(md);
								}
							};
						}
					}
				}

				new S()
				{
					@Override
					public void run()
					{
						try
						{
							long lm = M.ms() - i.lastModified();
							Profiler o = new Profiler();
							o.begin();
							File ff = new File(moduleFolder, i.getName());
							Files.copy(i, ff);
							i.delete();
							D.as("Module Reconstructor").l("Injecting Module Changes for " + ff.getName());
							Module nm = SVC.get(ModuleSVC.class).loadModule(ff);
							o.end();
							D.as("Module Reconstructor").l("Reconstructed " + nm.getColor() + nm.getName() + C.WHITE + " in " + F.time(o.getMilliseconds() + lm, 1));
						}

						catch(Throwable e)
						{
							e.printStackTrace();
						}
					}
				};

				break;
			}
		}

		catch(Throwable e)
		{

		}
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
}
