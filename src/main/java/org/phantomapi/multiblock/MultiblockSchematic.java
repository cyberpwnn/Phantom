package org.phantomapi.multiblock;

import org.phantomapi.schematic.VariableSchematic;
import org.phantomapi.world.Dimension;

/**
 * Represents a multiblock schematic
 * 
 * @author cyberpwn
 */
public class MultiblockSchematic extends VariableSchematic
{
	/**
	 * Multiblock schematics
	 * 
	 * @param dimension
	 *            the dimension
	 */
	public MultiblockSchematic(Dimension dimension)
	{
		super(dimension);
	}
	
	/**
	 * Get an alternative dimension
	 * 
	 * @return the xz flipped dimension
	 */
	public Dimension getAlternativeDimension()
	{
		return new Dimension(getDimension().getDepth(), getDimension().getHeight(), getDimension().getWidth());
	}
}
