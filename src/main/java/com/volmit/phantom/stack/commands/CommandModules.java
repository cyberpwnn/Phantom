package com.volmit.phantom.stack.commands;

import com.volmit.phantom.lang.F;
import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.Phantom;
import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.ModuleSVC;
import com.volmit.phantom.stack.PhantomModule;
import com.volmit.phantom.text.C;

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
