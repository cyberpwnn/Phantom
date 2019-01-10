package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.PhantomModule;

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
