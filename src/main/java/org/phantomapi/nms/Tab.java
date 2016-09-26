package org.phantomapi.nms;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.phantomapi.util.ExceptionUtil;

public class Tab
{
	protected static Object EMPTY;
	
	static
	{
		try
		{
			EMPTY = NMSX.getCBNMSClass("ChatComponentText").getConstructor(String.class).newInstance("");
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	protected final Player player;
	
	public String[] header;
	public String[] footer;
	public List<TabItem> items = new ArrayList<TabItem>();
	
	public Tab(Player p)
	{
		this.player = p;
	}
	
	public void clearItems()
	{
		updateItems(4);
	}
	
	public void updateItems()
	{
		updateItems(0);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void updateItems(int action)
	{
		try
		{
			List<Object> itemPackets = new ArrayList<Object>();
			List<Object> packets = new ArrayList<Object>();

			for(TabItem item : items)
			{
				itemPackets.add(item.toPacket(this, action));
			}
			
			
			if(NMSClass.version < 180)
			{
				packets.addAll(itemPackets);
			}
			
			else
			{
				Object playerInfoPacket = NMSClass.PacketPlayOutPlayerInfo.newInstance();
				NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("a")).set(playerInfoPacket, NMSClass.EnumPlayerInfoAction.getEnumConstants()[action]);
				List<Object> dataList = (List) NMSX.setAccessible(NMSClass.PacketPlayOutPlayerInfo.getDeclaredField("b")).get(playerInfoPacket);
				dataList.addAll(itemPackets);
				packets.add(playerInfoPacket);
			}
			
			for(Object packet : packets)
			{
				NMSX.sendPacket(player, packet);
			}
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	public void updateHeaderFooter()
	{
		if(header == null && footer == null)
		{
			return;
		}
		try
		{
			Object packet = NMSClass.PacketPlayOutPlayerListHeaderFooter.getConstructor(NMSClass.IChatBaseComponent).newInstance(new Object[] {null});
			
			Object serHeader = header == null ? EMPTY : NMSX.serializeChat(mergeJSON(header));
			Object serFooter = footer == null ? EMPTY : NMSX.serializeChat(mergeJSON(footer));
			
			NMSX.setAccessible(NMSClass.PacketPlayOutPlayerListHeaderFooter.getDeclaredField("a")).set(packet, serHeader);
			NMSX.setAccessible(NMSClass.PacketPlayOutPlayerListHeaderFooter.getDeclaredField("b")).set(packet, serFooter);
			
			NMSX.sendPacket(player, packet);
		}
		
		catch(Throwable e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	private String mergeJSON(String... json)
	{
		String merged = "";
		for(String s : json)
		{
			merged += s + ",";
		}
		merged = merged.substring(0, merged.length() - 1);
		return String.format("{\"text\":\"\",\"extra\":[%s]}", merged);
	}
	
}
