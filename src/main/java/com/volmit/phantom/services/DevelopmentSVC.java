package com.volmit.phantom.services;

import java.io.File;

import com.google.common.io.Files;
import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.Profiler;
import com.volmit.phantom.plugin.AR;
import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.S;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.SimpleService;
import com.volmit.phantom.text.C;
import com.volmit.phantom.time.M;

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
			e.printStackTrace();
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
