package org.phantomapi.example;

import org.phantomapi.Phantom;
import org.phantomapi.command.CommandController;
import org.phantomapi.command.CommandFilter;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;
import org.phantomapi.util.C;

public class CommandHandler extends CommandController
{
	public CommandHandler(Controllable parentController, String commandName)
	{
		super(parentController, commandName);
		
		Phantom.instance().getCommandRegistryController().unregister((CommandListener) this);
		Phantom.instance().getCommandRegistryController().register((CommandListener) this);
	}
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.RED + "TAG" + C.DARK_GRAY + "]: ";
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.RED + "Plugin name? Version?";
	}
	
	@CommandFilter.PlayerOnly
	@CommandFilter.ArgumentRange({0, 1})
	@CommandFilter.Permission("plugin.god")
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand command)
	{
		//This will only fire if the following match
		/*
		 * The sender must be a player
		 * The sender must have from 0-1 arguments
		 * The sender must have the permission "plugin.god"
		 */
		
		return false;
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("example").qadd("set").qadd("of").qadd("aliases");
	}

	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
