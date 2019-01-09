package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;

public class CommandRiftConstruct extends PhantomCommand
{
	public CommandRiftConstruct()
	{
		super("construct", "new", "create");
		requiresPermission(PhantomModule.perm.rift.create);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		SVC.get(RiftSVC.class).openTemporaryRift(sender.getName()).load().send(sender.player());
		return true;
	}
}
