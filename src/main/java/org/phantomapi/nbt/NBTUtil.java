package org.phantomapi.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.nms.NMSX;

public class NBTUtil
{
	
	public static <T extends Object> NBTTagCompound getNBTTag(T entity)
	{
		NBTTagCompound compound = new NBTTagCompound();
		
		Class<? extends Object> clazz = entity.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		
		for(Method method : methods)
		{
			if((method.getName() == "b") && (method.getParameterTypes().length == 1) && (method.getParameterTypes()[0] == NBTTagCompound.class))
			{
				try
				{
					method.invoke(entity, compound);
				}
				
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		return compound;
	}
	
	public static NBTTagCompound getNBTTag(ItemStack is) throws Exception
	{
		try
		{
			Class<?> clazz_cis = Class.forName("org.bukkit.craftbukkit." + NMSX.getNBTV() + ".inventory.CraftItemStack");
			Object nms_item = clazz_cis.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class).invoke(null, is);
			Object nms_nbt = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTTagCompound").newInstance();
			nms_item.getClass().getMethod("save", nms_nbt.getClass()).invoke(nms_item, nms_nbt);
			Class<?> clazz_nbttools = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTCompressedStreamTools");
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			clazz_nbttools.getMethod("a", nms_nbt.getClass(), OutputStream.class).invoke(null, nms_nbt, (OutputStream) os);
			byte[] out = os.toByteArray();
			NBTTagCompound c = NBTCompressedStreamTools.read(out, NBTReadLimiter.unlimited);
			return c;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static ItemStack getItemStack(NBTTagCompound nbt)
	{
		if(NMSX.getNBTV().startsWith("v1_11"))
		{
			return getItemStackNew(nbt);
		}
		
		else
		{
			return getItemStackOld(nbt);
		}
	}
	
	private static ItemStack getItemStackOld(NBTTagCompound nbt)
	{
		try
		{
			Class<?> clazz_cis = Class.forName("org.bukkit.craftbukkit." + NMSX.getNBTV() + ".inventory.CraftItemStack");
			Class<?> clazz_nms_item = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".ItemStack");
			Class<?> clazz_nms_nbt = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTTagCompound");
			Class<?> clazz_nbttools = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTCompressedStreamTools");
			byte[] data = NBTCompressedStreamTools.toByte(nbt);
			Object mns_nbt = clazz_nbttools.getMethod("a", InputStream.class).invoke(null, (InputStream) new ByteArrayInputStream(data));
			Object nms_item = clazz_nms_item.getMethod("createStack", clazz_nms_nbt).invoke(null, mns_nbt);
			org.bukkit.inventory.ItemStack item = (org.bukkit.inventory.ItemStack) clazz_cis.getMethod("asBukkitCopy", clazz_nms_item).invoke(null, nms_item);
			return item;
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static ItemStack getItemStackNew(NBTTagCompound nbt)
	{
		try
		{
			Class<?> clazz_cis = Class.forName("org.bukkit.craftbukkit." + NMSX.getNBTV() + ".inventory.CraftItemStack");
			Class<?> clazz_nms_item = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".ItemStack");
			Class<?> clazz_nms_nbt = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTTagCompound");
			Class<?> clazz_nbttools = Class.forName("net.minecraft.server." + NMSX.getNBTV() + ".NBTCompressedStreamTools");
			byte[] data = NBTCompressedStreamTools.toByte(nbt);
			Object mns_nbt = clazz_nbttools.getMethod("a", InputStream.class).invoke(null, (InputStream) new ByteArrayInputStream(data));
			Object nms_item = clazz_nms_item.getConstructor(clazz_nms_nbt).newInstance(mns_nbt);
			org.bukkit.inventory.ItemStack item = (org.bukkit.inventory.ItemStack) clazz_cis.getMethod("asBukkitCopy", clazz_nms_item).invoke(null, nms_item);
			return item;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}