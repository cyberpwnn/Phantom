package org.phantomapi.event;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.phantomapi.event.structure.MovementEvent;

/**
 * A player has changed chunks by movement
 * 
 * @author cyberpwn
 */
public class PlayerMoveChunkEvent extends MovementEvent
{
	public PlayerMoveChunkEvent(Player player, Location from, Location to)
	{
		super(player, from, to);
	}
	
	public Chunk getFromChunk()
	{
		return getFrom().getChunk();
	}
	
	public Chunk getToChunk()
	{
		return getTo().getChunk();
	}
}
