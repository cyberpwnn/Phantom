package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.PhantomModule;

public class CommandRiftVisit extends PhantomCommand
{
	public CommandRiftVisit()
	{
		super("visit", "goto", "tp");
		requiresPermission(PhantomModule.perm.rift.visit);
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
					if(i.isLoaded())
					{
						i.send(sender.player());
					}

					else
					{
						sender.sendMessage("Rift is not open. /rift open <rift>");
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
