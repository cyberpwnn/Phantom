package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.phantomapi.core.EditSessionController;
import com.sk89q.worldedit.blocks.BaseBlock;

/**
 * Material blocks
 * 
 * @author cyberpwn
 */
public class MaterialBlock
{
	private Material material;
	private Byte data;
	
	public MaterialBlock(Material material, Byte data)
	{
		this.material = material;
		this.data = data;
	}
	
	@SuppressWarnings("deprecation")
	public BaseBlock toBase()
	{
		return new BaseBlock(getMaterial().getId(), getData());
	}
	
	public MaterialBlock(Material material)
	{
		this.material = material;
		data = 0;
	}
	
	@SuppressWarnings("deprecation")
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
	public boolean equals(Object o)
	{
		if(o instanceof MaterialBlock)
		{
			MaterialBlock mb = (MaterialBlock) o;
			
			return mb.getData() == getData() && mb.getMaterial().equals(getMaterial());
		}
		
		return false;
	}
}
