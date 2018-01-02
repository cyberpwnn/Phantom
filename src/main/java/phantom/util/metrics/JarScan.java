package phantom.util.metrics;

import java.io.File;
import phantom.lang.GSet;

public class JarScan
{
	private final GSet<Class<?>> classes;
	private final File jar;
	
	public JarScan(File jar)
	{
		this.jar = jar;
		this.classes = new GSet<Class<?>>();
	}
	
	
}
