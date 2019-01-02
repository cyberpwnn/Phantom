package com.volmit.phantom.util;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.protocol.WrapperPlayServerEntityMetadata;
import com.volmit.phantom.services.NMSSVC;
import com.volmit.phantom.text.C;
import com.volmit.phantom.time.M;

public class GlowingBlock
{
	private static int idd = 123456789;
	private int id;
	private UUID uid;
	private Location location;
	private Location current;
	private Player player;
	private double factor;
	private Vector velocity;
	private boolean active;
	private long mv = M.ms();
	private MaterialBlock mb;
	private ChatColor c;

	public GlowingBlock(Player player, Location init, MaterialBlock mb, ChatColor c)
	{
		this.mb = mb;
		this.uid = UUID.randomUUID();
		this.id = idd--;
		location = init;
		current = init.clone();
		this.player = player;
		factor = Math.PI;
		active = false;
		velocity = new Vector();
		this.c = c;
	}

	public int getId()
	{
		return id;
	}

	private void sendMetadata(boolean glowing)
	{
		WrapperPlayServerEntityMetadata w = new WrapperPlayServerEntityMetadata();
		GList<WrappedWatchableObject> watch = new GList<WrappedWatchableObject>();
		watch.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (glowing ? 0x40 : 0)));
		watch.add(new WrappedWatchableObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), (boolean) (true)));
		w.setEntityID(id);
		w.setMetadata(watch);
		w.sendPacket(getPlayer());
	}

	public void sendMetadata(ChatColor c)
	{
		SVC.get(NMSSVC.class).sendGlowingColorMetaEntity(getPlayer(), uid, C.values()[c.ordinal()]);
	}

	public void update()
	{
		if(M.ms() - mv < 50)
		{
			return;
		}

		if(location.getX() == current.getX() && location.getY() == current.getY() && location.getZ() == current.getZ())
		{
			return;
		}

		mv = M.ms();

		if(location.distanceSquared(current) > 16)
		{
			sendTeleport(location);
			current = location;
		}

		else
		{
			double dx = location.getX() - current.getX();
			double dy = location.getY() - current.getY();
			double dz = location.getZ() - current.getZ();
			dx += velocity.getX();
			dy += velocity.getY();
			dz += velocity.getZ();
			dx = M.clip(dx, -8, 8);
			dy = M.clip(dy, -8, 8);
			dz = M.clip(dz, -8, 8);
			sendMove(dx / factor, dy / factor, dz / factor);
			current.add(dx / factor, dy / factor, dz / factor);
			current.setX(Math.abs(location.getX() - current.getX()) < 0.00001 ? location.getX() : current.getX());
			current.setY(Math.abs(location.getY() - current.getY()) < 0.00001 ? location.getY() : current.getY());
			current.setZ(Math.abs(location.getZ() - current.getZ()) < 0.00001 ? location.getZ() : current.getZ());

			if(location.getX() == current.getX() && location.getY() == current.getY() && location.getZ() == current.getZ())
			{
				sendTeleport(location);
				current = location;
			}
		}
	}

	public Location getPosition()
	{
		return location.clone();
	}

	public void setPosition(Location l)
	{
		location = l;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void destroy()
	{
		sendDestroy();
	}

	public void create()
	{
		sendSpawn();
	}

	public boolean isActive()
	{
		return active;
	}

	public void setFactor(int i)
	{
		factor = i;
	}

	private void sendTeleport(Location l)
	{
		SVC.get(NMSSVC.class).teleportEntity(id, player, l, false);
	}

	private void sendMove(double x, double y, double z)
	{
		SVC.get(NMSSVC.class).moveEntityRelative(id, player, x, y, z, false);
	}

	public void sendDestroy()
	{
		active = false;
		SVC.get(NMSSVC.class).sendRemoveGlowingColorMetaEntity(getPlayer(), uid);
		sendMetadata(false);
		SVC.get(NMSSVC.class).removeEntity(id, player);
	}

	public void sendSpawn()
	{
		SVC.get(NMSSVC.class).spawnFallingBlock(id, uid, location, player, mb);
		sendMetadata(c);
		sendMetadata(true);
		active = true;
	}
}
