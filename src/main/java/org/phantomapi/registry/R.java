package org.phantomapi.registry;

import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;

/**
 * Registry utils
 * 
 * @author cyberpwn
 */
public class R
{
	/**
	 * Register the registrant
	 * 
	 * @param registrant
	 *            the registrant
	 */
	public static void register(Controllable registrant)
	{
		Phantom.instance().getRegistryController().registerRegistrant(registrant);
	}
	
	/**
	 * Unregister the registrant
	 * 
	 * @param registrant
	 *            the registrant
	 */
	public static void unregister(Controllable registrant)
	{
		Phantom.instance().getRegistryController().unregisterRegistrant(registrant);
	}
	
	/**
	 * Register a registrar
	 * 
	 * @param registrar
	 *            the registrar
	 */
	public static void registerRegistrar(Registrar<?> registrar)
	{
		Phantom.instance().getRegistryController().registerRegistrar(registrar);
	}
}
