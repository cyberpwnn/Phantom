package org.phantomapi.papyrus;

/**
 * Paper wall renderer
 * 
 * @author cyberpwn
 */
public class PaperWallRenderer
{
	private PaperWallSet wall;
	private PaperRenderer[][] renderers;
	private PaperWall paperWall;
	
	/**
	 * Create a paper wall
	 * 
	 * @param wall
	 *            the wall
	 */
	public PaperWallRenderer(PaperWallSet wall)
	{
		this.wall = wall;
		this.paperWall = new PaperWall(wall.getWidth(), wall.getHeight());
		this.renderers = new PaperRenderer[wall.getWidth()][wall.getHeight()];
		
		for(int i = 0; i < wall.getWidth(); i++)
		{
			for(int j = 0; j < wall.getHeight(); j++)
			{
				renderers[i][j] = new PaperRenderer(paperWall.getPapers()[i][j]);
				PaperUtils.clearRenderers(wall.getBlockMaps().get(wall.getBlocks()[i][j]));
				PaperUtils.addRenderer(renderers[i][j], wall.getBlockMaps().get(wall.getBlocks()[i][j]));
			}
		}
	}
	
	public PaperWallSet getWall()
	{
		return wall;
	}
	
	public PaperRenderer[][] getRenderers()
	{
		return renderers;
	}
	
	public PaperWall getPaperWall()
	{
		return paperWall;
	}
}
