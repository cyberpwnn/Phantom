package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.cyberpwn.phantom.util.D;

import net.md_5.bungee.api.ChatColor;

/**
 * 
 * @author cyberpwn
 *
 */
public class ConfigurationHandler
{
	public static void toFields(Configurable c)
	{
		for(Field i : c.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Keyed.class))
			{
				if(isValidType(i.getType()))
				{
					if(Modifier.isPublic(i.getModifiers()) && !Modifier.isStatic(i.getModifiers()))
					{
						try
						{
							String key = i.getDeclaredAnnotation(Keyed.class).value();
							Object value = c.getConfiguration().getAbstract(key);
							i.set(c, value);
						}
						
						catch(IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						
						catch(IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
					
					else
					{
						new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID MODIFIERS. MUST BE PUBLIC NON STATIC");
					}
				}
				
				else
				{
					new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID TYPE. NOT SUPPORTED FOR KEYED CONFIGS");;
				}
			}
		}
	}
	
	public static void fromFields(Configurable c)
	{
		for(Field i : c.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Keyed.class))
			{
				if(isValidType(i.getType()))
				{
					if(Modifier.isPublic(i.getModifiers()) && !Modifier.isStatic(i.getModifiers()))
					{
						try
						{
							String key = i.getDeclaredAnnotation(Keyed.class).value();
							Object value = i.get(c);
							c.getConfiguration().trySet(key, value);
							new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).s(key + ChatColor.AQUA + " > " + value);
						}
						
						catch(IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						
						catch(IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
					
					else
					{
						new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID MODIFIERS. MUST BE PUBLIC NON STATIC");
					}
				}
				
				else
				{
					new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID TYPE. NOT SUPPORTED FOR KEYED CONFIGS");;
				}
			}
		}
	}
	
	public static boolean isValidType(Class<?> type)
	{
		if(type.equals(String.class))
		{
			return true;
		}
		
		else if(type.equals(Integer.class))
		{
			return true;
		}
		
		else if(type.equals(int.class))
		{
			return true;
		}
		
		else if(type.equals(Double.class))
		{
			return true;
		}
		
		else if(type.equals(double.class))
		{
			return true;
		}
		
		else if(type.equals(List.class))
		{
			return true;
		}
		
		else if(type.equals(Boolean.class))
		{
			return true;
		}
		
		else if(type.equals(boolean.class))
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	/**
	 * Handle reading in configs. Also adds new paths that do not exist in the
	 * file from the onNewConfig(), and adds default values
	 * 
	 * @param base
	 *            the base directory
	 * @param c
	 *            the configurable object
	 * @throws IOException
	 *             1337
	 */
	public static void read(File base, Configurable c) throws IOException
	{
		File config = new File(base, c.getCodeName() + ".yml");
		
		if(!config.getParentFile().exists())
		{
			config.getParentFile().mkdirs();
		}
		
		if(!config.exists())
		{
			config.createNewFile();
		}
		
		if(config.isDirectory())
		{
			throw new IOException("Cannot read config (it's a folder)");
		}
		
		fromFields(c);
		c.onNewConfig();
		new YAMLDataInput().load(c.getConfiguration(), config);
		new YAMLDataOutput().save(c.getConfiguration(), config);
		toFields(c);
		c.onReadConfig();
	}
}
