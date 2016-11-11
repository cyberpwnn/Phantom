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

public class ChromaUtils
{
	public static DataCluster getMapping() throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		InputStream resource = ChromaUtils.class.getClassLoader().getResourceAsStream("org/phantomapi/chromatic/map.yml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
		FileConfiguration fc = new YamlConfiguration();
		DataCluster cluster = new DataCluster();
		
		try
		{
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
	
	public static BufferedImage getTexture(String name) throws IOException
	{
		try
		{
			InputStream resource = ChromaUtils.class.getClassLoader().getResourceAsStream("org/phantomapi/texture/" + name + ".png");
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
	
	public static Color getColor(BufferedImage bu, int x, int y)
	{
		return new Color(bu.getRGB(x, y));
	}
	
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
