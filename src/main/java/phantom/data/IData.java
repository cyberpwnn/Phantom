package phantom.data;

import java.io.IOException;

/**
 * Represents an object which is capable of reading and writing bytes to
 * attributes
 *
 * @author cyberpwn
 */
public interface IData
{
	/**
	 * Construct data in this object from bytes given
	 *
	 * @param data
	 *            the bytes representing this object
	 * @throws IOException
	 *             shit happens
	 */
	public void fromBytes(byte[] data) throws IOException;

	/**
	 * Write this object's data to bytes
	 *
	 * @return the bytes
	 * @throws IOException
	 *             shit happens
	 */
	public byte[] toBytes() throws IOException;
}
