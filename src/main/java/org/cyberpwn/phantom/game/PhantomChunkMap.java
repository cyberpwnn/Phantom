package org.cyberpwn.phantom.game;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.W;

public class PhantomChunkMap<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends PhantomMap<M, G, T, P>
{
	private GList<Chunk> chunks;
	
	public PhantomChunkMap()
	{
		this.chunks = new GList<Chunk>();
	}
	
	public void addChunk(Chunk c) throws MapInvalidRegionAddException
	{
		if(!contains(c))
		{
			if(chunks.isEmpty())
			{
				chunks.add(c);
			}
			
			else
			{
				for(Chunk i : W.chunkFaces(c))
				{
					if(contains(i))
					{
						chunks.add(c);
						return;
					}
				}
				
				throw new MapInvalidRegionAddException("Chunk is not connected to any adjacent chunks in the map.");
			}
		}
	}
	
	@Override
	public boolean contains(Location location)
	{
		return contains(location.getChunk());
	}
	
	public boolean contains(Chunk c)
	{
		return chunks.contains(c);
	}
}
