package org.phantomapi.core;

import javax.script.ScriptException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.command.CommandController;
import org.phantomapi.command.CommandFilter;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.lang.GList;
import org.phantomapi.phast.Phast;
import org.phantomapi.phast.PhastAlertNode;
import org.phantomapi.phast.PhastCNode;
import org.phantomapi.phast.PhastCommand;
import org.phantomapi.phast.PhastDisableNode;
import org.phantomapi.phast.PhastEnableNode;
import org.phantomapi.phast.PhastLoadNode;
import org.phantomapi.phast.PhastReloadNode;
import org.phantomapi.phast.PhastResetNode;
import org.phantomapi.phast.PhastThrashNode;
import org.phantomapi.phast.PhastTitleNode;
import org.phantomapi.phast.PhastUnloadNode;
import org.phantomapi.phast.PhastWaitNode;
import org.phantomapi.text.GBook;
import org.phantomapi.util.C;

/**
 * Phast programming language
 * 
 * @author cyberpwn
 */
public class PhastController extends CommandController
{
	private GList<PhastCommand> commands;
	
	public PhastController(org.phantomapi.construct.Controllable parentController)
	{
		super(parentController, "phast");
		
		commands = new GList<PhastCommand>();
	}
	
	@CommandFilter.Permission("phantom.phast")
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand command)
	{
		if(command.getArgs().length == 0)
		{
			if(sender.isConsole())
			{
				sender.sendMessage(C.RED + "Console cannot hold books");
			}
			
			else
			{
				ItemStack is = sender.getPlayer().getItemInHand();
				
				if(is != null && is.getType().equals(Material.WRITTEN_BOOK))
				{
					String eval = GBook.read(is).toString(" ").replaceAll("\n", "");
					
					try
					{
						Phast.evaluate(eval, sender);
					}
					
					catch(ScriptException e)
					{
						sender.sendMessage(C.RED + e.getMessage());
					}
				}
			}
		}
		
		else
		{
			if(command.getArgs().length == 1 && command.getArgs()[0].equalsIgnoreCase("help"))
			{
				for(PhastCommand i : commands)
				{
					sender.sendMessage(i.phastHelp());
				}
				
				sender.sendMessage(commands.size() + " Commands");
				
				return true;
			}
			
			String eval = new GList<String>(command.getArgs()).toString(" ");
			
			try
			{
				Phast.evaluate(eval, sender);
			}
			
			catch(ScriptException e)
			{
				sender.sendMessage(C.RED + e.getMessage());
			}
		}
		
		return true;
	}
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phast" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.LIGHT_PURPLE + "Phast is a light weight scripting language.";
	}
	
	public void registerPhast(PhastCommand cmd)
	{
		commands.add(cmd);
	}
	
	public void unRegisterPhast(PhastCommand cmd)
	{
		commands.add(cmd);
	}
	
	public void handle(String command, String[] args)
	{
		for(PhastCommand i : commands)
		{
			i.phast(command, args);
		}
	}
	
	@Override
	public void onStart()
	{
		registerPhast(new PhastLoadNode());
		registerPhast(new PhastUnloadNode());
		registerPhast(new PhastEnableNode());
		registerPhast(new PhastDisableNode());
		registerPhast(new PhastResetNode());
		registerPhast(new PhastReloadNode());
		registerPhast(new PhastThrashNode());
		registerPhast(new PhastCNode());
		registerPhast(new PhastTitleNode());
		registerPhast(new PhastAlertNode());
		registerPhast(new PhastWaitNode());
	}
	
	@Override
	public void onStop()
	{
		
	}
}
