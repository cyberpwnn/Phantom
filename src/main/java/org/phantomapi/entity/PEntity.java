package org.phantomapi.entity;

import org.bukkit.entity.Entity;
import org.phantomapi.lang.GList;
import org.phantomapi.nbt.NBTTagCompound;
import org.phantomapi.nbt.NBTTagList;
import org.phantomapi.nms.NBTX;

public class PEntity
{
	private Entity e;
	
	public PEntity(Entity e)
	{
		this.e = e;
	}
	
	public NBTTagCompound getNBT()
	{
		return NBTX.getEntityNBT(e);
	}
	
	public void setNBT(NBTTagCompound c)
	{
		NBTX.setEntityNBT(e, c);
	}
	
	public boolean hasAi()
	{
		return !getNBT().hasKey("NoAI");
	}
	
	public void setAi(boolean ai)
	{
		NBTTagCompound c = getNBT();
		
		if(!ai)
		{
			c.setInt("NoAI", 1);
		}
		
		else
		{
			c.remove("NoAI");
		}
		
		setNBT(c);
	}
	
	public void clearPassengers()
	{
		NBTTagCompound c = getNBT();
		c.remove("Passengers");
		setNBT(c);
	}
	
	public boolean hasPassengers()
	{
		NBTTagCompound c = getNBT();
		return c.hasKey("Passengers");
		
	}
	
	public void setPassengers(PEntity... riders)
	{
		if(riders == null || riders.length == 0)
		{
			clearPassengers();
			return;
		}
		
		NBTTagCompound c = getNBT();
		GList<Object> os = new GList<Object>();
		
		for(PEntity rider : riders)
		{
			os.add(rider.getNBT());
		}
		
		c.setList("Passengers", new NBTTagList(os.toArray(new Object[os.size()])));
		setNBT(c);
	}
}
