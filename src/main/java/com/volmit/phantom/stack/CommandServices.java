package com.volmit.phantom.stack;

import com.volmit.phantom.plugin.IService;
import com.volmit.phantom.plugin.Phantom;
import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.text.C;

public class CommandServices extends PhantomCommand
{
	public CommandServices()
	{
		super("services", "svcs");
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		if(sender.isOp())
		{
			for(Class<? extends IService> i : Phantom.getRunningServices())
			{
				sender.sendMessage(C.BOLD + "" + C.WHITE + i.getSimpleName().replaceAll("SVC", "").replaceAll("Service", "") + " Service");
			}
		}

		return true;
	}
}
