package com.volmit.phantom.api.module;

import com.volmit.phantom.util.text.C;

public class ModuleDescription
{
	private String name;
	private String author;
	private String version;
	private C color;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public C getColor()
	{
		return color;
	}

	public void setColor(C color)
	{
		this.color = color;
	}
}
