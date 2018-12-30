package com.volmit.phantom.stack;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;

public class CommandPhantom extends PhantomCommand
{
	public CommandPhantom()
	{
		super("phantom", "pha");
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		sender.sendMessage("Hello World");

		return true;
	}
}
