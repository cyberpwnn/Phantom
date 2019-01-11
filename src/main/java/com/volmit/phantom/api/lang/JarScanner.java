package com.volmit.phantom.api.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarScanner
{
	private final GSet<Class<?>> classes;
	private final File jar;
	private final ClassLoader cl;
	private final GMap<String, String> poms;

	/**
	 * Create a scanner
	 *
	 * @param jar
	 *            the path to the jar
	 */
	public JarScanner(File jar, ClassLoader cl)
	{
		this.jar = jar;
		this.poms = new GMap<>();
		this.classes = new GSet<Class<?>>();
		this.cl = cl;
	}

	/**
	 * Scan the jar
	 *
	 * @throws IOException
	 *             bad things happen
	 */
	public void scan() throws IOException
	{
		classes.clear();
		FileInputStream fin = new FileInputStream(jar);
		ZipInputStream zip = new ZipInputStream(fin);

		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(!entry.isDirectory() && entry.getName().endsWith(".class"))
			{
				if(entry.getName().contains("$"))
				{
					continue;
				}

				String c = entry.getName().replaceAll("/", ".").replace(".class", "");

				try
				{
					Class<?> clazz = Class.forName(c, true, cl);
					classes.add(clazz);
				}

				catch(Throwable e)
				{
					try
					{
						Class<?> clazz = Class.forName(c, true, getClass().getClassLoader());
						classes.add(clazz);
					}

					catch(Throwable exz)
					{

					}
				}
			}
		}

		zip.close();
	}

	public void scanForPomProperties() throws IOException
	{
		poms.clear();
		FileInputStream fin = new FileInputStream(jar);
		ZipInputStream zip = new ZipInputStream(fin);

		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(!entry.isDirectory() && entry.getName().endsWith("pom.properties"))
			{
				String[] path = entry.getName().split("\\Q/\\E");
				String name = path[path.length - 2];
				VIO.readEntry(jar, entry.getName(), (input) ->
				{
					try
					{
						poms.put(name, VIO.readAll(input));
					}

					catch(IOException e)
					{
						e.printStackTrace();
					}
				});
			}
		}

		zip.close();
	}

	public GMap<String, String> getPoms()
	{
		return poms;
	}

	/**
	 * Get the scanned clases
	 *
	 * @return a gset of classes
	 */
	public GSet<Class<?>> getClasses()
	{
		return classes;
	}

	/**
	 * Get the file object for the jar
	 *
	 * @return a file object representing the jar
	 */
	public File getJar()
	{
		return jar;
	}
}