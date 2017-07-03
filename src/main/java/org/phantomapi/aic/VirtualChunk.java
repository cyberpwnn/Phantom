package org.phantomapi.aic;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.phantomapi.world.MaterialBlock;

public interface VirtualChunk
{
	public void set(int x, int y, int z, int id, byte data);
	
	public void set(int x, int y, int z, MaterialBlock m);
	
	public MaterialBlock get(int x, int y, int z);
	
	public int getId(int x, int y, int z);
	
	public byte getData(int x, int y, int z);
	
	public int getX();
	
	public int getZ();
	
	public World getWorld();
	
	public Chunk getChunk();
	
	public void send(Player p);
}
