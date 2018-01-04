package phantom.dispatch;

/**
 * Phantom dispatcher
 *
 * @author cyberpwn
 */
public class PD
{
	private static final D pd = new D("Phantom");

	/**
	 * Info
	 *
	 * @param message
	 *            the msg
	 */
	public static void l(String message)
	{
		pd.l(message);
	}

	/**
	 * Warn
	 *
	 * @param message
	 *            the msg
	 */
	public static void w(String message)
	{
		pd.w(message);
	}

	/**
	 * Fail
	 *
	 * @param message
	 *            the msg
	 */
	public static void f(String message)
	{
		pd.f(message);
	}

	/**
	 * Verbose
	 *
	 * @param message
	 *            the msg
	 */
	public static void v(String message)
	{
		pd.v(message);
	}
}
