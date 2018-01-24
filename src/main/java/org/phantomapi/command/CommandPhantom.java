package org.phantomapi.command;

import org.bukkit.command.CommandSender;

import phantom.command.Command;
import phantom.command.ICommand;
import phantom.command.PhantomCommand;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.text.C;
import phantom.text.TXT;

@Command
@Singular
@Name("CMD phantom")
public class CommandPhantom extends PhantomCommand
{
	public CommandPhantom()
	{
		super("phantom");
		setPriority(1337);
		setPrameterUsage("[sub] (opts)");
		addAlias("pha");
	}

	@Start
	public void onStart()
	{
		activateSubCommand(new CommandTest());
		activateSubCommand(new CommandKeychain());
		activateSubCommand(new CommandStatus());
	}

	@Stop
	public void onStop()
	{
		deactivateSubCommands();
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] a)
	{
		if(a.length == 0)
		{
			for(ICommand i : getSubCommandsByPriority())
			{
				msg(sender, C.LIGHT_PURPLE + "/pha " + C.WHITE + i.getCommandName() + C.GRAY + " " + i.getParameterUsage());
			}
		}

		else if(!fireSubCommand(sender, a))
		{
			msg(sender, "Unknown sub command: " + C.WHITE + a[0]);
		}

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom");
	}
}
