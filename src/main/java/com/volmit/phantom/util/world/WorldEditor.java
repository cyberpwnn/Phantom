package com.volmit.phantom.util.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;
import com.volmit.phantom.api.command.PhantomSender;
import com.volmit.phantom.api.rift.Rift;
import com.volmit.phantom.api.service.SVC;
import com.volmit.phantom.lib.service.WorldEditSVC;

@SuppressWarnings("deprecation")
public class WorldEditor
{
	public static World getAsyncWorld(World world)
	{
		return SVC.get(WorldEditSVC.class).getAsyncWorld(world);
	}

	public static boolean hasSelection(Player player)
	{
		return SVC.get(WorldEditSVC.class).hasSelection(player);
	}

	public static Cuboid getSelection(Player player)
	{
		return SVC.get(WorldEditSVC.class).getSelection(player);
	}

	public static Location getLocation(World w, Vector v)
	{
		return SVC.get(WorldEditSVC.class).getLocation(w, v);
	}

	public static EditSession getEditSession(World world)
	{
		return SVC.get(WorldEditSVC.class).getEditSession(world);
	}

	public static boolean saveSchematic(File file, Cuboid c)
	{
		try
		{
			SVC.get(WorldEditSVC.class).saveSchematic(file, c);
		}

		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void pasteSchematic(File file, EditSession session, Location to) throws MaxChangedBlocksException, DataException, IOException
	{
		SVC.get(WorldEditSVC.class).pasteSchematic(file, session, to);
	}

	public static void streamSchematicAnvil(File file, Rift rift, PhantomSender sender) throws FileNotFoundException, IOException
	{
		SVC.get(WorldEditSVC.class).streamSchematicAnvil(file, rift, sender);
	}

	public static Cuboid streamClipboardAnvil(Rift rift, Player p) throws Throwable
	{
		return SVC.get(WorldEditSVC.class).streamClipboardAnvil(rift, p);
	}
}
