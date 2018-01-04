package phantom.util.metrics;

import phantom.lang.format.F;

/**
 * Profiles things with a begin and end in nanos.
 *
 * @author cyberpwn
 */
@Documented
public class Profiler
{
	private long nanos;
	private long startNano;
	private long millis;
	private long startMillis;
	private double time;
	private boolean profiling;

	/**
	 * Create a new profiler
	 */
	public Profiler()
	{
		reset();
		profiling = false;
	}

	/**
	 * Begin time mark. Use end() to end the bench and get results
	 */
	public void begin()
	{
		profiling = true;
		startNano = System.nanoTime();
		startMillis = System.currentTimeMillis();
	}

	/**
	 * End the profiler. You can now access the results
	 */
	public void end()
	{
		if(!profiling)
		{
			return;
		}

		profiling = false;
		nanos = System.nanoTime() - startNano;
		millis = System.currentTimeMillis() - startMillis;
		time = (double) nanos / 1000000.0;
		time = (double) millis - time > 1.01 ? millis : time;
	}

	/**
	 * Reset the profilers metrics
	 */
	public void reset()
	{
		nanos = -1;
		millis = -1;
		startNano = -1;
		startMillis = -1;
		time = -0;
		profiling = false;
	}

	/**
	 * Get the time (measurement determined by number) in accuracy.
	 *
	 * @param dec
	 *            the amount of decimal places to use
	 * @return
	 */
	public String getTime(int dec)
	{
		if(getNanoseconds() < 1000.0)
		{
			return F.f(getNanoseconds()) + "ns";
		}

		if(getMilliseconds() < 1000.0)
		{
			return F.f(getMilliseconds(), dec) + "ms";
		}

		if(getSeconds() < 60.0)
		{
			return F.f(getSeconds(), dec) + "s";
		}

		if(getMinutes() < 60.0)
		{
			return F.f(getMinutes(), dec) + "m";
		}

		return F.f(getHours(), dec) + "h";
	}

	/**
	 * Get the time measured in ticks
	 *
	 * @return ticks
	 */
	public double getTicks()
	{
		return getMilliseconds() / 50.0;
	}

	/**
	 * Get the time measured in seconds
	 * @return seconds
	 */
	public double getSeconds()
	{
		return getMilliseconds() / 1000.0;
	}

	/**
	 * Get the time measured in minutes
	 * @return minutes
	 */
	public double getMinutes()
	{
		return getSeconds() / 60.0;
	}

	/**
	 * Get the time measured in hours
	 * @return hours
	 */
	public double getHours()
	{
		return getMinutes() / 60.0;
	}

	/**
	 * Get the time measured in milliseconds (double)
	 * @return milliseconds derived from nanotime
	 */
	public double getMilliseconds()
	{
		return time;
	}

	/**
	 * Get the time measured in nanoseconds. NOTE: May be inaccurate for longer benchmarks confirm accuracy with milliseconds if unsure.
	 * @return nanos
	 */
	public long getNanoseconds()
	{
		return (long) (time * 1000000.0);
	}

	/**
	 * Get nanos
	 * @return the nanos
	 */
	public long getNanos()
	{
		return nanos;
	}

	public long getStartNano()
	{
		return startNano;
	}

	public long getMillis()
	{
		return millis;
	}

	public long getStartMillis()
	{
		return startMillis;
	}

	public double getTime()
	{
		return time;
	}

	public boolean isProfiling()
	{
		return profiling;
	}
}
