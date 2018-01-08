package phantom.data.cluster;

import phantom.lang.GList;
import phantom.util.metrics.Documented;

/**
 * Data cluster
 *
 * @author cyberpwn
 */
@Documented
public interface IDataCluster
{
	/**
	 * Returns a duplicate of the data cluster
	 *
	 * @return the cluster
	 */
	public DataCluster copy();

	/**
	 * Check if the data cluster contains an object at the given key
	 *
	 * @param key
	 *            the key
	 * @return true if it does.
	 */
	public boolean contains(String key);

	/**
	 * Set a cluster to the given key
	 *
	 * @param key
	 *            the key
	 * @param cluster
	 *            the cluster to set
	 */
	public void setCluster(String key, ICluster<?> cluster);

	/**
	 * Get the cluster at the given key
	 *
	 * @param key
	 *            the key
	 * @return the cluster or null if it does not exista
	 */
	public ICluster<?> getCluster(String key);

	/**
	 * Check if the given object type is supported for storage in data clusters
	 *
	 * @param clazz
	 *            the class to check for support
	 * @return true if it does
	 */
	public boolean supports(Class<?> clazz);

	/**
	 * Set the given object to a key. If the object is not supported
	 * (isSupported(object.getClass()) then nothing will happen.
	 *
	 * @param key
	 *            the key
	 * @param object
	 *            the object
	 */
	public void set(String key, Object object);

	/**
	 * Get the object from the key
	 *
	 * @param key
	 *            the key
	 * @return the object or null
	 */
	public Object get(String key);

	/**
	 * Force cast the object to a class and get it from the given key
	 *
	 * @param key
	 *            the key
	 * @param of
	 *            the type expected
	 * @return the object or null if it does not exist. Also throws class cast
	 *         exception if the value is not the same
	 */
	public <T> T get(String key, Class<?> of);

	/**
	 * Get the type of the object at the given key
	 *
	 * @param key
	 *            the key
	 * @return the class type or null
	 */
	public Class<?> getType(String key);

	/**
	 * Get all keys in this datacluster
	 *
	 * @return the keys
	 */
	public GList<String> k();
}
