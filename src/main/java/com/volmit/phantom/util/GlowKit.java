package com.volmit.phantom.util;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.services.NMSSVC;
import com.volmit.phantom.text.C;

public class GlowKit
{
	private static GMap<Player, GMap<Block, GlowingBlock>> glowblocks = new GMap<>();

	public static void glow(Player observer, Block block, C c)
	{
		MaterialBlock mb = new MaterialBlock(block);

		if(mb.getMaterial().equals(Material.AIR))
		{
			return;
		}

		if(!glowblocks.containsKey(observer))
		{
			glowblocks.put(observer, new GMap<>());
		}

		GMap<Block, GlowingBlock> b = glowblocks.get(observer);

		if(b.containsKey(block))
		{
			b.get(block).sendMetadata(c.chatColor());
			return;
		}

		else
		{
			GlowingBlock gb = new GlowingBlock(observer, block.getLocation().clone().add(0.5, 0, 0.5), mb, c.chatColor());
			gb.sendSpawn();
			gb.update();
			glowblocks.get(observer).put(block, gb);
			SVC.get(NMSSVC.class).sendBlockChange(observer, block.getLocation(), new MaterialBlock(Material.AIR));
		}
	}

	public static void remove(Player observer, Block block)
	{
		MaterialBlock mb = new MaterialBlock(block);

		if(mb.getMaterial().equals(Material.AIR))
		{
			return;
		}

		if(!glowblocks.containsKey(observer))
		{
			return;
		}

		if(!glowblocks.get(observer).containsKey(block))
		{
			return;
		}

		GlowingBlock gb = glowblocks.get(observer).get(block);
		gb.sendDestroy();
		SVC.get(NMSSVC.class).sendBlockChange(observer, block.getLocation(), mb);
	}

	public static void glow(Player observer, Entity e, C c)
	{
		SVC.get(NMSSVC.class).sendGlowing(observer, e.getEntityId(), true);
		SVC.get(NMSSVC.class).sendGlowingColorMeta(observer, e, c);
	}

	public static void remove(Player observer, Entity e)
	{
		SVC.get(NMSSVC.class).sendGlowing(observer, e.getEntityId(), false);
		SVC.get(NMSSVC.class).sendRemoveGlowingColorMeta(observer, e);
	}

	public static void glow(Player observer, int euid, UUID fakeEntity, C c)
	{
		SVC.get(NMSSVC.class).sendGlowing(observer, euid, true);
		SVC.get(NMSSVC.class).sendGlowingColorMetaEntity(observer, fakeEntity, c);
	}

	public static void remove(Player observer, int euid, UUID fakeEntity)
	{
		SVC.get(NMSSVC.class).sendGlowing(observer, euid, false);
		SVC.get(NMSSVC.class).sendRemoveGlowingColorMetaEntity(observer, fakeEntity);
	}
}
