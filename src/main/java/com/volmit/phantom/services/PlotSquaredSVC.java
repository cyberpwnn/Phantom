package com.volmit.phantom.services;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import com.volmit.phantom.lang.FinalBoolean;
import com.volmit.phantom.lang.FinalInteger;
import com.volmit.phantom.lang.GBiset;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GSet;
import com.volmit.phantom.plugin.A;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.S;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.SimpleService;
import com.volmit.phantom.util.Cuboid;
import com.volmit.phantom.util.Cuboid.CuboidDirection;
import com.volmit.phantom.util.Direction;

@SuppressWarnings("deprecation")
public class PlotSquaredSVC extends SimpleService
{
	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public com.intellectualcrafters.plot.object.Location getLocation(Location l)
	{
		return new com.intellectualcrafters.plot.object.Location(l.getWorld().getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	public void autoClaimPaste(PhantomSender sender, File schematic) throws DataException, IOException
	{
		new A()
		{
			@Override
			public void run()
			{
				try
				{
					sender.sendMessage("Please Wait");
					Location min = sender.player().getLocation().clone();
					min.setY(16);
					Cuboid c = SVC.get(WorldEditSVC.class).getSchematicRegion(min, schematic);
					FinalBoolean r = new FinalBoolean(false);
					GBiset<Cuboid, GSet<Plot>> t = findSafeCuboid(c);

					if(r.get())
					{
						return;
					}

					r.set(true);

					if(t == null)
					{
						sender.sendMessage("Could not find a suitable area to claim plots. Try flying further out of the mess of claimed plots you are in, perhaps then we wouldnt have to waste so much cpu to find a clean spot.");
					}

					else
					{
						PlotPlayer pp = PlotPlayer.wrap(sender.player());
						GList<Plot> claimed = new GList<>();
						boolean fail = false;
						sender.sendMessage("Claiming " + t.getB().size() + " Plots for your schematic.");

						for(Plot i : t.getB())
						{
							if(!i.getOwners().isEmpty())
							{
								continue;
							}

							if(i.getOwners().isEmpty())
							{
								try
								{
									if(i.claim(pp, false, null))
									{
										claimed.add(i);
									}

									else
									{
										fail = true;
										sender.sendMessage("Failed to claim a plot. Unclaiming all previously claimed plots (in this operation)");
										break;
									}
								}

								catch(Throwable e)
								{

								}
							}

							else
							{
								fail = true;
								sender.sendMessage("Failed to claim a plot. Unclaiming all previously claimed plots (in this operation)");
								break;
							}
						}

						if(fail)
						{
							for(Plot i : claimed)
							{
								i.unclaim();
							}

							sender.sendMessage("Claiming Failed. Schematic not pasted.");
							return;
						}

						Plot p = claimed.get(0);
						p.autoMerge(-1, 256, sender.player().getUniqueId(), true);
						sender.sendMessage("Merged Plots. Streaming Blocks...");
						Vector vOffset = SVC.get(WorldEditSVC.class).getOffset(schematic);

						while(t.getA().getLowerNE().getY() < 10)
						{
							t.getA().shiftNoCopy(CuboidDirection.Up, 1);
						}

						while(t.getA().getLowerNE().getY() > 10)
						{
							t.getA().shiftNoCopy(CuboidDirection.Down, 1);
						}

						Location ll = t.getA().getLowerNE().clone();
						ll.add(-vOffset.getX(), -vOffset.getY(), -vOffset.getZ());
						SVC.get(WorldEditSVC.class).pasteSchematic(schematic, SVC.get(WorldEditSVC.class).getEditSession(sender.player().getWorld()), ll);
						sender.sendMessage("Schematic Pasted!");

						new S()
						{
							@Override
							public void run()
							{
								sender.player().teleport(ll);
								SVC.get(LightSVC.class).relight(t.getA());
							}
						};
					}
				}

				catch(DataException | IOException | MaxChangedBlocksException e)
				{
					e.printStackTrace();
				}
			}
		};

	}

	public PlotArea getPlotArea(Location l)
	{
		return PS.get().getPlotAreaAbs(getLocation(l));
	}

	public GBiset<Cuboid, GSet<Plot>> findSafeCuboid(Cuboid g)
	{
		try
		{
			//@builder
			Cuboid r = g.expand(CuboidDirection.North, Math.max(g.getSizeX(), g.getSizeZ()) / 3)
					.expand(CuboidDirection.South, Math.max(g.getSizeX(), g.getSizeZ()) / 3)
					.expand(CuboidDirection.East, Math.max(g.getSizeX(), g.getSizeZ()) / 3)
					.expand(CuboidDirection.West, Math.max(g.getSizeX(), g.getSizeZ()) / 3);
			//@done
			Cuboid[] cursor = new Cuboid[] {r.clone()};
			FinalInteger v = new FinalInteger(Math.max(r.getSizeX(), r.getSizeZ()));

			while(v.get() > 0)
			{
				v.sub(1);
				cursor[0].shiftNoCopy(Direction.news().pickRandom().f(), v.get());
				GSet<Plot> px = getPlotsIn(cursor[0]);
				if(px != null)
				{
					return new GBiset<Cuboid, GSet<Plot>>(cursor[0], px);
				}

				if(v.get() <= 0)
				{
					return null;
				}
			}
		}

		catch(CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public GSet<Plot> getPlotsIn(Cuboid c)
	{
		GSet<Plot> plots = new GSet<>();
		Location l = c.getLowerNE();
		PlotArea a = getPlotArea(l);

		for(int i = c.getLowerX(); i < c.getUpperX(); i++)
		{
			for(int j = c.getLowerZ(); j < c.getUpperZ(); j++)
			{
				Plot p = a.getPlot(getLocation(new Location(c.getWorld(), i, 63, j)));
				if(p != null && p.getOwners().isEmpty())
				{
					for(Plot k : p.getConnectedPlots())
					{
						if(k == null || !k.getOwners().isEmpty())
						{
							return null;
						}

						for(Plot kk : k.getConnectedPlots())
						{
							if(kk == null || !kk.getOwners().isEmpty())
							{
								return null;
							}

							for(Plot kkv : kk.getConnectedPlots())
							{
								if(kkv == null || !kkv.getOwners().isEmpty())
								{
									return null;
								}
							}
						}
					}

					plots.add(p);
				}

				else if(p != null)
				{
					return null;
				}
			}
		}

		return plots;
	}
}
