package org.phantomapi.multiblock;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PhantomWorldQueue;
import org.phantomapi.world.VariableBlock;
import org.phantomapi.world.VectorSchematic;

/**
 * Represents a multiblock structure
 * 
 * @author cyberpwn
 */
public class MultiblockStructure extends VectorSchematic
{
	private final String type;
	
	/**
	 * Create a multiblock structure
	 * 
	 * @param type
	 */
	public MultiblockStructure(String type)
	{
		super();
		this.type = type;
	}
	
	/**
	 * Get the type
	 * 
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Add a block to this structure
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @param mb
	 *            the variable block
	 */
	public void add(int x, int y, int z, VariableBlock mb)
	{
		add(new Vector(x, y, z), mb);
	}
	
	/**
	 * Add a block to this structure
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @param mb
	 *            the variable block
	 */
	public void add(int x, int y, int z, MaterialBlock mb)
	{
		add(new Vector(x, y, z), mb);
	}
	
	/**
	 * Add a block
	 * 
	 * @param v
	 *            the vector
	 * @param mb
	 *            the materialblock
	 */
	public void add(Vector v, VariableBlock mb)
	{
		getSchematic().put(v, mb);
	}
	
	/**
	 * Add a block
	 * 
	 * @param v
	 *            the vector
	 * @param mb
	 *            the materialblock
	 */
	public void add(Vector v, MaterialBlock mb)
	{
		if(!getSchematic().containsKey(v))
		{
			getSchematic().put(v, new VariableBlock(mb.getMaterial(), mb.getData()));
		}
		
		else
		{
			getSchematic().get(v).addBlock(mb);
		}
	}
	
	/**
	 * Register this structure
	 */
	public void register()
	{
		Phantom.instance().getMultiblockRegistryController().registerStructure(this);
	}
	
	/**
	 * Unregister structure
	 */
	public void unRegister()
	{
		Phantom.instance().getMultiblockRegistryController().unRegisterStructure(this);
	}
	
	/**
	 * Quickly build the schematic at the given location
	 * 
	 * @param center
	 *            the center point
	 */
	public void fastBuild(Location center)
	{
		PhantomWorldQueue wq = new PhantomWorldQueue();
		
		for(Vector i : getSchematic().k())
		{
			Location shift = center.clone().add(i);
			wq.set(shift, getSchematic().get(i).getBlocks().pickRandom());
		}
		
		wq.flush();
	}
	
	/**
	 * Slowly build the structure
	 * 
	 * @param center
	 *            the structure
	 */
	public void build(Location center)
	{
		GList<Vector> v = getSchematic().k();
		
		new Task(1)
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				if(v.isEmpty())
				{
					cancel();
					return;
				}
				
				Vector i = v.pop();
				MaterialBlock mb = getSchematic().get(i).getBlocks().pickRandom();
				Location shift = center.clone().add(i);
				shift.getBlock().setType(mb.getMaterial());
				shift.getBlock().setData(mb.getData());
				NMSX.breakParticles(shift, mb.getMaterial(), 12);
				new GSound(Sound.BLOCK_STONE_STEP, 1f, 1f).play(shift);
			}
		};
	}
	
	public void hallucinate(Location center, Player p)
	{
		GList<Vector> v = getSchematic().k();
		
		new Task(1)
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				if(v.isEmpty())
				{
					cancel();
					return;
				}
				
				Vector i = v.pop();
				MaterialBlock mb = getSchematic().get(i).getBlocks().pickRandom();
				Location shift = center.clone().add(i);
				p.sendBlockChange(shift, mb.getMaterial(), mb.getData());
				NMSX.breakParticles(shift, mb.getMaterial(), 12);
				new GSound(Sound.BLOCK_STONE_STEP, 1f, 1f).play(shift);
				
				new TaskLater(350)
				{
					@Override
					public void run()
					{
						p.sendBlockChange(shift, shift.getBlock().getType(), shift.getBlock().getData());
						NMSX.breakParticles(shift, mb.getMaterial(), 12);
						new GSound(Sound.BLOCK_STONE_STEP, 1f, 1f).play(shift);
					}
				};
			}
		};
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		MultiblockStructure other = (MultiblockStructure) obj;
		if(type == null)
		{
			if(other.type != null)
			{
				return false;
			}
		}
		else if(!type.equals(other.type))
		{
			return false;
		}
		return true;
	}
}
