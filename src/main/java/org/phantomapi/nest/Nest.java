package org.phantomapi.nest;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.phantomapi.Phantom;
import org.phantomapi.block.BlockHandler;
import org.phantomapi.papyrus.Maps;
import org.phantomapi.papyrus.PaperColor;
import org.phantomapi.papyrus.PapyrusRenderer;
import org.phantomapi.papyrus.RenderFilter;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.world.Blocks;

/**
 * Nest api hub
 * 
 * @author cyberpwn
 */
public class Nest
{
	public static NestedChunk getChunk(Chunk c)
	{
		return Phantom.instance().getNestController().get(c);
	}
	
	public static NestedBlock getBlock(Block block)
	{
		return getChunk(block.getChunk()).getBlock(block);
	}
	
	public static void giveMap(Player p)
	{
		ItemStack is = new ItemStack(Material.MAP);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(C.LIGHT_PURPLE + "Probe");
		is.setItemMeta(im);
		p.setItemInHand(is);
		
		new TaskLater()
		{
			@Override
			public void run()
			{
				MapView view = Maps.getView(p.getItemInHand());
				
				new PapyrusRenderer(view)
				{
					@Override
					public void render()
					{
						clear(PaperColor.GRAY_1);
						filter(new RenderFilter()
						{
							@Override
							public byte onRender(int x, int y, byte currentColor)
							{
								if(x == 64 && y == 64)
								{
									return PaperColor.BLUE;
								}
								
								Chunk c = p.getLocation().getChunk();
								int xx = c.getX();
								int yy = c.getZ();
								
								int cx = x - 64;
								int cy = y - 64;
								
								if(c.getWorld().isChunkLoaded(cx + xx, cy + yy))
								{
									if(Nest.getChunk(c.getWorld().getChunkAt(cx + xx, cy + yy)) != null)
									{
										if(Nest.getChunk(c.getWorld().getChunkAt(cx + xx, cy + yy)).getBlocks().size() > 0)
										{
											return PaperColor.RED;
										}
										
										for(BlockHandler i : Blocks.getHandlers())
										{
											if(i.hasProtection(c.getWorld().getChunkAt(cx + xx, cy + yy).getBlock(0, 0, 0)))
											{
												return PaperColor.PALE_BLUE;
											}
										}
									}
									
									else
									{
										return PaperColor.WHITE;
									}
									
									return PaperColor.DARK_GREEN;
								}
								
								return PaperColor.GRAY_1;
							}
						});
					}
				};
			}
		};
	}
}
