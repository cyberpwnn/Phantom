package org.phantomapi;

import org.bukkit.plugin.java.JavaPlugin;
import phantom.dispatch.PD;

public class PhantomPlugin extends JavaPlugin
{
	private static PhantomPlugin inst;
	
	public PhantomPlugin()
	{
		inst = this;
		Phantom.touch(this);
		PD.l("Starting Phantom " + Phantom.getVersion());
	}
	
	public void onLoad()
	{
		
	}
	
	public void onEnable()
	{
		Phantom.pulse(Signal.START);
	}
	
	public void onDisable()
	{
		Phantom.pulse(Signal.STOP);
	}
	
	public void onAbort()
	{
		Phantom.pulse(Signal.ABORT);
	}

	public static PhantomPlugin instance()
	{
		return inst;
	}
}
