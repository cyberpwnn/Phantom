package org.cyberpwn.phantom.game;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.W;

/**
 * A Game Map instance (IN CHUNKS)
 * 
 * @author cyberpwn
 *
 * @param <M>
 *            The MAP TYPE (this implementation class)
 * @param <G>
 *            The GAME TYPE
 * @param <T>
 *            The TEAM TYPE
 * @param <P>
 *            The PLAYER OBJECT TYPE
 */
public class PhantomChunkMap<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends PhantomMap<M, G, T, P>
{
	protected GList<Chunk> chunks;
	
	public PhantomChunkMap(World world)
	{
		super(world);
		
		this.chunks = new GList<Chunk>();
	}
	
	/**
	 * Add a chunk
	 * 
	 * @param c
	 *            the chunk
	 * @throws MapInvalidRegionAddException
	 *             if it doesn't connect to existing chunks in the map.
	 */
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
	
	/**
	 * Does it contain a given chunk?
	 * 
	 * @param c
	 *            the chunk
	 * @return true if the map contains the chunk
	 */
	public boolean contains(Chunk c)
	{
		return chunks.contains(c);
	}
}
