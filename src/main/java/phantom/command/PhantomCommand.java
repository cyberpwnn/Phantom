package phantom.command;

import org.bukkit.command.CommandSender;

import phantom.lang.GList;

public abstract class PhantomCommand implements ICommand
{
	private int priority;
	private final NameBank nameBank;
	private String parameterUsage;

	public PhantomCommand(String command)
	{
		this.priority = 100;
		this.nameBank = new NameBank(command);
		this.parameterUsage = "";
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
}
