package org.phantomapi.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.script.ScriptException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.command.CommandController;
import org.phantomapi.command.CommandFilter;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.lang.GList;
import org.phantomapi.phast.Phast;
import org.phantomapi.phast.PhastAlertNode;
import org.phantomapi.phast.PhastCNode;
import org.phantomapi.phast.PhastCommand;
import org.phantomapi.phast.PhastDisableNode;
import org.phantomapi.phast.PhastEnableNode;
import org.phantomapi.phast.PhastInstallNode;
import org.phantomapi.phast.PhastLoadNode;
import org.phantomapi.phast.PhastReloadNode;
import org.phantomapi.phast.PhastResetNode;
import org.phantomapi.phast.PhastThrashNode;
import org.phantomapi.phast.PhastTitleNode;
import org.phantomapi.phast.PhastUnloadNode;
import org.phantomapi.phast.PhastWaitNode;
import org.phantomapi.sync.TaskLater;
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
		registerPhast(new PhastInstallNode());
		
		new TaskLater(20)
		{
			@Override
			public void run()
			{
				File f = new File(getPlugin().getDataFolder(), "startup.txt");
				File f2 = new File(getPlugin().getDataFolder(), "initial.txt");
				File s = new File(getPlugin().getDataFolder(), "scripts");
				
				if(!s.exists())
				{
					s.mkdirs();
				}
				
				if(!f.exists())
				{
					try
					{
						f.createNewFile();
					}
					
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				
				if(!f2.exists())
				{
					try
					{
						f2.createNewFile();
					}
					
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				
				try
				{
					BufferedReader bu = new BufferedReader(new FileReader(f));
					String line;
					GList<String> files = new GList<String>();
					
					while((line = bu.readLine()) != null)
					{
						files.add(line);
					}
					
					bu.close();
					
					for(String i : files)
					{
						for(File j : new File(getPlugin().getDataFolder(), "scripts").listFiles())
						{
							if(j.getName().equals(i))
							{
								execute(j);
								break;
							}
						}
					}
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				try
				{
					BufferedReader bu = new BufferedReader(new FileReader(f2));
					String line;
					GList<String> files = new GList<String>();
					
					while((line = bu.readLine()) != null)
					{
						files.add(line);
					}
					
					bu.close();
					
					for(String i : files)
					{
						for(File j : new File(getPlugin().getDataFolder(), "scripts").listFiles())
						{
							if(j.getName().equals(i))
							{
								execute(j);
								break;
							}
						}
					}
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				f2.delete();
				
				try
				{
					f2.createNewFile();
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		};
	}
	
	public void execute(File file)
	{
		try
		{
			BufferedReader bu = new BufferedReader(new FileReader(file));
			String line;
			String k = "";
			
			while((line = bu.readLine()) != null)
			{
				k = k + line;
			}
			
			bu.close();
			v("Executing Script" + file.getName());
			Phast.evaluate(k, new PhantomSender(Bukkit.getConsoleSender()));
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
}
