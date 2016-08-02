package org.cyberpwn.phantom.async;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.cyberpwn.phantom.util.FU;

/**
 * Downloader
 * 
 * @author cyberpwn
 *
 */
public class Download extends Thread
{
	private URL url;
	private File path;
	private Callback<File> callback;
	
	/**
	 * Async download a file
	 * 
	 * @param url
	 *            the url
	 * @param path
	 *            the file
	 * @param callback
	 *            the callback
	 */
	public Download(URL url, File path, Callback<File> callback)
	{
		this.url = url;
		this.path = path;
		this.callback = callback;
	}
	
	public void run()
	{
		try
		{
			FU.copyURLToFile(url, path);
			callback.run(path);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
