package com.volmit.phantom.api.intercom;

public class Intercom
{
	public static IntercomCoordinator createServer(String name, int port, String token)
	{
		try
		{
			return new ServerIntercom(name, 16, port, token).begin();
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static ClientIntercom createClient(String name, String address, int port, String token)
	{
		try
		{
			return new ClientIntercom(name, name, address, port, token).begin();
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
