package org.phantomapi.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.phantomapi.Phantom;

import phantom.data.cluster.DataCluster;
import phantom.data.configurable.IConfigurable;
import phantom.data.configurable.Key;
import phantom.dispatch.PD;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Command service for handling cluster objects
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Object Cluster")
@Singular
public class ObjectClusterSVC implements IService
{
	@Start
	public void start()
	{

	}

	/**
	 * Convert a data cluster into a configurable object
	 *
	 * @param cc
	 *            the data cluster
	 * @param c
	 *            the configurable object class
	 * @return an instance of the configurable object representing the datacluster
	 */
	public <T extends IConfigurable> T toConfigurable(DataCluster cc, Class<T> c)
	{
		try
		{
			ClusterSVC svc = Phantom.getService(ClusterSVC.class);
			T ti = c.getConstructor().newInstance();

			for(Field i : c.getFields())
			{
				if(i.isAnnotationPresent(Key.class))
				{
					if(Modifier.isStatic(i.getModifiers()))
					{
						PD.w(c.getCanonicalName() + " field " + i.getName() + " cannot be static.");
						continue;
					}

					Class<?> t = i.getType();
					String k = i.getAnnotation(Key.class).value();

					if(!svc.supports(t))
					{
						PD.w(c.getCanonicalName() + " field " + i.getName() + " cannot support type: " + t.getCanonicalName());
						continue;
					}

					if(!cc.contains(k))
					{
						continue;
					}

					if(!cc.getType(k).equals(t))
					{
						PD.w("Skipping Key: " + k + " on " + c.getCanonicalName() + " Conflicts types. Cluster: " + cc.getType(k).getCanonicalName() + " Object: " + t.getCanonicalName());
						continue;
					}

					try
					{
						i.setAccessible(true);
						i.set(ti, cc.get(k));
					}

					catch(Exception e)
					{
						PD.w("Cannot set field " + i.getName() + " in " + c.getCanonicalName());
						Phantom.kick(e);
					}
				}
			}

			return ti;
		}

		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			PD.w("Cannot instantiate empty constructor for " + c.getCanonicalName());
			Phantom.kick(e);
		}

		return null;
	}

	/**
	 * Convert a configurable object into a data cluster
	 *
	 * @param confiruable
	 *            the data cluster
	 * @return the data cluster representing the given configurable object
	 */
	public DataCluster toCluster(IConfigurable confiruable)
	{
		Class<?> c = confiruable.getClass();
		ClusterSVC svc = Phantom.getService(ClusterSVC.class);
		DataCluster cc = new DataCluster();

		for(Field i : c.getFields())
		{
			if(i.isAnnotationPresent(Key.class))
			{
				if(Modifier.isStatic(i.getModifiers()))
				{
					PD.w(c.getCanonicalName() + " field " + i.getName() + " cannot be static.");
					continue;
				}

				Class<?> t = i.getType();
				String k = i.getAnnotation(Key.class).value();

				if(!svc.supports(t))
				{
					PD.w(c.getCanonicalName() + " field " + i.getName() + " cannot support type: " + t.getCanonicalName());
					continue;
				}

				i.setAccessible(true);

				try
				{
					cc.set(k, i.get(confiruable));
				}

				catch(IllegalArgumentException | IllegalAccessException e)
				{
					PD.f("Failed to access field " + i.getName() + " in " + c.getCanonicalName());
					Phantom.kick(e);
				}
			}
		}

		return cc;
	}
}
