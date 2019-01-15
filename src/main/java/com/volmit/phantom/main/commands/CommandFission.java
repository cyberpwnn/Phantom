package com.volmit.phantom.main.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.volmit.phantom.api.command.PhantomCommand;
import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.fission.FissionQueue;
import com.volmit.phantom.api.fission.SSphere;
import com.volmit.phantom.api.lang.F;
import com.volmit.phantom.api.lang.Profiler;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.api.sheduler.A;
import com.volmit.phantom.lib.service.FissionSVC;
import com.volmit.phantom.util.world.MaterialBlock;

public class CommandFission extends PhantomCommand
{
	public CommandFission()
	{
		super("fset");
	}

	@Override
	public boolean handle(PhantomSender sender, String[] args)
	{
		sender.sendMessage("Sec... ");

		new A()
		{
			@Override
			public void run()
			{
				Block b = sender.player().getLocation().getBlock();
				SSphere cc = new SSphere(b.getX(), b.getY(), b.getZ(), true, 1000);
				Profiler px = new Profiler();
				px.begin();
				FissionQueue q = SVC.get(FissionSVC.class).set(cc, b.getWorld(), new MaterialBlock(Material.STONE));
				px.end();
				sender.sendMessage("Set " + F.f((int) cc.getVolume()) + " blocks in " + F.time(q.getTotalComputeTime(), 2));
				sender.sendMessage("Waited for " + F.f(q.getKeepLoaded().size()) + " chunks to load in " + F.time(q.getChunkWaitTime(), 2));
				sender.sendMessage("Total Operation took " + F.time(px.getMilliseconds(), 2));
			}
		};

		return true;
	}
}
