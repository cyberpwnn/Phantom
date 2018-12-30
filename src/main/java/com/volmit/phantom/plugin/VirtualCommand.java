package com.volmit.phantom.plugin;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;

import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.V;
import com.volmit.phantom.plugin.Scaffold.Command;

/**
 * Represents a virtual command. A chain of iterative processing through
 * subcommands.
 *
 * @author cyberpwn
 *
 */
public class VirtualCommand
{
	private ICommand command;
	private String tag;
	private Module module;

	private GMap<GList<String>, VirtualCommand> children;

	public VirtualCommand(Module module, ICommand command)
	{
		this(module, command, "");
	}

	public VirtualCommand(Module module, ICommand command, String tag)
	{
		this.module = module;
		this.command = command;
		children = new GMap<GList<String>, VirtualCommand>();
		this.tag = tag;

		for(Field i : command.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Command.class))
			{
				try
				{
					Command cc = i.getAnnotation(Command.class);
					ICommand cmd = (ICommand) i.getType().getConstructor().newInstance();
					new V(command, true, true).set(i.getName(), cmd);
					children.put(cmd.getAllNodes(), new VirtualCommand(module, cmd, cc.value().trim().isEmpty() ? tag : module.getTag(cc.value().trim())));
				}

				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public String getTag()
	{
		return tag;
	}

	public Module getModule()
	{
		return module;
	}

	public ICommand getCommand()
	{
		return command;
	}

	public GMap<GList<String>, VirtualCommand> getChildren()
	{
		return children;
	}

	public boolean hit(CommandSender sender, GList<String> chain)
	{
		PhantomSender vs = new PhantomSender(sender);
		vs.setTag(tag);

		if(chain.isEmpty())
		{
			return command.handle(vs, new String[0]);
		}

		String nl = chain.get(0);

		for(GList<String> i : children.k())
		{
			for(String j : i)
			{
				if(j.equalsIgnoreCase(nl))
				{
					VirtualCommand cmd = children.get(i);
					GList<String> c = chain.copy();
					c.remove(0);
					if(cmd.hit(sender, c))
					{
						return true;
					}
				}
			}
		}

		return command.handle(vs, chain.toArray(new String[chain.size()]));
	}
}
