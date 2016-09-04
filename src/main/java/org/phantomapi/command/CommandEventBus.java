package org.phantomapi.command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.phantomapi.CommandRegistryController;
import org.phantomapi.Phantom;
import org.phantomapi.command.CommandFilter.ArgumentRange;
import org.phantomapi.command.CommandFilter.ConsoleOnly;
import org.phantomapi.command.CommandFilter.OperatorOnly;
import org.phantomapi.command.CommandFilter.Permission;
import org.phantomapi.command.CommandFilter.Permissions;
import org.phantomapi.command.CommandFilter.PlayerOnly;
import org.phantomapi.command.CommandFilter.SubCommands;
import org.phantomapi.command.CommandFilter.Tag;
import org.phantomapi.command.CommandFilter.TagHover;
import org.phantomapi.util.C;
import org.phantomapi.util.M;

public class CommandEventBus
{
	private String root;
	private String[] args;
	private CommandSender sender;
	private CommandResult result;
	
	public CommandEventBus(CommandRegistryController c, String root, String[] args, CommandSender sender)
	{
		this.root = root;
		this.args = args;
		this.sender = sender;
		this.result = CommandResult.NO_HANDLE;
		
		if(!c.getCommandableEvents().containsKey(root.toLowerCase()))
		{
			return;
		}
		
		for(Method i : c.getCommandableEvents().get(root.toLowerCase()))
		{
			if(hasFilter(i, ArgumentRange.class))
			{
				ArgumentRange ar = (ArgumentRange) getFilter(i, ArgumentRange.class);
				
				if(!M.within(M.min(ar.value()), M.max(ar.value()), args.length))
				{
					continue;
				}
			}
			
			if(hasFilter(i, ConsoleOnly.class))
			{
				if(sender instanceof Player)
				{
					continue;
				}
			}
			
			if(hasFilter(i, PlayerOnly.class))
			{
				if(!(sender instanceof Player))
				{
					continue;
				}
			}
			
			if(hasFilter(i, OperatorOnly.class))
			{
				if(!sender.isOp())
				{
					continue;
				}
			}
			
			if(hasFilter(i, Permission.class))
			{
				Permission p = (Permission) getFilter(i, Permission.class);
				
				if(!sender.hasPermission(p.value()))
				{
					continue;
				}
			}
			
			if(hasFilter(i, Permissions.class))
			{
				Permissions p = (Permissions) getFilter(i, Permissions.class);
				
				boolean f = false;
				
				for(String j : p.value())
				{
					if(!sender.hasPermission(j))
					{
						f = true;
						break;
					}
				}
				
				if(f)
				{
					continue;
				}
			}
			
			if(hasFilter(i, SubCommands.class))
			{
				SubCommands s = (SubCommands) getFilter(i, SubCommands.class);
				
				if(s.value().length > args.length)
				{
					continue;
				}
				
				boolean f = false;
				
				for(int j = 0; j < s.value().length; j++)
				{
					if(!s.value()[j].equalsIgnoreCase(args[j]))
					{
						f = true;
						break;
					}
				}
				
				if(f)
				{
					continue;
				}
			}
			
			try
			{
				PhantomCommandSender ps = new PhantomSender(sender);
				PhantomCommand cmd = new PhantomCommand(root, args);
				
				if(hasFilter(i, Tag.class))
				{
					ps.getMessageBuilder().setTag(((Tag) getFilter(i, Tag.class)).value());
				}
				
				if(hasFilter(i, TagHover.class))
				{
					ps.getMessageBuilder().setHoverText(((TagHover) getFilter(i, TagHover.class)).value());
				}
				
				i.invoke(Phantom.instance().getInstance(i.getDeclaringClass()), ps, cmd);
				result = CommandResult.HANDLED;
				return;
			}
			
			catch(Exception e)
			{
				result = CommandResult.HANDLED;
				e.printStackTrace();
				sender.sendMessage(C.RED + "#Boo" + e.getClass().getSimpleName() + "s");
			}
		}
	}
	
	public Annotation getFilter(Method l, Class<? extends Annotation> clazz)
	{
		try
		{
			return l.getDeclaredAnnotation(clazz);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean hasFilter(Method l, Class<? extends Annotation> clazz)
	{
		try
		{
			return l.isAnnotationPresent(clazz) && l.getParameterTypes().equals(new Class[] {PhantomSender.class, PhantomCommand.class});
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public CommandResult getResult()
	{
		return result;
	}
	
	public String getRoot()
	{
		return root;
	}
	
	public void setRoot(String root)
	{
		this.root = root;
	}
	
	public String[] getArgs()
	{
		return args;
	}
	
	public void setArgs(String[] args)
	{
		this.args = args;
	}
	
	public CommandSender getSender()
	{
		return sender;
	}
	
	public void setSender(CommandSender sender)
	{
		this.sender = sender;
	}
}
