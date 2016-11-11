package org.phantomapi.chromatic;

import java.awt.Color;
import org.bukkit.block.BlockFace;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

public class ChromaticBlock
{
	private MaterialBlock type;
	private GMap<BlockFace, Color> color;
	private GMap<BlockFace, Double> transparency;
	
	public ChromaticBlock(MaterialBlock type)
	{
		this.type = type;
		color = new GMap<BlockFace, Color>();
		transparency = new GMap<BlockFace, Double>();
	}
	
	public MaterialBlock getType()
	{
		return type;
	}
	
	public Color getColor(BlockFace face)
	{
		return color.get(face);
	}
	
	public double getTransparancy(BlockFace face)
	{
		return transparency.get(face);
	}
	
	public GMap<BlockFace, Color> getColor()
	{
		return color;
	}
	
	public GMap<BlockFace, Double> getTransparency()
	{
		return transparency;
	}
}
