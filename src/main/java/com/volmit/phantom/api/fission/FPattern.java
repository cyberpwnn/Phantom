package com.volmit.phantom.api.fission;

import com.volmit.phantom.api.math.M;

public class FPattern
{
	private final int[] id;
	private final byte[] data;

	public FPattern(int[] id, byte[] data)
	{
		this.id = id;
		this.data = data;
	}

	public int[] getId()
	{
		return id;
	}

	public byte[] getData()
	{
		return data;
	}

	public FBlockData getRandom()
	{
		int r = id[M.rand(0, id.length - 1)];
		return new FBlockData(id[r], data[r]);
	}
}
