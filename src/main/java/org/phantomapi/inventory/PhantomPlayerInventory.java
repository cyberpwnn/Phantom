package org.phantomapi.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PhantomPlayerInventory implements PhantomPlayerInventoryWrapper
{
	protected PlayerInventory i;
	
	public PhantomPlayerInventory(PlayerInventory i)
	{
		this.i = i;
	}
	
	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... arg0) throws IllegalArgumentException
	{
		return i.addItem(arg0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HashMap<Integer, ? extends ItemStack> all(int arg0)
	{
		return i.all(arg0);
	}
	
	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material arg0) throws IllegalArgumentException
	{
		return i.all(arg0);
	}
	
	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack arg0)
	{
		return i.all(arg0);
	}
	
	@Override
	public void clear()
	{
		i.clear();
	}
	
	@Override
	public void clear(int arg0)
	{
		i.clear(arg0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean contains(int arg0)
	{
		return i.contains(arg0);
	}
	
	@Override
	public boolean contains(Material arg0) throws IllegalArgumentException
	{
		return i.contains(arg0);
	}
	
	@Override
	public boolean contains(ItemStack arg0)
	{
		return i.contains(arg0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean contains(int arg0, int arg1)
	{
		return i.contains(arg0, arg1);
	}
	
	@Override
	public boolean contains(Material arg0, int arg1) throws IllegalArgumentException
	{
		return i.contains(arg0, arg1);
	}
	
	@Override
	public boolean contains(ItemStack arg0, int arg1)
	{
		return i.contains(arg0, arg1);
	}
	
	@Override
	public boolean containsAtLeast(ItemStack arg0, int arg1)
	{
		return i.containsAtLeast(arg0, arg1);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int first(int arg0)
	{
		return i.first(arg0);
	}
	
	@Override
	public int first(Material arg0) throws IllegalArgumentException
	{
		return i.first(arg0);
	}
	
	@Override
	public int first(ItemStack arg0)
	{
		return i.first(arg0);
	}
	
	@Override
	public int firstEmpty()
	{
		return i.firstEmpty();
	}
	
	@Override
	public ItemStack[] getContents()
	{
		return i.getContents();
	}
	
	@Override
	public HumanEntity getHolder()
	{
		return i.getHolder();
	}
	
	@Override
	public ItemStack getItem(int arg0)
	{
		return i.getItem(arg0);
	}
	
	@Override
	public int getMaxStackSize()
	{
		return i.getMaxStackSize();
	}
	
	@Override
	public String getName()
	{
		return i.getName();
	}
	
	@Override
	public int getSize()
	{
		return i.getSize();
	}
	
	@Override
	public String getTitle()
	{
		return i.getTitle();
	}
	
	@Override
	public InventoryType getType()
	{
		return i.getType();
	}
	
	@Override
	public List<HumanEntity> getViewers()
	{
		return i.getViewers();
	}
	
	@Override
	public ListIterator<ItemStack> iterator()
	{
		return i.iterator();
	}
	
	@Override
	public ListIterator<ItemStack> iterator(int arg0)
	{
		return i.iterator(arg0);
	}
	
	@Override
	public void remove(int arg0)
	{
		setItem(arg0, new ItemStack(Material.AIR));
	}
	
	@Override
	public void remove(Material arg0) throws IllegalArgumentException
	{
		i.remove(arg0);
	}
	
	@Override
	public void remove(ItemStack arg0)
	{
		i.remove(arg0);
	}
	
	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... arg0) throws IllegalArgumentException
	{
		return i.removeItem(arg0);
	}
	
	@Override
	public void setContents(ItemStack[] arg0) throws IllegalArgumentException
	{
		i.setContents(arg0);
	}
	
	@Override
	public void setItem(int arg0, ItemStack arg1)
	{
		i.setItem(arg0, arg1);
	}
	
	@Override
	public void setMaxStackSize(int arg0)
	{
		i.setMaxStackSize(arg0);
	}
	
	@Override
	public boolean hasSpace()
	{
		return firstEmpty() != -1;
	}
	
	@Override
	public int getSlotsLeft()
	{
		int x = 0;
		
		for(ItemStack i : getContents())
		{
			if(i == null || i.getType().equals(Material.AIR))
			{
				x++;
			}
		}
		
		return x;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int clear(int arg0, int arg1)
	{
		return i.clear(arg0, arg1);
	}
	
	@Override
	public ItemStack[] getArmorContents()
	{
		return i.getArmorContents();
	}
	
	@Override
	public ItemStack getBoots()
	{
		return i.getBoots();
	}
	
	@Override
	public ItemStack getChestplate()
	{
		return i.getChestplate();
	}
	
	@Override
	public int getHeldItemSlot()
	{
		return i.getHeldItemSlot();
	}
	
	@Override
	public ItemStack getHelmet()
	{
		return i.getHelmet();
	}
	
	@Override
	public ItemStack getItemInHand()
	{
		return i.getItemInHand();
	}
	
	@Override
	public ItemStack getLeggings()
	{
		return i.getLeggings();
	}
	
	@Override
	public void setArmorContents(ItemStack[] arg0)
	{
		i.setArmorContents(arg0);
	}
	
	@Override
	public void setBoots(ItemStack arg0)
	{
		i.setBoots(arg0);
	}
	
	@Override
	public void setChestplate(ItemStack arg0)
	{
		i.setChestplate(arg0);
	}
	
	@Override
	public void setHeldItemSlot(int arg0)
	{
		i.setHeldItemSlot(arg0);
	}
	
	@Override
	public void setHelmet(ItemStack arg0)
	{
		i.setHelmet(arg0);
	}
	
	@Deprecated
	@Override
	public void setItemInHand(ItemStack arg0)
	{
		i.setItemInHand(arg0);
	}
	
	@Override
	public void setLeggings(ItemStack arg0)
	{
		i.setLeggings(arg0);
	}

	@Override
	public ItemStack[] getExtraContents()
	{
		return i.getExtraContents();
	}

	@Override
	public ItemStack getItemInMainHand()
	{
		return i.getItemInMainHand();
	}

	@Override
	public ItemStack getItemInOffHand()
	{
		return i.getItemInOffHand();
	}

	@Override
	public void setExtraContents(ItemStack[] arg0)
	{
		i.setExtraContents(arg0);
	}

	@Override
	public void setItemInMainHand(ItemStack arg0)
	{
		i.setItemInMainHand(arg0);
	}

	@Override
	public void setItemInOffHand(ItemStack arg0)
	{
		i.setItemInOffHand(arg0);
	}

	@Override
	public Location getLocation()
	{
		return i.getLocation();
	}

	@Override
	public ItemStack[] getStorageContents()
	{
		return i.getStorageContents();
	}

	@Override
	public void setStorageContents(ItemStack[] arg0) throws IllegalArgumentException
	{
		i.setStorageContents(arg0);
	}
}
