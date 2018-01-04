package phantom.dispatch;

import phantom.text.C;

/**
 * Represents a dispatch channel
 *
 * @author cyberpwn
 */
public enum DispatchChannel
{
	/**
	 * Info
	 */
	INFO(C.WHITE),

	/**
	 * Warning
	 */
	WARN(C.YELLOW),

	/**
	 * Failure
	 */
	FAIL(C.RED),

	/**
	 * Verbose
	 */
	VERBOSE(C.LIGHT_PURPLE);

	private C text;

	private DispatchChannel(C text)
	{
		this.text = text;
	}

	/**
	 * Returns the channel color
	 */
	@Override
	public String toString()
	{
		return text.toString();
	}
}
