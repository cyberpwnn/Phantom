package org.cyberpwn.phantom.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.lang.GList;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

/**
 * NMS Implementation for doing dirty things. Does not use craftbukkit. Consider
 * yourself reflected
 * 
 * @author cyberpwn
 */
public class NMSX
{
	public static NMSX bountifulAPI;
	private static boolean useOldMethods;
	public static String nmsver;
	public static GList<Color> colors;
	
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
			e.printStackTrace();
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
			e.printStackTrace();
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
				title = ChatColor.translateAlternateColorCodes((char) '&', (String) title);
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
				subtitle = ChatColor.translateAlternateColorCodes((char) '&', (String) subtitle);
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
		NMSX.sendTitle(player, 0, 0, 0, "", "");
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
		
		header = ChatColor.translateAlternateColorCodes((char) '&', (String) header);
		
		if(footer == null)
		{
			footer = "";
		}
		
		footer = ChatColor.translateAlternateColorCodes((char) '&', (String) footer);
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
		try
		{
			Object ppoc;
			Class<?> c3;
			Class<?> c2;
			Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object p = c1.cast((Object) player);
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
			ex.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends packets to nearby players that the player is laying down/sleeping
	 * 
	 * @param asleep
	 *            the player to broadcast asleep
	 */
	public void playSleepAnimation(Player asleep)
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
	public void stopSleepAnimation(Player sleeping)
	{
		final PacketContainer animation = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ANIMATION, false);
		
		animation.getEntityModifier(sleeping.getWorld()).write(0, sleeping);
		animation.getIntegers().write(1, 2);
		
		broadcastNearby(sleeping, animation);
	}
	
	private void broadcastNearby(Player asleep, PacketContainer bedPacket)
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
	 * Forces all audio channels to stop playing by forcing new fast sounds in
	 * there instead
	 * 
	 * @param l
	 *            the location to ear-rape
	 */
	public static void stopAudio(Location l)
	{
		for(int i = 0; i < 64; i++)
		{
			l.getWorld().playSound(l, Sound.ANVIL_LAND, 12f, 1.0f);
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
