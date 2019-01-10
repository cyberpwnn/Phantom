package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.imp.plugin.Module;
import com.volmit.phantom.lib.service.ModuleSVC;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomModule;
import com.volmit.phantom.util.text.C;

public class CommandModules extends PhantomCommand
{
	public CommandModules()
	{
		super("modules", "mods");
		requiresPermission(PhantomModule.perm.modules);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		for(Module i : SVC.get(ModuleSVC.class).getLoadedModules())
		{
			sender.sendMessage(C.BOLD + "" + i.getColor() + i.getName() + " " + C.GRAY + i.getVersion() + " by " + C.WHITE + i.getAuthor() + C.GRAY + " Classes: " + C.WHITE + F.f(Phantom.getModuleManager().getLoadedClassCount(i)) + C.GRAY + " Services: " + C.WHITE + Phantom.getRunningServices(i));
		}

		return true;
	}
}
