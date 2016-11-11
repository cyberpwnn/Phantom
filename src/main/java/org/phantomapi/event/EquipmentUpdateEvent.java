package org.phantomapi.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.nms.FakeEquipment.EquipmentSlot;

public class EquipmentUpdateEvent extends CancellablePhantomEvent
{
	private LivingEntity entity;
	private ItemStack item;
	private Player viewer;
	private EquipmentSlot slot;
	
	public EquipmentUpdateEvent(LivingEntity entity, ItemStack item, Player viewer, EquipmentSlot slot)
	{
		this.entity = entity;
		this.item = item;
		this.viewer = viewer;
		this.slot = slot;
	}
	
	public LivingEntity getEntity()
	{
		return entity;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public void setItem(ItemStack item)
	{
		this.item = item;
	}
	
	public Player getViewer()
	{
		return viewer;
	}
	
	public EquipmentSlot getSlot()
	{
		return slot;
	}
}
