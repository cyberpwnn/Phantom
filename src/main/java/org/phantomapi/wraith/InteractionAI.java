package org.phantomapi.wraith;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.M;
import org.phantomapi.world.W;

public class InteractionAI extends WraithTrait implements Listener
{
	private GMap<Player, Integer> interest;
	private Boolean crouching;
	
	public InteractionAI()
	{
		super("ai-interaction");
		
		interest = new GMap<Player, Integer>();
		crouching = false;
		
		Phantom.getInstance().registerListener(this);
	}
	
	@EventHandler
	public void on(PlayerInteractAtEntityEvent e)
	{
		if(e.getRightClicked().equals(getNPC().getEntity()))
		{
			if(!interest.containsKey(e.getPlayer()))
			{
				interest.put(e.getPlayer(), 0);
			}
			
			interest.put(e.getPlayer(), interest.get(e.getPlayer()) + 1);
		}
	}
	
	@EventHandler
	public void on(PlayerToggleSneakEvent e)
	{
		Entity x = W.getEntityLookingAt(e.getPlayer(), 8, 2);
		
		if(x != null && x.equals(getNPC().getEntity()))
		{
			if(!interest.containsKey(e.getPlayer()))
			{
				interest.put(e.getPlayer(), 0);
			}
			
			interest.put(e.getPlayer(), interest.get(e.getPlayer()) + 1);
		}
	}
	
	@Override
	public void run()
	{
		if(interest.isEmpty())
		{
			if(crouching)
			{
				crouching = false;
				
				new TaskLater(20)
				{
					@Override
					public void run()
					{
						try
						{
							((Player) getNPC().getEntity()).setSneaking(crouching);
						}
						
						catch(Exception e)
						{
							
						}
					}
				};
			}
			
			return;
		}
		
		int interesting = 0;
		int highest = Integer.MIN_VALUE;
		Player inter = null;
		
		for(Player i : interest.k())
		{
			if(interest.get(i) > highest)
			{
				interesting = interest.get(i);
				highest = (int) (interest.get(i) + (Math.random() * 100));
				inter = i;
			}
		}
		
		if(M.r(0.4))
		{
			try
			{
				interesting -= Math.random() * 5;
				crouching = !crouching;
				((Player) getNPC().getEntity()).setSneaking(crouching);
			}
			
			catch(Exception e)
			{
				
			}
		}
		
		if(getNPC().getStoredLocation().distanceSquared(inter.getLocation()) > 1.1 * 1.1)
		{
			getNPC().faceLocation(inter.getEyeLocation());
		}
		
		interest.put(inter, interesting);
		
		if(interest.get(inter) < 0)
		{
			interest.remove(inter);
		}
	}
	
	@Override
	public void onAttach()
	{
		
	}
	
	@Override
	public void onDespawn()
	{
		Phantom.getInstance().unRegisterListener(this);
	}
	
	@Override
	public void onSpawn()
	{
		
	}
	
	@Override
	public void onRemove()
	{
		Phantom.getInstance().unRegisterListener(this);
	}
}
