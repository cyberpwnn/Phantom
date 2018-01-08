package phantom.data.cluster;

/**
 * A cluster object used in dataclusters
 *
 * @author cyberpwn
 *
 * @param <T>
 *            the type of the object
 */
public interface ICluster<T>
{
	/**
	 * Get the cluster object
	 *
	 * @return the object
	 */
	public T get();

	/**
	 * Set the cluster object value
	 *
	 * @param t
	 *            the value
	 */
	public void set(T t);

	/**
	 * Convert this object to string
	 *
	 * @return used in text based configs
	 */
	public String asString();

	/**
	 * Convert this object back from a string
	 *
	 * @param s
	 *            the string to send as
	 */
	public void fromString(String s);
}
