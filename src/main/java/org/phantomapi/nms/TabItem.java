package org.phantomapi.nms;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.phantomapi.Phantom;
import org.phantomapi.slate.SlateUtil;
import org.phantomapi.util.ExceptionUtil;

public class TabItem
{
	protected UUID uuid;
	protected String name;
	protected String skin;
	protected int ping;
	protected GameMode gamemode;
	
	protected Object profile;
	
	/**
	 * Constructs a new TabItem
	 */
	public TabItem(UUID uuid, String name, String skin, int ping, GameMode mode)
	{
		if(name.length() > 16)
		{
			throw new IllegalArgumentException("name can only be 16 characters long");
		}
		if(skin != null && skin.length() > 16)
		{
			throw new IllegalArgumentException("skin can only be 16 characters long");
		}
		
		this.uuid = uuid;
		this.name = name;
		this.skin = skin;
		this.ping = ping;
		this.gamemode = mode;
	}
	
	/**
	 * Constructs a new TabItem
	 */
	public TabItem(String name, String skin, int ping, GameMode mode)
	{
		this(UUID.randomUUID(), name, skin, ping, mode);
	}
	
	/**
	 * Constructs a new TabItem
	 */
	public TabItem(String name, int ping, GameMode mode)
	{
		this(name, name, ping, mode);
	}
	
	/**
	 * Constructs a new TabItem
	 */
	public TabItem(String name)
	{
		this(name, 0, Bukkit.getDefaultGameMode());
	}
	
	protected Object toPacket(final Tab tab, final int action)
	{
		if(profile == null)
		{
			try
			{
				profile = NMSClass.GameProfile.getConstructor(UUID.class, String.class).newInstance(this.uuid, this.name);
				
				if(skin != null)
				{
					Bukkit.getScheduler().runTaskAsynchronously(Phantom.instance(), new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								Object cache = NMSClass.TileEntitySkull.getDeclaredField("skinCache").get(null);
								
								Object skinProfile = NMSClass.LoadingCache.getDeclaredMethod("getUnchecked", Object.class).invoke(cache, (Object) skin.toLowerCase());
								
								NMSX.setAccessible(NMSClass.GameProfile.getDeclaredField("id")).set(skinProfile, uuid);
								NMSX.setAccessible(NMSClass.GameProfile.getDeclaredField("name")).set(skinProfile, name);
								
								profile = skinProfile;
								
								SlateUtil.updateTab(tab.player);
							}
							catch(Exception e)
							{
								ExceptionUtil.print(e);
							}
						}
					});
				}
			}
			catch(Exception e)
			{
				ExceptionUtil.print(e);
			}
		}
		
		if(NMSX.getVersion().contains("1_7"))
		{
			try
			{
				Object packet = NMSClass.PacketPlayOutPlayerInfo.newInstance();
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("action")).set(packet, action);
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("username")).set(packet, name);
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("gamemode")).set(packet, gamemode.ordinal());
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("ping")).set(packet, ping);
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("player")).set(packet, profile);
				return packet;
			}
			catch(Exception e)
			{
				ExceptionUtil.print(e);
			}
		}
		else
		{
			try
			{
				Object gMode = NMSClass.EnumGamemode.getDeclaredMethod("getById", int.class).invoke(null, gamemode.ordinal());
				Object packet = NMSClass.PlayerInfoData.getConstructor(NMSClass.PacketPlayOutPlayerInfo, NMSClass.GameProfile, int.class, NMSClass.EnumGamemode, NMSClass.IChatBaseComponent).newInstance(null, profile, ping, gMode, NMSX.serializeChat(SlateUtil.convertJSON(name)));
				return packet;
			}
			
			catch(Exception e)
			{
				ExceptionUtil.print(e);
			}
		}
		return null;
	}
}
