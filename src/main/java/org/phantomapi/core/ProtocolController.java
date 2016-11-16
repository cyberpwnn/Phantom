package org.phantomapi.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.event.EquipmentUpdateEvent;
import org.phantomapi.ext.Protocol;
import org.phantomapi.lang.GMap;
import org.phantomapi.nms.EntityHider;
import org.phantomapi.nms.EntityHider.Policy;
import org.phantomapi.nms.FakeEquipment;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.Timer;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class ProtocolController extends Controller
{
	private EntityHider entityHider;
	private FakeEquipment fakeEquipment;
	private GMap<Player, Double> realPing;
	private GMap<Player, Timer> timers;
	private GMap<Integer, Player> waiting;
	
	public ProtocolController(Controllable parentController)
	{
		super(parentController);
		
		realPing = new GMap<Player, Double>();
		timers = new GMap<Player, Timer>();
		waiting = new GMap<Integer, Player>();
	}
	
	@Override
	public void onStart()
	{
		entityHider = new EntityHider(getPlugin(), Policy.BLACKLIST);
		
		fakeEquipment = new FakeEquipment(getPlugin())
		{
			@Override
			protected boolean onEquipmentSending(EquipmentSendingEvent e)
			{
				EquipmentUpdateEvent ee = new EquipmentUpdateEvent(e.getVisibleEntity(), e.getEquipment(), e.getClient(), e.getSlot());
				callEvent(ee);
				
				if(ee.isCancelled())
				{
					return false;
				}
				
				e.setEquipment(ee.getItem());
				e.setSlot(ee.getSlot());
				
				return true;
			}
		};
		
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		mgr.addPacketListener(new PacketAdapter(getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Server.KEEP_ALIVE)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				if(event.getPacketType().equals(PacketType.Play.Server.KEEP_ALIVE))
				{
					int id = event.getPacket().getIntegers().read(0);
					Player player = event.getPlayer();
					Timer t = new Timer();
					t.start();
					waiting.put(id, player);
					timers.put(player, t);
				}
			}
		});
		
		mgr.addPacketListener(new PacketAdapter(getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE)
		{
			@Override
			public void onPacketReceiving(PacketEvent event)
			{
				if(event.getPacketType().equals(PacketType.Play.Client.KEEP_ALIVE))
				{
					int id = event.getPacket().getIntegers().read(0);
					Player player = event.getPlayer();
					
					if(waiting.containsKey(id) && waiting.get(id).equals(player))
					{
						waiting.remove(id);
						Timer t = timers.get(player);
						t.stop();
						timers.remove(player);
						realPing.put(player, (double) t.getTime());
					}
				}
			}
		});
	}
	
	public EntityHider getHider()
	{
		return entityHider;
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				w(e.getPlayer().getName() + " <> Protocol " + Protocol.getProtocol(e.getPlayer()));
			}
		};
	}
	
	public FakeEquipment getFakeEquipment()
	{
		return fakeEquipment;
	}
}
