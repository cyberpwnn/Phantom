package org.phantomapi.chromatic;

import java.awt.Color;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.util.Average;
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
	
	public Color mash(GList<Color> colors)
	{
		float r = 0;
		float g = 0;
		float b = 0;
		float a = 0;
		
		for(Color i : colors)
		{
			r += (float) ((float) i.getRed() / 255f) * (float) ((float) i.getAlpha() / 255f);
			g += (float) ((float) i.getGreen() / 255f) * (float) ((float) i.getAlpha() / 255f);
			b += (float) ((float) i.getBlue() / 255f) * (float) ((float) i.getAlpha() / 255f);
			a += (float) ((float) i.getAlpha() / 255f);
		}
		
		a = a / colors.size();
		r = r > 1f ? 1f : r;
		g = g > 1f ? 1f : g;
		b = b > 1f ? 1f : b;
		
		return new Color(r, g, b, a);
	}
	
	public double getTransparency(Vector direction)
	{
		Vector r = VectorMath.reverse(direction);
		BlockFace f = VectorMath.getBlockFace(r);
		Average trans = new Average(-1);
		
		for(BlockFace i : VectorMath.split(f))
		{
			if(getTransparency().containsKey(i))
			{
				trans.put(getTransparency(i));
			}
		}
		
		return trans.getAverage();
	}
	
	public Color getColor(Vector direction)
	{
		Vector r = VectorMath.reverse(direction);
		BlockFace f = VectorMath.getBlockFace(r);
		GList<Color> colors = new GList<Color>();
		
		for(BlockFace i : VectorMath.split(f))
		{
			if(getColor().containsKey(i))
			{
				colors.add(getAlphaColor(i));
			}
		}
		
		return mash(colors);
	}
	
	public Color getEffectiveColor(Block block, Vector direction)
	{
		double health = 1.0;
		GList<Color> colors = new GList<Color>();
		ChromaticBlock current = this;
		health -= getTransparency(direction);
		colors.add(getColor(direction));
		
		if(health > 0)
		{
			for(int i = 0; i < 3; i++)
			{
				current = current.getRelative(block, direction);
				
				if(current == null)
				{
					break;
				}
				
				health -= current.getTransparency(direction);
				colors.add(current.getColor(direction));
				
				if(health < 0)
				{
					break;
				}
			}
		}
		
		return mash(colors);
	}
	
	@SuppressWarnings("deprecation")
	public ChromaticBlock getRelative(Block block, Vector direction)
	{
		Block next = block.getWorld().getBlockAt(block.getLocation().add(VectorMath.triNormalize(direction)));
		return Chromatic.getBlock(new MaterialBlock(next.getType(), next.getData()));
	}
	
	public MaterialBlock getType()
	{
		return type;
	}
	
	public Color getColor(BlockFace face)
	{
		return color.get(face);
	}
	
	public Color getAlphaColor(BlockFace face)
	{
		Color c = getColor(face);
		return new Color((float) ((float) c.getRed() / (float) 255), (float) ((float) c.getGreen() / (float) 255), (float) ((float) c.getBlue() / (float) 255), (float) getTransparency(face));
	}
	
	public double getTransparency(BlockFace face)
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
