package org.phantomapi.nbt;

import org.bukkit.entity.EntityType;

public class SpawnerEntityNBT
{
	
	private int _weight;
	private EntityNBT _entityNbt;
	
	public SpawnerEntityNBT(EntityType entityType)
	{
		this(entityType, 1);
	}
	
	public SpawnerEntityNBT(EntityType entityType, int weight)
	{
		_weight = weight;
		_entityNbt = EntityNBT.fromEntityType(entityType);
	}
	
	public SpawnerEntityNBT(EntityNBT entityNbt)
	{
		this(entityNbt, 1);
	}
	
	public SpawnerEntityNBT(EntityNBT entityNbt, int weight)
	{
		_weight = weight;
		_entityNbt = entityNbt;
	}
	
	public int getWeight()
	{
		return _weight;
	}
	
	public EntityNBT getEntityNBT()
	{
		return _entityNbt;
	}
	
	public EntityType getEntityType()
	{
		return _entityNbt.getEntityType();
	}
	
	@Override
	public SpawnerEntityNBT clone()
	{
		return new SpawnerEntityNBT(_entityNbt.clone(), _weight);
	}
	
	NBTTagCompound buildTagCompound()
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setInt("Weight", _weight);
		data.setCompound("Entity", _entityNbt._data);
		return data;
	}
	
}
