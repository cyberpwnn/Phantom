package org.phantomapi.core;

import java.lang.reflect.Field;
import org.phantomapi.Phantom;
import org.phantomapi.service.ThreadPoolService;
import phantom.lang.GMap;
import phantom.pawn.DeployableService;
import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;

@Register
@Singular
@Name("Service Controller")
public class ServiceController implements IPawn
{
	@DeployableService
	private ThreadPoolService threadPoolSVC;

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
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}
	}

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

	public boolean isService(Class<? extends IService> svcClass)
	{
		return offeredServices.containsKey(svcClass);
	}

	public IService getService(Class<? extends IService> svcClass)
	{
		if(isService(svcClass))
		{
			if(!isServiceRunning(svcClass))
			{
				startService(svcClass);
			}

			try
			{
				return (IService) offeredServices.get(svcClass).get(this);
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}
		
		return null;
	}
}
