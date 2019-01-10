package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.PhantomModule;

public class CommandRiftOpen extends PhantomCommand
{
	public CommandRiftOpen()
	{
		super("open", "load");
		requiresPermission(PhantomModule.perm.rift.open);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		if(args.length == 1)
		{
			for(Rift i : SVC.get(RiftSVC.class).getRifts())
			{
				if(i.getName().replaceAll("rifts/", "").equalsIgnoreCase(args[0]))
				{
					if(!i.isLoaded())
					{
						sender.sendMessage("Rift opened.");
						i.load().send(sender.player());
					}

					else
					{
						sender.sendMessage("Rift is already open.");
					}

					break;
				}
			}
		}

		else
		{
			sender.sendMessage("/rift visit <riftname>");
		}

		return true;
	}
}
