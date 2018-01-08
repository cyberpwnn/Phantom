package phantom.data.cluster;

import phantom.util.metrics.Documented;

/**
 * For data clusters to allow short code conversion
 *
 * @author cyberpwn
 */
@Documented
public interface IShortcodeKeyed
{
	/**
	 * Get the shortcoded key for the actual key
	 *
	 * @param key
	 *            the key
	 * @return the shortcoded key
	 */
	public String getShortcodedKey(String key);

	/**
	 * Get the real key back from the shortcoded key
	 *
	 * @param shortcodedKey
	 *            the shortcoded key
	 * @return the real key
	 */
	public String getRealKeyFromShortcoded(String shortcodedKey);

	/**
	 * Get the shortcode from a shortcoded key
	 *
	 * @param shortcodedKey
	 *            the key
	 * @return the shortcode
	 */
	public String getShortCodeFromKey(String shortcodedKey);
}
