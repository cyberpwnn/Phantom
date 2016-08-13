package org.phantomapi.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.world.ChunkletMesh;

/**
 * Phantom region implementation
 * @author cyberpwn
 *
 */
public class PhantomRegion implements Region
{
	private Map map;
	private ChunkletMesh mesh;
	
	public PhantomRegion(Map map)
	{
		this.map = map;
		this.mesh = new ChunkletMesh(map.getWorld());
	}
	
	@Override
	public GList<GamePlayer> getPlayers()
	{
		GList<GamePlayer> px = new GList<GamePlayer>();
		
		for(GamePlayer i : map.getPlayers())
		{
			if(contains(i))
			{
				px.add(i);
			}
		}
		
		return px;
	}

	@Override
	public boolean contains(GamePlayer p)
	{
		return mesh.contains(p.getPlayer());
	}

	@Override
	public boolean contains(Player p)
	{
		return mesh.contains(p);
	}
	
	@Override
	public boolean contains(Location l)
	{
		return mesh.contains(l);
	}

	@Override
	public Map getMap()
	{
		return map;
	}

	@Override
	public ChunkletMesh getMesh()
	{
		return mesh;
	}
}
