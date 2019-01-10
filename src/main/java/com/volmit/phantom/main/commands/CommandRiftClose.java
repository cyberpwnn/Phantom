package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.PhantomModule;

public class CommandRiftClose extends PhantomCommand
{
	public CommandRiftClose()
	{
		super("close", "unload");
		requiresPermission(PhantomModule.perm.rift.close);
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
						sender.sendMessage("Rift closed.");
						i.unload();
					}

					else
					{
						sender.sendMessage("Rift isnt open.");
					}

					break;
				}
			}
		}

		else
		{
			if(SVC.get(RiftSVC.class).isInRift(sender.player().getWorld()))
			{
				SVC.get(RiftSVC.class).getRift(sender.player().getWorld()).unload();
			}

			else
			{
				sender.sendMessage("/rift close <rift> ... or be in a rift already.");
			}
		}

		return true;
	}
}
