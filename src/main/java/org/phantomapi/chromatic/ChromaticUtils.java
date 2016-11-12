package org.phantomapi.chromatic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.phantomapi.Phantom;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.YAMLDataOutput;

/**
 * File utilities for chromatics
 * 
 * @author cyberpwn
 */
public class ChromaticUtils
{
	/**
	 * Get the mapping either internally or from a cached version in phantom's
	 * data folder. If it reads it internally, it will also create a cache. DO
	 * NOT USE THIS OFTEN (DISK)
	 * 
	 * @return
	 * @throws FileNotFoundException
	 *             cant find the file
	 * @throws IOException
	 *             shit happens
	 * @throws InvalidConfigurationException
	 *             shit happens
	 */
	public static DataCluster getMapping() throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		FileConfiguration fc = new YamlConfiguration();
		DataCluster cluster = new DataCluster();
		
		try
		{
			InputStream resource = ChromaticUtils.class.getClassLoader().getResourceAsStream("org/phantomapi/chromatic/map.yml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
			fc.load(reader);
			
			for(String i : fc.getKeys(true))
			{
				if(fc.isBoolean(i))
				{
					cluster.set(i, fc.getBoolean(i));
				}
				
				else if(fc.isInt(i))
				{
					cluster.set(i, fc.getInt(i));
				}
				
				else if(fc.isLong(i))
				{
					cluster.set(i, fc.getLong(i));
				}
				
				else if(fc.isDouble(i))
				{
					cluster.set(i, fc.getDouble(i));
				}
				
				else if(fc.isList(i))
				{
					cluster.set(i, fc.getStringList(i));
				}
				
				else if(fc.isString(i))
				{
					cluster.set(i, fc.getString(i));
				}
			}
			
			File f = new File(Phantom.instance().getDataFolder(), "chromatic");
			File k = new File(f, "map.yml");
			new YAMLDataOutput().save(cluster, k);
		}
		
		catch(Exception e)
		{
			File f = new File(Phantom.instance().getDataFolder(), "chromatic");
			File k = new File(f, "map.yml");
			
			if(k.exists())
			{
				fc.load(k);
				
				for(String i : fc.getKeys(true))
				{
					if(fc.isBoolean(i))
					{
						cluster.set(i, fc.getBoolean(i));
					}
					
					else if(fc.isInt(i))
					{
						cluster.set(i, fc.getInt(i));
					}
					
					else if(fc.isLong(i))
					{
						cluster.set(i, fc.getLong(i));
					}
					
					else if(fc.isDouble(i))
					{
						cluster.set(i, fc.getDouble(i));
					}
					
					else if(fc.isList(i))
					{
						cluster.set(i, fc.getStringList(i));
					}
					
					else if(fc.isString(i))
					{
						cluster.set(i, fc.getString(i));
					}
				}
			}
		}
		
		return cluster;
	}
	
	/**
	 * Get the buffered image of the given texture file name. If it cannot be
	 * read internally, a cached version will be read from phantom's data
	 * folder. If it is read internally, it will be overwritten to the data
	 * folder for caching purposes. DO NOT USE THIS OFTEN (DISK)
	 * 
	 * @param name
	 *            the name of the texture
	 * @return the buffered image
	 * @throws IOException
	 *             shit happens
	 */
	public static BufferedImage getTexture(String name) throws IOException
	{
		try
		{
			InputStream resource = ChromaticUtils.class.getClassLoader().getResourceAsStream("org/phantomapi/texture/" + name + ".png");
			BufferedImage bu = ImageIO.read(resource);
			File f = new File(Phantom.instance().getDataFolder(), "chromatic");
			File k = new File(f, "texture");
			File v = new File(k, name + ".png");
			
			if(!v.exists())
			{
				k.mkdirs();
			}
			
			ImageIO.write(bu, "png", v);
			
			return bu;
		}
		
		catch(Exception e)
		{
			File f = new File(Phantom.instance().getDataFolder(), "chromatic");
			File k = new File(f, "texture");
			File v = new File(k, name + ".png");
			
			if(v.exists())
			{
				return ImageIO.read(v);
			}
		}
		
		return null;
	}
	
	/**
	 * Get the color of a given pixel
	 * 
	 * @param bu
	 *            the buffered image
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the pixel or null
	 */
	public static Color getColor(BufferedImage bu, int x, int y)
	{
		return new Color(bu.getRGB(x, y));
	}
	
	/**
	 * Get the prominent color in the entire buffered image ignoring alpha
	 * 
	 * @param bu
	 *            the buffered image
	 * @return the prominent color
	 */
	public static Color getProminentColor(BufferedImage bu)
	{
		double r = 0;
		double g = 0;
		double b = 0;
		
		for(int i = 0; i < bu.getWidth(); i++)
		{
			for(int j = 0; j < bu.getHeight(); j++)
			{
				Color c = getColor(bu, i, j);
				
				r += c.getRed();
				g += c.getGreen();
				b += c.getBlue();
			}
		}
		
		return new Color((int) r / (bu.getWidth() * bu.getHeight()), (int) g / (bu.getWidth() * bu.getHeight()), (int) b / (bu.getWidth() * bu.getHeight()));
	}
}
