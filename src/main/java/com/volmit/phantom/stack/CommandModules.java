package com.volmit.phantom.stack;

import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.ModuleSVC;
import com.volmit.phantom.text.C;

public class CommandModules extends PhantomCommand
{
	public CommandModules()
	{
		super("modules", "mods");
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		if(sender.isOp())
		{
			for(Module i : SVC.get(ModuleSVC.class).getLoadedModules())
			{
				sender.sendMessage(C.BOLD + "" + i.getColor() + i.getName() + " " + C.GRAY + i.getVersion() + " by " + C.WHITE + i.getAuthor());
			}
		}

		return true;
	}
}
