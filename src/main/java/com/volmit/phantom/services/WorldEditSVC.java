package com.volmit.phantom.services;

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
import com.sk89q.worldedit.CuboidClipboard;
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
import com.volmit.phantom.lang.F;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.plugin.SimpleService;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.util.Cuboid;

@SuppressWarnings("deprecation")
public class WorldEditSVC extends SimpleService
{
	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public boolean hasSelection(Player player)
	{
		return getSelection(player) != null;
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

	public void pasteSchematic(File file, EditSession session, Location to) throws MaxChangedBlocksException, DataException, IOException
	{
		MCEditSchematicFormat.getFormat(file).load(file).paste(session, new Vector(to.getX(), to.getY(), to.getZ()), true);
	}

	public void streamSchematicAnvil(File file, Rift rift, PhantomSender sender) throws FileNotFoundException, IOException
	{
		try
		{
			World tworld = rift.getWorld();
			sender.sendMessage("Opening NBT Block Streams");
			EditSession session = getEditSession(tworld);
			sender.sendMessage("Streaming Schematic Data");
			CuboidClipboard scm = MCEditSchematicFormat.getFormat(file).load(file);
			Vector dimensions = new Vector(scm.getWidth(), scm.getHeight(), scm.getLength());
			sender.sendMessage("Dimensions: " + dimensions.getBlockX() + ", " + dimensions.getBlockZ() + " (" + dimensions.getBlockY() + ")");
			Vector to = new Vector(0, -scm.getOffset().getBlockY() + 20, 0);
			sender.sendMessage("Generated " + generateChunks(tworld, dimensions) + " Chunks for MCAStream");
			scm.paste(session, to, true);
			session.flushQueue();
			sender.sendMessage("Modified " + F.f(dimensions.getBlockX() * dimensions.getBlockY() * dimensions.getZ()) + " Blocks");
			rift.setForceLoadX((int) (dimensions.getX() / 16D) + 1);
			rift.setForceLoadZ((int) (dimensions.getZ() / 16D) + 1);
			rift.setSpawn(new Location(rift.getWorld(), to.getX(), to.getY(), to.getZ()));
			rift.setTemporary(true);
			rift.saveConfiguration();
			rift.slowlyPreload();
		}

		catch(MaxChangedBlocksException | DataException e)
		{
			e.printStackTrace();
		}
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
		schem.save(file, ClipboardFormat.SCHEMATIC);
	}
}
