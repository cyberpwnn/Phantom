package org.phantomapi.core;

import org.phantomapi.Phantom;

import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Pawn;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.util.metrics.Documented;

/**
 * The main provider of the phantom api
 *
 * @author cyberpwn
 */
@Documented
@Register
@Singular
@Name("Phantom Controller")
public class PhantomProvider implements IPawn
{
	@Pawn
	private ServiceProvider serviceController;

	@Start
	public void onStart()
	{
		Phantom.crawlJar(Phantom.getPluginFile());
	}

	@Stop
	public void onStop()
	{

	}

	/**
	 * Get the service controller
	 *
	 * @return the phantom service controller
	 */
	public ServiceProvider getServiceProvider()
	{
		return serviceController;
	}
}
