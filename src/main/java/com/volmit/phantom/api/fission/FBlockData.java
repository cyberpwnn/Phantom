package com.volmit.phantom.api.fission;

import org.bukkit.Material;

import com.volmit.phantom.util.world.MaterialBlock;

public class FBlockData
{
	private final int id;
	private final byte data;

	public FBlockData(MaterialBlock mb)
	{
		this(mb.getMaterial(), mb.getData());
	}

	@SuppressWarnings("deprecation")
	public FBlockData(Material id, byte data)
	{
		this(id.getId(), data);
	}

	public FBlockData(int id, byte data)
	{
		this.id = id;
		this.data = data;
	}

	@SuppressWarnings("deprecation")
	public Material getMaterial()
	{
		return Material.getMaterial(id);
	}

	public int getId()
	{
		return id;
	}

	public byte getData()
	{
		return data;
	}
}
