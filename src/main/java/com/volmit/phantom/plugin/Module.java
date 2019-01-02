package com.volmit.phantom.plugin;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.V;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.text.C;

public class Module implements IModule, Listener, CommandExecutor
{
	private D d;
	private final GMap<GList<String>, VirtualCommand> commands = new GMap<GList<String>, VirtualCommand>();
	public final UUID INSTANCE_ID = UUID.randomUUID();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}

		PhantomSender s = new PhantomSender(e.getPlayer(), getTag("Test"));

		if(e.getMessage().equalsIgnoreCase("/tests") && e.getPlayer().isOp())
		{
			GList<String> m = getStructure().getTests(s);

			if(m.isEmpty())
			{
				s.sendMessage("No tests for Players");
			}

			else
			{
				s.sendMessage("Showing " + m.size() + " Test" + (m.size() == 1 ? "" : "s."));

				for(String i : m)
				{
					s.sendMessage("  " + i);
				}
			}
		}

		for(String i : getStructure().getTestsRaw(s))
		{
			if(e.getMessage().equalsIgnoreCase("/test " + i + "()"))
			{
				getStructure().test(s, i);
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void on(ServerCommandEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}

		PhantomSender s = new PhantomSender(e.getSender(), getTag("Test"));

		if(e.getCommand().equalsIgnoreCase("tests"))
		{
			GList<String> m = getStructure().getTests(s);

			if(m.isEmpty())
			{
				s.sendMessage("No tests for Consoles");
			}

			else
			{
				s.sendMessage("Showing " + m.size() + " Test" + (m.size() == 1 ? "" : "s."));

				for(String i : m)
				{
					s.sendMessage("  " + i);
				}
			}
		}

		for(String i : getStructure().getTestsRaw(s))
		{
			if(e.getCommand().equalsIgnoreCase("test " + i + "()"))
			{
				getStructure().test(s, i);
				e.setCancelled(true);
				return;
			}
		}
	}

	@Override
	public File getDataFile(String... path)
	{
		File ff = new File(getDataFolder(), new GList<String>(path).toString("/"));
		ff.getParentFile().mkdirs();
		return ff;
	}

	@Override
	public File getDataFolder(String... folders)
	{
		if(folders.length == 0)
		{
			return Phantom.getModuleManager().getDataFolder(this);
		}

		File ff = new File(getDataFolder(), new GList<String>(folders).toString("/"));
		ff.mkdirs();
		return ff;
	}

	private D d()
	{
		if(d == null)
		{
			d = new D(getName());
		}

		return d;
	}

	public void l(Object... s)
	{
		d().l(s);
	}

	public void w(Object... s)
	{
		d().w(s);
	}

	public void f(Object... s)
	{
		d().f(s);
	}

	protected void registerCommand(PhantomCommand cmd, String t)
	{
		commands.put(cmd.getAllNodes(), new VirtualCommand(this, cmd, t.trim().isEmpty() ? getTag() : getTag(t.trim())));
		PluginCommand cc = PhantomPlugin.plugin.getCommand(cmd.getNode().toLowerCase());

		if(cc != null)
		{
			cc.setExecutor(this);
			cc.setUsage(getName() + ":" + getClass().toString());
		}

		else
		{
			RouterCommand r = new RouterCommand(cmd, this);
			r.setUsage(getName() + ":" + getClass().toString());
			((CommandMap) new V(Bukkit.getServer()).get("commandMap")).register("", r);
		}
	}

	protected void unregisterCommand(PhantomCommand cmd)
	{
		SimpleCommandMap m = new V(Bukkit.getServer()).get("commandMap");
		Map<String, Command> k = new V(m).get("knownCommands");

		for(Iterator<Map.Entry<String, Command>> it = k.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<String, Command> entry = it.next();
			if(entry.getValue() instanceof Command)
			{
				Command c = (Command) entry.getValue();
				String u = c.getUsage();

				if(u != null && u.equals(getName() + ":" + getClass().toString()))
				{
					D.as("Module Manager").l("Unregistering Command: " + c.getName());
					c.unregister(m);
					it.remove();
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		GList<String> chain = new GList<String>().qadd(args);

		for(GList<String> i : commands.k())
		{
			for(String j : i)
			{
				if(j.equalsIgnoreCase(label))
				{
					VirtualCommand cmd = commands.get(i);

					if(cmd.hit(sender, chain.copy()))
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public StructuredModule getStructure()
	{
		return Phantom.getModuleManager().getStructure(this);
	}

	@Override
	public ModuleInfo getModuleInfo()
	{
		if(getStructure() == null)
		{
			return getClass().getAnnotation(ModuleInfo.class);
		}

		return getStructure().getInfo();
	}

	@Override
	public String getName()
	{
		return getModuleInfo().name();
	}

	@Override
	public String getVersion()
	{
		return getModuleInfo().version();
	}

	@Override
	public String getAuthor()
	{
		return getModuleInfo().author();
	}

	@Override
	public C getColor()
	{
		return getModuleInfo().color();
	}

	@Override
	public String getTag()
	{
		return C.DARK_GRAY + "[" + C.BOLD + getColor() + getName() + C.RESET + C.DARK_GRAY + "]" + C.GRAY + ": ";
	}

	@Override
	public String getTag(String sub)
	{
		return C.DARK_GRAY + "[" + C.BOLD + getColor() + getName() + C.RESET + C.DARK_GRAY + " - " + C.ITALIC + C.WHITE + sub + C.RESET + C.GRAY + "]" + C.GRAY + ": ";
	}

	@Override
	public File getModuleFile()
	{
		return getStructure().getModuleFile();
	}
}
