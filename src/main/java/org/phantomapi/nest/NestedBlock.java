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
	
	public boolean equals(Object object)
	{
		if(object instanceof Block)
		{
			if(new GLocation(((Block) object).getLocation()).equals(location))
			{
				return true;
			}
		}
		
		if(object instanceof NestedBlock)
		{
			return location.equals(((NestedBlock) object).location);
		}
		
		return false;
	}
}
