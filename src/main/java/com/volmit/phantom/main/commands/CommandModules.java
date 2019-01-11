package com.volmit.phantom.main.commands;

import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.module.Module;
import com.volmit.phantom.imp.command.PhantomCommand;
import com.volmit.phantom.main.Phantom;
import com.volmit.phantom.main.PhantomModule;
import com.volmit.phantom.util.text.C;

public class CommandModules extends PhantomCommand
{
	public CommandModules()
	{
		super("modules", "mods", "module", "mod");
		requiresPermission(PhantomModule.perm.modules);
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		String t = sender.getTag();
		sender.setTag("");

		for(Module i : Phantom.getModuleManager().getModules())
		{
			sender.sendMessage(i.getTag("Info") + "Version: " + C.WHITE + i.getVersion() + C.GRAY + " by " + C.WHITE + i.getAuthors() + " " + (!i.isNative() ? "" : C.RED + "" + C.BOLD + "NATIVE MODULE"));
		}

		sender.setTag(t);
		return true;
	}

}
