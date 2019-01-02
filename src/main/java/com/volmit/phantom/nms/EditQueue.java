package com.volmit.phantom.nms;

import org.bukkit.Location;

import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.plugin.A;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.NMSSVC;
import com.volmit.phantom.util.MaterialBlock;

public class EditQueue
{
	private GMap<Location, MaterialBlock> queue;
	private boolean flushing;
	private long count;

	public EditQueue()
	{
		count = 0;
		flushing = false;
		queue = new GMap<Location, MaterialBlock>();
	}

	public void queue(Location l, MaterialBlock mb)
	{
		queue.put(l, mb);
		count++;
	}

	public void startFlushing()
	{
		flushing = true;

		new A()
		{
			@Override
			public void run()
			{
				while(flushing)
				{
					GMap<Location, MaterialBlock> p = new GMap<Location, MaterialBlock>(queue);

					for(Location i : p.keySet())
					{
						SVC.get(NMSSVC.class).setBlock(i, p.get(i));
						SVC.get(NMSSVC.class).queueChunkUpdate(i.getChunk());
						queue.remove(i);
					}

				}
			}
		};
	}

	public void complete()
	{
		flushing = false;
		flush();
	}

	public void flush()
	{
		for(Location i : queue.keySet())
		{
			SVC.get(NMSSVC.class).setBlock(i, queue.get(i));
			SVC.get(NMSSVC.class).queueChunkUpdate(i.getChunk());
		}

		queue.clear();
	}

	public long getCount()
	{
		return count;
	}
}
