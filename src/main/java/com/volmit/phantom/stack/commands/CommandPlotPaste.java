package com.volmit.phantom.stack.commands;

import java.io.File;
import java.io.IOException;

import com.sk89q.worldedit.data.DataException;
import com.volmit.phantom.plugin.PhantomCommand;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.PlotSquaredSVC;

@SuppressWarnings("deprecation")
public class CommandPlotPaste extends PhantomCommand
{
	public CommandPlotPaste()
	{
		super("plotpaste", "ppaste");
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		if(args.length == 1)
		{
			File f = new File("plugins/WorldEdit/schematics/" + sender.player().getUniqueId().toString() + "/" + args[0] + ".schematic");

			if(f.exists())
			{
				try
				{
					SVC.get(PlotSquaredSVC.class).autoClaimPaste(sender, f);
				}

				catch(DataException | IOException e)
				{
					e.printStackTrace();
				}
			}

			else
			{
				sender.sendMessage("Cant find schematic: " + f.getPath());
			}
		}

		else
		{
			sender.sendMessage("/plotpaste <schematic> -- While in a plotworld.");
		}

		return true;
	}
}
