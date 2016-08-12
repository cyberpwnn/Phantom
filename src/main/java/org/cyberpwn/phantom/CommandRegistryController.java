package org.cyberpwn.phantom;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.cyberpwn.phantom.command.CommandBus;
import org.cyberpwn.phantom.command.CommandListener;
import org.cyberpwn.phantom.command.CommandResult;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.util.C;

/**
 * Command registry
 * 
 * @author cyberpwn
 */
public class CommandRegistryController extends Controller
{
	private final GMap<String, GList<CommandListener>> registry;
	private final GList<CommandListener> registrants;
	
	public CommandRegistryController(Controllable parentController)
	{
		super(parentController);
		
		registry = new GMap<String, GList<CommandListener>>();
		registrants = new GList<CommandListener>();
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
		
		return false;
	}
}
