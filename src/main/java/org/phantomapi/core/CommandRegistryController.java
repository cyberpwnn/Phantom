package org.phantomapi.core;

import java.lang.reflect.Method;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.phantomapi.command.Command;
import org.phantomapi.command.CommandAlias;
import org.phantomapi.command.CommandBus;
import org.phantomapi.command.CommandEventBus;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.CommandResult;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;

/**
 * Command registry
 * 
 * @author cyberpwn
 */
public class CommandRegistryController extends Controller implements Monitorable
{
	private final GMap<String, GList<CommandListener>> registry;
	private final GList<CommandListener> registrants;
	private final GMap<String, GList<Method>> commandableEvents;
	
	public CommandRegistryController(Controllable parentController)
	{
		super(parentController);
		
		registry = new GMap<String, GList<CommandListener>>();
		registrants = new GList<CommandListener>();
		commandableEvents = new GMap<String, GList<Method>>();
	}
	
	public void register(Controllable c)
	{
		for(Method i : c.getClass().getDeclaredMethods())
		{
			register(i);
		}
	}
	
	@Override
	public void unregister(Controllable c)
	{
		for(Method i : c.getClass().getDeclaredMethods())
		{
			if(i.isAnnotationPresent(Command.class))
			{
				unregister(i);
			}
		}
	}
	
	public void register(Method method)
	{
		if(method.isAnnotationPresent(Command.class))
		{
			Command c = method.getAnnotation(Command.class);
			
			register(c.value(), method);
			
			if(method.isAnnotationPresent(CommandAlias.class))
			{
				CommandAlias cx = method.getAnnotation(CommandAlias.class);
				
				for(String i : cx.value())
				{
					register(i, method);
				}
			}
		}
	}
	
	public void register(CommandListener listener)
	{
		register(listener.getCommandName(), listener);
		
		for(String i : listener.getCommandAliases())
		{
			register(i, listener);
		}
	}
	
	private void register(String node, CommandListener listener)
	{
		if(!registry.containsKey(node.toLowerCase()))
		{
			registry.put(node.toLowerCase(), new GList<CommandListener>());
		}
		
		registry.get(node.toLowerCase()).add(listener);
		
		if(!registrants.contains(listener))
		{
			registrants.add(listener);
		}
		
		o("Registered Command: " + C.GREEN + listener.getClass().getSimpleName() + " <> " + "/" + node);
	}
	
	private void register(String node, Method listener)
	{
		if(!commandableEvents.containsKey(node.toLowerCase()))
		{
			commandableEvents.put(node.toLowerCase(), new GList<Method>());
		}
		
		if(!commandableEvents.get(node.toLowerCase()).contains(listener))
		{
			commandableEvents.get(node.toLowerCase()).add(listener);
			o("Registered Command: " + C.GREEN + listener.getDeclaringClass().getSimpleName() + "." + listener.getName() + "()" + " <> " + "/" + node);
		}
		
		else
		{
			w("Duplicate Registry Ignored: " + C.RED + listener.getDeclaringClass().getSimpleName() + "." + listener.getName() + "()" + " <> " + "/" + node);
		}
	}
	
	public void unregister(Method c)
	{
		for(String i : commandableEvents.k())
		{
			commandableEvents.get(i).remove(c);
		}
		
		o(C.RED + "Unregistered all commands for " + C.YELLOW + c.getDeclaringClass().getSimpleName() + "." + c.getName() + "()");
	}
	
	public void unregister(CommandListener c)
	{
		for(String i : getRegistry().k())
		{
			getRegistry().get(i).remove(c);
		}
		
		registrants.removeAll(c);
		
		o(C.RED + "Unregistered all commands for " + C.YELLOW + c.getClass().getSimpleName());
	}
	
	public GList<CommandListener> getRegistrants()
	{
		return registrants;
	}
	
	public GMap<String, GList<CommandListener>> getRegistry()
	{
		return registry;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(processCommand(e.getPlayer(), e.getMessage()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(ServerCommandEvent e)
	{
		if(processCommand(e.getSender(), e.getCommand()))
		{
			e.setCancelled(true);
		}
	}
	
	private boolean processCommand(CommandSender sender, String message)
	{
		String roots = message.startsWith("/") ? message.substring(1) : message;
		GList<String> rtz = new GList<String>(roots.split(" "));
		String command = rtz.pop();
		String[] args = rtz.toArray(new String[rtz.size()]);
		CommandBus bus = new CommandBus(this, command, args, sender);
		
		if(bus.getResult().equals(CommandResult.HANDLED))
		{
			return true;
		}
		
		CommandEventBus busx = new CommandEventBus(this, command, args, sender);
		
		if(busx.getResult().equals(CommandResult.HANDLED))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public GMap<String, GList<Method>> getCommandableEvents()
	{
		return commandableEvents;
	}
	
	@Override
	public String getMonitorableData()
	{
		return "Roots: " + C.LIGHT_PURPLE + (registry.k().size() + commandableEvents.k().size()) + C.DARK_GRAY + " Listeners: " + C.LIGHT_PURPLE + registrants.size();
	}
}
