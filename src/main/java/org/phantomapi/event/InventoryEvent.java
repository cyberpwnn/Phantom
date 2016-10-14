package org.phantomapi.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Represents an inventory event
 * 
 * @author cyberpwn
 */
public class InventoryEvent extends CancellablePhantomEvent
{
	protected final Inventory inventory;
	protected final Player player;
	
	public InventoryEvent(Inventory inventory, Player player)
	{
		super();
		
		this.inventory = inventory;
		this.player = player;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public Player getPlayer()
	{
		return player;
	}
}
