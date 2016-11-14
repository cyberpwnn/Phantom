package org.phantomapi.nms;

import static com.comphenix.protocol.PacketType.Play.Server.ENTITY_EQUIPMENT;
import static com.comphenix.protocol.PacketType.Play.Server.NAMED_ENTITY_SPAWN;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;

/**
 * Modify player equipment.
 * 
 * @author Kristian
 */
public abstract class FakeEquipment
{
	/**
	 * Represents an equipment slot.
	 * 
	 * @author Kristian
	 */
	public enum EquipmentSlot
	{
		HELD(0),
		BOOTS(1),
		LEGGINGS(2),
		CHESTPLATE(3),
		HELMET(4);
		
		private int id;
		
		private EquipmentSlot(int id)
		{
			this.id = id;
		}
		
		/**
		 * Retrieve the entity's equipment in the current slot.
		 * 
		 * @param entity
		 *            - the entity.
		 * @return The equipment.
		 */
		public ItemStack getEquipment(LivingEntity entity)
		{
			switch(this)
			{
				case HELD:
					return entity.getEquipment().getItemInHand();
				case BOOTS:
					return entity.getEquipment().getBoots();
				case LEGGINGS:
					return entity.getEquipment().getLeggings();
				case CHESTPLATE:
					return entity.getEquipment().getChestplate();
				case HELMET:
					return entity.getEquipment().getHelmet();
				default:
					throw new IllegalArgumentException("Unknown slot: " + this);
			}
		}
		
		/**
		 * Determine if the entity has an equipment in the current slot.
		 * 
		 * @param entity
		 *            - the entity.
		 * @return TRUE if it is empty, FALSE otherwise.
		 */
		public boolean isEmpty(LivingEntity entity)
		{
			ItemStack stack = getEquipment(entity);
			return stack != null && stack.getType() == Material.AIR;
		}
		
		/**
		 * Retrieve the underlying equipment slot ID.
		 * 
		 * @return The ID.
		 */
		public int getId()
		{
			return id;
		}
		
		/**
		 * Find the corresponding equipment slot.
		 * 
		 * @param id
		 *            - the slot ID.
		 * @return The equipment slot.
		 */
		public static EquipmentSlot fromId(int id)
		{
			for(EquipmentSlot slot : values())
			{
				if(slot.getId() == id)
				{
					return slot;
				}
			}
			throw new IllegalArgumentException("Cannot find slot id: " + id);
		}
	}
	
	/**
	 * Represents an equipment event.
	 * 
	 * @author Kristian
	 */
	public static class EquipmentSendingEvent
	{
		private Player client;
		private LivingEntity visibleEntity;
		private EquipmentSlot slot;
		private ItemStack equipment;
		
		private EquipmentSendingEvent(Player client, LivingEntity visibleEntity, EquipmentSlot slot, ItemStack equipment)
		{
			this.client = client;
			this.visibleEntity = visibleEntity;
			this.slot = slot;
			this.equipment = equipment;
		}
		
		/**
		 * Retrieve the client that is observing the entity.
		 * 
		 * @return The observing client.
		 */
		public Player getClient()
		{
			return client;
		}
		
		/**
		 * Retrieve the entity whose armor or held item we are updating.
		 * 
		 * @return The visible entity.
		 */
		public LivingEntity getVisibleEntity()
		{
			return visibleEntity;
		}
		
		/**
		 * Retrieve the equipment that we are
		 * 
		 * @return
		 */
		public ItemStack getEquipment()
		{
			return equipment;
		}
		
		/**
		 * Set the equipment we will send to the player.
		 * 
		 * @param equipment
		 *            - the equipment, or NULL to sent air.
		 */
		public void setEquipment(ItemStack equipment)
		{
			this.equipment = equipment;
		}
		
		/**
		 * Retrieve the slot of this equipment.
		 * 
		 * @return The slot.
		 */
		public EquipmentSlot getSlot()
		{
			return slot;
		}
		
		/**
		 * Set the slot of this equipment.
		 * 
		 * @param slot
		 *            - the slot.
		 */
		public void setSlot(EquipmentSlot slot)
		{
			this.slot = Preconditions.checkNotNull(slot, "slot cannot be NULL");
		}
	}
	
	private Map<Object, EquipmentSlot> processedPackets = new MapMaker().weakKeys().makeMap();
	private Plugin plugin;
	private ProtocolManager manager;
	private PacketListener listener;
	
	public FakeEquipment(Plugin plugin)
	{
		this.plugin = plugin;
		manager = ProtocolLibrary.getProtocolManager();
		
		manager.addPacketListener(listener = new PacketAdapter(plugin, ENTITY_EQUIPMENT, NAMED_ENTITY_SPAWN)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer packet = event.getPacket();
				PacketType type = event.getPacketType();
				
				LivingEntity visibleEntity = (LivingEntity) packet.getEntityModifier(event).read(0);
				Player observingPlayer = event.getPlayer();
				
				if(ENTITY_EQUIPMENT.equals(type))
				{
					EquipmentSlot slot = EquipmentSlot.fromId(packet.getIntegers().read(1));
					ItemStack equipment = packet.getItemModifier().read(0);
					EquipmentSendingEvent sendingEvent = new EquipmentSendingEvent(observingPlayer, visibleEntity, slot, equipment);
					EquipmentSlot previous = processedPackets.get(packet.getHandle());
					
					if(previous != null)
					{
						packet = event.getPacket().deepClone();
						sendingEvent.setSlot(previous);
						sendingEvent.setEquipment(previous.getEquipment(visibleEntity).clone());
					}
					
					if(onEquipmentSending(sendingEvent))
					{
						processedPackets.put(packet.getHandle(), previous != null ? previous : slot);
					}
					
					if(slot != sendingEvent.getSlot())
					{
						packet.getIntegers().write(1, slot.getId());
					}
					
					if(equipment != sendingEvent.getEquipment())
					{
						packet.getItemModifier().write(0, sendingEvent.getEquipment());
					}
					
				}
				
				else if(NAMED_ENTITY_SPAWN.equals(type))
				{
					onEntitySpawn(observingPlayer, visibleEntity);
				}
				
				else
				{
					throw new IllegalArgumentException("Unknown packet type:" + type);
				}
			}
		});
	}
	
	/**
	 * Invoked when a living entity has been spawned on the given client.
	 * 
	 * @param client
	 *            - the client.
	 * @param visibleEntity
	 *            - the visibleEntity.
	 */
	protected void onEntitySpawn(Player client, LivingEntity visibleEntity)
	{
		
	}
	
	/**
	 * Invoked when the equipment or held item of an living entity is sent to a
	 * client.
	 * <p>
	 * This can be fully modified. Please return TRUE if you do, though.
	 * 
	 * @param equipmentEvent
	 *            - the equipment event.
	 * @return TRUE if the equipment was modified, FALSE otherwise.
	 */
	protected abstract boolean onEquipmentSending(EquipmentSendingEvent equipmentEvent);
	
	/**
	 * Update the given slot.
	 * 
	 * @param observer
	 *            - the observing client.
	 * @param entity
	 *            - the visible entity that will be updated.
	 * @param slot
	 *            - the equipment slot to update.
	 */
	public void updateSlot(final Player client, LivingEntity visibleEntity, EquipmentSlot slot)
	{
		if(listener == null)
		{
			throw new IllegalStateException("FakeEquipment has closed.");
		}
		
		final PacketContainer equipmentPacket = new PacketContainer(ENTITY_EQUIPMENT);
		equipmentPacket.getIntegers().write(0, visibleEntity.getEntityId()).write(1, slot.getId());
		equipmentPacket.getItemModifier().write(0, slot.getEquipment(visibleEntity));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ProtocolLibrary.getProtocolManager().sendServerPacket(client, equipmentPacket);
				}
				catch(InvocationTargetException e)
				{
					throw new RuntimeException("Unable to update slot.", e);
				}
			}
		});
	}
	
	/**
	 * Close the current equipment modifier.
	 */
	public void close()
	{
		if(listener != null)
		{
			manager.removePacketListener(listener);
			listener = null;
		}
	}
}
