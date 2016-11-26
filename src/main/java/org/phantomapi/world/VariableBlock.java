package org.phantomapi.world;

import org.bukkit.Material;
import org.phantomapi.lang.GList;
import com.sk89q.worldedit.patterns.BlockChance;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.RandomFillPattern;

/**
 * Represents a block that can be multiple blocks (used for variable schematics)
 * 
 * @author cyberpwn
 */
@SuppressWarnings("deprecation")
public class VariableBlock
{
	private GList<MaterialBlock> blocks;
	
	/**
	 * Create a variable block
	 * 
	 * @param blocks
	 *            blocks
	 */
	public VariableBlock(MaterialBlock... blocks)
	{
		this.blocks = new GList<MaterialBlock>(blocks);
	}
	
	/**
	 * Add a materialblock into this variable block
	 * 
	 * @param material
	 *            the material
	 * @param data
	 *            the data
	 */
	public VariableBlock(Material material, Byte data)
	{
		this(new MaterialBlock(material, data));
	}
	
	/**
	 * Add a materialblock into this variable block
	 * 
	 * @param material
	 *            the material
	 */
	public VariableBlock(Material material)
	{
		this(new MaterialBlock(material));
	}
	
	/**
	 * Get all variable blocks
	 * 
	 * @return the variable blocks
	 */
	public GList<MaterialBlock> getBlocks()
	{
		return blocks;
	}
	
	/**
	 * Is the given block this block? Does the variable block contain this block
	 * 
	 * @param block
	 *            the given block
	 * @return true if it is
	 */
	public boolean is(MaterialBlock block)
	{
		return blocks.contains(block);
	}
	
	/**
	 * Add a block to the list
	 * 
	 * @param block
	 *            the block
	 */
	public void addBlock(MaterialBlock block)
	{
		if(!blocks.contains(block))
		{
			blocks.add(block);
		}
	}
	
	/**
	 * Remove a block from the list
	 * 
	 * @param block
	 *            the block
	 */
	public void removeBlock(MaterialBlock block)
	{
		blocks.remove(block);
	}
	
	public Pattern toPattern()
	{
		if(blocks.isEmpty())
		{
			return null;
		}
		
		GList<BlockChance> ch = new GList<BlockChance>();
		Double prob = (double) (1.0 / (double) blocks.size());
		
		for(MaterialBlock i : blocks)
		{
			ch.add(new BlockChance(i.toBase(), prob));
		}
		
		return new RandomFillPattern(ch);
	}
	
	public void fromString(String string)
	{
		blocks.clear();
		
		if(string.contains(","))
		{
			for(String i : string.split(","))
			{
				try
				{
					blocks.add(W.getMaterialBlock(i));
				}
				
				catch(Exception e)
				{
					
				}
			}
		}
		
		else
		{
			try
			{
				blocks.add(W.getMaterialBlock(string));
			}
			
			catch(Exception e)
			{
				
			}
		}
	}
	
	@Override
	public String toString()
	{
		GList<String> s = new GList<String>();
		
		for(MaterialBlock i : blocks)
		{
			s.add(i.toString());
		}
		
		return s.toString(",");
	}
}
