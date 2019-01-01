package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;

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
