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

/**
 * Chromatic blocks used to represent a block TYPE not an actual block at a
 * location. Used for color and transparency properties
 * 
 * @author cyberpwn
 */
public class ChromaticBlock
{
	private MaterialBlock type;
	private GMap<BlockFace, Color> color;
	private GMap<BlockFace, Double> transparency;
	
	/**
	 * Create a chromatic block. Must fill data to use (use
	 * Chromatic.getBlock(MaterialBlock) for a pre-created version)
	 * 
	 * @param type
	 *            the materialblock type
	 */
	public ChromaticBlock(MaterialBlock type)
	{
		this.type = type;
		color = new GMap<BlockFace, Color>();
		transparency = new GMap<BlockFace, Double>();
	}
	
	/**
	 * Mash all of the given colors with alpha into a single color
	 * 
	 * @param colors
	 *            the colors
	 * @return the mashed color
	 */
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
	
	/**
	 * Get the estimated transparancy of a block of this type when looked at
	 * from the given vector angle
	 * 
	 * @param direction
	 *            the direction looked ad
	 * @return the transparency (0 being invisible, 1 being opaque)
	 */
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
	
	/**
	 * Get the estimated color of the block while looked at from the given angle
	 * 
	 * @param direction
	 *            the direction looked at
	 * @return the color
	 */
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
	
	/**
	 * Get the effective color from the position seen. This uses raytracing and
	 * applies colorblending depending on the transparency of the blocks. This
	 * will only blend to a maximum of 4 blocks deep
	 * 
	 * @param block
	 *            the block looked at
	 * @param direction
	 *            the direction looked at the block
	 * @return the effective color
	 */
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
	
	/**
	 * Get the relative chromatic block type based on the looked at block and
	 * direction
	 * 
	 * @param block
	 *            the block
	 * @param direction
	 *            the direction
	 * @return the chromatic block
	 */
	@SuppressWarnings("deprecation")
	public ChromaticBlock getRelative(Block block, Vector direction)
	{
		Block next = block.getWorld().getBlockAt(block.getLocation().add(VectorMath.triNormalize(direction)));
		return Chromatic.getBlock(new MaterialBlock(next.getType(), next.getData()));
	}
	
	/**
	 * Get the type of this block
	 * 
	 * @return the materialblock
	 */
	public MaterialBlock getType()
	{
		return type;
	}
	
	/**
	 * Get the color when viewed at a single direct face
	 * 
	 * @param face
	 *            the face
	 * @return the color or null
	 */
	public Color getColor(BlockFace face)
	{
		return color.get(face);
	}
	
	/**
	 * Get the color when viewed at a single direct face including transparency
	 * values as alpha in the color object
	 * 
	 * @param face
	 *            the face
	 * @return the color which may include alpha depending on transparency or
	 *         null
	 */
	public Color getAlphaColor(BlockFace face)
	{
		Color c = getColor(face);
		return new Color((float) ((float) c.getRed() / (float) 255), (float) ((float) c.getGreen() / (float) 255), (float) ((float) c.getBlue() / (float) 255), (float) getTransparency(face));
	}
	
	/**
	 * Get the transparency of the given block when looked at directly from the
	 * given face
	 * 
	 * @param face
	 *            the face
	 * @return the transparency value (0 being invisible, 1 being opaque)
	 */
	public double getTransparency(BlockFace face)
	{
		return transparency.get(face);
	}
	
	/**
	 * Get the color reference map
	 * 
	 * @return the color reference map
	 */
	public GMap<BlockFace, Color> getColor()
	{
		return color;
	}
	
	/**
	 * Get the transparency map
	 * 
	 * @return the transparency map
	 */
	public GMap<BlockFace, Double> getTransparency()
	{
		return transparency;
	}
}
