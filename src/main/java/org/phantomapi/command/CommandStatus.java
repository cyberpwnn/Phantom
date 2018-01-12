package org.phantomapi.command;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;

import phantom.command.Command;
import phantom.command.PhantomCommand;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.text.C;
import phantom.text.TXT;

@Command
@Singular
@Name("CMD status")
public class CommandStatus extends PhantomCommand
{
	public CommandStatus()
	{
		super("status");
		setPriority(1337);
		setPrameterUsage("");
		addAlias("s");
		addAlias("stat");
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
		sender.sendMessage(TXT.line(C.DARK_GRAY, C.LIGHT_PURPLE + "Deployed Services"));

		for(IService i : Phantom.getAPI().getServiceProvider().getDeployedServices())
		{
			String name = i.getClass().getDeclaredAnnotation(Name.class).value();
			sender.sendMessage(C.GREEN + "DEPLOYED " + C.GRAY + name);
		}

		sender.sendMessage(TXT.line(C.DARK_GRAY, C.LIGHT_PURPLE + "Dormant Services"));

		for(IService i : Phantom.getAPI().getServiceProvider().getDormantServices())
		{
			String name = i.getClass().getDeclaredAnnotation(Name.class).value();
			sender.sendMessage(C.DARK_GRAY + "DORMANT " + C.GRAY + name);
		}

		sender.sendMessage(TXT.line(C.DARK_GRAY, C.LIGHT_PURPLE + "Pawn Space"));

		for(String i : Phantom.getPawnSpace().printSingularitySpace(Phantom.getAPI()))
		{
			String cx = "";

			if(i.contains("SVC"))
			{
				cx = C.UNDERLINE + "" + C.GREEN;
			}

			else if(i.contains("CMD"))
			{
				cx = C.UNDERLINE + "" + C.AQUA;
			}
			
			else if(i.contains("LINK"))
			{
				cx = C.UNDERLINE + "" + C.RED;
			}
			
			else if(i.contains("THREAD"))
			{
				cx = C.UNDERLINE + "" + C.LIGHT_PURPLE;
			}

			else if(i.contains("NMS"))
			{
				cx = C.UNDERLINE + "" + C.YELLOW;
			}

			sender.sendMessage(C.DARK_GRAY + cx + i);
		}

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom");
	}
}
