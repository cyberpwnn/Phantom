package org.phantomapi.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.phantomapi.Phantom;
import org.phantomapi.event.PlayerArrowDamagePlayerEvent;
import org.phantomapi.event.PlayerDamagePlayerEvent;
import org.phantomapi.event.PlayerKillPlayerEvent;
import org.phantomapi.event.PlayerMoveLookEvent;
import org.phantomapi.event.PlayerMovePositionEvent;
import org.phantomapi.event.PlayerProjectileDamagePlayerEvent;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;

public abstract class WorldHandler implements Listener
{
	public WorldHandler()
	{
		
	}
	
	public void register()
	{
		Phantom.instance().registerListener(this);
	}
	
	public void unregister()
	{
		Phantom.instance().unRegisterListener(this);
	}
	
	@EventHandler
	public abstract void on(PlayerDeathEvent e);
	
	@EventHandler
	public abstract void on(PlayerMovePositionEvent e);
	
	@EventHandler
	public abstract void on(PlayerMoveLookEvent e);
	
	@EventHandler
	public abstract void on(PlayerArrowDamagePlayerEvent e);
	
	@EventHandler
	public abstract void on(PlayerDamagePlayerEvent e);
	
	@EventHandler
	public abstract void on(PlayerBedEnterEvent e);
	
	@EventHandler
	public abstract void on(PlayerBedLeaveEvent e);
	
	@EventHandler
	public abstract void on(PlayerProjectileDamagePlayerEvent e);
	
	@EventHandler
	public abstract void on(PlayerKillPlayerEvent e);
	
	@EventHandler
	public abstract void on(EntityDeathEvent e);
	
	@EventHandler
	public abstract void on(EntityDamageEvent e);
	
	@EventHandler
	public abstract void on(EntityDamageByEntityEvent e);
	
	@EventHandler
	public abstract void on(EntityDamageByBlockEvent e);
	
	@EventHandler
	public abstract void on(EntityTargetLivingEntityEvent e);
	
	@EventHandler
	public abstract void on(EntitySpawnEvent e);
	
	@EventHandler
	public abstract void on(BlockBreakEvent e);
	
	@EventHandler
	public abstract void on(BlockPlaceEvent e);
	
	@EventHandler
	public abstract void on(BlockBurnEvent e);
	
	@EventHandler
	public abstract void on(BlockDamageEvent e);
	
	@EventHandler
	public abstract void on(BlockDispenseEvent e);
	
	@EventHandler
	public abstract void on(BlockGrowEvent e);
	
	@EventHandler
	public abstract void on(BlockFadeEvent e);
	
	@EventHandler
	public abstract void on(BlockExplodeEvent e);
	
	@EventHandler
	public abstract void on(BlockFromToEvent e);
	
	@EventHandler
	public abstract void on(BlockPhysicsEvent e);
	
	@EventHandler
	public abstract void on(BlockInteractEvent e);
	
	@EventHandler
	public abstract void on(BlockSpreadEvent e);
	
	@EventHandler
	public abstract void on(BlockRedstoneEvent e);
	
	@EventHandler
	public abstract void on(WorldSaveEvent e);
	
	@EventHandler
	public abstract void on(ItemMergeEvent e);
	
	@EventHandler
	public abstract void on(ItemDespawnEvent e);
	
	@EventHandler
	public abstract void on(ItemSpawnEvent e);
}
