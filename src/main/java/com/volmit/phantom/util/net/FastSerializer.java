package com.volmit.phantom.util.net;

import java.io.Serializable;

import org.nustaq.serialization.FSTConfiguration;

public class FastSerializer
{
	private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	public static byte[] serializeOffHeap(Serializable s, int[] length)
	{
		return conf.asSharedByteArray(s, length);
	}

	public static byte[] serialize(Serializable s)
	{
		return conf.asByteArray(s);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserialize(byte[] data)
	{
		return (T) conf.asObject(data);
	}
}
