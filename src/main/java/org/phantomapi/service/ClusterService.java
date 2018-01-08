package org.phantomapi.service;

import phantom.data.cluster.Cluster;
import phantom.data.cluster.ICluster;
import phantom.lang.GMap;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.service.IService;
import phantom.util.data.ClassUtil;
import phantom.util.metrics.Documented;

/**
 * Command service for handling cluster types
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Cluster")
@Singular
public class ClusterService implements IService
{
	private GMap<Class<?>, Class<? extends ICluster<?>>> clusterTypes;

	@Start
	public void start()
	{
		clusterTypes = new GMap<Class<?>, Class<? extends ICluster<?>>>();
	}

	/**
	 * Add (and register) the cluster class
	 *
	 * @param c
	 *            the cluster class
	 */
	public void add(Class<? extends ICluster<?>> c)
	{
		Class<?> type = c.getDeclaredAnnotation(Cluster.class).value();
		registerClusterType(type, c);

		if(ClassUtil.isWrapperOrPrimative(type))
		{
			registerClusterType(ClassUtil.toPrimative(type), c);
		}
	}

	/**
	 * Get the type for the cluster
	 *
	 * @param b
	 *            the type
	 * @return the cluster type
	 */
	public Class<? extends ICluster<?>> getType(Class<?> b)
	{
		return clusterTypes.get(b);
	}

	/**
	 * Register a new cluster type
	 *
	 * @param type
	 *            the type
	 * @param clazz
	 *            the class of the type
	 */
	public void registerClusterType(Class<?> type, Class<? extends ICluster<?>> clazz)
	{
		clusterTypes.put(type, clazz);
	}

	/**
	 * Does the cluster actually support the given class
	 *
	 * @param clazz
	 *            the class to check
	 * @return true if it does
	 */
	public boolean supports(Class<?> clazz)
	{
		return clusterTypes.containsKey(clazz);
	}
}
