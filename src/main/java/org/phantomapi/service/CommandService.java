package org.phantomapi.service;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.phantomapi.Phantom;

import phantom.command.ICommand;
import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Command service for registering phantom commands and handling them
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Command")
@Singular
public class CommandService implements IService
{
	private GList<ICommand> activeCommands;

	@Start
	public void start()
	{
		activeCommands = new GList<ICommand>();
	}

	@Stop
	public void stop()
	{

	}

	/**
	 * Get parameters from string
	 *
	 * @param inv
	 *            the string
	 * @return the parameters
	 */
	public String[] getParameters(String inv)
	{
		GList<String> pars = new GList<String>();

		if(inv.startsWith("/"))
		{
			inv = inv.substring(1);
		}

		for(String i : inv.split(" "))
		{
			String v = i.trim();

			if(v.length() == 0 || v.equals(" "))
			{
				continue;
			}

			pars.add(i);
		}

		pars.set(0, pars.get(0).toLowerCase());

		return pars.toArray(new String[pars.size()]);
	}

	/**
	 * Get arguments from parameters
	 *
	 * @param pars
	 *            the parameters
	 * @return the args
	 */
	public String[] getArgsFromPars(String[] pars)
	{
		GList<String> f = new GList<String>(pars);
		f.pop();
		return f.toArray(new String[f.size()]);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(ServerCommandEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}

		if(processCommand(e.getSender(), getParameters(e.getCommand())))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}

		if(processCommand(e.getPlayer(), getParameters(e.getMessage())))
		{
			e.setCancelled(true);
		}
	}

	/**
	 * Get the commands by priority. Highest priority would be the first element in
	 * the list
	 *
	 * @return the commands
	 */
	public GList<ICommand> getCommandsByPriority()
	{
		GList<ICommand> cmds = getActiveCommands();
		GList<ICommand> cmdo = new GList<ICommand>();

		while(!cmds.isEmpty())
		{
			int v = Integer.MIN_VALUE;
			ICommand cmd = null;

			for(ICommand i : cmds)
			{
				if(i.getPriority() > v)
				{
					v = i.getPriority();
					cmd = i;
				}
			}

			cmds.remove(cmd);
			cmdo.add(cmd);
		}

		return cmdo;
	}

	/**
	 * Processes parameters into commands being executed. This follows priority. If
	 * two commands have the same command or alias priority takes over. If a
	 * command's command name and another commands alias match, the command with the
	 * actual command name is preferred even if the priority of the alias is higher.
	 *
	 * @param sender
	 *            the command sender
	 * @param pars
	 *            the parameters
	 * @return true if a command was fired. False otherwise
	 */
	public boolean processCommand(CommandSender sender, String[] pars)
	{
		String[] args = getArgsFromPars(pars);
		String command = pars[0];
		GList<ICommand> thens = new GList<ICommand>();

		for(ICommand i : getCommandsByPriority())
		{
			if(i.getCommandName().equalsIgnoreCase(command))
			{
				thens.add(i);
			}
		}

		for(ICommand i : getCommandsByPriority())
		{
			for(String j : i.getAliases())
			{
				if(j.equalsIgnoreCase(command))
				{
					thens.add(i);
				}
			}
		}

		for(ICommand i : thens)
		{
			if(i.onCommand(sender, args))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Get all active commands
	 *
	 * @return the commands activated
	 */
	public GList<ICommand> getActiveCommands()
	{
		return activeCommands.copy();
	}

	/**
	 * Activate a command
	 *
	 * @param command
	 *            the command to activate
	 */
	public void activateCommand(ICommand command)
	{
		activeCommands.add(command);
		Phantom.activate(command);
		Phantom.claim(this, command);
		PD.l("Activated Command: /" + command.getCommandName());
	}

	/**
	 * Deactivate a command
	 *
	 * @param command
	 *            the command to deactivate
	 */
	public void deactivateCommand(ICommand command)
	{
		activeCommands.remove(command);
		Phantom.deactivate(command);
		Phantom.unclaim(this, command);
		PD.l("Deactivated Command: /" + command.getCommandName());
	}
}
