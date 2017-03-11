package org.phantomapi.filesystem;

import java.io.File;

public class IPatch extends IOP
{
	private File target;
	private String key;
	private String value;
	
	public IPatch(FileHack h, File target, String key, String value)
	{
		super(h);
		
		this.target = target;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public void operate()
	{
		if(target.isDirectory())
		{
			for(File i : target.listFiles())
			{
				new IPatch(h, i, key, value).operate();
			}
			
			return;
		}
		
		try
		{
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
