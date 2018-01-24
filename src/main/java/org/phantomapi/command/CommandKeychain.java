package org.phantomapi.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;
import org.phantomapi.service.DeveloperSVC;

import phantom.command.Command;
import phantom.command.PhantomCommand;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.text.C;
import phantom.text.TXT;

@Command
@Singular
@Name("CMD keychain")
public class CommandKeychain extends PhantomCommand
{
	public CommandKeychain()
	{
		super("keychain");
		setPriority(1337);
		setPrameterUsage("[name] [payload-number]");
		addAlias("kchain");
		addAlias("kc");
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
		if(!sender.equals(Bukkit.getConsoleSender()))
		{
			msg(sender, C.RED + "Console only");
			return true;
		}

		if(a.length == 2)
		{
			String s = a[0];

			try
			{
				Integer n = Integer.valueOf(a[1]);
				Phantom.getService(DeveloperSVC.class).addDeveloper(s, n);

				return true;
			}

			catch(NumberFormatException e)
			{
				msg(sender, C.RED + "Invalid parameters: " + getParameterUsage());
			}

			catch(Exception e)
			{
				msg(sender, C.RED + "FAILED");
				e.printStackTrace();
			}
		}

		else
		{
			msg(sender, C.RED + "Invalid parameters: " + getParameterUsage());
		}

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom");
	}
}
