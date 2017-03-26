package org.phantomapi.pet;

import java.lang.reflect.Field;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import net.minecraft.server.v1_9_R2.Entity;

public enum EntityTypes
{
	MINI_PET("MiniPet", 54, MiniEntityPet.class);
	
	private EntityTypes(String name, int id, Class<? extends Entity> custom)
	{
		addToMaps(custom, name, id);
	}
	
	public static org.bukkit.entity.Entity spawnEntity(Entity entity, Location loc)
	{
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
		return entity.getBukkitEntity();
	}
	
	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object)
	{
		Field field;
		Object o = null;
		try
		{
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			o = field.get(object);
		}
		catch(NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return o;
	}
	
	@SuppressWarnings("unchecked")
	private static void addToMaps(Class<?> clazz, String name, int id)
	{
		((Map<String, Class<?>>) getPrivateField("c", net.minecraft.server.v1_9_R2.EntityTypes.class, null)).put(name, clazz);
		((Map<Class<?>, String>) getPrivateField("d", net.minecraft.server.v1_9_R2.EntityTypes.class, null)).put(clazz, name);
		((Map<Class<?>, Integer>) getPrivateField("f", net.minecraft.server.v1_9_R2.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
	}
}