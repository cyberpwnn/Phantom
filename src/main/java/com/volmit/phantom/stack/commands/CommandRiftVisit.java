package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;

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
