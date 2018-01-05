package phantom.command;

import org.bukkit.command.CommandSender;

import phantom.lang.GList;
import phantom.pawn.IPawn;
import phantom.util.metrics.Documented;

/**
 * Represents a command
 *
 * @author cyberpwn
 */
@Documented
public interface ICommand extends IPawn
{
	/**
	 * Used as a method of determining order since all commands run on the event
	 *
	 * @return the priroity of the command
	 */
	public int getPriority();

	/**
	 * Get the name bank representing the aliases and main command string
	 *
	 * @return the name bank
	 */
	public NameBank getNameBank();

	/**
	 * Get the command default name
	 *
	 * @return the command name
	 */
	public String getCommandName();

	/**
	 * Get all command aliases
	 *
	 * @return the aliases
	 */
	public GList<String> getAliases();

	/**
	 * Get the usage of parameters for this command
	 *
	 * @return the parameter usage
	 */
	public String getParameterUsage();

	/**
	 * Called when a command matching this is executed. This is only called when the
	 * command (pawn) is activated
	 *
	 * @param sender
	 *            the command sender
	 * @param a
	 *            the arguments
	 * @return returns true if the command took the message, and phantom should no
	 *         longer check for other commands
	 */
	public boolean onCommand(CommandSender sender, String[] a);
}
