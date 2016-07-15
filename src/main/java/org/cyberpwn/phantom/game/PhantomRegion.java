package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.world.ChunkletMesh;

public class PhantomRegion<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> implements Region<R, M, G, T, P>
{
	private ChunkletMesh mesh;
	private M map;
	
	public PhantomRegion(M map)
	{
		this.map = map;
		this.mesh = new ChunkletMesh(map.getWorld());
	}
	
	@Override
	public M getMap()
	{
		return map;
	}
	
	@Override
	public boolean contains(P player)
	{
		return mesh.contains(player.getPlayer());
	}
	
	@Override
	public boolean contains(Player player)
	{
		return mesh.contains(player);
	}
}
