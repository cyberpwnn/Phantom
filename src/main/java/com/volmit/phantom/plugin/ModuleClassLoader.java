package com.volmit.phantom.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class ModuleClassLoader extends URLClassLoader
{
	private final File module;
	private int loadedClasses;

	public ModuleClassLoader(File module, URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
	{
		super(urls, parent, factory);
		this.module = module;
	}

	public ModuleClassLoader(File module, URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
		this.module = module;
	}

	public ModuleClassLoader(File module, URL[] urls)
	{
		super(urls);
		this.module = module;
	}

	public File getModule()
	{
		return module;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		loadedClasses++;
		return super.loadClass(name, resolve);
	}

	public int getLoadedClasses()
	{
		return loadedClasses;
	}
}
