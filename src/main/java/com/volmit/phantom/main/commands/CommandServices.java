package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomModule;
import com.volmit.phantom.util.text.C;

public class CommandServices extends PhantomCommand
{
	public CommandServices()
	{
		super("services", "svcs");
		requiresPermission(PhantomModule.perm.services);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		for(Class<? extends IService> i : Phantom.getRunningServices())
		{
			sender.sendMessage(C.BOLD + "" + C.WHITE + i.getSimpleName().replaceAll("SVC", "").replaceAll("Service", "") + " Service");
		}

		return true;
	}
}
