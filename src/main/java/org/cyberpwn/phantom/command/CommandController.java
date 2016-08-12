package org.cyberpwn.phantom.command;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.C;

public abstract class CommandController extends Controller implements CommandListener
{
	private String commandName;
	
	public CommandController(Controllable parentController, String commandName)
	{
		super(parentController);
		
		this.commandName = commandName;
	}

	@Override
	public String getMessageNoPermission()
	{
		return C.RED + "No Permission";
	}

	@Override
	public String getMessageNotPlayer()
	{
		return C.RED + "Player Only";
	}

	@Override
	public String getMessageNotConsole()
	{
		return C.RED + "Console Only";
	}

	@Override
	public String getMessageInvalidArgument(String arg, String neededType)
	{
		return C.RED + "Invalid Argument type '" + arg + "'. Expected TYPE: " + neededType;
	}

	@Override
	public String getMessageInvalidArguments(int given, int expected, int expectedMax)
	{
		return C.RED + "Invalid number of arguements (" + given + "). Expected " + expected + "-" + expectedMax;
	}

	@Override
	public String getMessageUnknownSubCommand(String given)
	{
		return C.RED + "Unknown Sub Command: " + given;
	}

	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.WHITE + getPlugin().getName() + C.DARK_GRAY + "]" + C.GRAY + ": ";
	}

	@Override
	public String getChatTagHover()
	{
		return C.GREEN + getPlugin().getName() + " " + getPlugin().getDescription().getVersion();
	}

	@Override
	public abstract boolean onCommand(PhantomCommandSender sender, PhantomCommand command);

	@Override
	public String getCommandName()
	{
		return commandName;
	}

	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>();
	}
}
