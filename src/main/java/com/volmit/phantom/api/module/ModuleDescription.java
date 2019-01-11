package com.volmit.phantom.api.module;

import com.volmit.phantom.util.text.C;

public class ModuleDescription
{
	private String name;
	private String authors;
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

	public String getAuthors()
	{
		if(authors == null)
		{
			return "Anonymous";
		}

		return authors;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
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
		if(color == null)
		{
			return C.WHITE;
		}

		return color;
	}

	public void setColor(C color)
	{
		this.color = color;
	}
}
