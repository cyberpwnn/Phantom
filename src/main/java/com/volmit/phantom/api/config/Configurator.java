package com.volmit.phantom.api.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.util.plugin.Documented;

/**
 * Tools for reading and writing files to file configurations, and objects to
 * file configurations
 *
 * @author cyberpwn
 *
 */
@Documented
public class Configurator
{
	/**
	 * Peels the object's data into a "default" file configuration, then reads the
	 * file's config data layered over the existing default file configuration. The
	 * resulting "customized default" is then applied to the object. Finally the end
	 * result is re-written back to the file (for example new keys or changed
	 * restrictions)
	 *
	 * @param object
	 *            the object representing this config
	 * @param file
	 *            the file to read/write from
	 * @return returns true if the operation was successful. Otherwise false is
	 *         returned and an exception is printed.
	 */
	public static boolean load(Object object, File file)
	{
		try
		{
			file.getParentFile().mkdirs();
			FileConfiguration fc = peel(object);

			if(!file.exists())
			{
				fc.save(file);
				D.ll("Generated default config: " + file.getPath());
			}

			fc.load(file);
			stick(fc, object);
			write(object, file);
			D.ll("Loaded config: " + file.getPath());
			return true;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Write the object's data to a file
	 *
	 * @param object
	 *            the object representing this config
	 * @param file
	 *            the file
	 * @throws IOException
	 *             file crap
	 * @throws IllegalArgumentException
	 *             reflection crap
	 * @throws IllegalAccessException
	 *             shouldnt happen
	 */
	public static void write(Object object, File file) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		peel(object).save(file);
	}

	/**
	 * Read the file into the objects variables
	 *
	 * @param object
	 *            the object
	 * @param file
	 *            the file to read
	 * @throws FileNotFoundException
	 *             the file doesnt exist or is something other than a file
	 * @throws IOException
	 *             more file crap
	 * @throws InvalidConfigurationException
	 *             your yaml is yack, not to be confused with yack, which is
	 *             probably some language now.
	 * @throws IllegalArgumentException
	 *             reflection crap
	 * @throws IllegalAccessException
	 *             shouldnt happen (probably)
	 */
	public static void read(Object object, File file) throws FileNotFoundException, IOException, InvalidConfigurationException, IllegalArgumentException, IllegalAccessException
	{
		file.getParentFile().mkdirs();
		FileConfiguration fc = new YamlConfiguration();
		fc.load(file);
		stick(fc, object);
	}

	/**
	 * Stick the file configurations values into the given object
	 *
	 * @param fc
	 *            the file config
	 * @param object
	 *            the object
	 * @throws IllegalArgumentException
	 *             shouldnt happen unless you are doing dumb crap like player
	 *             variables without keydefs
	 * @throws IllegalAccessException
	 *             really shouldnt happen
	 */
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
				String key = i.getName().toLowerCase().replaceAll("__", ".").replaceAll("_", "-");

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

				else if(!stat)
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

	/**
	 * Peels the objects variables into a file configuration.
	 *
	 * If you use Key annotations, it will run this method in exclusive mode,
	 * ignoring any variable without a key object. Using a blank key annotation will
	 * default to the variable name as the config ref.
	 *
	 * One underscore represents a dash, and two represents a dot in the key
	 * (indent)
	 *
	 * @param object
	 *            the object
	 * @return the file configuration peeled off the object
	 * @throws IllegalArgumentException
	 *             shoudlnt happen unless you are doing dumb shit.
	 * @throws IllegalAccessException
	 *             shouldnt happen
	 */
	@SuppressWarnings("unchecked")
	public static FileConfiguration peel(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		FileConfiguration fc = new YamlConfiguration();
		boolean explicit = false;
		boolean stat = object.getClass().getCanonicalName().equals(Class.class.getCanonicalName());
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
				String key = i.getName().toLowerCase().replaceAll("__", ".").replaceAll("_", "-");

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

				else if(!stat)
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
