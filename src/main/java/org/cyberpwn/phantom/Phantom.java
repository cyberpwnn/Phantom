package org.cyberpwn.phantom;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.cyberpwn.phantom.construct.PhantomPlugin;
import org.cyberpwn.phantom.test.TestController;

public class Phantom extends PhantomPlugin
{
	private static Phantom instance;
	private TestController testController;
	
	public void enable()
	{
		testController = new TestController(this);
		register(testController);
		instance = this;
	}
	
	public void disable()
	{
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equals("phantom"))
		{
			if(args.length > 0)
			{
				if(args[0].equalsIgnoreCase("test") || args[0].equalsIgnoreCase("t"))
				{
					if(args.length == 2)
					{
						testController.execute(sender, args[1]);
					}
					
					else
					{
						sender.sendMessage(ChatColor.GREEN + testController.getTests().k().toString(", "));
					}
				}
			}
		}
		
		return false;
	}
	
	public static Phantom instance()
	{
		return instance;
	}
}
