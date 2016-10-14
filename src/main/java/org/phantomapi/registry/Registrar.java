package org.phantomapi.registry;

import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GSet;

/**
 * A Registrar holds registrants
 *
 * @author cyberpwn
 */
public interface Registrar<T extends Registrant>
{
	/**
	 * Get the registrar type
	 * 
	 * @return the registrar type
	 */
	public String getType();
	
	/**
	 * Is the given object capable of being registered
	 * 
	 * @param o
	 *            the object
	 * @return true if it can be registered
	 */
	public boolean isValid(Controllable o);
	
	/**
	 * Get all registrants
	 *
	 * @return the registrants
	 */
	public GSet<T> getRegistrants();
	
	/**
	 * Register a registrant
	 *
	 * @param registrant
	 *            the registrant
	 */
	public void register(Controllable registrant);
	
	/**
	 * Unregister the registrant
	 *
	 * @param registrant
	 *            the registrant
	 */
	public void unregister(Controllable registrant);
	
	/**
	 * Is the given registrant registered?
	 * 
	 * @param registrant
	 *            the registrant
	 * @return true if it is
	 */
	public boolean isRegistered(Controllable registrant);
	
	/**
	 * Unregister all registrants
	 */
	public void unregisterAll();
}
