package com.volmit.phantom.util.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

import com.volmit.phantom.util.plugin.Alphabet;

public class ExecutorUtil
{
	public static ExecutorService createForkJoinPool(String prefix, boolean alpha, int count, UncaughtExceptionHandler handler)
	{
		return new ForkJoinPool(count, new ForkJoinWorkerThreadFactory()
		{
			@Override
			public ForkJoinWorkerThread newThread(ForkJoinPool pool)
			{
				final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
				worker.setName(prefix + (alpha ? Alphabet.values()[worker.getPoolIndex()] : (worker.getPoolIndex() + 1)));
				worker.setPriority(Thread.MIN_PRIORITY);
				return worker;
			}
		}, handler, true);
	}
}
