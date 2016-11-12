package org.phantomapi.core;

import java.io.IOException;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.phantomapi.chromatic.ChromaticHost;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

@Ticked(0)
public class BlockUpdateController extends Controller implements Monitorable
{
	private GList<Block> queue;
	private String est;
	private ChromaticHost chrome;
	
	public BlockUpdateController(Controllable parentController)
	{
		super(parentController);
		
		queue = new GList<Block>();
		est = "";
		try
		{
			chrome = new ChromaticHost();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		catch(InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart()
	{
		new TaskLater(1)
		{
			@Override
			public void run()
			{
				s("Selected for Chromatic Host. Initiating Chromatic Builder");
				chrome.start();
			}
		};
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
	
	public GList<Block> getQueue()
	{
		return queue;
	}
	
	public String getEst()
	{
		return est;
	}
	
	public ChromaticHost getChrome()
	{
		return chrome;
	}
}
