package com.volmit.phantom.input;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.volmit.phantom.plugin.PhantomPlugin;
import com.volmit.phantom.plugin.SR;
import com.volmit.phantom.text.C;
import com.volmit.phantom.util.GlowKit;
import com.volmit.phantom.util.MaterialBlock;
import com.volmit.phantom.util.Players;

public abstract class BlockEntitySelector implements Listener
{
	private Player viewer;
	private int range;
	private C color;
	private boolean active;
	private SR sr;
	private Object lookingAt;

	public BlockEntitySelector(Player viewer, int range, C color)
	{
		this.viewer = viewer;
		this.range = range;
		this.color = color;
	}

	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(e.getAction().equals(Action.LEFT_CLICK_AIR))
		{
			clicked(lookingAt);
		}
	}

	public BlockEntitySelector open()
	{
		active = true;
		sr = new SR(0)
		{
			@Override
			public void run()
			{
				drun();
			}
		};
		Bukkit.getPluginManager().registerEvents(this, PhantomPlugin.plugin);
		return this;
	}

	public BlockEntitySelector close()
	{
		if(lookingAt != null)
		{
			if(lookingAt instanceof Entity)
			{
				GlowKit.remove(viewer, (Entity) lookingAt);
			}

			if(lookingAt instanceof Block)
			{
				GlowKit.remove(viewer, (Block) lookingAt);
			}
		}

		active = false;
		try
		{
			sr.cancel();
		}

		catch(Throwable e)
		{

		}

		HandlerList.unregisterAll(this);
		return this;
	}

	public abstract void clicked(Object b);

	public abstract void selected(Object b);

	public abstract boolean canSelect(MaterialBlock b);

	public abstract boolean canSelect(EntityType t);

	private void changeTarget(Object o)
	{
		if(!active)
		{
			return;
		}

		boolean inform = false;

		if((lookingAt != null && o == null) || (lookingAt != null && o != null && (!o.equals(lookingAt))))
		{
			if(lookingAt instanceof Entity)
			{
				GlowKit.remove(viewer, (Entity) lookingAt);
			}

			if(lookingAt instanceof Block)
			{
				GlowKit.remove(viewer, (Block) lookingAt);
			}

			inform = true;
		}

		if(o != null)
		{
			if(o instanceof Entity)
			{
				GlowKit.glow(viewer, (Entity) o, color);
			}

			if(o instanceof Block)
			{
				GlowKit.glow(viewer, (Block) o, color);
			}

			inform = true;
		}

		if(inform)
		{
			selected(o);
		}

		lookingAt = o;
	}

	public void drun()
	{
		Entity e = Players.getEntityLookingAt(viewer, range, 1);

		if(e != null && canSelect(e.getType()))
		{
			changeTarget(e);
			return;
		}

		Block b = Players.targetBlock(viewer, range).getBlock();

		if(!b.isEmpty())
		{
			changeTarget(b);
		}

		else
		{
			changeTarget(null);
		}
	}
}
