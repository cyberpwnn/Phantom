package org.phantomapi.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class IInflate extends IOP
{
	private File target;
	private File destination;
	
	public IInflate(FileHack h, File target, File destination)
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
			Manifest manifest = new Manifest();
			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			JarOutputStream target = new JarOutputStream(new FileOutputStream(destination), manifest);
			
			for(File i : this.target.listFiles())
			{
				add(i, target);
			}
			
			target.close();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void add(File source, JarOutputStream target) throws IOException
	{
		BufferedInputStream in = null;
		
		log("Inflate", source.toString(), this.target.toString());
		
		try
		{
			if(source.isDirectory())
			{
				String name = source.getPath().replace("\\", "/").replaceAll("plugins/React/work/patch/", "");
				
				if(!name.isEmpty())
				{
					if(!name.endsWith("/"))
					{
						name += "/";
					}
					
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				
				for(File nestedFile : source.listFiles())
				{
					add(nestedFile, target);
				}
				
				return;
			}
			
			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/").replaceAll("plugins/React/work/patch/", ""));
			entry.setTime(source.lastModified());
			
			try
			{
				target.putNextEntry(entry);
			}
			
			catch(Exception e)
			{
				return;
			}
			
			in = new BufferedInputStream(new FileInputStream(source));
			
			byte[] buffer = new byte[1024];
			
			while(true)
			{
				int count = in.read(buffer);
				
				if(count == -1)
				{
					break;
				}
				
				target.write(buffer, 0, count);
			}
			
			target.closeEntry();
		}
		
		finally
		{
			if(in != null)
			{
				in.close();
			}
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
