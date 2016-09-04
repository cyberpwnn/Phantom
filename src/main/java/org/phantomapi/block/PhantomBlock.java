package org.phantomapi.block;

import java.util.Collection;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PhantomBlock implements Block
{
	private Block block;
	
	public PhantomBlock(Block block)
	{
		this.block = block;
	}
	
	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
	{
		block.setMetadata(metadataKey, newMetadataValue);;
	}
	
	@Override
	public List<MetadataValue> getMetadata(String metadataKey)
	{
		return block.getMetadata(metadataKey);
	}
	
	@Override
	public boolean hasMetadata(String metadataKey)
	{
		return block.hasMetadata(metadataKey);
	}
	
	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin)
	{
		block.removeMetadata(metadataKey, owningPlugin);;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public byte getData()
	{
		return block.getData();
	}
	
	@Override
	public Block getRelative(int modX, int modY, int modZ)
	{
		return block.getRelative(modX, modY, modZ);
	}
	
	@Override
	public Block getRelative(BlockFace face)
	{
		return block.getRelative(face);
	}
	
	@Override
	public Block getRelative(BlockFace face, int distance)
	{
		return block.getRelative(face, distance);
	}
	
	@Override
	public Material getType()
	{
		return block.getType();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getTypeId()
	{
		return block.getTypeId();
	}
	
	@Override
	public byte getLightLevel()
	{
		return block.getLightLevel();
	}
	
	@Override
	public byte getLightFromSky()
	{
		return block.getLightFromSky();
	}
	
	@Override
	public byte getLightFromBlocks()
	{
		return block.getLightFromBlocks();
	}
	
	@Override
	public World getWorld()
	{
		return block.getWorld();
	}
	
	@Override
	public int getX()
	{
		return block.getX();
	}
	
	@Override
	public int getY()
	{
		return block.getY();
	}
	
	@Override
	public int getZ()
	{
		return block.getZ();
	}
	
	@Override
	public Location getLocation()
	{
		return block.getLocation();
	}
	
	@Override
	public Location getLocation(Location loc)
	{
		return block.getLocation(loc);
	}
	
	@Override
	public Chunk getChunk()
	{
		return block.getChunk();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setData(byte data)
	{
		block.setData(data);;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setData(byte data, boolean applyPhysics)
	{
		block.setData(data, applyPhysics);;
	}
	
	@Override
	public void setType(Material type)
	{
		block.setType(type);;
	}
	
	@Override
	public void setType(Material type, boolean applyPhysics)
	{
		block.setType(type, applyPhysics);;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeId(int type)
	{
		return block.setTypeId(type);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeId(int type, boolean applyPhysics)
	{
		return block.setTypeId(type, applyPhysics);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics)
	{
		return block.setTypeIdAndData(type, data, applyPhysics);
	}
	
	@Override
	public BlockFace getFace(Block block)
	{
		return block.getFace(block);
	}
	
	@Override
	public BlockState getState()
	{
		return block.getState();
	}
	
	@Override
	public Biome getBiome()
	{
		return block.getBiome();
	}
	
	@Override
	public void setBiome(Biome bio)
	{
		block.setBiome(bio);;
	}
	
	@Override
	public boolean isBlockPowered()
	{
		return block.isBlockPowered();
	}
	
	@Override
	public boolean isBlockIndirectlyPowered()
	{
		return block.isBlockIndirectlyPowered();
	}
	
	@Override
	public boolean isBlockFacePowered(BlockFace face)
	{
		return block.isBlockFaceIndirectlyPowered(face);
	}
	
	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace face)
	{
		return block.isBlockFaceIndirectlyPowered(face);
	}
	
	@Override
	public int getBlockPower(BlockFace face)
	{
		return block.getBlockPower(face);
	}
	
	@Override
	public int getBlockPower()
	{
		return block.getBlockPower();
	}
	
	@Override
	public boolean isEmpty()
	{
		return block.isEmpty();
	}
	
	@Override
	public boolean isLiquid()
	{
		return block.isLiquid();
	}
	
	@Override
	public double getTemperature()
	{
		return block.getTemperature();
	}
	
	@Override
	public double getHumidity()
	{
		return block.getHumidity();
	}
	
	@Override
	public PistonMoveReaction getPistonMoveReaction()
	{
		return block.getPistonMoveReaction();
	}
	
	@Override
	public boolean breakNaturally()
	{
		return block.breakNaturally();
	}
	
	@Override
	public boolean breakNaturally(ItemStack tool)
	{
		return block.breakNaturally(tool);
	}
	
	@Override
	public Collection<ItemStack> getDrops()
	{
		return block.getDrops();
	}
	
	@Override
	public Collection<ItemStack> getDrops(ItemStack tool)
	{
		return block.getDrops(tool);
	}
}
