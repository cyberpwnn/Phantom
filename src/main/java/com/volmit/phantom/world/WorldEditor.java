package com.volmit.phantom.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.volmit.phantom.lang.F;
import com.volmit.phantom.plugin.PhantomSender;
import com.volmit.phantom.rift.Rift;
import com.volmit.phantom.util.Cuboid;

@SuppressWarnings("deprecation")
public class WorldEditor
{
	public static EditSession getEditSession(World world)
	{
		return new EditSessionBuilder(FaweAPI.getWorld(world.getName())).fastmode(true).build();
	}

	public static void pasteSchematic(File file, EditSession session, Location to) throws MaxChangedBlocksException, DataException, IOException
	{
		MCEditSchematicFormat.getFormat(file).load(file).paste(session, new Vector(to.getX(), to.getY(), to.getZ()), true);
	}

	public static void streamSchematicAnvil(File file, Rift rift, PhantomSender sender) throws FileNotFoundException, IOException
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

	private static int generateChunks(World tworld, Vector dimensions)
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
}
