package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomCommand;
import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.module.Module;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomModule;
import com.volmit.phantom.util.text.C;

public class CommandServices extends PhantomCommand
{
	public CommandServices()
	{
		super("services", "svcs", "svc", "service");
		requiresPermission(PhantomModule.perm.services);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		String t = sender.getTag();
		sender.setTag("");
		String filter = null;
		boolean showOffline = false;

		for(String i : args)
		{
			if(i.equalsIgnoreCase("-o") || i.equalsIgnoreCase("--offline"))
			{
				showOffline = true;
			}

			if(i.toLowerCase().startsWith("-m:") || i.toLowerCase().startsWith("--module:"))
			{
				filter = i.split(":")[1].trim();
			}
		}

		int m = 0;
		int v = 0;

		for(Module i : Phantom.getModuleManager().getModules())
		{
			if(filter != null && !i.getName().toLowerCase().contains(filter.toLowerCase()))
			{
				continue;
			}

			if(i.isNative())
			{
				continue;
			}

			boolean b = false;

			for(Class<? extends IService> j : i.getRegisteredServices())
			{
				if(showOffline || SVC.isRunning(j))
				{
					m++;
					b = true;
					sender.sendMessage(i.getTag((SVC.isRunning(j) ? C.GREEN : C.GRAY) + "" + C.BOLD + j.getSimpleName().replaceAll("SVC", " Service")) + " " + C.GRAY + "(" + j.getSuperclass().getSimpleName() + ")");
				}
			}

			if(b)
			{
				v++;
			}
		}

		sender.setTag(t);

		if(m == 0)
		{
			sender.sendMessage("No Services Found");
		}

		else
		{
			sender.sendMessage("Listing " + F.f(m) + " Service" + (m == 1 ? "" : "s") + " from " + F.f(v) + " Module" + (v == 1 ? "" : "s") + ".");
		}

		if(args.length == 0)
		{
			sender.sendMessage("Try /svc [-o|--offline] [-m:<mod>|--module:<mod>]");
		}

		return true;
	}

}
