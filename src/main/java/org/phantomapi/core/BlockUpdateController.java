package org.phantomapi.core;

import org.bukkit.block.Block;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

@Ticked(0)
public class BlockUpdateController extends Controller implements Monitorable
{
	private GList<Block> queue;
	private String est;
	
	public BlockUpdateController(Controllable parentController)
	{
		super(parentController);
		
		queue = new GList<Block>();
		est = "";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	public void update(Block block)
	{
		if(!queue.contains(block))
		{
			queue.add(block);
		}
	}
	
	@Override
	public void onTick()
	{
		if(queue.isEmpty())
		{
			return;
		}
		
		try
		{
			NMSX.updateBlock(queue.pop());
			
			if(queue.size() > 32)
			{
				for(int i = 0; i < 30; i++)
				{
					NMSX.updateBlock(queue.pop());
				}
			}
			
			if(queue.size() > 128)
			{
				for(int i = 0; i < 120; i++)
				{
					NMSX.updateBlock(queue.pop());
				}
			}
			
			if(queue.size() > 256)
			{
				for(int i = 0; i < 250; i++)
				{
					NMSX.updateBlock(queue.pop());
				}
			}
			
			if(queue.size() > 512)
			{
				for(int i = 0; i < 500; i++)
				{
					NMSX.updateBlock(queue.pop());
				}
			}
		}
		
		catch(Exception e)
		{
			
		}
		
		est = F.f(queue.size());
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public String getMonitorableData()
	{
		return C.LIGHT_PURPLE + est;
	}
}
