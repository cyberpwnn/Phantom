package org.phantomapi.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.phantomapi.Phantom;
import org.phantomapi.ext.Protocol;
import org.phantomapi.lang.GList;
import org.phantomapi.text.RTX;
import org.phantomapi.util.C;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.world.MaterialBlock;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.EnumItemSlot;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_9_R2.PacketPlayOutSetSlot;

/**
 * NMS Implementation for doing dirty things. Does not use craftbukkit. Consider
 * yourself reflected
 * 
 * @author cyberpwn
 */
public class NMSX
{
	public static NMSX instance;
	private static boolean useOldMethods;
	public static String nmsver;
	public static GList<Color> colors;
	
	public static String getVersion()
	{
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		final String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}
	
	public static Object serializeChat(String msg)
	{
		try
		{
			return NMSClass.ChatSerializer.getDeclaredMethod("a", String.class).invoke(null, msg);
		}
		
		catch(Throwable e)
		{
			ExceptionUtil.print(e);
		}
		
		return null;
	}
	
	/**
	 * Set a specified Field accessible
	 *
	 * @param f
	 *            Field set accessible
	 */
	public static Field setAccessible(Field f) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		f.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		
		return f;
	}
	
	/**
	 * Change a chest state from open or closed
	 * 
	 * @param location
	 *            the location of the chest
	 * @param open
	 *            the state
	 */
	public static void setChestState(Location location, boolean open)
	{
		try
		{
			Class<?> cNmsWorld = getNMSClass("World");
			Class<?> cCraftWorld = getCraftClass("CraftWorld");
			Class<?> cBPosition = getNMSClass("BlockPosition");
			Class<?> cBlock = getNMSClass("Block");
			Class<?> cChestTile = getNMSClass("TileEntityChest");
			World world = location.getWorld();
			Object nmsworld = cCraftWorld.getMethod("getHandle").invoke(world);
			Object position = cBPosition.getConstructor(double.class, double.class, double.class).newInstance(location.getX(), location.getY(), location.getZ());
			Object tileChest = cNmsWorld.getMethod("getTileEntity", cBPosition).invoke(nmsworld, position);
			Object tileBlock = cChestTile.getMethod("getBlock").invoke(tileChest);
			
			cNmsWorld.getMethod("playBlockAction", cBPosition, cBlock, int.class, int.class).invoke(nmsworld, position, tileBlock, 1, open ? 1 : 0);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Class<?> getCraftClass(String className)
	{
		final String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		
		Class<?> clazz = null;
		
		try
		{
			clazz = Class.forName(fullName);
		}
		
		catch(final Exception e)
		{
			
		}
		
		return clazz;
	}
	
	public static void updateArmor(Player p)
	{
		try
		{
			for(Player observer : ProtocolLibrary.getProtocolManager().getEntityTrackers(p))
			{
				if(p.getInventory().getArmorContents()[3] != null)
				{
					sendPacket(observer, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(p.getInventory().getArmorContents()[3])));
				}
				
				if(p.getInventory().getArmorContents()[2] != null)
				{
					sendPacket(observer, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(p.getInventory().getArmorContents()[2])));
				}
				
				if(p.getInventory().getArmorContents()[1] != null)
				{
					sendPacket(observer, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(p.getInventory().getArmorContents()[1])));
				}
				
				if(p.getInventory().getArmorContents()[0] != null)
				{
					sendPacket(observer, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(p.getInventory().getArmorContents()[0])));
				}
			}
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	public static void setItem(Player p, int slot, ItemStack is)
	{
		sendPacket(p, new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(is)));
	}
	
	public static void updateSelfArmor(Player p)
	{
		try
		{
			for(ItemStack i : p.getInventory().getArmorContents())
			{
				if(i != null)
				{
					if(i.getType().toString().endsWith("_HELMET"))
					{
						sendPacket(p, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(i)));
					}
					
					if(i.getType().toString().endsWith("_CHESTPLATE"))
					{
						sendPacket(p, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(i)));
					}
					
					if(i.getType().toString().endsWith("_LEGGINGS"))
					{
						sendPacket(p, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(i)));
					}
					
					if(i.getType().toString().endsWith("_BOOTS"))
					{
						sendPacket(p, new PacketPlayOutEntityEquipment(p.getEntityId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(i)));
					}
				}
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void crash(Player p)
	{
		
	}
	
	/**
	 * Update the block
	 * 
	 * @param i
	 *            the block
	 */
	public static void updateBlock(Block i)
	{
		try
		{
			CraftWorld c = (CraftWorld) i.getWorld();
			CraftBlock cb = (CraftBlock) i;
			Method method = CraftBlock.class.getDeclaredMethod("getNMSBlock");
			method.setAccessible(true);
			net.minecraft.server.v1_9_R2.Block b = (net.minecraft.server.v1_9_R2.Block) method.invoke(cb);
			c.getHandle().applyPhysics(new BlockPosition(i.getX(), i.getY(), i.getZ()), b);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * Set a specified Method accessible
	 *
	 * @param m
	 *            Method set accessible
	 */
	public static Method setAccessible(Method m) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		m.setAccessible(true);
		
		return m;
	}
	
	/**
	 * Get the nms class
	 * 
	 * @param className
	 *            the class name
	 * @return returns "net.minecraft.server." + version + "." + className
	 */
	public static Class<?> getCBNMSClass(String className)
	{
		final String fullName = "net.minecraft.server." + getVersion() + className;
		
		Class<?> clazz = null;
		
		try
		{
			clazz = Class.forName(fullName);
		}
		
		catch(final Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return clazz;
	}
	
	/**
	 * Get the cb class
	 * 
	 * @param className
	 *            the class name
	 * @return returns "org.bukkit.craftbukkit." + version + "." + className
	 */
	public static Class<?> getOBCClass(String className)
	{
		final String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		
		Class<?> clazz = null;
		
		try
		{
			clazz = Class.forName(fullName);
		}
		
		catch(final Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return clazz;
	}
	
	public static Object getHandle(Object obj)
	{
		try
		{
			return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		}
		
		catch(final Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return null;
	}
	
	public static Field getField(Class<?> clazz, String name)
	{
		try
		{
			final Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		}
		
		catch(final Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return null;
	}
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... args)
	{
		for(final Method m : clazz.getMethods())
		{
			if(m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes())))
			{
				m.setAccessible(true);
				return m;
			}
		}
		
		return null;
	}
	
	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2)
	{
		boolean equal = true;
		
		if(l1.length != l2.length)
		{
			return false;
		}
		
		for(int i = 0; i < l1.length; i++)
		{
			if(l1[i] != l2[i])
			{
				equal = false;
				break;
			}
		}
		
		return equal;
	}
	
	/**
	 * Send a packet (get the handle and send it yada yada)
	 * 
	 * @param player
	 *            to this player
	 * @param packet
	 *            the packet
	 */
	public static void sendPacket(Player player, Object packet)
	{
		try
		{
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object) player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", NMSX.getNMSClass("Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Display a firework
	 * 
	 * @param l
	 *            the launch location
	 * @param c1
	 *            the first color
	 * @param c2
	 *            then fade into this color (or the same)
	 * @param type
	 *            the type of firework
	 */
	public static void launchFirework(Location l, Color c1, Color c2, Type type)
	{
		Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
		Integer rp = r.nextInt(2) + 1;
		
		fwm.addEffect(effect);
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}
	
	/**
	 * Display a static colored firework
	 * 
	 * @param l
	 *            the launch location
	 * @param c
	 *            the color
	 * @param type
	 *            the type of firework
	 */
	public static void launchFirework(Location l, Color c, Type type)
	{
		launchFirework(l, c, c, type);
	}
	
	/**
	 * Launches a randomly typed firework
	 * 
	 * @param l
	 *            the launch location
	 * @param c
	 *            the color
	 */
	public static void launchFirework(Location l, Color c)
	{
		launchFirework(l, c, new GList<Type>(Type.values()).pickRandom());
	}
	
	/**
	 * Play a record at the player's location
	 * 
	 * @param p
	 *            the player
	 * @param material
	 *            the record
	 */
	@SuppressWarnings("deprecation")
	public static void playRecord(Player p, Material material)
	{
		p.playEffect(p.getLocation(), Effect.RECORD_PLAY, material.getId());
	}
	
	/**
	 * Play a record to a player at a location
	 * 
	 * @param p
	 *            the player
	 * @param l
	 *            the location
	 * @param material
	 *            the record
	 */
	@SuppressWarnings("deprecation")
	public static void playRecord(Player p, Location l, Material material)
	{
		p.playEffect(l, Effect.RECORD_PLAY, material.getId());
	}
	
	/**
	 * Launches a randomly colored, randomly typed firework
	 * 
	 * @param l
	 *            the launch location
	 */
	public static void launchFirework(Location l)
	{
		launchFirework(l, colors.pickRandom(), colors.pickRandom(), new GList<Type>(Type.values()).pickRandom());
	}
	
	/**
	 * Spread dust particles
	 * 
	 * @param l
	 *            the location
	 * @param type
	 *            the material type
	 * @param amt
	 *            the amount of particles
	 */
	@SuppressWarnings("deprecation")
	public static void spreadParticles(Location l, Material type, int amt)
	{
		for(int i = 0; i < amt; i++)
		{
			l.getWorld().playEffect(l, Effect.TILE_DUST, type.getId());
		}
	}
	
	/**
	 * Break particles
	 * 
	 * @param l
	 *            the location
	 * @param type
	 *            the material type
	 * @param amt
	 *            the amount of particles
	 */
	@SuppressWarnings("deprecation")
	public static void breakParticles(Location l, Material type, int amt)
	{
		for(int i = 0; i < amt; i++)
		{
			l.getWorld().playEffect(l, Effect.TILE_BREAK, type.getId());
		}
	}
	
	/**
	 * Get the NMS Class instance from a name. Package versions are handled.
	 * automatically. JUST THE NAME, not the package
	 * 
	 * @param name
	 *            the name
	 * @return the class, null if none.
	 */
	public static Class<?> getNMSClass(String name)
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		try
		{
			return Class.forName("net.minecraft.server." + version + "." + name);
		}
		
		catch(ClassNotFoundException e)
		{
			ExceptionUtil.print(e);
			return null;
		}
	}
	
	/**
	 * Get the packet class
	 * 
	 * @param packet
	 *            the packet name
	 * @return the packet class
	 */
	public Class<?> getPacket(String packet)
	{
		try
		{
			return Class.forName(nmsPackage() + "." + packet);
		}
		
		catch(ClassNotFoundException e)
		{
			return null;
		}
	}
	
	/**
	 * Set the player weather to downfall
	 * 
	 * @param p
	 *            the player
	 */
	public void setWeatherDownfall(Player p)
	{
		p.setPlayerWeather(WeatherType.DOWNFALL);
	}
	
	/**
	 * Set the player weather to clear
	 * 
	 * @param p
	 *            the player
	 */
	public void setWeatherClear(Player p)
	{
		p.setPlayerWeather(WeatherType.CLEAR);
	}
	
	/**
	 * Reset weather
	 * 
	 * @param p
	 *            the player
	 */
	public void resetWeather(Player p)
	{
		p.resetPlayerWeather();
	}
	
	/**
	 * Set the player time
	 * 
	 * @param p
	 *            the player
	 * @param time
	 *            the time
	 */
	public void setTime(Player p, long time)
	{
		p.setPlayerTime(time, false);
	}
	
	/**
	 * Reset the player time
	 * 
	 * @param p
	 *            the player
	 */
	public void resetTime(Player p)
	{
		p.resetPlayerTime();
	}
	
	/**
	 * Create a falling block
	 * 
	 * @param location
	 *            the location
	 * @param mb
	 *            the materialblock
	 * @return the falling entity
	 */
	@SuppressWarnings("deprecation")
	public static Entity createFallingBlock(Location location, MaterialBlock mb)
	{
		return location.getWorld().spawnFallingBlock(location, mb.getMaterial(), mb.getData());
	}
	
	/**
	 * Create a falling block
	 * 
	 * @param location
	 *            the location
	 * @return the falling block entity
	 */
	public static Entity createFallingBlock(Location location)
	{
		return createFallingBlock(location, new MaterialBlock(location));
	}
	
	/**
	 * Get the player time
	 * 
	 * @param p
	 *            the player
	 * @return the time
	 */
	public long getPlayerTime(Player p)
	{
		return p.getPlayerTime();
	}
	
	/**
	 * Send a title message to a player
	 * 
	 * @param player
	 *            the player
	 * @param fadeIn
	 *            the fade in (ticks)
	 * @param stay
	 *            the stay time (ticks)
	 * @param fadeOut
	 *            the fade out (ticks)
	 * @param title
	 *            the title
	 * @param subtitle
	 *            the subtitle
	 */
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
	{
		try
		{
			Object e;
			Constructor<?> subtitleConstructor;
			
			if(title != null)
			{
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());
				e = NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chatTitle = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
				subtitleConstructor = NMSX.getNMSClass("PacketPlayOutTitle").getConstructor(NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], NMSX.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
				NMSX.sendPacket(player, titlePacket);
				e = NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				chatTitle = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
				subtitleConstructor = NMSX.getNMSClass("PacketPlayOutTitle").getConstructor(NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], NMSX.getNMSClass("IChatBaseComponent"));
				titlePacket = subtitleConstructor.newInstance(e, chatTitle);
				NMSX.sendPacket(player, titlePacket);
			}
			
			if(subtitle != null)
			{
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
				e = NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chatSubtitle = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
				subtitleConstructor = NMSX.getNMSClass("PacketPlayOutTitle").getConstructor(NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], NMSX.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
				NMSX.sendPacket(player, subtitlePacket);
				e = NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				chatSubtitle = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
				subtitleConstructor = NMSX.getNMSClass("PacketPlayOutTitle").getConstructor(NMSX.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], NMSX.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
				NMSX.sendPacket(player, subtitlePacket);
			}
		}
		
		catch(Exception var11)
		{
			var11.printStackTrace();
		}
	}
	
	/**
	 * Clear the title and settings
	 * 
	 * @param player
	 *            the player
	 */
	public static void clearTitle(Player player)
	{
		NMSX.sendTitle(player, 0, 30, 50, "", "");
	}
	
	/**
	 * Send a tab title
	 * 
	 * @param player
	 *            the player
	 * @param header
	 *            the header
	 * @param footer
	 *            the footer
	 */
	public static void sendTabTitle(Player player, String header, String footer)
	{
		if(header == null)
		{
			header = "";
		}
		
		header = ChatColor.translateAlternateColorCodes('&', header);
		
		if(footer == null)
		{
			footer = "";
		}
		
		footer = ChatColor.translateAlternateColorCodes('&', footer);
		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());
		
		try
		{
			Object tabHeader = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
			Object tabFooter = NMSX.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
			Constructor<?> titleConstructor = NMSX.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(NMSX.getNMSClass("IChatBaseComponent"));
			Object packet = titleConstructor.newInstance(tabHeader);
			Field field = packet.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(packet, tabFooter);
			NMSX.sendPacket(player, packet);
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Send an action bar message
	 * 
	 * @param player
	 *            the player,
	 * @param message
	 *            the message
	 */
	public static void sendActionBar(Player player, String message)
	{
		if(Protocol.getProtocol(player).equals(Protocol.V7))
		{
			return;
		}
		
		try
		{
			Object ppoc;
			Class<?> c3;
			Class<?> c2;
			Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object p = c1.cast(player);
			Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			
			if(useOldMethods)
			{
				c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
				c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Method m3 = c2.getDeclaredMethod("a", String.class);
				Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
				ppoc = c4.getConstructor(c3, Byte.TYPE).newInstance(cbc, Byte.valueOf((byte) 2));
			}
			
			else
			{
				c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
				c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Object o = c2.getConstructor(String.class).newInstance(message);
				ppoc = c4.getConstructor(c3, Byte.TYPE).newInstance(o, Byte.valueOf((byte) 2));
			}
			
			Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(p, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
			m5.invoke(pc, ppoc);
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	/**
	 * Get an entity name, or null if not supported
	 * 
	 * @param e
	 *            the entity
	 * @return string or null or ""
	 */
	public static String getEntityName(Entity e)
	{
		if(VersionBukkit.tc())
		{
			return null;
		}
		
		return e.getCustomName();
	}
	
	public static String getNBTV()
	{
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	/**
	 * Get the bukkit version
	 * 
	 * @return the package sub for the nms version
	 */
	public static String getBukkitVersion()
	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
	
	/**
	 * Get the base nms package
	 * 
	 * @return the nms package
	 */
	public static String nmsPackage()
	{
		return "net.minecraft.server." + getBukkitVersion();
	}
	
	/**
	 * Get the crafting package
	 * 
	 * @return the entire package
	 */
	public static String craftPackage()
	{
		return "org.bukkit.craftbukkit." + getBukkitVersion();
	}
	
	/**
	 * Shows the end to the player via reflection
	 * 
	 * @param player
	 *            the player
	 */
	public static void showEnd(Player player)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutGameStateChange");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, float.class).newInstance(4, 0.0F);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			handle.getClass().getDeclaredField("viewingCredits").set(handle, true);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Shows the demo screen
	 * 
	 * @param player
	 *            the player
	 */
	public static void showDemo(Player player)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutGameStateChange");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, float.class).newInstance(5, 0.0F);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Shows the dark value
	 * 
	 * @param player
	 *            the player
	 * @param value
	 *            the darkness value, 1=dark 0=bright others freeze client
	 */
	public static void showBrightness(Player player, float value)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutGameStateChange");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, float.class).newInstance(7, value);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Shows the weather
	 * 
	 * @param player
	 *            the player
	 * @param value
	 *            the weather intensity
	 */
	public static void showWeather(Player player, float value)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutGameStateChange");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, float.class).newInstance(7, value);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	public static Object getBlockPosition(Location l)
	{
		try
		{
			Class<?> blockPosition = Class.forName(nmsPackage() + ".BlockPosition");
			
			return blockPosition.getConstructor(int.class, int.class, int.class).newInstance(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return null;
	}
	
	public static void showBlockBreakAnimation(Player player, Location l, int level)
	{
		try
		{
			Object blockPosition = getBlockPosition(l);
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutBlockBreakAnimation");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, Class.forName(nmsPackage() + ".BlockPosition"), int.class).newInstance(player.getEntityId(), blockPosition, level);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Shows the demo controls screen
	 * 
	 * @param player
	 *            the player
	 */
	public static void showControls(Player player)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutGameStateChange");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, float.class).newInstance(5, 101.0F);
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Pick up something
	 * 
	 * @param entity
	 *            the entity
	 * @param pick
	 *            the item
	 */
	public static void showPickup(Player player, Entity entity, Entity pick)
	{
		try
		{
			Class<?> craftPlayer = Class.forName(craftPackage() + ".entity.CraftPlayer");
			Class<?> packetGameStateChange = Class.forName(nmsPackage() + ".PacketPlayOutCollect");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Object packet = packetGameStateChange.getConstructor(int.class, int.class).newInstance(pick.getEntityId(), entity.getEntityId());
			Object playerConnection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", Class.forName(nmsPackage() + ".Packet")).invoke(playerConnection, packet);
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Sends packets to nearby players that the player is laying down/sleeping
	 * 
	 * @param asleep
	 *            the player to broadcast asleep
	 */
	public static void playSleepAnimation(Player asleep)
	{
		final PacketContainer bedPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BED, false);
		final Location loc = asleep.getLocation();
		
		bedPacket.getEntityModifier(asleep.getWorld()).write(0, asleep);
		bedPacket.getIntegers().write(1, loc.getBlockX()).write(2, loc.getBlockY() + 1).write(3, loc.getBlockZ());
		
		broadcastNearby(asleep, bedPacket);
	}
	
	/**
	 * Sends packets to nearby players that the player is no longer laying
	 * down/sleeping
	 * 
	 * @param sleeping
	 *            the player to broadcast no longer asleep
	 */
	public static void stopSleepAnimation(Player sleeping)
	{
		final PacketContainer animation = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ANIMATION, false);
		
		animation.getEntityModifier(sleeping.getWorld()).write(0, sleeping);
		animation.getIntegers().write(1, 2);
		
		broadcastNearby(sleeping, animation);
	}
	
	private static void broadcastNearby(Player asleep, PacketContainer bedPacket)
	{
		for(Player observer : ProtocolLibrary.getProtocolManager().getEntityTrackers(asleep))
		{
			try
			{
				ProtocolLibrary.getProtocolManager().sendServerPacket(observer, bedPacket);
			}
			
			catch(InvocationTargetException e)
			{
				throw new RuntimeException("Cannot send packet.", e);
			}
		}
	}
	
	/**
	 * Get the raw ping by injecting into the keep-alive packets and getting raw
	 * nanosecond timings from it. This is still measured in milliseconds, but
	 * it is in double format. This ping is not averaged.
	 * 
	 * @param player
	 *            the player
	 * @return the raw ping
	 */
	public static double rawPing(Player player)
	{
		return Phantom.instance().getProtocolController().getPing(player);
	}
	
	/**
	 * Ping a player
	 * 
	 * @param player
	 *            the player
	 * @return the players ping
	 */
	public static int ping(Player player)
	{
		try
		{
			String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
			Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			Integer ping = (Integer) handle.getClass().getDeclaredField("ping").get(handle);
			
			return ping.intValue();
		}
		
		catch(Exception e)
		{
			
		}
		
		return -1;
	}
	
	/**
	 * Hide an entity for a player
	 * 
	 * @param p
	 *            the viewer
	 * @param e
	 *            the entity to hide
	 */
	public static void hideEntity(Player p, Entity e)
	{
		Phantom.instance().getProtocolController().getHider().hideEntity(p, e);
	}
	
	public static void openURL(Player player, String referenceLink)
	{
		RTX rtx = new RTX();
		rtx.addTextOpenURL("Click here to view in browser", referenceLink, C.AQUA);
		rtx.tellRawTo(player);
	}
	
	/**
	 * Un-hide an entity for a player
	 * 
	 * @param p
	 *            the viewer
	 * @param e
	 *            the entity to unhide
	 */
	public static void showEntity(Player p, Entity e)
	{
		Phantom.instance().getProtocolController().getHider().showEntity(p, e);
	}
	
	/**
	 * Hide an entity for all players
	 * 
	 * @param e
	 *            the entity to hide
	 */
	public static void hideEntity(Entity e)
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			hideEntity(i, e);
		}
	}
	
	/**
	 * Un-hide an entity for all players
	 * 
	 * @param e
	 *            the entity to unhide
	 */
	public static void showEntity(Entity e)
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			showEntity(i, e);
		}
	}
	
	static
	{
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
		if(nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_7_"))
		{
			useOldMethods = true;
		}
		
		colors = new GList<Color>();
		colors.add(Color.AQUA);
		colors.add(Color.BLACK);
		colors.add(Color.BLUE);
		colors.add(Color.FUCHSIA);
		colors.add(Color.GRAY);
		colors.add(Color.GREEN);
		colors.add(Color.LIME);
		colors.add(Color.MAROON);
		colors.add(Color.NAVY);
		colors.add(Color.OLIVE);
		colors.add(Color.ORANGE);
		colors.add(Color.PURPLE);
		colors.add(Color.RED);
		colors.add(Color.SILVER);
		colors.add(Color.TEAL);
		colors.add(Color.WHITE);
		colors.add(Color.YELLOW);
	}
}
