package org.phantomapi.text;

/**
 * Simple singular or plural amount representation
 * 
 * @author cyberpwn
 */
public class AmountName
{
	private final String singular;
	private final String plural;
	
	/**
	 * Create an amount name
	 * 
	 * @param singular
	 *            the singular name
	 * @param plural
	 *            the plural name
	 */
	public AmountName(String singular, String plural)
	{
		this.singular = singular;
		this.plural = plural;
	}
	
	/**
	 * Get the singular name
	 * 
	 * @return the singular name
	 */
	public String getSingular()
	{
		return singular;
	}
	
	/**
	 * Get the plural name
	 * 
	 * @return the plural name
	 */
	public String getPlural()
	{
		return plural;
	}
	
	/**
	 * Get the singular or plural name for the given amount
	 * 
	 * @param amount
	 *            the given amount
	 * @return the singular or plural text
	 */
	public String forAmount(int amount)
	{
		if(amount != 1)
		{
			return plural;
		}
		
		return singular;
	}
	
	/**
	 * Suffix an amount
	 * 
	 * @param amount
	 *            the given amount
	 * @return a string representing the amount followed by a suffix of plural
	 *         or singular
	 */
	public String suffix(int amount)
	{
		return amount + " " + forAmount(amount);
	}
}
