package org.phantomapi.nbt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

public abstract class CustomFirework extends CustomItem
{
	
	protected CustomFirework(String slug, String name)
	{
		super(slug, name, Material.FIREWORK);
	}
	
	@Override
	public final void onRightClick(PlayerInteractEvent event, PlayerDetails details)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Location loc = event.getClickedBlock().getLocation();
			if(fire(loc.add(UtilsMc.faceToDelta(event.getBlockFace())), details, null) != null)
			{
				details.consumeItem();
			}
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onDispense(BlockDispenseEvent event, DispenserDetails details)
	{
		event.setCancelled(true);
	}
	
	protected final Firework fire(Location location, IConsumableDetails details, Object userObject)
	{
		final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		final FireworkPlayerDetails fDetails = FireworkPlayerDetails.fromConsumableDetails(details, firework, userObject);
		if(!onFire(fDetails, meta))
		{
			firework.remove();
			return null;
		}
		firework.setFireworkMeta(meta);
		
		final BukkitTask[] task = new BukkitTask[1];
		task[0] = Bukkit.getScheduler().runTaskTimer(getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(firework.isDead())
				{
					onExplode(fDetails);
					task[0].cancel();
				}
				firework.setTicksLived(Integer.MAX_VALUE);
			}
		}, 10 * (1 + meta.getPower()), 2);
		return firework;
	}
	
	public boolean onFire(FireworkPlayerDetails details, FireworkMeta meta)
	{
		return true;
	};
	
	public void onExplode(FireworkPlayerDetails details)
	{
	};
	
}
