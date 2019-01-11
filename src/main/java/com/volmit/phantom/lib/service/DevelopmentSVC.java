package com.volmit.phantom.lib.service;

import java.io.File;

import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.api.sheduler.AR;
import com.volmit.phantom.main.Phantom;

public class DevelopmentSVC implements IService
{
	private AR ar;
	private File moduleFolder;
	private File moduleHotloadFolder;

	@Override
	public void onStart()
	{
		moduleFolder = Phantom.getModuleManager().getModuleFolder();
		moduleHotloadFolder = new File(moduleFolder, "inject");
		moduleHotloadFolder.mkdirs();
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
