package org.phantomapi.command;

import org.bukkit.command.CommandSender;

import phantom.command.Command;
import phantom.command.PhantomCommand;
import phantom.pawn.Name;
import phantom.pawn.Singular;
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

	@Override
	public boolean onCommand(CommandSender sender, String[] a)
	{
		msg(sender, "Oi");

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom");
	}
}
