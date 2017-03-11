package org.phantomapi.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IDeflate extends IOP
{
	private File target;
	private File destination;
	
	public IDeflate(FileHack h, File target, File destination)
	{
		super(h);
		
		this.target = target;
		this.destination = destination;
	}
	
	@Override
	public void operate()
	{
		try
		{
			JarFile jar = new JarFile(target);
			Enumeration<JarEntry> enumEntries = jar.entries();
			
			while(enumEntries.hasMoreElements())
			{
				JarEntry file = (JarEntry) enumEntries.nextElement();
				File f = new File(destination + File.separator + file.getName());
				
				if(file.isDirectory())
				{
					f.mkdir();
					continue;
				}
				
				InputStream is = jar.getInputStream(file);
				FileOutputStream fos = new FileOutputStream(f);
				
				while(is.available() > 0)
				{
					fos.write(is.read());
				}
				
				log("Deflate", target.toString(), f.toString());
				
				fos.close();
				is.close();
			}
			
			jar.close();
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
