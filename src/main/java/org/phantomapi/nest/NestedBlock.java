package org.phantomapi.nest;

import org.bukkit.block.Block;
import org.phantomapi.lang.GLocation;

/**
 * Nested blocks
 * 
 * @author cyberpwn
 */
public class NestedBlock extends NestedObject
{
	private static final long serialVersionUID = 1L;
	private final GLocation location;
	
	public NestedBlock(GLocation location)
	{
		this.location = location;
	}
	
	public GLocation getLocation()
	{
		return location;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(obj instanceof Block)
		{
			if(new GLocation(((Block) obj).getLocation()).equals(location))
			{
				return true;
			}
		}
		
		if(obj instanceof NestedBlock)
		{
			return location.equals(((NestedBlock) obj).location);
		}
		if(getClass() != obj.getClass())
			return false;
		NestedBlock other = (NestedBlock) obj;
		if(location == null)
		{
			if(other.location != null)
				return false;
		}
		else if(!location.equals(other.location))
			return false;
		return true;
	}
}
