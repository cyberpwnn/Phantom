package org.phantomapi.nbt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public final class SpawnerNBTWrapper
{
	
	private static NBTGenericVariableContainer _variables;
	
	static
	{
		_variables = new NBTGenericVariableContainer("Spawner");
		_variables.add("Count", new ShortVariable("SpawnCount", (short) 0));
		_variables.add("Range", new ShortVariable("SpawnRange", (short) 0));
		_variables.add("Delay", new ShortVariable("Delay", (short) 0));
		_variables.add("MinDelay", new ShortVariable("MinSpawnDelay", (short) 0));
		_variables.add("MaxDelay", new ShortVariable("MaxSpawnDelay", (short) 0));
		_variables.add("MaxEntities", new ShortVariable("MaxNearbyEntities", (short) 0));
		_variables.add("PlayerRange", new ShortVariable("RequiredPlayerRange", (short) 0));
	}
	
	private Block _spawnerBlock;
	private NBTTagCompound _data;
	private List<SpawnerEntityNBT> _entities;
	
	public static Collection<String> variableNames()
	{
		return Collections.unmodifiableCollection(_variables.getVarNames());
	}
	
	public SpawnerNBTWrapper(Block block)
	{
		_spawnerBlock = block;
		_data = NBTUtils.getTileEntityNBTData(block);
		
		if(_data.hasKey("SpawnPotentials"))
		{
			NBTTagList spawnPotentials = _data.getList("SpawnPotentials");
			int l = spawnPotentials.size();
			
			_entities = new ArrayList<SpawnerEntityNBT>(l);
			for(int i = 0; i < l; ++i)
			{
				NBTTagCompound potential = (NBTTagCompound) spawnPotentials.get(i);
				EntityNBT entityNbt = EntityNBT.fromEntityData(potential.getCompound("Entity"));
				if(entityNbt != null)
				{
					_entities.add(new SpawnerEntityNBT(entityNbt, potential.getInt("Weight")));
				}
			}
			_data.remove("SpawnPotentials");
		}
		else
		{
			_entities = new ArrayList<SpawnerEntityNBT>();
		}
	}
	
	public void addEntity(SpawnerEntityNBT spawnerEntityNbt)
	{
		_data.setCompound("SpawnData", spawnerEntityNbt.getEntityNBT()._data.clone());
		_entities.add(spawnerEntityNbt);
	}
	
	public void clearEntities()
	{
		_entities.clear();
	}
	
	public void cloneFrom(SpawnerNBTWrapper other)
	{
		NBTTagCompound clone = other._data.clone();
		clone.remove("id");
		clone.remove("x");
		clone.remove("y");
		clone.remove("z");
		_data.merge(clone);
		_entities = new ArrayList<SpawnerEntityNBT>(other._entities.size());
		for(SpawnerEntityNBT spawnerEntityNBT : other._entities)
		{
			_entities.add(spawnerEntityNBT.clone());
		}
	}
	
	public List<SpawnerEntityNBT> getEntities()
	{
		return _entities;
	}
	
	public void removeEntity(int index)
	{
		_entities.remove(index);
	}
	
	public EntityType getCurrentEntity()
	{
		NBTTagCompound spawnData = _data.getCompound("SpawnData");
		if(spawnData != null)
		{
			return EntityTypeMap.getByName(spawnData.getString("id"));
		}
		return null;
	}
	
	public Location getLocation()
	{
		return _spawnerBlock.getLocation();
	}
	
	public NBTVariableContainer getVariables()
	{
		return _variables.boundToData(_data);
	}
	
	public NBTVariable getVariable(String name)
	{
		return _variables.getVariable(name, _data);
	}
	
	public void save()
	{
		if(_entities.size() > 0)
		{
			NBTTagList spawnPotentials = new NBTTagList();
			for(SpawnerEntityNBT spawnerEntityNbt : _entities)
			{
				spawnPotentials.add(spawnerEntityNbt.buildTagCompound());
			}
			_data.setList("SpawnPotentials", spawnPotentials);
		}
		else
		{
			NBTTagCompound simplePig = new NBTTagCompound();
			simplePig.setString("id", "Pig");
			_data.setCompound("SpawnData", simplePig);
			_data.remove("SpawnPotentials");
		}
		NBTUtils.setTileEntityNBTData(_spawnerBlock, _data);
	}
	
}
