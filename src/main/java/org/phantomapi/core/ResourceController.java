package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.ResourcePackAcceptedEvent;
import org.phantomapi.event.ResourcePackDeclinedEvent;
import org.phantomapi.event.ResourcePackFailedEvent;
import org.phantomapi.event.ResourcePackLoadedEvent;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.Depend;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;

/**
 * Represents a resource controller
 * 
 * @author cyberpwn
 */
public class ResourceController extends Controller
{
	private ProtocolManager protocolManager;
	
	public ResourceController(Controllable parentController)
	{
		super(parentController);
		
		protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	public void onDecline(Player p)
	{
		callEvent(new ResourcePackDeclinedEvent(p));
	}
	
	public void onAccept(Player p)
	{
		callEvent(new ResourcePackAcceptedEvent(p));
	}
	
	public void onLoaded(Player p)
	{
		callEvent(new ResourcePackLoadedEvent(p));
	}
	
	public void onFailed(Player p)
	{
		callEvent(new ResourcePackFailedEvent(p));
	}
	
	public void send(Player p, String pack)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				p.setResourcePack(pack);
			}
		};
	}
	
	@Override
	public void onStart()
	{
		if(!Depend.PROTOLIB.exists())
		{
			return;
		}
		
		protocolManager.addPacketListener((PacketListener) new PacketAdapter((Plugin) Phantom.instance(), ListenerPriority.NORMAL, new PacketType[] {PacketType.Play.Client.RESOURCE_PACK_STATUS})
		{
			@Override
			public void onPacketReceiving(final PacketEvent event)
			{
				if(event.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS)
				{
					EnumWrappers.ResourcePackStatus status = (EnumWrappers.ResourcePackStatus) event.getPacket().getResourcePackStatus().read(0);
					
					if(status.equals((Object) EnumWrappers.ResourcePackStatus.DECLINED))
					{
						onDecline(event.getPlayer());
					}
					
					else
					{
						if(status.equals((Object) EnumWrappers.ResourcePackStatus.ACCEPTED))
						{
							onAccept(event.getPlayer());
						}
						
						else if(status.equals((Object) EnumWrappers.ResourcePackStatus.SUCCESSFULLY_LOADED))
						{
							onLoaded(event.getPlayer());
						}
						
						else if(status.equals((Object) EnumWrappers.ResourcePackStatus.FAILED_DOWNLOAD))
						{
							onDecline(event.getPlayer());
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onStop()
	{
		
	}
}
