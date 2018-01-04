package org.phantomapi;

import org.bukkit.plugin.java.JavaPlugin;

import phantom.dispatch.PD;

/**
 * The phantom plugin instance
 *
 * @author cyberpwn
 */
public class PhantomPlugin extends JavaPlugin
{
	private static PhantomPlugin inst;

	public PhantomPlugin()
	{
		inst = this;
		Phantom.touch(this);
		PD.l("Starting Phantom " + Phantom.getVersion());
	}

	@Override
	public void onLoad()
	{

	}

	@Override
	public void onEnable()
	{
		Phantom.pulse(Signal.START);
	}

	@Override
	public void onDisable()
	{
		Phantom.pulse(Signal.STOP);
	}

	public void onAbort()
	{
		Phantom.pulse(Signal.ABORT);
	}

	/**
	 * Get the instance of phantom
	 *
	 * @return phantom plugin instance
	 */
	public static PhantomPlugin instance()
	{
		return inst;
	}
}
