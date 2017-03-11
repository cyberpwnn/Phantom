package org.phantomapi.filesystem;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class IModify extends IOP
{
	private File target;
	private String key;
	private Object value;
	
	public IModify(FileHack h, File target, String key, Object value)
	{
		super(h);
		
		this.target = target;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public void operate()
	{
		try
		{
			FileConfiguration fc = new YamlConfiguration();
			fc.load(target);
			fc.set(key, value);
			fc.save(target);
			log("Modify", target.toString(), key, value.toString());
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public void reverse()
	{
		
	}
}
