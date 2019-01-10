package com.volmit.phantom.main;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.volmit.phantom.api.inventory.Window;
import com.volmit.phantom.api.inventory.WindowResolution;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.imp.generator.VoidGenerator;
import com.volmit.phantom.imp.inventory.UIElement;
import com.volmit.phantom.imp.inventory.UIPaneDecorator;
import com.volmit.phantom.imp.inventory.UIWindow;
import com.volmit.phantom.imp.plugin.Module;
import com.volmit.phantom.imp.plugin.Scaffold.Command;
import com.volmit.phantom.imp.plugin.Scaffold.Instance;
import com.volmit.phantom.imp.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.imp.plugin.Scaffold.Permission;
import com.volmit.phantom.imp.plugin.Scaffold.PlayerTest;
import com.volmit.phantom.imp.plugin.Scaffold.Start;
import com.volmit.phantom.imp.rift.RiftException;
import com.volmit.phantom.lib.service.DevelopmentSVC;
import com.volmit.phantom.lib.service.RiftSVC;
import com.volmit.phantom.main.commands.CommandModules;
import com.volmit.phantom.main.commands.CommandPhantom;
import com.volmit.phantom.main.commands.CommandPlotPaste;
import com.volmit.phantom.main.commands.CommandRift;
import com.volmit.phantom.main.commands.CommandServices;
import com.volmit.phantom.main.permissions.PermissionPhantom;
import com.volmit.phantom.util.text.C;
import com.volmit.phantom.util.vfx.GlowKit;
import com.volmit.phantom.util.world.BlockEntitySelector;
import com.volmit.phantom.util.world.MaterialBlock;
import com.volmit.phantom.util.world.Players;

@ModuleInfo(name = "Phantom", version = "3.1.30", author = "cyberpwn", color = C.LIGHT_PURPLE)
public class PhantomModule extends Module
{
	@Instance
	public static PhantomModule instance;

	@Permission
	public static PermissionPhantom perm;

	@Command
	public CommandPhantom phantom;

	@Command
	public CommandPlotPaste plotpaste;

	@Command("Rift")
	public CommandRift rfit;

	@Command("Modules")
	public CommandModules modules;

	@Command("Services")
	public CommandServices services;

	@Start
	public void start()
	{
		SVC.start(DevelopmentSVC.class);
	}

	@PlayerTest
	public void selector(Player p)
	{
		new BlockEntitySelector(p, 256, C.GREEN)
		{
			@Override
			public void selected(Object b)
			{

			}

			@Override
			public void clicked(Object b)
			{
				close();
				p.sendMessage("You selected " + b);
			}

			@Override
			public boolean canSelect(EntityType t)
			{
				return true;
			}

			@Override
			public boolean canSelect(MaterialBlock b)
			{
				return true;
			}
		}.open();
	}

	@PlayerTest
	public void glowBlock(Player p)
	{
		Block b = Players.targetBlock(p, 128).getBlock();
		GlowKit.glow(p, b, C.LIGHT_PURPLE);

		new S(100)
		{
			@Override
			public void run()
			{
				GlowKit.remove(p, b);
			}
		};
	}

	@PlayerTest
	public void glowEntity(Player p)
	{
		Entity e = Players.getEntityLookingAt(p, 7, 0.7);

		if(e == null)
		{
			p.sendMessage(getTag("Test") + "GlowTarget -> Yourself");
			e = p;
		}

		Entity ex = e;
		e.setGlowing(true);
		GlowKit.glow(p, e, C.LIGHT_PURPLE);

		new S(100)
		{
			@Override
			public void run()
			{
				ex.setGlowing(false);
				GlowKit.remove(p, ex);
			}
		};
	}

	@PlayerTest
	public void newRift(Player p)
	{
		try
		{
			//@builder
			SVC.get(RiftSVC.class).createRift("test-" + UUID.randomUUID())
			.setEntityTickLimit(0.1)
			.setTileTickLimit(0.1)
			.setPhysicsThrottle(20)
			.setGenerator(VoidGenerator.class)
			.setEnvironment(Environment.THE_END)
			.setMaxTNTUpdatesPerTick(1)
			.setRandomLightUpdates(false)
			.setSeed(1337)
			.setViewDistance(8)
			.setTemporary(true)
			.setForcedGameMode(GameMode.SPECTATOR)
			.setAllowBosses(false)
			.load().send(p);
			//@done
		}

		catch(RiftException e)
		{
			e.printStackTrace();
		}
	}

	@PlayerTest
	public void inventoryScrolling(Player p)
	{
		Window w = new UIWindow(p).setResolution(WindowResolution.W9_H6).setDecorator(new UIPaneDecorator(C.BLACK)).setTitle("Testing").setViewportHeight(6);

		for(int i = 0; i < 128; i++)
		{
			w.setElement(M.rand(-1, 1), i, new UIElement("tow").setEnchanted(true).setMaterial(new MaterialBlock(Material.DIRT, (byte) 1)).setName("Row " + i).addLore("Lore 2 1").addLore("Lore 2 2").setCount(3));
		}

		w.open();
	}

	@Override
	public boolean isNative()
	{
		return true;
	}
}
