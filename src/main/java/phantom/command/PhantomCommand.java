package phantom.command;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;
import org.phantomapi.service.CommandService;

import phantom.lang.GList;
import phantom.util.metrics.Documented;

/**
 * A command object representing ICommand
 *
 * @author cyberpwn
 */
@Documented
public abstract class PhantomCommand implements ICommand
{
	private GList<ICommand> subCommands;
	private int priority;
	private final NameBank nameBank;
	private String parameterUsage;

	public PhantomCommand(String command)
	{
		this.subCommands = new GList<ICommand>();
		this.priority = 100;
		this.nameBank = new NameBank(command);
		this.parameterUsage = "";
	}

	@Override
	public GList<ICommand> getSubCommands()
	{
		return subCommands;
	}

	@Override
	public int getPriority()
	{
		return priority;
	}

	@Override
	public NameBank getNameBank()
	{
		return nameBank;
	}

	@Override
	public String getCommandName()
	{
		return getNameBank().getDefaultName();
	}

	@Override
	public GList<String> getAliases()
	{
		return getNameBank().getNames();
	}

	@Override
	public String getParameterUsage()
	{
		return parameterUsage;
	}

	@Override
	public void setPriority(int p)
	{
		priority = p;
	}

	@Override
	public void addAlias(String alias)
	{
		nameBank.add(alias);
	}

	@Override
	public void setPrameterUsage(String p)
	{
		parameterUsage = p;
	}

	@Override
	public void msg(CommandSender sender, String msg)
	{
		sender.sendMessage(getTag(sender) + msg);
	}

	@Override
	public void activateSubCommand(ICommand command)
	{
		getSubCommands().add(command);
		Phantom.activate(command);
		Phantom.claim(this, command);
	}

	@Override
	public void deactivateSubCommand(ICommand command)
	{
		getSubCommands().remove(command);
		Phantom.deactivate(command);
		Phantom.unclaim(this, command);
	}

	@Override
	public void deactivateSubCommands()
	{
		for(ICommand i : getSubCommands().copy())
		{
			deactivateSubCommand(i);
		}
	}

	@Override
	public GList<ICommand> getSubCommandsByPriority()
	{
		GList<ICommand> cmds = getSubCommands().copy();
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

	@Override
	public boolean fireSubCommand(CommandSender sender, String[] a)
	{
		ICommand cmd = getResolvedSubCommand(a[0]);
		String[] args = Phantom.getService(CommandService.class).getArgsFromPars(a);

		if(cmd == null)
		{
			return false;
		}

		return cmd.onCommand(sender, args);
	}

	@Override
	public ICommand getResolvedSubCommand(String sub)
	{
		for(ICommand i : getSubCommandsByPriority())
		{
			if(i.getCommandName().equalsIgnoreCase(sub))
			{
				return i;
			}
		}

		for(ICommand i : getSubCommandsByPriority())
		{
			for(String j : i.getAliases())
			{
				if(j.equalsIgnoreCase(sub))
				{
					return i;
				}
			}
		}

		return null;
	}
}
