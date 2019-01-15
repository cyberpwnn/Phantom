package com.volmit.phantom.lib.service;

import org.bukkit.World;

import com.volmit.phantom.api.fission.FBlockData;
import com.volmit.phantom.api.fission.FissionQueue;
import com.volmit.phantom.api.fission.SCuboid;
import com.volmit.phantom.api.fission.Selection;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.service.Service;
import com.volmit.phantom.util.world.Cuboid;
import com.volmit.phantom.util.world.MaterialBlock;

public class FissionSVC extends Service
{
	private GMap<World, FissionQueue> modifiers;

	@Override
	public void onStart()
	{
		modifiers = new GMap<>();
	}

	public FissionQueue set(Cuboid s, MaterialBlock b)
	{
		FissionQueue q = getModifier(s.getWorld());
		q.queue(new SCuboid(s), new FBlockData(b));
		q.flush();
		return q;
	}

	public FissionQueue set(Selection s, World w, MaterialBlock b)
	{
		FissionQueue q = getModifier(w);
		q.queue(s, new FBlockData(b));
		q.flush();
		return q;
	}

	public FissionQueue getModifier(World world)
	{
		if(!modifiers.containsKey(world))
		{
			modifiers.put(world, new FissionQueue(world));
		}

		return modifiers.get(world);
	}

	@Override
	public void onStop()
	{

	}
}
