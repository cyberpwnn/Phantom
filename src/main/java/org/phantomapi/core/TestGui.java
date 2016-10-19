package org.phantomapi.core;

import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.phantomapi.block.BlockHandler;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.hud.Hud;
import org.phantomapi.nest.Nest;
import org.phantomapi.util.Players;
import org.phantomapi.world.Blocks;
import org.phantomapi.world.W;

public class TestGui extends Hud
{
	private TestFrame frame;
	
	public TestGui(PhantomSender sender)
	{
		super(sender);
		
		frame = TestFrame.begin();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTick()
	{
		Entity e = W.getEntityLookingAt(Players.getPlayer("cyberpwn"), 12, 1);
		Location l = null;
		
		if(e != null)
		{
			l = e.getLocation();
			frame.blockEntity.setText(StringUtils.capitalise(e.getType().toString().toLowerCase().replaceAll("_", " ")));
		}
		
		else
		{
			Block b = Players.getPlayer("cyberpwn").getTargetBlock((Set<Material>) null, 128);
			l = b.getLocation();
			frame.blockEntity.setText(b.getType().toString() + ":" + b.getData());
		}
		
		frame.coords.setText((int) l.getX() + ", " + (int) l.getY() + ", " + (int) l.getZ());
		
		boolean bb = false;
		
		for(BlockHandler i : Blocks.getHandlers())
		{
			if(i.hasProtection(l.getBlock()))
			{
				frame.lblProtector.setText(i.getProtector());
				frame.lblMeta.setText(i.getProtector(l.getBlock()));
				bb = true;
				break;
			}
		}
		
		if(!bb)
		{
			frame.lblProtector.setText("-");
			frame.lblMeta.setText("-");
		}
		
		String s = "";
		
		for(String i : Nest.getBlock(l.getBlock()).toLines(false))
		{
			s = s + i + "\n";
		}
		
		frame.nestdata.setText(s);
	}
}
