package phantom.data.ports;

import java.io.IOException;

import phantom.data.cluster.DataCluster;
import phantom.util.metrics.Documented;

/**
 * Represents a port in which data can be written to and from
 *
 * @author cyberpwn
 *
 * @param <T>
 *            the souce of the data which will interoperate with dataclusters
 */
@Documented
public interface IDataPort<T>
{
	/**
	 * Write data from the datacluster to the destination type
	 *
	 * @param data
	 *            the datacluster
	 * @return the object representing the dataclusters data
	 * @throws IOException
	 */
	public T write(DataCluster data) throws IOException;

	/**
	 * Read data from the object representing data into a datacluster
	 *
	 * @param source
	 *            the source object containing data
	 * @return the datacluster
	 * @throws IOException
	 */
	public DataCluster read(T source) throws IOException;
}
