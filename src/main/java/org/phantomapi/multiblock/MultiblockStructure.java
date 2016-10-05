package org.phantomapi.multiblock;

import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.VariableBlock;
import org.phantomapi.world.VectorSchematic;

/**
 * Represents a multiblock structure
 * 
 * @author cyberpwn
 */
public class MultiblockStructure extends VectorSchematic
{
	private final String type;
	
	/**
	 * Create a multiblock structure
	 * 
	 * @param type
	 */
	public MultiblockStructure(String type)
	{
		super();
		this.type = type;
	}
	
	/**
	 * Get the type
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Add a block to this structure
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @param mb
	 *            the variable block
	 */
	public void add(int x, int y, int z, VariableBlock mb)
	{
		add(new Vector(x, y, z), mb);
	}
	
	/**
	 * Add a block to this structure
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @param mb
	 *            the variable block
	 */
	public void add(int x, int y, int z, MaterialBlock mb)
	{
		add(new Vector(x, y, z), mb);
	}
	
	/**
	 * Add a block
	 * 
	 * @param v
	 *            the vector
	 * @param mb
	 *            the materialblock
	 */
	public void add(Vector v, VariableBlock mb)
	{
		getSchematic().put(v, mb);
	}
	
	/**
	 * Add a block
	 * 
	 * @param v
	 *            the vector
	 * @param mb
	 *            the materialblock
	 */
	public void add(Vector v, MaterialBlock mb)
	{
		if(!getSchematic().containsKey(v))
		{
			getSchematic().put(v, new VariableBlock(mb.getMaterial(), mb.getData()));
		}
		
		else
		{
			getSchematic().get(v).addBlock(mb);
		}
	}
	
	/**
	 * Register this structure
	 */
	public void register()
	{
		Phantom.instance().getMultiblockRegistryController().registerStructure(this);
	}
	
	/**
	 * Unregister structure
	 */
	public void unRegister()
	{
		Phantom.instance().getMultiblockRegistryController().unRegisterStructure(this);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		MultiblockStructure other = (MultiblockStructure) obj;
		if(type == null)
		{
			if(other.type != null)
				return false;
		}
		else if(!type.equals(other.type))
			return false;
		return true;
	}
}
