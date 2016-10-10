package org.phantomapi.core;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.phantomapi.block.BlockHandler;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GList;
import org.phantomapi.util.C;

/**
 * Checks blocks
 * 
 * @author cyberpwn
 */
public class BlockCheckController extends Controller
{
	GList<BlockHandler> handlers;
	
	public BlockCheckController(Controllable parentController)
	{
		super(parentController);
		
		handlers = new GList<BlockHandler>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void registerBlockHandler(BlockHandler handler)
	{
		s("Registered Block Handler: " + C.AQUA + handler.getClass().getSimpleName());
		handlers.add(handler);
	}
	
	public void unRegisterBlockHandler(BlockHandler handler)
	{
		f("Unregistered Block Handler: " + C.YELLOW + handler.getClass().getSimpleName());
		handlers.remove(handler);
	}
	
	/**
	 * Can the given block be modified by a player
	 * 
	 * @param p
	 *            the player
	 * @param block
	 *            the block
	 * @return true if it can be modified
	 */
	public GList<BlockHandler> canModify(Player p, Block block)
	{
		GList<BlockHandler> h = new GList<BlockHandler>();
		
		for(BlockHandler i : handlers)
		{
			if(!i.canModify(p, block))
			{
				h.add(i);
			}
		}
		
		return h;
	}
	
	/**
	 * Does the block have any protection?
	 * 
	 * @param block
	 *            the block
	 * @return true if it does
	 */
	public GList<BlockHandler> hasProtection(Block block)
	{
		GList<BlockHandler> h = new GList<BlockHandler>();
		
		for(BlockHandler i : handlers)
		{
			if(i.hasProtection(block))
			{
				h.add(i);
			}
		}
		
		return h;
	}
	
	/**
	 * Get the protector
	 * 
	 * @return the protector
	 */
	public GList<String> getProtectors()
	{
		GList<String> h = new GList<String>();
		
		for(BlockHandler i : handlers)
		{
			h.add(i.getProtector());
		}
		
		return h;
	}
	
	/**
	 * Get the protector for this block
	 * 
	 * @param block
	 *            the specific block
	 * @return the protector for the given block
	 */
	public GList<String> getProtectors(Block block)
	{
		GList<String> h = new GList<String>();
		
		for(BlockHandler i : handlers)
		{
			if(i.getProtector(block) != null)
			{
				h.add(i.getProtector(block));
			}
		}
		
		return h;
	}
	
	public GList<BlockHandler> getHandlers()
	{
		return handlers;
	}
}
