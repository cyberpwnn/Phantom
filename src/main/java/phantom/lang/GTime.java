package phantom.lang;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Time object for time
 *
 * @author cyberpwn
 *
 */
public class GTime implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final long days, hours, minutes, seconds, milliseconds;

	/**
	 * Create time
	 *
	 * @param d
	 *            days
	 * @param h
	 *            hours
	 * @param m
	 *            minutes
	 * @param s
	 *            seconds
	 * @param ms
	 *            millis
	 */
	public GTime(long d, long h, long m, long s, long ms)
	{
		this(ms + (s + m * 60 + h * 3600 + d * 86400) * 1000);
	}

	/**
	 * Create time
	 *
	 * @param duration
	 *            the duration in MS (use System.ctm for current)
	 */
	public GTime(long duration)
	{
		if(duration < 0)
		{
			throw new IllegalArgumentException("duration must be positive");
		}

		days = TimeUnit.MILLISECONDS.toDays(duration);
		hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(days);
		minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
		seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
		milliseconds = duration % 1000;
	}

	/**
	 * Create time
	 *
	 * @param duration
	 *            string duration of time (from tostring)
	 */
	public GTime(String duration)
	{
		duration = duration.replaceAll("(\\d)(\\D)", "$1 $2");

		String[] fields = duration.toLowerCase().split("\\s+");
		long total = 0;

		if(fields.length > 1)
		{
			if(fields.length % 2 == 1)
			{
				throw new IllegalArgumentException("Odd number of parameters in duration specification");
			}

			for(int i = 0; i < fields.length; i += 2)
			{
				total += Long.parseLong(fields[i]) * getMult(fields[i + 1]);
			}
		}

		else if(fields.length == 1)
		{
			total = Long.parseLong(fields[0]) * 1000;
		}

		else
		{
			throw new IllegalArgumentException("Empty duration specification");
		}

		GTime d = new GTime(total);
		days = d.getDays();
		hours = d.getHours();
		minutes = d.getMinutes();
		seconds = d.getSeconds();
		milliseconds = d.getMilliseconds();
	}

	private int getMult(String str)
	{
		if(str.startsWith("ms") || str.startsWith("mil"))
		{
			return 1;
		}

		else if(str.startsWith("s"))
		{
			return 1000;
		}

		else if(str.startsWith("m"))
		{
			return 60000;
		}

		else if(str.startsWith("h"))
		{
			return 3600000;
		}

		else if(str.startsWith("d"))
		{
			return 86400000;
		}

		else
		{
			throw new IllegalArgumentException("Unknown duration specifier " + str);
		}
	}

	public long getDays()
	{
		return days;
	}

	public long getHours()
	{
		return hours;
	}

	public long getMinutes()
	{
		return minutes;
	}

	public long getSeconds()
	{
		return seconds;
	}

	public long getMilliseconds()
	{
		return milliseconds;
	}

	public long getTotalDuration()
	{
		return milliseconds + (seconds + minutes * 60 + hours * 3600 + days * 86400) * 1000;
	}

	/**
	 * Describe time
	 *
	 * @return string time
	 */
	public String shortDescription()
	{
		if(days == 0 && milliseconds == 0)
		{
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}

		else if(days > 0 && milliseconds == 0)
		{
			return String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
		}

		else if(days == 0 && milliseconds > 0)
		{
			return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
		}

		else
		{
			return String.format("%dd%02d:%02d:%02d.%03d", days, hours, minutes, seconds, milliseconds);
		}
	}

	private String s(long x)
	{
		if(x == 1)
		{
			return "";
		}

		return "s";
	}

	/**
	 * short time rep
	 *
	 * @return string time
	 */
	public String to()
	{
		return to("");
	}

	/**
	 * Returns time with a suffix
	 *
	 * @param suff
	 *            the suffix
	 * @return the time
	 */
	public String to(String suff)
	{
		int months = (int) (days / 30);
		int years = (int)(months / 12);

		if(years > 0)
		{
			return years + " year" + s((int) (months / 12)) + " " + suff;
		}

		if(months > 0)
		{
			return months + " month" + s((int) (days / 30)) + " " + suff;
		}

		if(days > 0)
		{
			return days + " day" + s(days) + " " + suff;
		}

		if(hours > 0)
		{
			return hours + " hour" + s(hours) + " " + suff;
		}

		if(minutes > 0)
		{
			return minutes + " minute" + s(minutes) + " " + suff;
		}

		if(seconds > 0)
		{
			return seconds + " second" + s(seconds) + " " + suff;
		}

		if(milliseconds > 0)
		{
			return milliseconds + " ms" + " " + suff;
		}

		return "Instantly";
	}

	/**
	 * Time ago ex(4 hours ago)
	 *
	 * @return time ago
	 */
	public String ago()
	{
		if(days > 0)
		{
			return days + " day" + s(days) + " ago";
		}

		if(hours > 0)
		{
			return hours + " hour" + s(hours) + " ago";
		}

		if(minutes > 0)
		{
			return minutes + " minute" + s(minutes) + " ago";
		}

		if(seconds > 30)
		{
			return seconds + " second" + s(seconds) + " ago";
		}

		return "Just Now";
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		if(days > 0)
		{
			sb.append(days + " Day(s) ");
		}

		if(hours > 0)
		{
			sb.append(hours + " Hour(s) ");
		}

		if(minutes > 0)
		{
			sb.append(minutes + " Minute(s) ");
		}

		if(seconds > 0)
		{
			sb.append(seconds + " Second(s)");
		}

		return sb.toString().trim();
	}
}