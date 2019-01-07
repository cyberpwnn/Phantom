package com.volmit.phantom.services;

import java.util.List;

import org.bukkit.Chunk;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.FaweQueue.RelightMode;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.plugin.AsyncTickService;
import com.volmit.phantom.time.M;
import com.volmit.phantom.util.Cuboid;

public class LightSVC extends AsyncTickService
{
	private GList<Chunk> queue;

	public LightSVC()
	{
		super(5);
		queue = new GList<Chunk>();
	}

	@Override
	public void onBegin()
	{

	}

	@Override
	public void onEnd()
	{

	}

	@Override
	public void onAsyncTick()
	{
		try
		{
			if(!queue.isEmpty())
			{
				if(M.interval(23))
				{
					l("Relight Queue: " + F.f(queue.size()));
				}

				for(int i = 0; i < 7; i++)
				{
					if(queue.isEmpty())
					{
						break;
					}

					relightNow(queue.popRandom());
				}
			}
		}

		catch(Throwable e)
		{

		}
	}

	public void relight(Cuboid c)
	{
		relight(c.getChunks());
	}

	public void relight(List<Chunk> c)
	{
		queue.addAll(c);
	}

	public void relight(Chunk c)
	{
		queue.add(c);
	}

	@SuppressWarnings("deprecation")
	private void relightNow(Chunk pop)
	{
		queue.remove(pop);
		FaweAPI.fixLighting(pop.getWorld().getName(), new CuboidRegion(new Vector(pop.getX() << 4, 0, pop.getZ() << 4), new Vector((pop.getX() << 4) + 15, 0, (pop.getZ() << 4) + 15)), RelightMode.OPTIMAL);
	}
}
