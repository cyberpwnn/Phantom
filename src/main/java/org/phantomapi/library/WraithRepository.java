package org.phantomapi.library;

import java.net.MalformedURLException;
import java.net.URL;

public class WraithRepository implements Repository
{
	private URL url;
	
	public WraithRepository(URL url)
	{
		this.url = url;
	}
	
	@Override
	public URL getUrl()
	{
		return url;
	}
	
	@Override
	public URL getUrl(Coordinate c)
	{
		try
		{
			return new URL(getUrl().toString() + "/" + c.getEffectivePath() + "/" + c.getEffectiveName());
		}
		
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		return getUrl();
	}
}
