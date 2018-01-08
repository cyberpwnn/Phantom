package phantom.data.cluster;

import phantom.data.IData;

/**
 * Represents a general cluster
 *
 * @author cyberpwn
 *
 * @param <T>
 *            the type of the object
 */
public abstract class PhantomCluster<T> implements ICluster<T>, IData
{
	private T value;

	/**
	 * Create a cluster object
	 *
	 * @param value
	 *            the value (initially)
	 */
	public PhantomCluster(T value)
	{
		this.value = value;
	}

	@Override
	public T get()
	{
		return value;
	}

	@Override
	public void set(T value)
	{
		this.value = value;
	}
}
