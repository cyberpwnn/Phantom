package phantom.net;

import java.io.File;
import java.net.URL;

import org.phantomapi.Phantom;

import phantom.lang.GList;
import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Stop;
import phantom.util.metrics.Documented;

/**
 * Represents a download queue
 * 
 * @author cyberpwn
 */
@Documented
@Name("Download Queue")
public class DownloadQueue extends Thread implements IPawn
{
	private GList<Download> downloads;
	private boolean running;
	private long downloaded;
	private long size;
	private double progress;
	private double bps;

	public DownloadQueue()
	{
		downloads = new GList<Download>();
		running = true;
		this.downloaded = 0;
		this.size = 0;
		this.progress = 0;
		this.bps = 0;
	}

	@Stop
	public void shutdown()
	{
		this.interrupt();
		cancel();
	}

	/**
	 * Add a download to the queue
	 * 
	 * @param url
	 *            the url
	 * @param destination
	 *            the file destination
	 */
	public void add(URL url, File destination)
	{
		downloads.add(new Download(url, destination, new DownloadMonitor()));
	}

	/**
	 * Start downloading from the queue
	 */
	public void start()
	{
		super.start();

		for(Download i : downloads)
		{
			i.start();
		}

		Phantom.activate(this);
	}

	public void run()
	{
		while(running)
		{
			long d = 0;
			long s = 0;
			long b = 0;

			for(Download i : downloads)
			{
				d += ((DownloadMonitor) i.getMonitor()).getDownloaded();
				s += ((DownloadMonitor) i.getMonitor()).getSize();
				b += ((DownloadMonitor) i.getMonitor()).getBytesPerSecond();
			}

			downloaded = d;
			size = s;
			bps = b;
			progress = (double) downloaded / (double) size;

			try
			{
				Thread.sleep(50);
			}

			catch(InterruptedException e)
			{
				return;
			}
		}

		Phantom.deactivate(this);
	}

	/**
	 * Cancel the downloads
	 */
	public void cancel()
	{
		for(Download i : downloads)
		{
			Phantom.deactivate(i);
		}
		
		Phantom.deactivate(this);
		running = false;
	}

	public GList<Download> getDownloads()
	{
		return downloads;
	}

	public boolean isRunning()
	{
		return running;
	}

	public long getDownloaded()
	{
		return downloaded;
	}

	public long getSize()
	{
		return size;
	}

	public double getProgress()
	{
		return progress;
	}

	public double getBps()
	{
		return bps;
	}
}
