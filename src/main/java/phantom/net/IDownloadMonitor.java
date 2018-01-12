package phantom.net;

import phantom.util.metrics.Documented;

/**
 * Represents a download monitor
 * 
 * @author cyberpwn
 */
@Documented
public interface IDownloadMonitor
{
	/**
	 * Fired when a download fails
	 * 
	 * @param e
	 *            the exception thrown
	 */
	public void onDownloadFailed(Exception e);

	/**
	 * Fires 20 times a second
	 * 
	 * @param progress
	 *            the progress (percent)
	 * @param bytesPerSecond
	 *            the bytes per second
	 * @param downloaded
	 *            the downloaded size
	 * @param timeLeft
	 *            the time left
	 * @param elapsed
	 *            the elapsed time
	 */
	public void onDownloadProgress(double progress, double bytesPerSecond, long downloaded, double timeLeft, long elapsed);

	/**
	 * Fired when the download finished
	 * 
	 * @param elapsed
	 *            the total time elapsed
	 */
	public void onDownloadFinished(long elapsed);

	/**
	 * Fired when the download starts
	 * 
	 * @param size
	 *            the size of the download or -1 if the url has no head size
	 */
	public void onDownloadStarted(long size);
}
