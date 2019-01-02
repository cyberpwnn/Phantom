package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.Scaffold.Command;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;
import com.volmit.phantom.text.C;

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
