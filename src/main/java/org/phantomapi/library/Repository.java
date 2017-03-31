package org.phantomapi.library;

import java.net.MalformedURLException;
import java.net.URL;

public interface Repository
{
	public URL getUrl();
	
	public URL getUrl(Coordinate c);
	
	public static Repository create(String url)
	{
		if(url.endsWith("/"))
		{
			url = url.substring(0, url.length() - 1);
		}
		
		try
		{
			return new WraithRepository(new URL(url));
		}
		
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
