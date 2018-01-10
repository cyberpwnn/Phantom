package org.phantomapi;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.phantomapi.service.ClusterSVC;

import phantom.data.cluster.ICluster;
import phantom.dispatch.PD;
import phantom.event.PhantomJarScannedEvent;
import phantom.util.metrics.Documented;

/**
 * The phantom plugin instance
 *
 * @author cyberpwn
 */
@Documented
public class PhantomPlugin extends JavaPlugin implements Listener
{
	private static PhantomPlugin inst;

	public PhantomPlugin()
	{

	}

	@Override
	public void onLoad()
	{

	}

	@Override
	public void onEnable()
	{
		inst = this;
		Bukkit.getPluginManager().registerEvents(this, this);
		Phantom.touch(this);
		PD.l("Starting Phantom " + Phantom.getVersion());
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

	@SuppressWarnings("unchecked")
	@EventHandler
	public void on(PhantomJarScannedEvent e)
	{
		if(e.getClasses().containsKey("phantom-cluster"))
		{
			for(Class<?> i : e.getClasses().get("phantom-cluster"))
			{
				PD.v("Registered ClusterType: " + i.getSimpleName());
				Phantom.getService(ClusterSVC.class).add((Class<? extends ICluster<?>>) i);
			}
		}
	}
}
