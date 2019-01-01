package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.stack.PhantomModule;

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
