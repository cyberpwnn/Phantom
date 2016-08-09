package org.cyberpwn.phantom.world;

import org.cyberpwn.phantom.lang.GList;

/**
 * Random Distortions
 * 
 * @author cyberpwn
 */
public class RandomDistortion implements Distortion
{
	private MaterialBlock replace;
	private GList<MaterialBlock> blocks;
	
	/**
	 * Replace blocks with the random set of blocks
	 * 
	 * @param replace
	 *            the replace block. If set to null, all blocks will be replaced
	 *            with the random set
	 * @param blocks
	 *            the blocks to replace with.
	 */
	public RandomDistortion(MaterialBlock replace, MaterialBlock... blocks)
	{
		this.replace = replace;
		this.blocks = new GList<MaterialBlock>(blocks);
	}
	
	@Override
	public void onDistort(Schematic s)
	{
		s.set(new SchematicIterator()
		{
			public void run()
			{
				if(replace == null)
				{
					setB(blocks.pickRandom());
				}
				
				else if(getB().equals(replace))
				{
					setB(blocks.pickRandom());
				}
			}
		});
	}
}
