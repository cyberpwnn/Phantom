package org.cyberpwn.phantom.lang;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class GFile extends File
{
	private static final long serialVersionUID = 1L;

	public GFile(File parent, String child)
	{
		super(parent, child);
	}
	
	public GFile(URI uri)
	{
		super(uri);
	}
	
	public GFile(String parent, String child)
	{
		super(parent, child);
	}
	
	public GFile(GFile parent, String... childs)
	{
		super(parent, new GList<String>(childs).toString(pathSeparator));
	}
	
	public GFile(String child)
	{
		super(child);
	}
	
	public boolean createNewFile() throws IOException
	{
		if(!getParentFile().exists())
		{
			getParentFile().mkdirs();
		}
		
		return super.createNewFile();
	}
}
