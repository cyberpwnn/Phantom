package phantom.data.cluster;

import phantom.util.metrics.Documented;

/**
 * Generic getters for data clusters
 *
 * @author cyberpwn
 *
 */
@Documented
public interface IGenericTyped
{
	/**
	 * Get a string
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public String getString(String key);

	/**
	 * Get a double
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Double getDouble(String key);

	/**
	 * Get a float
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Float getFloat(String key);

	/**
	 * Get an int
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Integer getInteger(String key);

	/**
	 * Get a short
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Short getShort(String key);

	/**
	 * Get a long
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Long getLong(String key);

	/**
	 * Get a char
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Character getCharacter(String key);

	/**
	 * Get a boolean
	 *
	 * @param key
	 *            the key
	 * @return the object
	 */
	public Boolean getBoolean(String key);
}
