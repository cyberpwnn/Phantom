package org.phantomapi.schematic;

import org.phantomapi.world.MaterialBlock;

/**
 * Set edge blocks to a given materialblock
 * 
 * @author cyberpwn
 */
public class EdgeDistortion implements Distortion
{
	private MaterialBlock mb;
	
	/**
	 * Set edge blocks to a given materialblock
	 */
	public EdgeDistortion(MaterialBlock mb)
	{
		this.mb = mb;
	}
	
	@Override
	public void onDistort(Schematic s)
	{
		s.setStripY(mb, 0, 0);
		s.setStripY(mb, 0, s.mz());
		s.setStripY(mb, s.mx(), s.mz());
		s.setStripY(mb, s.mx(), 0);
		
		s.setStripX(mb, 0, 0);
		s.setStripX(mb, 0, s.my());
		s.setStripX(mb, s.mz(), s.my());
		s.setStripX(mb, s.mz(), 0);
		
		s.setStripZ(mb, 0, 0);
		s.setStripZ(mb, 0, s.my());
		s.setStripZ(mb, s.mx(), s.my());
		s.setStripZ(mb, s.mx(), 0);
	}
}
