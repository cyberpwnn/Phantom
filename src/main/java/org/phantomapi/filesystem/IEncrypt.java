package org.phantomapi.filesystem;

import java.io.File;

public class IEncrypt extends IOP
{
	private File target;
	private File destination;
	
	public IEncrypt(FileHack h, File target, File destination)
	{
		super(h);
		
		this.target = target;
		this.destination = destination;
	}
	
	@Override
	public void operate()
	{
		if(target.exists() && target.isFile())
		{
			if(destination.exists())
			{
				queue(new IDelete(h, destination));
				queue(new IEncrypt(h, target, destination));
				return;
			}
			
			else
			{
				try
				{
					FM.create(target, destination);
					log("Encrypt", target.toString(), destination.toString());
				}
				
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
