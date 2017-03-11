package org.phantomapi.filesystem;

import java.io.File;

public class IDelete extends IOP
{
	private File target;
	
	public IDelete(FileHack h, File target)
	{
		super(h);
		
		this.target = target;
	}
	
	@Override
	public void operate()
	{
		if(target.exists())
		{
			if(target.isDirectory())
			{
				for(File i : target.listFiles())
				{
					queue(new IDelete(h, i));
				}
				
				if(target.listFiles().length == 0)
				{
					log("Delete Folder", target.toString());
					target.delete();
				}
				
				else
				{
					queue(new IDelete(h, target));
				}
			}
			
			else
			{
				log("Delete File", target.toString());
				target.delete();
			}
		}
	}
	
	@Override
	public void reverse()
	{
		// TODO Auto-generated method stub
		
	}
}
