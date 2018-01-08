package phantom.event;

import java.io.File;

import phantom.lang.GList;
import phantom.lang.GMap;

public class PhantomJarScannedEvent extends PhantomEvent
{
	private GMap<String, GList<Class<?>>> classes;
	private File jar;

	public PhantomJarScannedEvent(File jar, GMap<String, GList<Class<?>>> classes)
	{
		this.jar = jar;
		this.classes = classes;
	}

	public GMap<String, GList<Class<?>>> getClasses()
	{
		return classes;
	}

	public File getJar()
	{
		return jar;
	}
}
