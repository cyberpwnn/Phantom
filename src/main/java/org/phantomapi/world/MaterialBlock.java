package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
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
		this.data = 0;
	}
	
	@SuppressWarnings("deprecation")
	public MaterialBlock(Location location)
	{
		this.material = location.getBlock().getType();
		this.data = location.getBlock().getData();
	}
	
	public MaterialBlock()
	{
		this.material = Material.AIR;
		this.data = 0;
	}
	
	@SuppressWarnings("deprecation")
	public void apply(Location location)
	{
		location.getBlock().setType(material);
		location.getBlock().setData(data);
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
