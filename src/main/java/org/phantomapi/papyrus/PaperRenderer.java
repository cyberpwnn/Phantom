package org.phantomapi.papyrus;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

/**
 * A paper renderer
 * 
 * @author cyberpwn
 */
public class PaperRenderer extends MapRenderer
{
	private Paper paper;
	
	/**
	 * Create a paper renderer
	 * 
	 * @param paper
	 *            the renderer
	 */
	public PaperRenderer(Paper paper)
	{
		this.paper = paper;
	}
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player)
	{
		for(int y = 0; y < 128; y++)
		{
			for(int x = 0; x < 128; x++)
			{
				canvas.setPixel(x, y, paper.getImage()[x][y]);
			}
		}
	}
	
	/**
	 * Get the paper
	 * @return the paper
	 */
	public Paper getPaper()
	{
		return paper;
	}
}
