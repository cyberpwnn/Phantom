package org.phantomapi.command;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;
import org.phantomapi.service.CommandService;
import org.phantomapi.service.PhantomTestService;

import phantom.command.Command;
import phantom.command.ICommand;
import phantom.command.PhantomCommand;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.test.IUnitTest;
import phantom.text.C;
import phantom.text.TXT;

@Command
@Singular
@Name("CMD test")
public class CommandTest extends PhantomCommand
{
	public CommandTest()
	{
		super("test");
		setPriority(1337);
		setPrameterUsage("[test] (opts)");
		addAlias("t");
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
		if(a.length == 0)
		{
			for(IUnitTest i : Phantom.getService(PhantomTestService.class).getTests())
			{
				ICommand cmdx = (ICommand) i;
				msg(sender, C.LIGHT_PURPLE + "/pha test " + C.WHITE + cmdx.getCommandName() + C.GRAY + " " + cmdx.getParameterUsage());
			}
		}

		else
		{
			String nm = a[0];
			String[] args = Phantom.getService(CommandService.class).getArgsFromPars(a);

			for(IUnitTest i : Phantom.getService(PhantomTestService.class).getTests())
			{
				ICommand cmdx = (ICommand) i;

				if(cmdx.getCommandName().equalsIgnoreCase(nm) || cmdx.getAliases().contains(nm.toLowerCase()))
				{
					msg(sender, "Running Unit Test: " + C.WHITE + i.getTestNames()[0]);
					cmdx.onCommand(sender, args);
				}
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
