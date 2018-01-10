package phantom.hive;

import phantom.data.cluster.DataCluster;
import phantom.util.metrics.Documented;

/**
 * Represents a hive data object
 *
 * @author cyberpwn
 */
@Documented
public interface IHive
{
	/**
	 * Read data from this hive object
	 *
	 * @param channel
	 *            the channel to pull data from
	 * @return the hive object represented in a data cluster
	 */
	public DataCluster pull(String channel);

	/**
	 * Push data to this hive cluster (overwrite)
	 *
	 * @param cc
	 *            the data cluster
	 * @param channel
	 *            the channel to push to
	 */
	public void push(DataCluster cc, String channel);

	/**
	 * Delete data from the channel
	 *
	 * @param channel
	 *            the channel to delete
	 */
	public void drop(String channel);
}
