package org.phantomapi.registry;

import org.phantomapi.lang.GSet;

/**
 * A Registrar holds registrants
 *
 * @author cyberpwn
 */
public interface Registrar
{
	/**
	 * Returns the class this registrar is capable of registering
	 *
	 * @return
	 */
	public Class<? extends Registrant> getRegistrantClass();

	/**
	 * Get all registrants
	 *
	 * @return the registrants
	 */
	public GSet<Registrant> getRegistrants();

	/**
	 * Register a registrant
	 *
	 * @param registrant
	 *            the registrant
	 */
	public void register(Registrant registrant);

	/**
	 * Unregister the registrant
	 *
	 * @param registrant
	 *            the registrant
	 */
	public void unregister(Registrant registrant);

	/**
	 * Unregister all registrants
	 */
	public void unregisterAll();
}
