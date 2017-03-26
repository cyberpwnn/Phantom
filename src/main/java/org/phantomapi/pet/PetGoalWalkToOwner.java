package org.phantomapi.pet;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import net.minecraft.server.v1_9_R2.EntityInsentient;
import net.minecraft.server.v1_9_R2.PathEntity;
import net.minecraft.server.v1_9_R2.PathfinderGoal;

public class PetGoalWalkToOwner extends PathfinderGoal
{
	private EntityInsentient entity;
	private PathEntity path;
	private UUID playerUUID;
	private double speed;
	
	public PetGoalWalkToOwner(EntityInsentient paramEntityInsentient, UUID paramUUID, double paramDouble)
	{
		entity = paramEntityInsentient;
		playerUUID = paramUUID;
		speed = paramDouble;
	}
	
	@Override
	public boolean a()
	{
		if(Bukkit.getPlayer(playerUUID) == null)
		{
			return path != null;
		}
		
		Location localLocation = Bukkit.getPlayer(playerUUID).getLocation();
		
		entity.getNavigation();
		path = entity.getNavigation().a(localLocation.getX() + 1.0D, localLocation.getY(), localLocation.getZ() + 1.0D);
		entity.getNavigation();
		
		if(path != null)
		{
			c();
		}
		return path != null;
	}
	
	@Override
	public void c()
	{
		entity.getNavigation().a(path, speed);
	}
}
