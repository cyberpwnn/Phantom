package com.volmit.phantom.lib.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.object.FawePlayer;
import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.object.schematic.Schematic;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.api.sheduler.A;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.util.world.Cuboid;
import com.volmit.phantom.util.world.Cuboid.CuboidDirection;

@SuppressWarnings("deprecation")
public class WorldEditSVC implements IService
{
	private GMap<File, Schematic> schematicCache;
	private GMap<File, Vector> schematicOffsetCache;

	@Override
	public void onStart()
	{
		schematicCache = new GMap<>();
		schematicOffsetCache = new GMap<>();
	}

	@Override
	public void onStop()
	{

	}

	public boolean hasSelection(Player player)
	{
		return getSelection(player) != null;
	}

	public Cuboid getCuboid(World w, Region r)
	{
		return new Cuboid(getLocation(w, r.getMaximumPoint()), getLocation(w, r.getMinimumPoint()));
	}

	public Cuboid getSelection(Player player)
	{
		Region r = FawePlayer.wrap(player).getSelection();

		if(r != null)
		{
			return new Cuboid(getLocation(player.getWorld(), r.getMaximumPoint()), getLocation(player.getWorld(), r.getMinimumPoint()));
		}

		return null;
	}

	public Location getLocation(World w, Vector v)
	{
		return new Location(w, v.getX(), v.getY(), v.getZ());
	}

	public EditSession getEditSession(World world)
	{
		return new EditSessionBuilder(FaweAPI.getWorld(world.getName())).fastmode(true).build();
	}

	public Cuboid getSchematicRegion(Location minimum, File file) throws DataException, IOException
	{
		Schematic scm = getSchematic(file);
		Vector dimensions = new Vector(scm.getClipboard().getDimensions().getX(), scm.getClipboard().getDimensions().getY(), scm.getClipboard().getDimensions().getZ());
		return new Cuboid(minimum).expand(CuboidDirection.North, dimensions.getBlockX()).expand(CuboidDirection.East, dimensions.getBlockZ()).expand(CuboidDirection.Up, dimensions.getBlockY());
	}

	public void pasteSchematic(File file, EditSession session, Location to) throws MaxChangedBlocksException, DataException, IOException
	{
		session.setFastMode(true);
		getSchematic(file).paste(session, new Vector(to.getX(), to.getY(), to.getZ()), true);
		session.flushQueue();
	}

	public void streamSchematicAnvil(File file, Rift rift, PhantomSender sender) throws FileNotFoundException, IOException
	{
		new A()
		{
			@Override
			public void run()
			{
				World tworld = rift.getWorld();
				sender.sendMessage("Opening NBT Block Streams");
				EditSession session = getEditSession(tworld);
				sender.sendMessage("Streaming Schematic Data");
				Schematic scm = getSchematic(file);
				Vector dimensions = scm.getClipboard().getDimensions();
				sender.sendMessage("Dimensions: " + dimensions.getBlockX() + ", " + dimensions.getBlockZ() + " (" + dimensions.getBlockY() + ")");
				Vector to = new Vector(0, -getOffset(file).getBlockY() + 20, 0);
				generateChunks(tworld, dimensions);
				scm.paste(session, to, true);
				session.flushQueue();

				new S()
				{
					@Override
					public void run()
					{
						rift.setForceLoadX((int) (dimensions.getX() / 16D) + 1);
						rift.setForceLoadZ((int) (dimensions.getZ() / 16D) + 1);
						rift.setSpawn(new Location(rift.getWorld(), to.getX(), to.getY(), to.getZ()));
						rift.setTemporary(true);
						rift.saveConfiguration();
						rift.slowlyPreload();
					}
				};
			}
		};
	}

	public Cuboid streamClipboardAnvil(Rift rift, Player p) throws Throwable
	{
		World tworld = rift.getWorld();
		EditSession csession = getEditSession(p.getWorld());
		EditSession session = getEditSession(tworld);
		session.setFastMode(true);
		csession.setFastMode(true);
		FaweQueue q = FaweAPI.createQueue(tworld.getName(), true);
		Region r = FawePlayer.wrap(p).getSelection();
		Region rx = r.clone();
		Vector dimensions = new Vector(r.getWidth(), r.getHeight(), r.getLength());
		ForwardExtentCopy fc = new ForwardExtentCopy(csession, r, new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ()), q, new Vector(0, p.getLocation().getY(), 0));
		fc.setSourceMask(new ExistingBlockMask(csession));
		Operations.completeBlindly(fc);
		session.flushQueue();
		rx.shift(new Vector(-p.getLocation().getX(), 0, -p.getLocation().getZ()));
		rift.setForceLoadX((int) (dimensions.getX() / 16D) + 1);
		rift.setForceLoadZ((int) (dimensions.getZ() / 16D) + 1);
		rift.setTemporary(true);
		rift.saveConfiguration();
		rift.slowlyPreload();
		q.flush(10000);
		FaweAPI.fixLighting(tworld.getName(), rx);

		return new Cuboid(getLocation(rift.getWorld(), rx.getMaximumPoint()), getLocation(rift.getWorld(), rx.getMinimumPoint()));
	}

	private int generateChunks(World tworld, Vector dimensions)
	{
		Cuboid c = new Cuboid(tworld, -dimensions.getBlockX(), -dimensions.getBlockY(), -dimensions.getBlockZ(), dimensions.getBlockX(), dimensions.getBlockY(), dimensions.getBlockZ());
		int d = 0;

		for(Chunk i : c.getChunks())
		{
			d++;
			i.load();
		}

		return d;
	}

	public World getAsyncWorld(World world)
	{
		return AsyncWorld.wrap(world);
	}

	public void saveSchematic(File file, Cuboid c) throws IOException
	{
		Vector bot = new Vector(c.getLowerX(), c.getLowerY(), c.getLowerZ());
		Vector top = new Vector(c.getUpperX(), c.getUpperY(), c.getUpperZ());
		CuboidRegion region = new CuboidRegion(new BukkitWorld(c.getWorld()), bot, top);
		Schematic schem = new Schematic(region);
		schem.save(file, ClipboardFormat.FAWE);
	}

	public Schematic getSchematic(File f)
	{
		if(!schematicCache.containsKey(f))
		{
			try
			{

				return ClipboardFormat.findByFile(f).load(f);
			}

			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return schematicCache.get(f);
	}

	private Vector cacheOffset(File file) throws FileNotFoundException, IOException, DataException
	{
		Vector off = MCEditSchematicFormat.getFormat(file).load(file).getOffset();
		System.out.println("Offset " + off);
		return off;
	}

	public org.bukkit.util.Vector getOffset(File schematic)
	{
		if(!schematicOffsetCache.containsKey(schematic))
		{
			try
			{
				schematicOffsetCache.put(schematic, cacheOffset(schematic));
			}

			catch(DataException | IOException e)
			{
				e.printStackTrace();
			}
		}

		return new org.bukkit.util.Vector(schematicOffsetCache.get(schematic).getX(), schematicOffsetCache.get(schematic).getY(), schematicOffsetCache.get(schematic).getZ());
	}

	public org.bukkit.util.Vector getOrigin(File schematic) throws DataException, IOException
	{
		Schematic scm = getSchematic(schematic);
		return new org.bukkit.util.Vector(scm.getClipboard().getOrigin().getX(), scm.getClipboard().getOrigin().getY(), scm.getClipboard().getOrigin().getZ());
	}
}
