package org.phantomapi.filesystem;

import java.io.File;

public class IMove extends IOP
{
	private File target;
	private File destination;
	
	public IMove(FileHack h, File target, File destination)
	{
		super(h);
		
		this.target = target;
		this.destination = destination;
	}
	
	@Override
	public void operate()
	{
		queue(new ICopy(h, target, destination));
		queue(new IDelete(h, target));
	}
	
	@Override
	public void reverse()
	{
		
	}
}
