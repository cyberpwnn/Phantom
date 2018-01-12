package phantom.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.phantomapi.Phantom;

import phantom.math.M;
import phantom.pawn.IPawn;
import phantom.pawn.Name;
import phantom.pawn.Stop;
import phantom.util.metrics.Documented;

/**
 * Download object used for downloading objects...
 * 
 * @author cyberpwn
 */
@Documented
@Name("Download")
public class Download extends Thread implements IPawn
{
	private static int dln = 0;
	private File destination;
	private URL url;
	private IDownloadMonitor monitor;
	private boolean running;

	/**
	 * Create a download
	 * 
	 * @param url
	 *            the url to download from
	 * @param destination
	 *            the destination to download to
	 * @param monitor
	 *            the monitor to monitor this download
	 */
	public Download(URL url, File destination, IDownloadMonitor monitor)
	{
		this.destination = destination;
		this.url = url;
		this.monitor = monitor;
		setName("Download #" + dln++);
		Phantom.activate(this);
	}

	@Stop
	public void shutdown()
	{
		running = false;
		cancel();
	}

	/**
	 * Create a download
	 * 
	 * @param url
	 *            the url to download from
	 * @param destination
	 *            the destination to download to
	 */
	public Download(URL url, File destination)
	{
		this(url, destination, new UselessDownloadMonitor());
	}

	public void start()
	{
		running = true;
		super.start();
	}

	public static int getDln()
	{
		return dln;
	}

	public static void setDln(int dln)
	{
		Download.dln = dln;
	}

	public File getDestination()
	{
		return destination;
	}

	public void setDestination(File destination)
	{
		this.destination = destination;
	}

	public URL getUrl()
	{
		return url;
	}

	public void setUrl(URL url)
	{
		this.url = url;
	}

	public IDownloadMonitor getMonitor()
	{
		return monitor;
	}

	public void setMonitor(IDownloadMonitor monitor)
	{
		this.monitor = monitor;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public void run()
	{
		try
		{
			byte[] buffer = new byte[8192];
			long size = URLUtils.getFileSize(url);
			long start = M.ms();
			long startb = M.ms();
			double progress = -1;
			int read = 0;
			long downloaded = 0;
			double bytesPerSecond = 0;
			double timeLeft = -1;
			long elapsed = 0;
			FileOutputStream fos = new FileOutputStream(destination);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			monitor.onDownloadStarted(size);

			while(running && (read = in.read(buffer)) != -1)
			{
				fos.write(buffer, 0, read);
				downloaded += read;
				elapsed = M.ms() - start;

				if(M.ms() - startb > 50)
				{
					startb = M.ms();
					bytesPerSecond = (downloaded / elapsed) * 1000.0;
					progress = size >= 0 ? ((double) downloaded / (double) size) : progress;
					timeLeft = 0;
					monitor.onDownloadProgress(progress, bytesPerSecond, downloaded, timeLeft, elapsed);
				}
			}

			in.close();
			fos.close();
			monitor.onDownloadFinished(elapsed);

			if(running)
			{
				Phantom.deactivate(this);
			}
		}

		catch(Exception e)
		{
			monitor.onDownloadFailed(e);
			Phantom.deactivate(this);
		}
	}

	public void cancel()
	{
		running = false;
	}
}
