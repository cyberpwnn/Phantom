package org.phantomapi.network;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.phantomapi.async.Callback;
import org.phantomapi.util.FU;

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
