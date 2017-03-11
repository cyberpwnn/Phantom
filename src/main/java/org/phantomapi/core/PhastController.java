package org.phantomapi.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.script.ScriptException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controller;
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
public class PhastController extends Controller implements CommandExecutor
{
	private GList<PhastCommand> commands;
	
	public PhastController(org.phantomapi.construct.Controllable parentController)
	{
		super(parentController);
		
		commands = new GList<PhastCommand>();
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
		getPlugin().getCommand("phast").setExecutor(this);
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command comand, String na, String[] args)
	{
		if(args.length == 0)
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage(C.RED + "Console cannot hold books");
			}
			
			else
			{
				ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
				
				if(is != null && is.getType().equals(Material.WRITTEN_BOOK))
				{
					String eval = GBook.read(is).toString(" ").replaceAll("\n", "");
					
					try
					{
						Phast.evaluate(eval, new PhantomSender(sender));
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
			if(args.length == 1 && args[0].equalsIgnoreCase("help"))
			{
				for(PhastCommand i : commands)
				{
					sender.sendMessage(i.phastHelp());
				}
				
				sender.sendMessage(commands.size() + " Commands");
				
				return true;
			}
			
			String eval = new GList<String>(args).toString(" ");
			
			try
			{
				Phast.evaluate(eval, new PhantomSender(sender));
			}
			
			catch(ScriptException e)
			{
				sender.sendMessage(C.RED + e.getMessage());
			}
		}
		
		return true;
	}
}
