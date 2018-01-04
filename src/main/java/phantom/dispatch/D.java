package phantom.dispatch;

import org.phantomapi.Phantom;

import phantom.lang.GQueue;
import phantom.text.C;
import phantom.util.metrics.Documented;

/**
 * Dispatcher used for logging
 *
 * @author cyberpwn
 */
@Documented
public class D
{
	private static final GQueue<String> sendBuffer = new GQueue<String>();
	private String header;

	/**
	 * Create a dispatcher
	 *
	 * @param header
	 *            the messenger source name
	 */
	public D(String header)
	{
		this.header = header;
	}

	/**
	 * Logging INFO
	 *
	 * @param message
	 *            the message
	 */
	public void l(String message)
	{
		l(header, message);
	}

	/**
	 * Logging WARN
	 *
	 * @param message
	 *            the message
	 */
	public void w(String message)
	{
		w(header, message);
	}

	/**
	 * Logging FAIL
	 *
	 * @param message
	 *            the message
	 */
	public void f(String message)
	{
		f(header, message);
	}

	/**
	 * Logging VERBOSE
	 *
	 * @param message
	 *            the message
	 */
	public void v(String message)
	{
		v(header, message);
	}

	/**
	 * Logging INFO
	 *
	 * @param header
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void l(String header, String message)
	{
		log(DispatchChannel.INFO, header, message);
	}

	/**
	 * Logging WARN
	 *
	 * @param header
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void w(String header, String message)
	{
		log(DispatchChannel.WARN, header, message);
	}

	/**
	 * Logging FAIL
	 *
	 * @param header
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void f(String header, String message)
	{
		log(DispatchChannel.FAIL, header, message);
	}

	/**
	 * Logging VERBOSE
	 *
	 * @param header
	 *            the tag
	 * @param message
	 *            the message
	 */
	public static void v(String header, String message)
	{
		log(DispatchChannel.VERBOSE, header, message);
	}

	private static void log(DispatchChannel channel, String header, String message)
	{
		String ak = "";

		if(!Phantom.isServerThread())
		{
			ak = C.AQUA + "|" + C.DARK_GRAY + "A";
		}

		String compiledMessage = ak + channel.toString() + "|" + C.DARK_GRAY + C.stripColor(header) + ": " + (channel == DispatchChannel.VERBOSE ? C.GRAY : channel.toString()) + message;
		sendBuffer.offer(compiledMessage);

		if(Phantom.isServerThread())
		{
			flush();
		}
	}

	/**
	 * Flush the send buffer. The send buffer is automagically flushed every time a
	 * log is dispatched on the main thread, flushing any async messages. It is also
	 * flushed 4 times a second IF the thread pool service is online (there is no
	 * point to flush it otherwise)
	 */
	public static void flush()
	{
		while(!sendBuffer.isEmpty())
		{
			String message = sendBuffer.poll();
			Phantom.sendConsoleMessage(message);
		}
	}
}
