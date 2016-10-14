package org.phantomapi.network;

import org.phantomapi.lang.GList;
import org.phantomapi.util.Average;

/**
 * Used for downloading multiple files
 * 
 * @author cyberpwn
 */
public class MultithreadedFileQueue
{
	private GList<FileDownload> downloads;
	
	/**
	 * Create a new queue
	 */
	public MultithreadedFileQueue()
	{
		this.downloads = new GList<FileDownload>();
	}
	
	/**
	 * Start downloads
	 */
	public void start()
	{
		for(FileDownload i : downloads)
		{
			i.start();
		}
	}
	
	/**
	 * Queue some downloads
	 * 
	 * @param download
	 *            the downloads or download
	 */
	public void queue(FileDownload... download)
	{
		downloads.add(downloads);
	}
	
	/**
	 * Is the queue currently downloading
	 * 
	 * @return true if there are still active downloads
	 */
	public boolean isDownloading()
	{
		return getActiveDownloads() != 0;
	}
	
	/**
	 * Get the download percent averaged across all downloads
	 * 
	 * @return the averaged percent
	 */
	public double getPercent()
	{
		Average percents = new Average(-1);
		
		for(FileDownload i : downloads)
		{
			percents.put(i.getPercent());
		}
		
		return percents.getAverage();
	}
	
	/**
	 * Get the estimate time remaining
	 * 
	 * @return the estimated time remaining
	 */
	public double getRemainingTime()
	{
		Average percents = new Average(-1);
		
		for(FileDownload i : downloads)
		{
			percents.put(i.getRemainingTime());
		}
		
		return percents.getAverage();
	}
	
	/**
	 * Get the total bytes per second download speed
	 * 
	 * @return the bytes per second
	 */
	public double getBytesPerSecond()
	{
		double bps = 0;
		
		for(FileDownload i : downloads)
		{
			bps += i.getBytesPerSecond();
		}
		
		return bps;
	}
	
	/**
	 * Get the downloaded size in total
	 * 
	 * @return the total amount of bytes downloaded
	 */
	public long getDownloadedSize()
	{
		long bps = 0;
		
		for(FileDownload i : downloads)
		{
			bps += i.getDownloadedSize();
		}
		
		return bps;
	}
	
	/**
	 * Get the total size of all downloads
	 * 
	 * @return the total size
	 */
	public long getTotalSize()
	{
		long bps = 0;
		
		for(FileDownload i : downloads)
		{
			bps += i.getTotalSize();
		}
		
		return bps;
	}
	
	/**
	 * Get the count active downloads
	 * 
	 * @return the active download size
	 */
	public int getActiveDownloads()
	{
		int c = 0;
		
		for(FileDownload i : downloads)
		{
			if(i.isDownloading())
			{
				c++;
			}
		}
		
		return c;
	}
	
	/**
	 * Get the number of failed downloads
	 * 
	 * @return the failed downloads
	 */
	public int getFailedDownloads()
	{
		int c = 0;
		
		for(FileDownload i : downloads)
		{
			if(i.hasFailed())
			{
				c++;
			}
		}
		
		return c;
	}
	
	/**
	 * Get the number of completed or failed downloads
	 * 
	 * @return the failed/completed downloads
	 */
	public int getCompletedDownloads()
	{
		int c = 0;
		
		for(FileDownload i : downloads)
		{
			if(i.isComplete())
			{
				c++;
			}
		}
		
		return c;
	}
	
	/**
	 * Get the queue size
	 * 
	 * @return the size of downloads in the queue
	 */
	public int queueSize()
	{
		return downloads.size();
	}
	
	/**
	 * Get the percent completed in the queue
	 * 
	 * @return the completed percent
	 */
	public double getQueuePercent()
	{
		return (double) ((double) getCompletedDownloads() / (double) queueSize());
	}
	
	/**
	 * Get the downloads
	 * 
	 * @return the downloads
	 */
	public GList<FileDownload> getDownloads()
	{
		return downloads;
	}
}
