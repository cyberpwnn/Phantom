package phantom.dispatch;

import org.phantomapi.Phantom;

import phantom.lang.GQueue;
import phantom.text.C;

public class D
{
	private static final GQueue<String> sendBuffer = new GQueue<String>();
	private String header;

	public D(String header)
	{
		this.header = header;
	}

	public void l(String message)
	{
		l(header, message);
	}

	public void w(String message)
	{
		w(header, message);
	}

	public void f(String message)
	{
		f(header, message);
	}

	public void v(String message)
	{
		v(header, message);
	}

	public static void l(String header, String message)
	{
		log(DispatchChannel.INFO, header, message);
	}

	public static void w(String header, String message)
	{
		log(DispatchChannel.WARN, header, message);
	}

	public static void f(String header, String message)
	{
		log(DispatchChannel.FAIL, header, message);
	}

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

	public static void flush()
	{
		while(!sendBuffer.isEmpty())
		{
			String message = sendBuffer.poll();
			Phantom.sendConsoleMessage(message);
		}
	}
}
