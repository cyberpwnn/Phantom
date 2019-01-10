package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.imp.plugin.PhantomCommand;
import com.volmit.phantom.main.PhantomModule;

public class CommandPhantom extends PhantomCommand
{
	public CommandPhantom()
	{
		super("phantom", "pha");
		requiresPermission(PhantomModule.perm);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		sender.sendMessage("Hello World");

		return true;
	}
}
