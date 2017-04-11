package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.util.P;
import org.phantomapi.vfx.LineParticleManipulator;
import org.phantomapi.vfx.ParticleEffect;

public class Pulse
{
	private Location center;
	private Vector shape;
	private Double damage;
	private Boolean breakBlocks;
	private Boolean pushEntities;
	
	public Pulse()
	{
		shape = new Vector(1, 1, 1);
		damage = 0.0;
		breakBlocks = false;
		pushEntities = false;
	}
	
	public Pulse shape(Vector shape)
	{
		this.shape = shape;
		return this;
	}
	
	public Pulse damage(double damage)
	{
		this.damage = damage;
		return this;
	}
	
	public Pulse breakBlocks()
	{
		breakBlocks = true;
		return this;
	}
	
	public Pulse pushEntities()
	{
		pushEntities = true;
		return this;
	}
	
	public void impulse(Location center)
	{
		this.center = center;
		
		if(pushEntities)
		{
			Area a = new Area(center, Math.max(shape.getX(), Math.max(shape.getY(), shape.getZ())));
			
			for(Entity i : a.getNearbyEntities())
			{
				push(i);
			}
		}
	}
	
	private Vector getImpulse(Location target)
	{
		Vector to = center.subtract(target).toVector().normalize();
		Vector range = shape.clone();
		
		if(to.getX() < range.getX() && to.getY() < range.getY() && to.getZ() < range.getZ())
		{
			return new Vector().add(to).normalize().multiply(getIntensity(target));
		}
		
		return null;
	}
	
	private double getIntensity(Location target)
	{
		Vector to = center.subtract(target).toVector().normalize();
		Vector range = shape.clone();
		
		if(to.getX() < range.getX() && to.getY() < range.getY() && to.getZ() < range.getZ())
		{
			new LineParticleManipulator()
			{
				@Override
				public void play(Location l)
				{
					ParticleEffect.BARRIER.display(0, 1, l, 128);
				}
			}.play(target, center, 0.1);
			
			return Math.abs(Math.abs(Math.max(range.getX(), Math.max(range.getY(), range.getZ()))) / (target.distance(center)));
		}
		
		return 0;
	}
	
	private void push(Entity e)
	{
		Vector imp = getImpulse(e.getLocation());
		
		if(imp != null)
		{
			e.setVelocity(getImpulse(e.getLocation()));
			e.sendMessage(getIntensity(e.getLocation()) + " fff");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void push(Block b)
	{
		MaterialBlock mb = new MaterialBlock(b.getLocation());
		b.setType(Material.AIR);
		FallingBlock f = b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), mb.getMaterial(), mb.getData());
		f.setInvulnerable(true);
		f.setVelocity(getImpulse(f.getLocation()));
		f.setHurtEntities(false);
		f.setDropItem(false);
		f.setGlowing(true);
	}
}
