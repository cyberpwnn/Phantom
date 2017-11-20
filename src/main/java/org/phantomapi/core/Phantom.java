package org.phantomapi.core;

public class Phantom
{
	private static ICorePlugin core;

	public static IGateway getGateway()
	{

		return core.getGateway();
	}
}
