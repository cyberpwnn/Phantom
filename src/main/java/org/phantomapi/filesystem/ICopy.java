package org.phantomapi.filesystem;

import java.io.File;
import java.io.IOException;
import org.phantomapi.util.FU;

public class ICopy extends IOP
{
	private File target;
	private File destination;
	
	public ICopy(FileHack h, File target, File destination)
	{
		super(h);
		
		this.target = target;
		this.destination = destination;
	}
	
	@Override
	public void operate()
	{
		if(target.isDirectory())
		{
			File[] files = target.listFiles();
			
			for(File file : files)
			{
				File copiedFile = new File(destination, file.getName());
				queue(new ICopy(h, file, copiedFile));
			}
		}
		
		else
		{
			try
			{
				FU.copyFile(target, destination);
			}
			
			catch(IOException e)
			{
				
			}
			
			log("Copy", target.toString(), destination.toString());
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
