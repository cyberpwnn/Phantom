package org.phantomapi.command;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;

import phantom.command.Command;
import phantom.command.PhantomCommand;
import phantom.lang.GList;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.text.C;
import phantom.text.TXT;

@Command
@Singular
@Name("CMD console")
public class CommandConsole extends PhantomCommand
{
	public CommandConsole()
	{
		super("console");
		setPriority(1337);
		setPrameterUsage("[command]");
		addAlias("con");
		addAlias("cli");
	}

	@Start
	public void onStart()
	{

	}

	@Stop
	public void onStop()
	{

	}

	@Override
	public boolean onCommand(CommandSender sender, String[] a)
	{
		if(sender instanceof Player)
		{
			UUID uid = ((Player) sender).getUniqueId();
			File folder = new File(Phantom.getDataFolder(), "cli");
			File mf = new File(folder, uid.toString());

			if(mf.exists() && mf.isDirectory())
			{
				String cmd = new GList<String>(a).toString(" ");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
				msg(sender, C.LIGHT_PURPLE + "Command Executed: " + C.GRAY + cmd);
			}

			else
			{
				msg(sender, C.RED + "Unauthorized");
			}
		}

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom");
	}
}
