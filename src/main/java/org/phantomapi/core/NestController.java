package org.phantomapi.core;

import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.phantomapi.async.AsyncUtil;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.nest.NestedBlock;
import org.phantomapi.nest.NestedChunk;
import org.phantomapi.nest.PhantomChunkNest;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.world.W;

public class NestController extends Controller
{
	private GMap<Chunk, PhantomChunkNest> nests;
	
	public NestController(Controllable parentController)
	{
		super(parentController);
		
		nests = new GMap<Chunk, PhantomChunkNest>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		s("Saving " + F.f(nests.k().size()) + " Chunks...");
		
		for(Chunk i : nests.k())
		{
			try
			{
				nests.get(i).save();
			}
			
			catch(IOException e)
			{
				ExceptionUtil.print(e);
			}
		}
	}
	
	public NestedChunk get(Chunk c)
	{
		AsyncUtil.enforceSync();
		c = W.toSync(c);
		
		if(!c.isLoaded())
		{
			return null;
		}
		
		if(!nests.contains(c))
		{
			nests.put(c, new PhantomChunkNest(c));
		}
		
		return nests.get(c);
	}
	
	public NestedBlock get(Block block)
	{
		AsyncUtil.enforceSync();
		block = W.toSync(block);
		
		if(!block.getChunk().isLoaded())
		{
			return null;
		}
		
		return get(block.getChunk()).getBlock(block);
	}
}
