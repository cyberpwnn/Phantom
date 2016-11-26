package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.phantomapi.core.EditSessionController;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;

/**
 * Material blocks
 * 
 * @author cyberpwn
 */
@SuppressWarnings("deprecation")
public class MaterialBlock
{
	private Material material;
	private Byte data;
	
	/**
	 * Create a materialblock
	 * 
	 * @param material
	 *            the material
	 * @param data
	 *            the data
	 */
	public MaterialBlock(Material material, Byte data)
	{
		this.material = material;
		this.data = data;
	}
	
	/**
	 * Get the base block
	 * 
	 * @return the base block
	 */
	public BaseBlock toBase()
	{
		return new BaseBlock(getMaterial().getId(), getData());
	}
	
	public MaterialBlock(Material material)
	{
		this.material = material;
		data = 0;
	}
	
	public MaterialBlock(Location location)
	{
		material = location.getBlock().getType();
		data = location.getBlock().getData();
	}
	
	public MaterialBlock()
	{
		material = Material.AIR;
		data = 0;
	}
	
	public void apply(Location location)
	{
		EditSessionController.queue(location, this);
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public Byte getData()
	{
		return data;
	}
	
	public void setData(Byte data)
	{
		this.data = data;
	}
	
	@Override
	public String toString()
	{
		if(getData() == 0)
		{
			return getMaterial().toString();
		}
		
		return getMaterial().toString() + ":" + getData();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((material == null) ? 0 : material.hashCode());
		return result;
	}
	
	public Pattern toPattern()
	{
		return new SingleBlockPattern(toBase());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		MaterialBlock other = (MaterialBlock) obj;
		if(data == null)
		{
			if(other.data != null)
			{
				return false;
			}
		}
		else if(!data.equals(other.data))
		{
			return false;
		}
		if(material != other.material)
		{
			return false;
		}
		return true;
	}
}
