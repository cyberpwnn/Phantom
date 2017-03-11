package org.phantomapi.filesystem;

import java.io.File;

public class IDirectory extends IOP
{
	private File target;
	
	public IDirectory(FileHack h, File target)
	{
		super(h);
		
		this.target = target;
	}
	
	@Override
	public void operate()
	{
		log("Directory", target.toString());
		target.mkdirs();
	}
	
	@Override
	public void reverse()
	{
		
	}
}
