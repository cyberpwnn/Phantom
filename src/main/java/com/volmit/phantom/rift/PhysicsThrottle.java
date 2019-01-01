package com.volmit.phantom.rift;

import java.util.Iterator;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.HashTreeSet;

import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.V;

import net.minecraft.server.v1_12_R1.NextTickListEntry;
import net.minecraft.server.v1_12_R1.WorldServer;

public class PhysicsThrottle
{
	private World world;
	private int delay;
	private int reschedule;
	private GMap<NextTickListEntry, Integer> delayedEntries;
	private GList<NextTickListEntry> delayedOrder;
	private HashTreeSet<NextTickListEntry> t;

	public PhysicsThrottle(World world)
	{
		this.world = world;
		delay = 0;
		reschedule = 0;
		delayedEntries = new GMap<>();
		delayedOrder = new GList<>();
		t = getTickList();
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	public void dumpTicklist()
	{
		try
		{
			GMap<Integer, GList<NextTickListEntry>> v = delayedEntries.flip();
			delayedEntries.clear();
			delayedOrder.clear();

			for(GList<NextTickListEntry> i : v.sortV().reverse())
			{
				t.addAll(i);
			}
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public void tick()
	{
		if(delay > 0 && reschedule <= 0)
		{
			Iterator<NextTickListEntry> it = t.iterator();

			while(it.hasNext())
			{
				NextTickListEntry e = it.next();
				it.remove();
				delayedEntries.put(e, delay);
				delayedOrder.add(e);
			}
		}

		if(delayedEntries.size() > 0)
		{
			for(NextTickListEntry i : delayedOrder.copy())
			{
				try
				{
					if(delayedEntries.get(i) <= 0)
					{
						t.add(i);
						delayedEntries.remove(i);
						delayedOrder.remove(i);
					}

					else
					{
						delayedEntries.put(i, delayedEntries.get(i) - 1);
					}
				}

				catch(Throwable e)
				{
					delayedEntries.remove(i);
					delayedOrder.remove(i);
				}
			}
		}

		reschedule--;
	}

	public HashTreeSet<NextTickListEntry> getTickList()
	{
		CraftWorld w = (CraftWorld) world;
		WorldServer ws = new V(w).get("world");
		HashTreeSet<NextTickListEntry> set = new V(ws).get("nextTickList");

		return set;
	}
}
