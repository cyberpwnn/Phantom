package com.volmit.phantom.api.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.lang.GList;

public class Configurator
{
	public static void save(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		FileConfiguration fc = peel(object);
		GList<String> lines = new GList<String>(fc.saveToString().split("\n"));
		GList<String> nlines = new GList<>();
		boolean explicit = false;
		boolean stat = object instanceof Class<?>;
		Class<?> c = stat ? (Class<? extends Object>) object : object.getClass();

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if(i.isAnnotationPresent(Key.class))
			{
				explicit = true;
				break;
			}
		}

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if((explicit && i.isAnnotationPresent(Key.class)) || !explicit)
			{
				String key = i.getName().toLowerCase().replaceAll("__", ".").replaceAll("_", "");

				if(i.isAnnotationPresent(Key.class))
				{
					String m = i.getAnnotation(Key.class).value().trim();
					if(!m.isEmpty())
					{
						key = m;
					}
				}

				if(i.isAnnotationPresent(Comment.class))
				{
					String comment = F.wrapWords(i.getAnnotation(Comment.class).value(), 48);

					if(i.isAnnotationPresent(Clip.class))
					{

					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void stick(FileConfiguration fc, Object object) throws IllegalArgumentException, IllegalAccessException
	{
		boolean explicit = false;
		boolean stat = object instanceof Class<?>;
		Class<?> c = stat ? (Class<? extends Object>) object : object.getClass();

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if(i.isAnnotationPresent(Key.class))
			{
				explicit = true;
				break;
			}
		}

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if((explicit && i.isAnnotationPresent(Key.class)) || !explicit)
			{
				String key = i.getName().toLowerCase().replaceAll("__", ".").replaceAll("_", "");

				if(i.isAnnotationPresent(Key.class))
				{
					String m = i.getAnnotation(Key.class).value().trim();
					if(!m.isEmpty())
					{
						key = m;
					}
				}

				if(Modifier.isStatic(i.getModifiers()))
				{
					i.set(null, fc.get(key));
				}

				else if(stat)
				{
					i.set(object, fc.get(key));
				}

				else
				{
					D.ww("Cannot stick configuration field " + i.getName() + " (not static) on a STATIC CLASS. We have no refrence to " + c.getCanonicalName());
					throw new IllegalArgumentException();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static FileConfiguration peel(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		FileConfiguration fc = new YamlConfiguration();
		boolean explicit = false;
		boolean stat = object instanceof Class<?>;
		Class<?> c = stat ? (Class<? extends Object>) object : object.getClass();

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if(i.isAnnotationPresent(Key.class))
			{
				explicit = true;
				break;
			}
		}

		for(Field i : c.getDeclaredFields())
		{
			i.setAccessible(true);

			if((explicit && i.isAnnotationPresent(Key.class)) || !explicit)
			{
				String key = i.getName().toLowerCase().replaceAll("__", ".").replaceAll("_", "");

				if(i.isAnnotationPresent(Key.class))
				{
					String m = i.getAnnotation(Key.class).value().trim();
					if(!m.isEmpty())
					{
						key = m;
					}
				}

				if(Modifier.isStatic(i.getModifiers()))
				{
					fc.set(key, i.get(null));
				}

				else if(stat)
				{
					fc.set(key, i.get(object));
				}

				else
				{
					D.ww("Cannot peel configuration field " + i.getName() + " (not static) on a STATIC CLASS. We have no refrence to " + c.getCanonicalName());
					throw new IllegalArgumentException();
				}
			}
		}

		return fc;
	}
}
