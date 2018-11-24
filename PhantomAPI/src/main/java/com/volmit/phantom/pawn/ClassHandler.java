package com.volmit.phantom.pawn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.PackageScanner;

public interface ClassHandler<T>
{
	public static final GMap<Class<?>, ClassHandler<?>> handlers = new GMap<>();
	
	public Class<? extends T> getHandledClass();
	
	public String toString(T t);
	
	public T fromString(String s);
	
	public static void registerHandlers(String searchPackage, Class<?> jar) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		PackageScanner ps = new PackageScanner(new File(jar.getProtectionDomain().getCodeSource().getLocation().getFile()), searchPackage);
		ps.scan();
		
		for(Class<?> i : ps.getClasses())
		{
			if(i.isAssignableFrom(ClassHandler.class))
			{
				ClassHandler<?> h = (ClassHandler<?>) i.getConstructor().newInstance();
				registerHandler(h.getHandledClass(), h);
			}
		}
	}
	
	public static void registerHandler(Class<?> c, ClassHandler<?> h)
	{
		handlers.put(c, h);
	}
	
	public static ClassHandler<?> getHandler(Class<?> c)
	{
		return handlers.get(c);
	}
}
