package org.phantomapi.registry;

import org.phantomapi.Phantom;

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
	public static void register(Registrant registrant)
	{
		Phantom.instance().getRegistryController().register(registrant);
	}
	
	/**
	 * Unregister the registrant
	 * 
	 * @param registrant
	 *            the registrant
	 */
	public static void unregister(Registrant registrant)
	{
		Phantom.instance().getRegistryController().register(registrant);
	}
	
	/**
	 * Register a registrar
	 * 
	 * @param registrar
	 *            the registrar
	 */
	public static void registerRegistrar(Registrar registrar)
	{
		Phantom.instance().getRegistryController().getRegisters().add(registrar);
	}
}
