package org.phantomapi.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.phantomapi.Phantom;
import org.phantomapi.service.ClassAnchorSVC;
import org.phantomapi.service.ClusterSVC;
import org.phantomapi.service.CommandSVC;
import org.phantomapi.service.EventSVC;
import org.phantomapi.service.HiveSVC;
import org.phantomapi.service.NMSBinderSVC;
import org.phantomapi.service.ObjectClusterSVC;
import org.phantomapi.service.PluginLinkSVC;
import org.phantomapi.service.RecipeSVC;
import org.phantomapi.service.TestSVC;
import org.phantomapi.service.ThreadPoolSVC;

import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.pawn.DeployableService;
import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Service provider for deploying and sending services
 *
 * @author cyberpwn
 */
@Documented
@Register
@Singular
@Name("Service Controller")
public class ServiceProvider implements IPawn
{
	@DeployableService
	private ThreadPoolSVC threadPoolSVC;

	@DeployableService
	private ClassAnchorSVC classAnchorSVC;

	@DeployableService
	private TestSVC phantomTestSVC;

	@DeployableService
	private NMSBinderSVC nmsBinderSVC;

	@DeployableService
	private CommandSVC commandSVC;

	@DeployableService
	private EventSVC eventSVC;

	@DeployableService
	private ClusterSVC clusterSVC;

	@DeployableService
	private ObjectClusterSVC objectClusterSVC;

	@DeployableService
	private HiveSVC hiveSVC;

	@DeployableService
	private RecipeSVC recipeSVC;

	@DeployableService
	private PluginLinkSVC pluginLinkSVC;

	private GMap<Class<? extends IService>, Field> offeredServices;

	@SuppressWarnings("unchecked")
	@Start
	public void onStart()
	{
		offeredServices = new GMap<Class<? extends IService>, Field>();

		for(Field i : this.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(DeployableService.class))
			{
				offeredServices.put((Class<? extends IService>) i.getType(), i);

				try
				{
					i.setAccessible(true);
					i.set(this, null);
				}

				catch(Exception e)
				{
					Phantom.kick(e);
				}
			}
		}
	}

	@Stop
	public void onStop()
	{

	}

	/**
	 * Shut down a service
	 *
	 * @param svcClass
	 *            the service class to shut down
	 */
	public void stopService(Class<? extends IService> svcClass)
	{
		if(isService(svcClass) && isServiceRunning(svcClass))
		{
			try
			{
				offeredServices.get(svcClass).setAccessible(true);
				IService svc = (IService) offeredServices.get(svcClass).get(this);
				Phantom.deactivate(svc);
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}
	}

	/**
	 * Start a service
	 *
	 * @param svcClass
	 *            the service class
	 */
	public void startService(Class<? extends IService> svcClass)
	{
		if(isService(svcClass) && !isServiceRunning(svcClass))
		{
			try
			{
				IService svc = svcClass.getConstructor().newInstance();
				offeredServices.get(svcClass).setAccessible(true);
				offeredServices.get(svcClass).set(this, svc);
				Phantom.activate(svc);
				Phantom.claim(this, svc, offeredServices.get(svcClass));
				PD.l(svcClass.getSimpleName() + " Started");
			}

			catch(Exception e)
			{
				PD.f("Failed to start " + svcClass.getSimpleName());
				Phantom.kick(e);
			}
		}
	}

	/**
	 * Check if the given service class is running
	 *
	 * @param svcClass
	 *            the service to check
	 * @return true if it is running, false if it is not running, isnt a valid
	 *         service or failures to check
	 */
	public boolean isServiceRunning(Class<? extends IService> svcClass)
	{
		if(!isService(svcClass))
		{
			return false;
		}

		try
		{
			return offeredServices.get(svcClass).get(this) != null;
		}

		catch(Exception e)
		{
			Phantom.kick(e);
		}

		return false;
	}

	/**
	 * Check if the service class is a valid service
	 *
	 * @param svcClass
	 *            the service class to check
	 * @return true if it is
	 */
	public boolean isService(Class<? extends IService> svcClass)
	{
		return offeredServices.containsKey(svcClass);
	}

	/**
	 * Get a running service object from the service class (typed). If the service
	 * is not running it will be started
	 *
	 * @param svcClass
	 *            the service class to get the instance for
	 * @return the service or null pointer
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T getService(Class<? extends T> svcClass)
	{
		if(isService(svcClass))
		{
			if(!isServiceRunning(svcClass))
			{
				startService(svcClass);
			}

			try
			{
				return (T) offeredServices.get(svcClass).get(this);
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}

		return null;
	}

	public GList<IService> getDeployedServices()
	{
		GList<IService> svc = new GList<IService>();

		for(Class<? extends IService> i : offeredServices.k())
		{
			if(isServiceRunning(i))
			{
				try
				{
					svc.add(i.getConstructor().newInstance());
				}

				catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					Phantom.kick(e);
				}
			}
		}

		return svc;
	}

	public GList<IService> getDormantServices()
	{
		GList<IService> svc = new GList<IService>();

		for(Class<? extends IService> i : offeredServices.k())
		{
			if(!isServiceRunning(i))
			{
				try
				{
					svc.add(i.getConstructor().newInstance());
				}

				catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					Phantom.kick(e);
				}
			}
		}

		return svc;
	}

	public GList<IService> getOfferedServices()
	{
		GList<IService> svc = new GList<IService>();

		for(Class<? extends IService> i : offeredServices.k())
		{
			try
			{
				svc.add(i.getConstructor().newInstance());
			}

			catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				Phantom.kick(e);
			}
		}

		return svc;
	}
}
