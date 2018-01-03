package phantom.scheduler;

import org.phantomapi.Phantom;
import phantom.util.metrics.Profiler;

public abstract class S implements ITask, ICancellable
{
	private int id;
	private boolean repeating;
	private double computeTime;
	private double totalComputeTime;
	private double activeTime;
	private boolean completed;
	private Profiler profiler;
	private Profiler activeProfiler;
	protected int ticks;

	public S()
	{
		this(0);
	}

	public S(int delay)
	{
		setup(true);
		profiler.begin();

		id = Phantom.startTask(delay, new Runnable()
		{
			@Override
			public void run()
			{
				activeProfiler.begin();
				S.this.run();
				ticks++;
				activeProfiler.end();
				computeTime = activeProfiler.getMilliseconds();
				totalComputeTime += computeTime;
				profiler.end();
				activeTime += profiler.getMilliseconds();
				activeProfiler.reset();
				profiler.reset();
				profiler.begin();
			}
		});
	}

	private void setup(boolean r)
	{
		profiler = new Profiler();
		activeProfiler = new Profiler();
		repeating = r;
		completed = false;
		computeTime = 0;
		activeTime = 0;
		totalComputeTime = 0;
		ticks = 0;
	}

	@Override
	public void cancel()
	{
		Phantom.stopTask(id);
		completed = true;
		profiler.end();
		activeTime += profiler.getMilliseconds();
		profiler.reset();
		activeProfiler.reset();
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public boolean isRepeating()
	{
		return repeating;
	}

	@Override
	public double getComputeTime()
	{
		return computeTime;
	}

	@Override
	public boolean hasCompleted()
	{
		return completed;
	}

	@Override
	public double getTotalComputeTime()
	{
		return totalComputeTime;
	}

	@Override
	public double getActiveTime()
	{
		return activeTime;
	}
}