package com.volmit.phantom.stack.commands;

import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;

public class CommandRiftDelete extends PhantomCommand
{
	public CommandRiftDelete()
	{
		super("destroy", "delete");
		requiresPermission(PhantomModule.perm.rift.delete);
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
					i.destroy();
					sender.sendMessage("Rift destroyed.");
					break;
				}
			}
		}

		else
		{
			if(SVC.get(RiftSVC.class).isInRift(sender.player().getWorld()))
			{
				SVC.get(RiftSVC.class).getRift(sender.player().getWorld()).destroy();
			}

			else
			{
				sender.sendMessage("/rift destroy <rift> ... or be in a rift already.");
			}
		}

		return true;
	}
}
