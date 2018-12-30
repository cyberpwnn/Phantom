package com.volmit.phantom.nms;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.GSet;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.NMSSVC;

public class ChunkTracker
{
	private GMap<Chunk, GSet<Integer>> ig;

	public ChunkTracker()
	{
		ig = new GMap<Chunk, GSet<Integer>>();
	}

	public void hit(Location l)
	{
		if(!ig.containsKey(l.getChunk()))
		{
			ig.put(l.getChunk(), new GSet<Integer>());
		}

		ig.get(l.getChunk()).add(l.getBlockY() >> 4);
	}

	public void flush()
	{
		for(Chunk i : ig.k())
		{
			for(int j : ig.get(i))
			{
				SVC.get(NMSSVC.class).queueSection(i, j);
			}
		}

		ig.clear();
	}
}
