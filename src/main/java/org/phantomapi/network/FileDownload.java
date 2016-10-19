package org.phantomapi.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.phantomapi.async.A;
import org.phantomapi.lang.GTime;
import org.phantomapi.sync.S;
import org.phantomapi.sync.Task;
import org.phantomapi.util.Average;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.util.T;

/**
 * File async download with progress and status
 * 
 * @author cyberpwn
 */
public abstract class FileDownload
{
	private final URL source;
	private final File destination;
	private double percent;
	private boolean complete;
	private boolean downloading;
	private boolean failed;
	private long downloadedSize;
	private long totalSize;
	private long startTime;
	private long finishTime;
	private long elapsedTime;
	private long remainingTime;
	private Average remainingAverage;
	private double bytesPerSecond;
	
	/**
	 * Create a new file download (starts immediately)
	 * 
	 * @param source
	 *            the source url
	 * @param destination
	 *            the destination file path
	 */
	public FileDownload(URL source, File destination)
	{
		this.source = source;
		this.destination = destination;
		percent = -1;
		downloading = false;
		complete = false;
		failed = false;
		startTime = M.ms();
		finishTime = M.ms() - 1000;
		elapsedTime = 0;
		remainingTime = 1;
		remainingAverage = new Average(4);
	}
	
	/**
	 * Begin the download
	 */
	public void start()
	{
		new A()
		{
			@Override
			public void async()
			{
				BufferedInputStream in = null;
				FileOutputStream out = null;
				
				try
				{
					URLConnection conn = source.openConnection();
					int size = conn.getContentLength();
					byte data[] = new byte[1024];
					int count;
					double sumCount = 0;
					in = new BufferedInputStream(source.openStream());
					out = new FileOutputStream(destination);
					downloading = true;
					bytesPerSecond = 0;
					elapsedTime = 0;
					remainingTime = 1;
					totalSize = size;
					
					while((count = in.read(data, 0, 1024)) != -1)
					{
						out.write(data, 0, count);
						sumCount += count;
						bytesPerSecond = sumCount / (((double) M.ms() - (double) startTime) / 1000.0);
						elapsedTime = M.ms() - startTime;
						downloadedSize = (long) sumCount;
						
						if(size > 0)
						{
							percent = sumCount / size;
							remainingTime = (long) ((elapsedTime / percent - elapsedTime + bytesPerSecond / size * 1000) / 2);
							remainingAverage.put(remainingTime);
						}
					}
				}
				
				catch(MalformedURLException e)
				{
					failed = true;
					ExceptionUtil.print(e);
				}
				
				catch(IOException e)
				{
					failed = true;
					ExceptionUtil.print(e);
				}
				
				finally
				{
					if(in != null)
					{
						try
						{
							in.close();
						}
						
						catch(IOException e)
						{
							ExceptionUtil.print(e);
						}
					}
					
					if(out != null)
					{
						try
						{
							out.close();
						}
						
						catch(IOException e)
						{
							ExceptionUtil.print(e);
						}
					}
					
					downloading = false;
					complete = true;
					bytesPerSecond = 0;
				}
				
				new S()
				{
					@Override
					public void sync()
					{
						finishTime = M.ms();
						onCompleted();
					}
				};
			}
		};
	}
	
	/**
	 * Called when the download has completed or failed
	 */
	public abstract void onCompleted();
	
	/**
	 * Get the url source for this download
	 * 
	 * @return the url
	 */
	public URL getSource()
	{
		return source;
	}
	
	/**
	 * Get the file destination for this download
	 * 
	 * @return the file destination
	 */
	public File getDestination()
	{
		return destination;
	}
	
	/**
	 * Get the percent complete on this download
	 * 
	 * @return the download percent
	 */
	public double getPercent()
	{
		return percent;
	}
	
	/**
	 * Has the download completed?
	 * 
	 * @return true if the download has finished or failed
	 */
	public boolean isComplete()
	{
		return complete;
	}
	
	/**
	 * Is the download currently downloading
	 * 
	 * @return true if it is
	 */
	public boolean isDownloading()
	{
		return downloading;
	}
	
	/**
	 * Has the download failed?
	 * 
	 * @return true if it has failed either ioexception, network issues or
	 *         invalid url
	 */
	public boolean hasFailed()
	{
		return failed;
	}
	
	/**
	 * Get the download start time
	 * 
	 * @return the start time
	 */
	public long getStartTime()
	{
		return startTime;
	}
	
	/**
	 * Get the finish time
	 * 
	 * @return the time the download finished or the start time - 1000
	 */
	public long getFinishTime()
	{
		return finishTime;
	}
	
	/**
	 * Returns the time it took to download.
	 * 
	 * @return the time it took to download or -1000 if the download has not yet
	 *         finished or the download failed
	 */
	public long getElapsedTime()
	{
		return elapsedTime;
	}
	
	/**
	 * Get the bytes per second transfer speed
	 * 
	 * @return the bytes per second
	 */
	public double getBytesPerSecond()
	{
		return bytesPerSecond;
	}
	
	/**
	 * Get the estimate remaining time
	 * 
	 * @return the remaining time
	 */
	public double getRemainingTime()
	{
		return remainingAverage.getAverage();
	}
	
	/**
	 * Get the amount of bytes downloaded
	 * 
	 * @return the bytes downloaded
	 */
	public long getDownloadedSize()
	{
		return downloadedSize;
	}
	
	/**
	 * Get the size of this remote file
	 * 
	 * @return the size in bytes
	 */
	public long getTotalSize()
	{
		return totalSize;
	}
	
	public static void download(URL url, File file)
	{
		D d = new D("Downloading " + file.getName());
		
		T t = new T()
		{
			@Override
			public void onStop(long nsTime, double msTime)
			{
				d.s("Finished Downloading " + file.getName() + " in " + new GTime((long) msTime));
			}
		};
		
		FileDownload fd = new FileDownload(url, file)
		{
			@Override
			public void onCompleted()
			{
				t.stop();
			}
		};
		
		fd.start();
		
		new Task(20)
		{
			@Override
			public void run()
			{
				if(fd.isComplete())
				{
					cancel();
					return;
				}
				
				if(fd.isDownloading())
				{
					d.v("@ " + F.fileSize((long) fd.getBytesPerSecond()) + "/s " + C.AQUA + new GTime((long) fd.getRemainingTime()).to() + "left");
				}
			}
		};
	}
}
