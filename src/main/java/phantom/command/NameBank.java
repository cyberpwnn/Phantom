package phantom.command;

import phantom.lang.GList;
import phantom.util.metrics.Documented;

/**
 * Represents a name bank that holds names and aliases with a default
 *
 * @author cyberpwn
 */
@Documented
public class NameBank
{
	private String defaultName;
	private GList<String> names;

	/**
	 * Create a name bank
	 *
	 * @param defaultName
	 *            the default name (added to bank)
	 */
	public NameBank(String defaultName)
	{
		names = new GList<String>().qadd(defaultName);
		this.defaultName = defaultName;
	}

	/**
	 * Add a name
	 *
	 * @param name
	 *            a name
	 */
	public void add(String name)
	{
		names.add(name);
	}

	/**
	 * Get the default name
	 *
	 * @return the default name
	 */
	public String getDefaultName()
	{
		return defaultName;
	}

	/**
	 * Get all names
	 *
	 * @return the list of names
	 */
	public GList<String> getNames()
	{
		return names;
	}
}
