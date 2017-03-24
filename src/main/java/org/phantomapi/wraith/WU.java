package org.phantomapi.wraith;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.phantomapi.lang.GList;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class WU
{
	public static void pathfindTo(NPC npc, Location location)
	{
		npc.getNavigator().setPaused(false);
		npc.getNavigator().setTarget(location);
	}
	
	public static void pathfindTo(NPC npc, Entity e)
	{
		npc.getNavigator().setPaused(false);
		npc.getNavigator().setTarget(e, false);
	}
	
	public static boolean isNPC(Entity e)
	{
		return CitizensAPI.getNPCRegistry().isNPC(e);
	}
	
	public static GList<Entity> getNPCs()
	{
		GList<Entity> entities = new GList<Entity>();
		
		for(World i : Bukkit.getWorlds())
		{
			for(Entity j : i.getEntities())
			{
				if(isNPC(j))
				{
					entities.add(j);
				}
			}
		}
		
		return entities;
	}
}
