package com.volmit.phantom.api.registry;

import java.io.File;

import com.volmit.phantom.api.command.ICommand;

public class CommandRegistry extends ModuleRegistry<ICommand>
{
	public void register(File t, Runnable r)
	{
		registries.add(t);
	}

	@Override
	public boolean unregister(File t)
	{
		return registries.remove(t);
	}
}
