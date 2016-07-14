package org.cyberpwn.phantom.sfx;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.sync.Task;
import org.cyberpwn.phantom.world.Area;

public class AudibleEntity
{
	private Entity entity;
	private Audible audible;
	private Task task;
	
	public AudibleEntity(Entity entity, Audible audible, Integer interval)
	{
		this.entity = entity;
		this.audible = audible;
		this.task = null;
		
		if(interval > -1)
		{
			task = new Task(interval)
			{
				public void run()
				{
					if(entity == null || entity.isDead())
					{
						cancel();
						return;
					}
					
					Area a = new Area(entity.getLocation(), 64);
					
					for(Player i : a.getNearbyPlayers())
					{
						onPlay(i, audible.clone(), entity);
					}
				}
			};
		}
	}
	
	public void cancel()
	{
		if(task != null)
		{
			task.cancel();
		}
	}

	protected void onPlay(Player i, Audible audible, Entity entity)
	{
		audible.play(i, entity.getLocation());
	}

	public Audible getAudible()
	{
		return audible;
	}

	public void setAudible(Audible audible)
	{
		this.audible = audible;
	}

	public Entity getEntity()
	{
		return entity;
	}
}
