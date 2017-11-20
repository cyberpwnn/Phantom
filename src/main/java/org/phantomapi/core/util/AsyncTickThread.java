package org.phantomapi.core.util;

public class AsyncTickThread extends Thread
{
	private Runnable r;

	public AsyncTickThread(Runnable r)
	{
		this.r = r;
	}

	@Override
	public void run()
	{
		while(!interrupted())
		{
			try
			{
				r.run();
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
}
