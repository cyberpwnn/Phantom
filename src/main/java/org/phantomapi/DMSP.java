package org.phantomapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.phantomapi.core.PhantomProvider;
import org.phantomapi.pluginadapter.FAWEAdapter;
import org.phantomapi.pluginadapter.ProtocolLibAdapter;
import org.phantomapi.pluginadapter.VaultAdapter;
import org.phantomapi.service.EventSVC;
import org.phantomapi.service.PluginLinkSVC;
import org.phantomapi.service.RecipeSVC;

import phantom.dispatch.PD;
import phantom.event.PhantomStopEvent;
import phantom.sched.A;
import phantom.sched.TICK;
import phantom.sched.Task;
import phantom.util.metrics.Documented;

/**
 * DMSP Used for managing the phantom api
 *
 * @author cyberpwn
 */
@Documented
public class DMSP
{
	private Task task;
	private PhantomProvider api;
	private String ip;

	/**
	 * Create a dmsp instance
	 */
	public DMSP()
	{
		PD.v("DMSp Initialized");
		api = new PhantomProvider();
	}

	/**
	 * Start DMSP
	 */
	public void start()
	{
		startTickMethod();
		activateAPI();
		checkForPluginLinks();
		checkIP();
		PD.v("DMSp Online");
	}

	private void activateAPI()
	{
		Phantom.activate(api);
		api.getServiceProvider().startService(EventSVC.class);
		api.getServiceProvider().startService(RecipeSVC.class);
	}

	private void checkForPluginLinks()
	{
		PD.v("Scanning for plugin link adapters...");

		if(Phantom.getService(PluginLinkSVC.class).isPluginInstalled("ProtocolLib"))
		{
			Phantom.getService(PluginLinkSVC.class).getLink(ProtocolLibAdapter.class);
		}

		if(Phantom.getService(PluginLinkSVC.class).isPluginInstalled("FastAsyncWorldEdit"))
		{
			Phantom.getService(PluginLinkSVC.class).getLink(FAWEAdapter.class);
		}

		if(Phantom.getService(PluginLinkSVC.class).isPluginInstalled("Vault"))
		{
			Phantom.getService(PluginLinkSVC.class).getLink(VaultAdapter.class);
		}
	}

	private void checkIP()
	{
		new A()
		{
			@Override
			public void run()
			{
				try
				{
					URL url = new URL("http://checkip.amazonaws.com/");
					InputStream is = null;
					BufferedReader br;

					try
					{
						is = url.openStream();
						br = new BufferedReader(new InputStreamReader(is));
						ip = br.readLine();
						PD.v("Online: " + ip);
					}

					catch(MalformedURLException mue)
					{
						mue.printStackTrace();
					}

					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}

					finally
					{
						try
						{
							if(is != null)
							{
								is.close();
							}
						}

						catch(IOException ioe)
						{

						}
					}
				}

				catch(Exception e)
				{

				}
			}
		};
	}

	/**
	 * Stop DMSP
	 */
	public void stop()
	{
		Phantom.callEvent(new PhantomStopEvent());
		task.cancel();
		Phantom.deactivate(api);
		PD.v("DMSp Offline");
	}

	private void tick()
	{
		TICK.tick++;
		Phantom.getPawnSpace().tick();
	}

	private void startTickMethod()
	{
		task = new Task(0)
		{
			@Override
			public void run()
			{
				tick();
			}
		};
	}

	/**
	 * Get the root phantom provider
	 *
	 * @return the provider
	 */
	public PhantomProvider getApi()
	{
		return api;
	}
}
