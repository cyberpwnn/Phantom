package com.volmit.phantom.api.fission;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.World;
import org.bukkit.event.Listener;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GSet;
import com.volmit.phantom.api.lang.Profiler;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.util.concurrent.ExecutorUtil;
import com.volmit.phantom.util.nms.ChunkSendQueue;
import com.volmit.phantom.util.nms.WorldModifier;
import com.volmit.phantom.util.queue.PhantomQueue;
import com.volmit.phantom.util.queue.Queue;

public class FissionQueue implements Listener
{
	private CountDownLatch latch;
	private static ExecutorService svc;
	private boolean running;
	private World world;
	private Queue<Runnable> editQueue;
	private GSet<FChunk> keepLoaded;
	private WorldModifier mod;
	private int concurrency;
	private D d;
	private ChunkSendQueue q;
	private volatile double totalComputeTime;
	private volatile double chunkWaitTime;

	public FissionQueue(World world)
	{
		concurrency = 48;
		q = new ChunkSendQueue(5, 3);
		d = new D("FissionQueue<" + world.getName() + ">");
		editQueue = new PhantomQueue<Runnable>();
		keepLoaded = new GSet<>();
		this.world = world;
		mod = new WorldModifier(world);
		running = false;
		q.start();
	}

	public GSet<FChunk> getKeepLoaded()
	{
		return keepLoaded;
	}

	public void queue(Selection s, FBlockData data)
	{
		queueUp(s, data);
	}

	private void queueUp(Selection s, FBlockData data)
	{
		s.getChunks(concurrency * 3, (xxx) -> editQueue.queue(() ->
		{
			for(FChunk i : xxx)
			{
				J.s(() -> i.toBukkit(world).load());
			}

			for(FChunk chunk : xxx)
			{
				Profiler px = new Profiler();
				px.begin();
				waitForChunk(chunk);
				px.end();
				chunkWaitTime += px.getMilliseconds();
				Selection chs = s.getSelection(chunk);
				px = new Profiler();
				px.begin();
				chs.apply(mod, data, chunk, q, world);
				px.end();
				totalComputeTime += px.getMilliseconds();
				latch.countDown();
				keepLoaded.remove(chunk);
				J.ls(() -> chunk.toBukkit(world).unload());
			}
		}));
	}

	private void waitForChunk(FChunk c)
	{
		if(keepLoaded.contains(c) && c.toBukkit(world).isLoaded())
		{
			return;
		}

		long since = M.ms();

		if(!keepLoaded.contains(c))
		{
			J.s(() -> syncLoadChunk(c));
		}

		while(!keepLoaded.contains(c) && M.ms() - since < 50)
		{
			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		if(!keepLoaded.contains(c))
		{
			waitForChunk(c);
		}
	}

	private void syncLoadChunk(FChunk c)
	{
		c.toBukkit(world).load();
		keepLoaded.add(c);
	}

	private void initService()
	{
		if(svc == null)
		{
			svc = ExecutorUtil.createForkJoinPool("Fission Reactor ", false, concurrency, new UncaughtExceptionHandler()
			{
				@Override
				public void uncaughtException(Thread t, Throwable e)
				{
					e.printStackTrace();
				}
			});
		}
	}

	public boolean isBusy()
	{
		return running;
	}

	@SuppressWarnings("deprecation")
	public void takeABreather()
	{
		for(FChunk i : new GList<>(keepLoaded))
		{
			if(!i.isLoaded(world))
			{
				keepLoaded.remove(i);
				continue;
			}

			if(!i.isUsed(world))
			{
				i.toBukkit(world).unload(true, false);
			}
		}
	}

	public void flush()
	{
		if(isBusy())
		{
			d.f("Cannot fluuh becuase its already busy.");
			return;
		}

		initService();
		running = true;
		J.a(() -> execute());

		while(isBusy())
		{
			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void execute()
	{
		chunkWaitTime = 0;
		totalComputeTime = 0;
		latch = new CountDownLatch(editQueue.size());
		d.w("Executing " + latch.getCount() + " tasks.");

		while(editQueue.hasNext())
		{
			execute(editQueue.next());
		}

		try
		{
			latch.await(5, TimeUnit.SECONDS);
		}

		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		J.s(() -> takeABreather(), 5);
		running = false;
	}

	public double getTotalComputeTime()
	{
		return totalComputeTime;
	}

	public double getChunkWaitTime()
	{
		return chunkWaitTime;
	}

	private void execute(Runnable r)
	{
		try
		{
			svc.submit(r);
		}

		catch(RejectedExecutionException e)
		{
			d.w("Rejected... Dont worry, i'll wait.");

			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e1)
			{
				e1.printStackTrace();
			}

			execute(r);
		}
	}
}
