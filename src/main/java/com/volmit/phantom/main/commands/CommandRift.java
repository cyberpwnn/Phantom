package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.imp.plugin.Scaffold.Command;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.PhantomModule;
import com.volmit.phantom.util.text.C;

public class CommandRift extends PhantomCommand
{
	@Command
	public CommandRiftVisit visit;

	@Command
	public CommandRiftConstruct construct;

	@Command
	public CommandRiftDelete destroy;

	@Command
	public CommandRiftClose close;

	@Command
	public CommandRiftOpen open;

	@Command
	public CommandRiftSCM scm;

	public CommandRift()
	{
		super("rift", "rifts", "rft");
		requiresPermission(PhantomModule.perm.rift);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		for(Rift i : SVC.get(RiftSVC.class).getRifts())
		{
			sender.sendMessage(C.BOLD + i.getName().replaceAll("rifts/", "") + " " + (i.isLoaded() ? C.RED + "OPEN" : C.GRAY + "CLOSED"));
		}

		sender.sendMessage("/rift open/close/destroy/visit <rift>");

		return true;
	}
}
